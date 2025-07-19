package xspleet.daggerapi.commands;

import io.netty.handler.logging.LogLevel;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.config.ServerDevModeConfig;

public class ServerCommands
{
    public static final String COMMAND_PREFIX = "daggerapi";
    public static final String COMMAND_LOG = "log";
    public static final String COMMAND_DUMP = "dump";

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {
                dispatcher.register(
                        CommandManager.literal(COMMAND_PREFIX)
                                .requires(source -> source.hasPermissionLevel(2) && ServerDevModeConfig.SERVER_DEV_MODE)
                                .then(CommandManager.literal(COMMAND_LOG)
                                        .then(CommandManager.literal(COMMAND_DUMP)
                                                .executes(ctx -> {
                                                    DaggerLogger.dumpAll(LogLevel.WARN);
                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal("DaggerAPI log dumped successfully. Check the logs for details."),
                                                            true
                                                    );
                                                    return 1;
                                                })
                                        ))
                );
            }
        );
    }
}
