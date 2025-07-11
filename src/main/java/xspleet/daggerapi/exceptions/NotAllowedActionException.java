package xspleet.daggerapi.exceptions;

public class NotAllowedActionException extends DaggerAPIException
{
    private final String action;
    private final String reason;

    public NotAllowedActionException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Action '" + action + "' is not allowed: " + reason;
    }
}
