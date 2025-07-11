package xspleet.daggerapi.attributes;

import java.util.Objects;

public class UnclampedAttribute<T> implements Attribute<T> {
    private final String name;
    private final T defaultValue;
    private final Class<T> type;
    private boolean tracked = true;

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
    public Attribute<T> setUntracked() {
        tracked = false;
        return this;
    }

    @Override
    public boolean isTracked() {
        return tracked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnclampedAttribute<?> that = (UnclampedAttribute<?>) o;
        return tracked == that.tracked && Objects.equals(name, that.name) && Objects.equals(defaultValue, that.defaultValue) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, defaultValue, type, tracked);
    }
}
