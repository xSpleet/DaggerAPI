package xspleet.daggerapi.exceptions;

public class DaggerAPIException extends Exception
{
    protected String message;

    public DaggerAPIException(String message) {
        this.message = message;
    }

    public DaggerAPIException() {
        this.message = "An unknown error occurred in the DaggerAPI.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
