package xspleet.daggerapi.api.logging;

import io.netty.handler.logging.LogLevel;

public record LoggerMessage(LoggingContext context, LogLevel level, String message) {
}
