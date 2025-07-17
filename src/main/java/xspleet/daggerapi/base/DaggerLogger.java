package xspleet.daggerapi.base;

import io.netty.handler.logging.LogLevel;
import net.fabricmc.fabric.api.client.sound.v1.FabricSoundInstance;
import net.fabricmc.fabric.api.recipe.v1.ingredient.FabricIngredient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.event.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.DaggerAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static xspleet.daggerapi.config.ServerDevModeConfig.SERVER_DEV_MODE;

public class DaggerLogger
{
    private static final Logger logger = LoggerFactory.getLogger(DaggerAPI.MOD_ID);
    private static String currentPack = null;
    private static final Map<String, List<LoggerMessage>> packMessages = new HashMap<>();
    private static final List<LoggerMessage> messages = new ArrayList<>();

    public static void setCurrentPack(String pack) {
        currentPack = pack;
    }

    public static void removeCurrentPack() {
        currentPack = null;
    }

    public static void debug(LoggingContext context, String format, Object... args) {
        if(DaggerAPI.DEBUG_MODE) {
            logger.info("[DEBUG] " + generateMessage(context, format, args));
        }
    }

    public static void info(LoggingContext context, String format, Object... args) {
        logger.info("[INFO] " + generateMessage(context, format, args));
    }

    public static void warn(LoggingContext context, String format, Object... args) {
        logger.warn("[WARN] " + generateMessage(context, format, args));
    }

    public static void error(LoggingContext context, String format, Object... args) {
        logger.error("[ERROR] " + generateMessage(context, format, args));
    }

    public static void report(LoggingContext context, LogLevel level, String format, Object... args) {
        String message = generateMessage(context, format, args);
        saveMessage(context, level, message);
        switch (level)
        {
            case DEBUG -> logger.info("[DEBUG] " + message);
            case INFO -> logger.info("[INFO] " + message);
            case WARN -> logger.warn("[WARN] " + message);
            case ERROR -> logger.error("[ERROR] " + message);
            default -> logger.info(message); // Fallback for any other log level
        }
    }

    public static String placeOf(String type, int place) {
        return type + "[" + place + "]";
    }

    public static String placeOf(String type, int place, String innerType, int innerPlace) {
        return type + "[" + place + "]" + "{ " + innerType + "[" + innerPlace + "] }";
    }

    public static boolean hasErrors() {
        for (String packName : packMessages.keySet()) {
            for (LoggerMessage message : packMessages.get(packName)) {
                if (message.level() == LogLevel.ERROR) {
                    return true;
                }
            }
        }
        for (LoggerMessage message : messages) {
            if (message.level() == LogLevel.ERROR) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasErrors(String packName) {
        if (packMessages.containsKey(packName)) {
            for (LoggerMessage message : packMessages.get(packName)) {
                if (message.level() == LogLevel.ERROR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void dump(String packName, LogLevel level){
        if (packMessages.containsKey(packName)) {
            var now = LocalDateTime.now();
            Path path = FabricLoader.getInstance().getGameDir().resolve("daggerapi/logs");
            if (!path.toFile().exists()) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    logger.error("Failed to create logs directory", e);
                }
            }
            String formattedTime = now.toString().replace(':', '-').replace('T', '_').split("\\.")[0];
            Path logFile = path.resolve(formattedTime + "_" + packName + "_log.txt");
            try {
                if (!logFile.toFile().exists()) {
                    Files.createFile(logFile);
                }
            } catch (IOException e) {
                logger.error("Failed to write log message to file", e);
            }

            try (var writer = Files.newBufferedWriter(logFile)) {
                writer.write("Logs for pack: " + packName + "\n");
                for(LoggerMessage message : packMessages.get(packName)) {
                    if(message.level().compareTo(level) >= 0) {
                        writer.write(String.format("[%s]<%s>|%s| %s\n", message.level(), message.context().getPrefix(), packName, message.message()));
                    }
                }
            } catch (IOException e) {
                logger.error("Failed to write log message to file", e);
            }

            packMessages.get(packName).removeIf(msg -> msg.level().compareTo(level) >= 0);
        }
    }

    public static void dumpAll(LogLevel level) {
        var now = LocalDateTime.now();
        Path path = FabricLoader.getInstance().getGameDir().resolve("daggerapi/logs");
        if (!path.toFile().exists()) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("Failed to create logs directory", e);
            }
        }
        String formattedTime = now.toString().replace(':', '-').replace('T', '_').split("\\.")[0];
        Path logFile = path.resolve(formattedTime + "_log.txt");
        try {
            if (!logFile.toFile().exists()) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            logger.error("Failed to write log message to file", e);
        }
        try (var writer = Files.newBufferedWriter(logFile)) {
            for (String packName : packMessages.keySet()) {
                writer.write("Logs for pack: " + packName + "\n");
                for (LoggerMessage message : packMessages.get(packName)) {
                    if (message.level().compareTo(level) >= 0) {
                        writer.write(String.format("[%s] in %s : %s\n", message.level(), message.context().getPrefix(), message.message()));
                    }
                }
                writer.write("==================================\n");
                packMessages.get(packName).removeIf(msg -> msg.level().compareTo(level) >= 0);
            }
            writer.write("General Logs:\n");
            for(LoggerMessage message : messages) {
                if (message.level().compareTo(level) >= 0) {
                    writer.write(String.format("[%s] in %s : %s\n", message.level(), message.context().getPrefix(), message.message()));
                }
            }
            messages.removeIf(msg -> msg.level().compareTo(level) >= 0);
        }
        catch (IOException e) {
            logger.error("Failed to write log message to file", e);
        }
    }

    private static String generateMessage(LoggingContext context, String format, Object... args) {
        String formatString;
        if(currentPack != null) {
            formatString = String.format("<%s> |%s|: " + format, context.getPrefix(), currentPack);
        } else {
            formatString = String.format("<%s>: " + format, context.getPrefix());
        }
        return formatBraces(formatString, args);
    }

    private static void saveMessage(LoggingContext context, LogLevel level, String format, Object... args) {
        LoggerMessage loggerMessage = new LoggerMessage(context, level, formatBraces(format, args));
        if (currentPack != null) {
            packMessages.putIfAbsent(currentPack, new ArrayList<>());
            packMessages.get(currentPack).add(loggerMessage);
        } else {
            messages.add(loggerMessage);
        }
    }

    private static String formatBraces(String template, Object... args) {
        for (Object arg : args) {
            template = template.replaceFirst("\\{}", Matcher.quoteReplacement(String.valueOf(arg)));
        }
        return template;
    }
}
