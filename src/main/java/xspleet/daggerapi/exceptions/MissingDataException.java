package xspleet.daggerapi.exceptions;

import xspleet.daggerapi.data.key.DaggerKey;

public class MissingDataException extends RuntimeException
{
    public DaggerKey<?> key;

    public MissingDataException(DaggerKey<?> key) {
        this.key = key;
    }

    @Override
    public String getMessage() {
        return "Missing data for key: " + key.key();
    }
}
