package xspleet.daggerapi.attributes;

import java.util.Objects;

public class ClampedAttribute<T extends Comparable<T>> implements Attribute<T>
{
    private final String name;
    private final Class<T> type;
    private final T defaultValue;
    private final T minValue;
    private final T maxValue;
    private boolean tracked = true;

    public ClampedAttribute(String name, Class<T> type, T defaultValue, T minValue, T maxValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
        T clampedValue = value;

        if (value.compareTo(minValue) < 0) {
            clampedValue = minValue;
        }
        if (value.compareTo(maxValue) > 0) {
            clampedValue = maxValue;
        }

        return clampedValue;
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
        ClampedAttribute<?> that = (ClampedAttribute<?>) o;
        return tracked == that.tracked && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(defaultValue, that.defaultValue) && Objects.equals(minValue, that.minValue) && Objects.equals(maxValue, that.maxValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, defaultValue, minValue, maxValue, tracked);
    }
}
