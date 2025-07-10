package xspleet.daggerapi.exceptions;

import java.util.List;

public class MissingArgumentException extends DaggerAPIException
{
    private final List<String> arguments;

    @Override
    public String getMessage() {
        return "Missing attributes: " + String.join(", ", arguments);
    }

    public MissingArgumentException(List<String> arguments) {
        this.arguments = arguments;
    }
}
