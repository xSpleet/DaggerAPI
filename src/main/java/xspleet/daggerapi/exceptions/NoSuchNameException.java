package xspleet.daggerapi.exceptions;

public class NoSuchNameException extends DaggerAPIException
{
    private final String name;

    public String getName()
    {
        return name;
    }

    public NoSuchNameException(String name)
    {
        this.name = name;
    }
}
