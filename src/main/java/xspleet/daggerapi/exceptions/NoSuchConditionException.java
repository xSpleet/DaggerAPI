package xspleet.daggerapi.exceptions;

public class NoSuchConditionException extends DaggerAPIException {

    private final String name;

    @Override
    public String getMessage() {
        return "No such condition with the name: " + name;
    }

    public NoSuchConditionException(String name) {
        this.name = name;
    }
}
