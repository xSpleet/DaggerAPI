package xspleet.daggerapi.attributes;

public interface DaggerAttributeModifier<T>
{
    public T getValue();
    public int getPrecedence();
    public DaggerAttributeOperation<T> getOperation();
}
