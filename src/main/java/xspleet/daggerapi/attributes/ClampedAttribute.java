package xspleet.daggerapi.attributes;

public class ClampedAttribute<T extends Comparable<T>> implements Attribute<T>
{
    private final String name;
    private final T defaultValue;
    private final T minValue;
    private final T maxValue;

    public ClampedAttribute(String name, T defaultValue, T minValue, T maxValue) {
        this.name = name;
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
}
