package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.exceptions.BadArgumentsException;
import xspleet.daggerapi.exceptions.ParseException;

@FunctionalInterface
public interface Parser<T>
{
    public T parse(JsonElement element) throws BadArgumentsException, ParseException;
}
