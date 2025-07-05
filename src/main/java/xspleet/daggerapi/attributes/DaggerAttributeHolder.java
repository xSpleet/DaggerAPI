package xspleet.daggerapi.attributes;

public interface DaggerAttributeHolder
{
    public <T> DaggerAttributeInstance<T> getAttributeInstance(DaggerAttribute<T> attribute);
}
