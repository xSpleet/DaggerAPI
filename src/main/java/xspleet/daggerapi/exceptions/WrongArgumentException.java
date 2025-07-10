package xspleet.daggerapi.exceptions;

public class WrongArgumentException extends RuntimeException
{
    private final String name;
    private final String value;

    @Override
    public String getMessage()
    {
        return "Wrong value of argument" + name + ": " + value;
    }

    public WrongArgumentException(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
}
