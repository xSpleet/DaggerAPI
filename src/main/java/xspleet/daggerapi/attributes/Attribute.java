package xspleet.daggerapi.attributes;

public interface Attribute<T>
{
    public T getDefaultValue();
    public T clamp(T value);
}
