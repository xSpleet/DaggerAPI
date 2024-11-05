package xspleet.daggerapi.exceptions;

public class WrongArgumentException extends DaggerAPIException
{
    private final String name;
    private final String value;

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public WrongArgumentException(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
}
