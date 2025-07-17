package xspleet.daggerapi.base;

import xspleet.daggerapi.data.key.DaggerKey;

import java.util.function.Function;

public class DoubleVariablePath<T> extends VariablePath<T, Double>
{
    public DoubleVariablePath(String path, DaggerKey<T> key, Function<T, Double> function) {
        super(path, key, function);
    }
}
