package xspleet.daggerapi.exceptions;

public class WrongConditionOnException extends DaggerAPIException
{
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    public WrongConditionOnException(String message) {
        this.message = message;
    }
}
