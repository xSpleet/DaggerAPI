package xspleet.daggerapi.attributes;

public class ClampedAttribute<T extends Comparable<T>> implements Attribute<T>
{
    private final T defaultValue;
    private final T minValue;
    private final T maxValue;

    public ClampedAttribute(T defaultValue, T minValue, T maxValue) {
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
