package xspleet.daggerapi.attributes;

public class UnclampedAttribute<T> implements Attribute<T> {
    private final String name;
    private final T defaultValue;
    private final Class<T> type;
    private boolean tracked = false;

    public UnclampedAttribute(String name, Class<T> type, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = type;
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
    public Class<T> getType() {
        return type;
    }

    @Override
    public T clamp(T value) {
        return value;
    }

    @Override
    public Attribute<T> setTracked() {
        tracked = true;
        return this;
    }

    @Override
    public boolean isTracked() {
        return tracked;
    }
}
