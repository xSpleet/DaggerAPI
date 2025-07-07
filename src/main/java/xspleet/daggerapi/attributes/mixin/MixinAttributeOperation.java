package xspleet.daggerapi.attributes.mixin;

import com.mojang.datafixers.util.Function3;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

public interface MixinAttributeOperation<T> extends AttributeOperation<T>
{
    public String DaggerAPI$getName();
    public int DaggerAPI$getPrecedence();
    public Function3<T, T, T, T> DaggerAPI$getOperation();
    public T DaggerAPI$apply(T prevResult, T resultFromPrevType, T value);

    public default String getName() {
        return DaggerAPI$getName();
    }

    public default int getPrecedence() {
        return DaggerAPI$getPrecedence();
    }

    public default Function3<T, T, T, T> getOperation() {
        return DaggerAPI$getOperation();
    }

    public default T apply(T prevResult, T resultFromPrevType, T value) {
        return DaggerAPI$apply(prevResult, resultFromPrevType, value);
    }
}
