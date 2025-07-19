package xspleet.daggerapi.evaluation;

import xspleet.daggerapi.data.collection.DaggerContext;
import xspleet.daggerapi.data.key.DaggerKey;

import java.util.function.Function;

public class VariablePath<T, U>
{
    private final Class<T> type;
    private final Class<U> returnType;
    private final Function<T, U> function;
    private DaggerKey<T> key;

    public VariablePath(Function<T, U> function, Class<T> type, Class<U> returnType){
        this.function = function;
        this.type = type;
        this.returnType = returnType;
    }

    public U get(DaggerContext context)
    {
        T value = context.getData(key);
        if(value == null)
            return null;
        return function.apply(value);
    }

    public void setKey(DaggerKey<T> key) {
        this.key = key;
    }

    public Class<T> getType() {
        return type;
    }

    public Class<U> getReturnType() {
        return returnType;
    }
}
