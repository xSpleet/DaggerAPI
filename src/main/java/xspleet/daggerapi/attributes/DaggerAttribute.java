package xspleet.daggerapi.attributes;

public interface DaggerAttribute<T>
{
    public DaggerAttributeInstance<T> getAttributeInstance();
}
