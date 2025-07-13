package xspleet.daggerapi.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.models.ConfigModel;
import xspleet.daggerapi.networking.NetworkingConstants;

import java.io.*;

import static xspleet.daggerapi.DaggerAPI.JSON_PARSER;

public class ClientDevModeConfig
{
    public static boolean CLIENT_DEV_MODE = false;

    public static void init() {
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
            ClientDevModeConfig.CLIENT_DEV_MODE = json.isDevMode();
        } catch (IOException e) {
            DaggerLogger.error("Failed to read DaggerAPI config file: " + e.getMessage());
        }

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_DEV_MODE_PACKET_ID,
                (client, handler, packet, sender) -> {
                    boolean devMode = packet.readBoolean();
                    client.execute(() -> {
                        ClientDevModeConfig.CLIENT_DEV_MODE = devMode && ClientDevModeConfig.CLIENT_DEV_MODE;
                        DaggerLogger.debug("Received dev mode sync packet: " + devMode);
                    });
                }
        );
    }
}
