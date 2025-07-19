package xspleet.daggerapi.exceptions;

public class BadArgumentException extends DaggerAPIException
{
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    public BadArgumentException(String message) {
        this.message = message;
    }
}
