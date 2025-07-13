package xspleet.daggerapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.DaggerAPI;

import static xspleet.daggerapi.DaggerAPI.SERVER_DEV_MODE;

public class DaggerLogger
{
    private static final Logger logger = LoggerFactory.getLogger(DaggerAPI.MOD_ID);
    public static void debug(String message) {
        if(SERVER_DEV_MODE) {
            logger.info("[DEBUG] {}", message);
        }
    }

    public static void debug(String format, Object... args) {
        if(SERVER_DEV_MODE) {
            logger.info("[DEBUG] " + format, args);
        }
    }

    public static void info(String message) {
        logger.info("[INFO] {}", message);
    }

    public static void info(String format, Object... args) {
        logger.info("[INFO] " + format, args);
    }

    public static void warn(String message) {
        logger.warn("[WARN] {}", message);
    }

    public static void warn(String format, Object... args) {
        logger.warn("[WARN] " + format, args);
    }

    public static void error(String message) {
        logger.error("[ERROR] {}", message);
    }

    public static void error(String format, Object... args) {
        logger.error("[ERROR] " + format, args);
    }
}
