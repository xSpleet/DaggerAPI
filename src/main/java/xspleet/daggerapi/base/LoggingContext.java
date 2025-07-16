package xspleet.daggerapi.base;

public enum LoggingContext
{
    STARTUP("{STARTUP}"),
    BEHAVIOR("{BEHAVIOR}"),
    GENERATION("{GENERATION}"),
    PACK_RELOAD("{PACK_RELOAD}"),
    GENERIC("{GENERIC}"),;

    private String prefix;

    private LoggingContext(String prefix) {
        this.prefix = prefix;
    }
}
