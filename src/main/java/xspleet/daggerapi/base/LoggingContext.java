package xspleet.daggerapi.base;

public enum LoggingContext
{
    STARTUP("STARTUP"),
    BEHAVIOR("BEHAVIOR"),
    PARSING("PARSING"),
    GENERATION("GENERATION"),
    GENERIC("GENERIC"),
    SYNC("SYNC"),;

    private final String prefix;

    private LoggingContext(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
