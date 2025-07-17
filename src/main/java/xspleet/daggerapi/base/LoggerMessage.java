package xspleet.daggerapi.base;

import io.netty.handler.logging.LogLevel;

public record LoggerMessage(LoggingContext context, LogLevel level, String message) {
}
