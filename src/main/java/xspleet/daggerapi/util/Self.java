package xspleet.daggerapi.util;

public interface Self<T>
{
    default T self()
    {
        return (T) this;
    }
}
