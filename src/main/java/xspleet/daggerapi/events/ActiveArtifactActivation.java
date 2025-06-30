package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import xspleet.daggerapi.base.TrinketsUtil;

public class ActiveArtifactActivation
{
    public static final Identifier USE_ACTIVE_ARTIFACT_PACKET_ID = new Identifier("jdag-api", "use_active_artifact");
    public static final String KEY_CATEGORY_JDAGAPI = "key.category.magpie.magpie";
    public static final String KEY_USE_ACTIVE_ARTIFACT = "key.magpie.use_active_artifact";
    public static KeyBinding artifactUsingKey;
    public static void registerActivation()
    {
        ServerPlayNetworking.registerGlobalReceiver(USE_ACTIVE_ARTIFACT_PACKET_ID, (server, player, handler, buf, responseSender) ->
        {
            TrinketsUtil.activateAllActiveArtifacts(player);
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
                KEY_CATEGORY_JDAGAPI
        ));
        registerKeyInputs();
    }
}
