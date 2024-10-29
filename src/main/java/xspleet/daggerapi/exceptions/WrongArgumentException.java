package xspleet.daggerapi.exceptions;

public class WrongArgumentException extends RuntimeException
{
    private String name;
    private String value;

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
