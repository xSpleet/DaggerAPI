package xspleet.daggerapi.attributes.instance.delta;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.Objects;

public class AttributeInstanceDeltaUnit<T>
{
    AttributeModifierChangeType changeType;
    AttributeModifier<T> modifier;

    public AttributeInstanceDeltaUnit(AttributeModifierChangeType changeType, AttributeModifier<T> modifier) {
        this.changeType = changeType;
        this.modifier = modifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeInstanceDeltaUnit<?> that = (AttributeInstanceDeltaUnit<?>) o;
        return changeType == that.changeType && Objects.equals(modifier, that.modifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changeType, modifier);
    }
}
