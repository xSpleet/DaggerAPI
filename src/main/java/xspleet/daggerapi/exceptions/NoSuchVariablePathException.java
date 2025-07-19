package xspleet.daggerapi.exceptions;

public class NoSuchVariablePathException extends DaggerAPIException
{
    private final String name;

    @Override
    public String getMessage() {
        return "No such variable path with the name: " + name;
    }

    public NoSuchVariablePathException(String name) {
        this.name = name;
    }
}
