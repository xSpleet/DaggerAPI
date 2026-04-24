package xspleet.daggerapi.api.logging;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.DaggerAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DaggerLogger
{
    private static final Logger logger = LoggerFactory.getLogger(DaggerAPI.MOD_ID);
    private static volatile String currentPack = null;
    private static final Map<String, List<LoggerMessage>> packMessages = new ConcurrentHashMap<>();
    private static final List<LoggerMessage> messages = new CopyOnWriteArrayList<>();

    public static void setCurrentPack(String pack) {
        currentPack = pack;
    }

    public static void removeCurrentPack() {
        currentPack = null;
    }

    public static void debug(LoggingContext context, String format, Object... args) {
        if (DaggerAPI.DEBUG_MODE) {
            logger.info("[DEBUG] " + generateMessage(context, format, args));
        }
    }

    public static void info(LoggingContext context, String format, Object... args) {
        logger.info(generateMessage(context, format, args));
    }

    public static void warn(LoggingContext context, String format, Object... args) {
        logger.warn(generateMessage(context, format, args));
    }

    public static void error(LoggingContext context, String format, Object... args) {
        logger.error(generateMessage(context, format, args));
    }

    public static void report(LoggingContext context, LogLevel level, String format, Object... args) {
        String message = generateMessage(context, format, args);
        saveMessage(context, level, message);
        switch (level) {
            case DEBUG -> { if (DaggerAPI.DEBUG_MODE) logger.info("[DEBUG] " + message); }
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
            default -> logger.info(message);
        }
    }

    public static String placeOf(String type, int place) {
        return type + "[" + place + "]";
    }

    public static String placeOf(String type, int place, String innerType, int innerPlace) {
        return type + "[" + place + "]" + "{ " + innerType + "[" + innerPlace + "] }";
    }

    public static boolean hasErrors() {
        for (List<LoggerMessage> packMsgs : packMessages.values()) {
            for (LoggerMessage message : packMsgs) {
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
        List<LoggerMessage> packMsgs = packMessages.get(packName);
        if (packMsgs != null) {
            for (LoggerMessage message : packMsgs) {
                if (message.level() == LogLevel.ERROR) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void dump(String packName, LogLevel level) {
        List<LoggerMessage> packMsgs = packMessages.get(packName);
        if (packMsgs != null) {
            logger.info("=== DaggerAPI Log Dump ===");
            printPack(packName, packMsgs, level);
            logger.info("=========================");

            Path logFile = createLogFile(
                    FabricLoader.getInstance().getGameDir().resolve("daggerapi/logs"),
                    getTimestamp() + "_" + packName + "_log.txt"
            );
            try (var writer = Files.newBufferedWriter(logFile)) {
                writer.write("Logs for pack: " + packName + "\n");
                for (LoggerMessage message : packMsgs) {
                    if (message.level().compareTo(level) >= 0) {
                        writer.write(String.format("[%s]<%s>|%s| %s\n", message.level(), message.context().name(), packName, message.message()));
                    }
                }
            } catch (IOException e) {
                logger.error("Failed to write log message to file", e);
            }

            packMsgs.removeIf(msg -> msg.level().compareTo(level) >= 0);
        }
    }

    public static void dumpAll(LogLevel level) {
        boolean hasPackMessages = packMessages.values().stream()
                .anyMatch(msgs -> msgs.stream().anyMatch(msg -> msg.level().compareTo(level) >= 0));
        boolean hasGeneralMessages = messages.stream().anyMatch(msg -> msg.level().compareTo(level) >= 0);

        if (!hasPackMessages && !hasGeneralMessages) {
            return;
        }

        printAll(level);

        Path logFile = createLogFile(
                FabricLoader.getInstance().getGameDir().resolve("daggerapi/logs"),
                getTimestamp() + "_log.txt"
        );
        try (var writer = Files.newBufferedWriter(logFile)) {
            for (Map.Entry<String, List<LoggerMessage>> entry : packMessages.entrySet()) {
                String packName = entry.getKey();
                writer.write("Logs for pack: " + packName + "\n");
                for (LoggerMessage message : entry.getValue()) {
                    if (message.level().compareTo(level) >= 0) {
                        writer.write(String.format("[%s] in %s : %s\n", message.level(), message.context().name(), message.message()));
                    }
                }
                writer.write("==================================\n");
                entry.getValue().removeIf(msg -> msg.level().compareTo(level) >= 0);
            }
            writer.write("General Logs:\n");
            for (LoggerMessage message : messages) {
                if (message.level().compareTo(level) >= 0) {
                    writer.write(String.format("[%s] in %s : %s\n", message.level(), message.context().name(), message.message()));
                }
            }
            messages.removeIf(msg -> msg.level().compareTo(level) >= 0);
        } catch (IOException e) {
            logger.error("Failed to write log message to file", e);
        }
    }

    public static void printAll(LogLevel minLevel) {
        logger.info("=== DaggerAPI Log Dump ===");
        for (Map.Entry<String, List<LoggerMessage>> entry : packMessages.entrySet()) {
            printPack(entry.getKey(), entry.getValue(), minLevel);
        }
        if (!messages.isEmpty()) {
            logger.info("--- General ---");
            for (LoggerMessage message : messages) {
                if (message.level().compareTo(minLevel) >= 0) {
                    logToConsole(message);
                }
            }
        }
        logger.info("=========================");
    }

    private static void printPack(String packName, List<LoggerMessage> packMsgs, LogLevel minLevel) {
        logger.info("--- Pack: {} ---", packName);
        for (LoggerMessage message : packMsgs) {
            if (message.level().compareTo(minLevel) >= 0) {
                logToConsole(message);
            }
        }
    }

    private static void logToConsole(LoggerMessage message) {
        switch (message.level()) {
            case ERROR -> logger.error(message.message());
            case WARN -> logger.warn(message.message());
            case DEBUG -> { if (DaggerAPI.DEBUG_MODE) logger.info("[DEBUG] " + message.message()); }
            default -> logger.info(message.message());
        }
    }

    private static String generateMessage(LoggingContext context, String format, Object... args) {
        String pack = currentPack;
        String prefix = pack != null
                ? "<" + context.name() + "> |" + pack + "|: " + format
                : "<" + context.name() + ">: " + format;
        return formatBraces(prefix, args);
    }

    private static void saveMessage(LoggingContext context, LogLevel level, String message) {
        String pack = currentPack;
        LoggerMessage loggerMessage = new LoggerMessage(context, level, message);
        if (pack != null) {
            packMessages.computeIfAbsent(pack, k -> new CopyOnWriteArrayList<>()).add(loggerMessage);
        } else {
            messages.add(loggerMessage);
        }
    }

    private static String formatBraces(String template, Object... args) {
        if (args.length == 0) return template;
        StringBuilder sb = new StringBuilder();
        int argIndex = 0;
        int i = 0;
        while (i < template.length()) {
            if (i + 1 < template.length() && template.charAt(i) == '{' && template.charAt(i + 1) == '}' && argIndex < args.length) {
                sb.append(args[argIndex++]);
                i += 2;
            } else {
                sb.append(template.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }

    private static Path createLogFile(Path logsDir, String filename) {
        if (!logsDir.toFile().exists()) {
            try {
                Files.createDirectories(logsDir);
            } catch (IOException e) {
                logger.error("Failed to create logs directory", e);
            }
        }
        Path logFile = logsDir.resolve(filename);
        try {
            if (!logFile.toFile().exists()) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            logger.error("Failed to create log file", e);
        }
        return logFile;
    }

    private static String getTimestamp() {
        return LocalDateTime.now().toString().replace(':', '-').replace('T', '_').split("\\.")[0];
    }
}
