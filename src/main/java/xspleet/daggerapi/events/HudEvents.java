package xspleet.daggerapi.events;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeHolder;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.base.LoggingContext;

public class HudEvents
{
    public static void init() {
        HudRenderCallback.EVENT.register(
                ((drawContext, v) -> {
                    var client = MinecraftClient.getInstance();

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
                        DaggerLogger.warn(LoggingContext.GENERIC, "Client or player is null, cannot render attributes.");
                    }
                })
        );
    }
}
