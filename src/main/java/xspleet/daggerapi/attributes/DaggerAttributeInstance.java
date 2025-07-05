package xspleet.daggerapi.attributes;

public interface DaggerAttributeInstance<T>
{
    public boolean hasModifier(DaggerAttributeModifier<T> modifier);
    public void addTemporaryModifier(DaggerAttributeModifier<T> modifier);
    public void removeModifier(DaggerAttributeModifier<T> modifier);
    public T getValue();
    public T getBaseValue();
}
