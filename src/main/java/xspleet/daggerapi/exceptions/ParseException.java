package xspleet.daggerapi.exceptions;

import java.util.List;

public class ParseException extends DaggerAPIException
{
    private final String message;

    public ParseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Parsing failed: " + message;
    }
}
