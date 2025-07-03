package xspleet.daggerapi.events;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.collections.Triggers;
import xspleet.daggerapi.data.TriggerData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.data.key.DaggerKeys;

public class ActiveArtifactActivation
{
    public static final Identifier USE_ACTIVE_ARTIFACT_PACKET_ID = new Identifier("daggerapi", "use_active_artifact");
    public static final String KEY_CATEGORY_DAGGERAPI = "key.category.daggerapi.daggerapi";
    public static final String KEY_USE_ACTIVE_ARTIFACT = "key.daggerapi.use_active_artifact";
    public static KeyBinding artifactUsingKey;

    public static void registerActivation()
    {
        ServerPlayNetworking.registerGlobalReceiver(USE_ACTIVE_ARTIFACT_PACKET_ID, (server, player, handler, buf, responseSender) ->
        {
            ItemCooldownManager cooldownManager = player.getItemCooldownManager();
            if(TrinketsApi.getTrinketComponent(player).isPresent())
            {
                for(Pair<SlotReference, ItemStack> pair: TrinketsApi.getTrinketComponent(player).get().getEquipped(stack -> (stack.getItem() instanceof ArtifactItem artifact && artifact.isActive())))
                {
                    Item item = pair.getRight().getItem();

                    if(item instanceof ArtifactItem artifactItem && !artifactItem.isActive())
                        continue;

                    if(!cooldownManager.isCoolingDown(item)) {
                        Triggers.ACTIVATE.trigger(new TriggerData()
                                .addData(DaggerKeys.TRIGGERER, player)
                                .addData(DaggerKeys.WORLD, player.getWorld())
                                .addData(DaggerKeys.ARTIFACT_ID, Registries.ITEM.getId(item))
                                .addData(DaggerKeys.SUCCESSFUL, true));

                        cooldownManager.set(item, ((ArtifactItem) item).getCooldown());
                    }
                    else
                    {
                        Triggers.ACTIVATE.trigger(new TriggerData()
                                .addData(DaggerKeys.TRIGGERER, player)
                                .addData(DaggerKeys.WORLD, player.getWorld())
                                .addData(DaggerKeys.ARTIFACT_ID, Registries.ITEM.getId(item))
                                .addData(DaggerKeys.SUCCESSFUL, false));
                    }
                }
            }
        });
    }

    public static void registerKeyInputs()
    {
        ClientTickEvents.END_CLIENT_TICK.register(client->
                {
                    if(artifactUsingKey.wasPressed())
                    {
                        ClientPlayNetworking.send(USE_ACTIVE_ARTIFACT_PACKET_ID, PacketByteBufs.empty());
                    }
                }
        );
    }

    public static void register()
    {
        artifactUsingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_USE_ACTIVE_ARTIFACT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                KEY_CATEGORY_DAGGERAPI
        ));
        registerKeyInputs();
        registerActivation();
    }
}
