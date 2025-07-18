package xspleet.daggerapi.exceptions;

import java.util.List;

public class MissingRequiredDataException extends DaggerAPIException
{
    private final List<String> names;

    @Override
    public String getMessage() {
        return "Missing required data: " + String.join(", ", names);
    }

    public MissingRequiredDataException(List<String> names) {
        this.names = names;
    }
}
