package xspleet.daggerapi.attributes;

import java.util.function.BiFunction;

public interface DaggerAttributeOperation<T>
{
    public BiFunction<T, T, T> getOperation();
    public int getPrecedence();
    public T apply(T a, T b);
}
