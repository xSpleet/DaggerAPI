package xspleet.daggerapi.exceptions;

public class NoSuchActionException extends DaggerAPIException
{
    private final String name;

    @Override
    public String getMessage() {
        return "No such action with the name: " + name;
    }

    public NoSuchActionException(String name) {
        this.name = name;
    }
}
