package xspleet.daggerapi.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.models.ConfigModel;
import xspleet.daggerapi.networking.NetworkingConstants;

import java.io.*;

import static xspleet.daggerapi.DaggerAPI.JSON_PARSER;

public class ServerDevModeConfig
{
    public static boolean SERVER_DEV_MODE = false;

    public static void init() {
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
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
                SERVER_DEV_MODE = json.isDevMode();
            } catch (IOException e) {
                DaggerLogger.error("Failed to read DaggerAPI config file: " + e.getMessage());
            }

            ServerPlayConnectionEvents.JOIN.register(
                    (handler, sender, server) -> {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeBoolean(ServerDevModeConfig.SERVER_DEV_MODE);
                        ServerPlayNetworking.send(handler.getPlayer(), NetworkingConstants.SYNC_DEV_MODE_PACKET_ID, buf);
                    }
            );
        }
    }
}
