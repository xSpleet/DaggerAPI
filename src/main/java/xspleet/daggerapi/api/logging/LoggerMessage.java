package xspleet.daggerapi.api.logging;

public record LoggerMessage(LoggingContext context, LogLevel level, String message) {
}
