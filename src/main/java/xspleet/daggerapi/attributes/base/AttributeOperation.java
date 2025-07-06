package xspleet.daggerapi.attributes.base;

import com.mojang.datafixers.util.Function3;

public interface AttributeOperation<T>
{
    public String getName();
    public int getPrecedence();
    public Function3<T, T, T, T> getOperation();
    public T apply(T prevResult, T resultFromPrevType, T value);
}
