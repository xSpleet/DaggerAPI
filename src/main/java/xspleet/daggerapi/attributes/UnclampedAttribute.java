package xspleet.daggerapi.attributes;

public class UnclampedAttribute<T> implements Attribute<T> {
    private final String name;
    private final T defaultValue;

    public UnclampedAttribute(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T clamp(T value) {
        return value;
    }
}
