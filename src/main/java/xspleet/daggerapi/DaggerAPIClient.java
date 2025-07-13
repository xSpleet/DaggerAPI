package xspleet.daggerapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.commands.ClientCommands;
import xspleet.daggerapi.events.KeyBindRegistration;
import xspleet.daggerapi.models.ConfigModel;
import xspleet.daggerapi.networking.NetworkingConstants;

import java.io.*;

import static xspleet.daggerapi.DaggerAPI.JSON_PARSER;

public class DaggerAPIClient implements ClientModInitializer {
    public static boolean CLIENT_DEV_MODE = false;

    @Override
    public void onInitializeClient()
    {
        NetworkingConstants.init();
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

        var configs = FabricLoader.getInstance().getConfigDir();
        var config = configs.resolve("daggerapi.json").toFile();
        if(!config.exists()) {
            DaggerLogger.warn("DaggerAPI config file not found at " + config.getAbsolutePath() + ". Creating default one");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(config))) {
                var defaultConfig = new ConfigModel();
                JSON_PARSER.toJson(defaultConfig, writer);
                DaggerLogger.warn("Default DaggerAPI config created at " + config.getAbsolutePath());
            } catch (IOException e) {
                DaggerLogger.error("Failed to create DaggerAPI config file: " + e.getMessage());
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
            var json = JSON_PARSER.fromJson(reader, ConfigModel.class);
            DaggerLogger.debug("DaggerAPI config loaded: " + json);
            if(json.isDevMode()) {
                DaggerLogger.warn("DaggerAPI is running in development mode. This is not recommended for production servers.");
            }
            DaggerAPIClient.CLIENT_DEV_MODE = json.isDevMode();
        } catch (IOException e) {
            DaggerLogger.error("Failed to read DaggerAPI config file: " + e.getMessage());
        }

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_DEV_MODE_PACKET_ID,
                (client, handler, packet, sender) -> {
                    boolean devMode = packet.readBoolean();
                    client.execute(() -> {
                        DaggerAPIClient.CLIENT_DEV_MODE = devMode && DaggerAPIClient.CLIENT_DEV_MODE;
                        DaggerLogger.debug("Received dev mode sync packet: " + devMode);
                    });
                }
        );

        KeyBindRegistration.register();
        ClientCommands.registerCommands();
    }
}
