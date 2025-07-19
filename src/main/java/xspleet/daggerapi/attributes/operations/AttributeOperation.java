package xspleet.daggerapi.attributes.operations;

import com.mojang.datafixers.util.Function3;

public interface AttributeOperation<T>
{
    String getName();
    int getPrecedence();
    Function3<T, T, T, T> getOperation();
    T apply(T prevResult, T resultFromPrevType, T value);
    Class<T> getType();
}
