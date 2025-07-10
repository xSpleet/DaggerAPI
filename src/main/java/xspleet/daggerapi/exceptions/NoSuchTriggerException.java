package xspleet.daggerapi.exceptions;

public class NoSuchTriggerException extends DaggerAPIException
{
    private final String name;

    @Override
    public String getMessage()
    {
        return "No such trigger with the name: " + name;
    }

    public NoSuchTriggerException(String name) {
        this.name = name;
    }
}
