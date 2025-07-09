package xspleet.daggerapi.attributes.instance.delta;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;

public class AttributeInstanceDeltaUnit<T>
{
    AttributeModifierChangeType changeType;
    AttributeModifier<T> modifier;

    public AttributeInstanceDeltaUnit(AttributeModifierChangeType changeType, AttributeModifier<T> modifier) {
        this.changeType = changeType;
        this.modifier = modifier;
    }
}
