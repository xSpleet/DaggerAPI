package xspleet.daggerapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.collections.Attributes;
import xspleet.daggerapi.commands.ClientCommands;
import xspleet.daggerapi.events.KeyBindRegistration;
import xspleet.daggerapi.networking.NetworkingConstants;
import xspleet.daggerapi.trigger.TriggerTracker;

public class DaggerAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient()
    {
        NetworkingConstants.init();
        HudRenderCallback.EVENT.register(
                ((drawContext, v) -> {
                    var client = MinecraftClient.getInstance();
                    if(client != null && client.player instanceof TriggerTracker tracker && tracker.isTrackingEnabled())
                    {
                        var triggers = tracker.getTriggerTrackEntries(client.player.getWorld().getTime());
                        var worldTick = client.player.getWorld().getTime() + v;
                        for(var entry : triggers) {
                            int index = triggers.indexOf(entry);
                            var trigger = entry.trigger().getName();
                            var item = entry.item().getName();
                            var tick = entry.tick();

                            int screenWidth = client.getWindow().getScaledWidth();
                            int textWidth = client.textRenderer.getWidth(item + ": " + trigger);

                            int x = screenWidth - textWidth - 10;
                            int y = 10 + index * 10;

                            int color = 0xFFFFFF + ((int)(worldTick - tick)) << 24;

                            drawContext.drawText(
                                    client.textRenderer,
                                    item + ": " + trigger,
                                    x, y,
                                    color,
                                    false
                            );
                        }
                    }

                    if(client != null && client.player instanceof ClientAttributeHolder clientHolder && client.player instanceof AttributeHolder holder) {
                        var attributes = clientHolder.getAttributesToUpdate();
                        if (attributes.isEmpty()) {
                            return; // No attributes to update
                        }

                        for (var attribute : attributes) {
                            var instance = holder.getAttributeInstance(attribute);
                            if (instance != null) {
                                if (clientHolder.getAttributeUpdateTime(attribute) + 100 < client.player.getWorld().getTime()) {
                                    drawContext.drawTextWithShadow(
                                            client.textRenderer,
                                            attribute.getName() + ": " + instance.getValue(),
                                            10, 10 + attributes.indexOf(attribute) * 10,
                                            0xFFFFFF
                                    );
                                    clientHolder.reset(attribute);
                                } else {
                                    drawContext.drawTextWithShadow(
                                            client.textRenderer,
                                            "[UPDATED] " + attribute.getName() + ": " + instance.getValue(),
                                            10, 10 + attributes.indexOf(attribute) * 10,
                                            0xFF4444
                                    );
                                }
                            }
                        }
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
                            if(player instanceof ClientAttributeHolder clientHolder) {
                                for(var attribute : syncContainer.getSyncData().keySet()) {
                                    if(clientHolder.updatesAttribute(attribute)) {
                                        clientHolder.updateAttribute(attribute, player.getWorld().getTime());
                                    }
                                }
                            }
                            holder.acceptSyncContainer(syncContainer);
                            DaggerLogger.debug("Received attribute sync packet for player: {}", player.getName().getString());
                        } else {
                            DaggerLogger.error("Received attribute sync packet but player is null.");
                        }
                    });
                }
        );

        KeyBindRegistration.register();
        ClientCommands.registerCommands();
    }
}
