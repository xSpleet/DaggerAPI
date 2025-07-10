package xspleet.daggerapi.exceptions;

public class ActivateTriggerNotRegistered extends DaggerAPIException {
    public ActivateTriggerNotRegistered() {}

    @Override
    public String getMessage() {
        return "Activate trigger not registered on active artifact";
    }
}
