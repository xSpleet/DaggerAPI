package xspleet.daggerapi.exceptions;

public class NoSuchOperationException extends DaggerAPIException
{
    private final String name;

    @Override
    public String getMessage() {
        return "No such operation with the name: " + name;
    }

    public NoSuchOperationException(String name) {
        this.name = name;
    }
}
