package xspleet.daggerapi.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.models.ConfigModel;

import java.io.*;

import static xspleet.daggerapi.DaggerAPI.JSON_PARSER;

public class ServerDevModeConfig
{
    public static boolean SERVER_DEV_MODE = false;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTING.register(
                (minecraftServer -> {
                    var configs = FabricLoader.getInstance().getConfigDir();
                    var config = configs.resolve("daggerapi.json").toFile();
                    if(!config.exists()) {
                        DaggerLogger.warn(LoggingContext.STARTUP, "DaggerAPI config file not found at " + config.getAbsolutePath() + ". Creating default one");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config))) {
                            var defaultConfig = new ConfigModel();
                            JSON_PARSER.toJson(defaultConfig, writer);
                            DaggerLogger.warn(LoggingContext.STARTUP, "Default DaggerAPI config created at " + config.getAbsolutePath());
                        } catch (IOException e) {
                            DaggerLogger.error(LoggingContext.STARTUP, "Failed to create DaggerAPI config file: " + e.getMessage());
                        }
                    }
                    try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
                        var json = JSON_PARSER.fromJson(reader, ConfigModel.class);
                        DaggerLogger.debug(LoggingContext.STARTUP, "DaggerAPI config loaded: " + json);
                        if(json.isDevMode()) {
                            DaggerLogger.warn(LoggingContext.STARTUP, "DaggerAPI is running in development mode. This is not recommended for production servers.");
                        }
                        SERVER_DEV_MODE = json.isDevMode();
                    } catch (IOException e) {
                        DaggerLogger.error(LoggingContext.STARTUP, "Failed to read DaggerAPI config file: " + e.getMessage());
                    }
                })
        );
    }
}
