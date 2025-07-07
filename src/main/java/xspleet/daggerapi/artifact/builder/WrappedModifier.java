package xspleet.daggerapi.artifact.builder;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;

public record WrappedModifier<T>(Attribute<T> attribute, AttributeModifier<T> modifier) {
}
