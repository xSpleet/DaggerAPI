package xspleet.daggerapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.collections.Attributes;
import xspleet.daggerapi.events.KeyBindRegistration;
import xspleet.daggerapi.networking.NetworkingConstants;

public class DaggerAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient()
    {
        NetworkingConstants.init();
        HudRenderCallback.EVENT.register(
                ((drawContext, v) -> {
                    var client = MinecraftClient.getInstance();
                    if(client != null && client.player instanceof AttributeHolder holder) {
                        var canWalkOnWater = holder.getAttributeInstance(Attributes.CAN_WALK_ON_WATER);
                        var jumpHeight = holder.getAttributeInstance(Attributes.JUMP_HEIGHT);

                        drawContext.drawTextWithShadow(
                                client.textRenderer,
                                "Can walk on water: " + canWalkOnWater.getValue(),
                                10, 10, 0xFFFFFF);

                        drawContext.drawTextWithShadow(
                                client.textRenderer,
                                "Jump height: " + jumpHeight.getValue(),
                                10, 20, 0xFFFFFF);
                    } else {
                        DaggerLogger.warn("Client or player is null, cannot render attributes.");
                    }
                })
        );

        ClientPlayNetworking.registerGlobalReceiver(
                NetworkingConstants.SYNC_ATTRIBUTES_PACKET_ID,
                (client, handler, packet, sender) -> {
                    var syncContainer = DaggerAttributeContainer.readFromPacket(packet);
                    client.execute(() -> {
                        var player = client.player;
                        if (player instanceof AttributeHolder holder) {
                            holder.acceptSyncContainer(syncContainer);
                            DaggerLogger.debug("Received attribute sync packet for player: {}", player.getName().getString());
                        } else {
                            DaggerLogger.error("Received attribute sync packet but player is null.");
                        }
                    });
                }
        );

        KeyBindRegistration.register();
    }
}
