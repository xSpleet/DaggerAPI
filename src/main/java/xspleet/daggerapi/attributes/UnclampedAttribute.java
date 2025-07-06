package xspleet.daggerapi.attributes;

import xspleet.daggerapi.attributes.base.Attribute;

public class UnclampedAttribute<T> implements Attribute<T> {
    private final T defaultValue;

    public UnclampedAttribute(T defaultValue) {
        this.defaultValue = defaultValue;
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
