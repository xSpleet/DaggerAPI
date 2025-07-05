package xspleet.daggerapi.base;

public interface Self<T>
{
    default public T self()
    {
        return (T) this;
    }
}
