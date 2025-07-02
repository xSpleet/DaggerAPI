package xspleet.daggerapi.data;

public interface DaggerContext
{
    public <T> DaggerContext addData(DaggerKey<T> key, T value);
    public <T> T getData(DaggerKey<T> key);
}
