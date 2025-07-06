package xspleet.daggerapi.attributes.operations;

import com.mojang.datafixers.util.Function3;

public interface DaggerOperation<T>
{
    public Function3<T, T, T, T> getFunction();
}
