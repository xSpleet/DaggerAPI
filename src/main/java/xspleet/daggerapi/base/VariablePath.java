package xspleet.daggerapi.base;

import xspleet.daggerapi.data.DaggerContext;
import xspleet.daggerapi.data.key.DaggerKey;

import java.util.function.Function;

public class VariablePath<T, U>
{
    private final DaggerKey<T> key;
    private final Function<T, U> function;

    public VariablePath(DaggerKey<T> key, Function<T, U> function)
    {
        this.key = key;
        this.function = function;
    }

    public U get(DaggerContext context)
    {
        T value = context.getData(key);
        if(value == null)
            return null;
        return function.apply(value);
    }
}
