package xspleet.daggerapi.exceptions;

public class IllegalTriggerException extends DaggerAPIException
{
    private final String trigger;
    private final String action;

    public IllegalTriggerException(String trigger, String action) {
        this.trigger = trigger;
        this.action = action;
    }

    @Override
    public String getMessage() {
        return "Action '" + action + "' is not allowed for trigger '" + trigger + "'.";
    }
}
