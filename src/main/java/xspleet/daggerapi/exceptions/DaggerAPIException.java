package xspleet.daggerapi.exceptions;

public class DaggerAPIException extends Exception
{
    protected String message;

    @Override
    public String getMessage() {
        return message;
    }
}
