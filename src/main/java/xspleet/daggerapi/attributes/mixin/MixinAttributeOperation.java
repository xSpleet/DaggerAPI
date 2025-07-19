package xspleet.daggerapi.attributes.mixin;

import com.mojang.datafixers.util.Function3;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

public interface MixinAttributeOperation<T> extends AttributeOperation<T>
{
    String DaggerAPI$getName();
    int DaggerAPI$getPrecedence();
    Function3<T, T, T, T> DaggerAPI$getOperation();
    T DaggerAPI$apply(T prevResult, T resultFromPrevType, T value);
    Class<T> DaggerAPI$getType();

    default String getName() {
        return DaggerAPI$getName();
    }

    default int getPrecedence() {
        return DaggerAPI$getPrecedence();
    }

    default Function3<T, T, T, T> getOperation() {
        return DaggerAPI$getOperation();
    }

    default T apply(T prevResult, T resultFromPrevType, T value) {
        return DaggerAPI$apply(prevResult, resultFromPrevType, value);
    }

    default Class<T> getType() {
        return DaggerAPI$getType();
    }
}
