package xspleet.daggerapi.attributes.base;

public interface Attribute<T>
{
    public T getDefaultValue();
    public T clamp(T value);
}
