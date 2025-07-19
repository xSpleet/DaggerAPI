package xspleet.daggerapi.data.collection;

import xspleet.daggerapi.data.key.DaggerKey;

public interface DaggerContext
{
    <T> DaggerContext addData(DaggerKey<T> key, T value);
    <T> T getData(DaggerKey<T> key);
    boolean hasData(DaggerKey<?> key);
}
