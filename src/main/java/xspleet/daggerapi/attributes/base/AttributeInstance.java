package xspleet.daggerapi.attributes.base;

public interface AttributeInstance<T>
{
    public boolean hasModifier(AttributeModifier<T> modifier);
    public void addTemporaryModifier(AttributeModifier<T> modifier);
    public void removeModifier(AttributeModifier<T> modifier);
    public T getValue();
    public T getBaseValue();
}
