package xspleet.daggerapi.evaluation;

import xspleet.daggerapi.data.key.DaggerKey;

import java.util.function.Function;

public class VariablePathTemplate<T, U> {
    private final Class<T> type;
    private final Class<U> returnType;
    private final Function<T, U> function;

    public VariablePathTemplate(Function<T, U> function, Class<T> type, Class<U> returnType){
        this.function = function;
        this.type = type;
        this.returnType = returnType;
    }

    public VariablePath<T, U> getPath() {
        return new VariablePath<>(function, type, returnType);
    }

    public Class<T> getType() {
        return type;
    }

    public Class<U> getReturnType() {
        return returnType;
    }
}
