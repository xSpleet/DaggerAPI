package xspleet.daggerapi.exceptions;

public class NoSuchNameException extends RuntimeException
{
    private String name;

    public String getName()
    {
        return name;
    }

    public NoSuchNameException(String name)
    {
        this.name = name;
    }
}
