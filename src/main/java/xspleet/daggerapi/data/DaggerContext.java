package xspleet.daggerapi.data;

import xspleet.daggerapi.data.key.DaggerKey;

public interface DaggerContext
{
    public <T> DaggerContext addData(DaggerKey<T> key, T value);
    public <T> T getData(DaggerKey<T> key);
}
