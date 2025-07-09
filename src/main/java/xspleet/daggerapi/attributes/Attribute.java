package xspleet.daggerapi.attributes;

public interface Attribute<T>
{
    public String getName();
    public T getDefaultValue();
    public Class<T> getType();
    public T clamp(T value);
    public Attribute<T> setTracked();
    public boolean isTracked();
}
