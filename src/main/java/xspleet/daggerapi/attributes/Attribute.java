package xspleet.daggerapi.attributes;

public interface Attribute<T>
{
    String getName();
    T getDefaultValue();
    Class<T> getType();
    T clamp(T value);
    Attribute<T> setUntracked();
    boolean isTracked();
}
