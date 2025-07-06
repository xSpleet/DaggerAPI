package xspleet.daggerapi.attributes.base;

public interface AttributeHolder
{
    public <T> AttributeInstance<T> getAttributeInstance(Attribute<T> attribute);
}
