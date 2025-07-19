package xspleet.daggerapi.api.logging;

public enum LoggingContext
{
    STARTUP("STARTUP"),
    BEHAVIOR("BEHAVIOR"),
    PARSING("PARSING"),
    GENERATION("GENERATION"),
    GENERIC("GENERIC"),
    SYNC("SYNC"),;

    private final String prefix;

    LoggingContext(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
