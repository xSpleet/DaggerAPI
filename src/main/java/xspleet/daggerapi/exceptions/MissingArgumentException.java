package xspleet.daggerapi.exceptions;

public class MissingArgumentException extends RuntimeException
{
    private String argument;

    public String getArgumentName()
    {
        return argument;
    }

    public MissingArgumentException(String argument)
    {
        this.argument = argument;
    }
}
