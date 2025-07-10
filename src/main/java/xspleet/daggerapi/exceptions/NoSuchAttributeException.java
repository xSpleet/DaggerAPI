package xspleet.daggerapi.exceptions;

public class NoSuchAttributeException extends DaggerAPIException{
    private final String name;

    @Override
    public String getMessage()
    {
        return "No such attribute with the name: " + name;
    }

    public NoSuchAttributeException(String name) {
        this.name = name;
    }
}
