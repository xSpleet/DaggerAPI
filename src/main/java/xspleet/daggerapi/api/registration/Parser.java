package xspleet.daggerapi.api.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.exceptions.BadArgumentsException;
import xspleet.daggerapi.exceptions.ExpressionParseException;
import xspleet.daggerapi.exceptions.ParseException;

@FunctionalInterface
public interface Parser<T>
{
    T parse(JsonElement element) throws BadArgumentsException, ParseException, ExpressionParseException;
}
