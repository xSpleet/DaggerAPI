package xspleet.daggerapi.attributes;

import xspleet.daggerapi.attributes.base.Attribute;
import xspleet.daggerapi.attributes.base.AttributeInstance;
import xspleet.daggerapi.attributes.base.AttributeModifier;
import xspleet.daggerapi.attributes.base.AttributeOperation;

import java.util.*;
import java.util.stream.Collectors;

public class DaggerAttributeInstance<T> implements AttributeInstance<T>
{
    private final TreeMap<AttributeOperation<T>, Set<AttributeModifier<T>>> temporaryModifiers = new TreeMap<>(Comparator.comparingInt(AttributeOperation::getPrecedence));
    private final Set<AttributeModifier<T>> allModifiers = new HashSet<>();
    private final Attribute<T> attribute;

    public DaggerAttributeInstance(Attribute<T> attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean hasModifier(AttributeModifier<T> modifier) {
        return allModifiers.contains(modifier);
    }

    @Override
    public void addTemporaryModifier(AttributeModifier<T> modifier) {
        temporaryModifiers.putIfAbsent(modifier.getOperation(), new HashSet<>());
        temporaryModifiers.get(modifier.getOperation()).add(modifier);
        allModifiers.add(modifier);
    }

    @Override
    public void removeModifier(AttributeModifier<T> modifier) {
        if (allModifiers.remove(modifier)) {
            Set<AttributeModifier<T>> modifiers = temporaryModifiers.get(modifier.getOperation());
            if (modifiers != null) {
                modifiers.remove(modifier);
                if (modifiers.isEmpty()) {
                    temporaryModifiers.remove(modifier.getOperation());
                }
            }
        }

    }

    @Override
    public T getValue() {
        T value = attribute.getDefaultValue();
        for(AttributeOperation<T> operation : temporaryModifiers.keySet()) {
            Set<AttributeModifier<T>> modifiers = temporaryModifiers.getOrDefault(operation, Collections.emptySet());
            T valuePreviousGroup = value;
            for (AttributeModifier<T> modifier : modifiers) {
                value = operation.apply(value, valuePreviousGroup, modifier.getValue());
            }
        }
        return attribute.clamp(value);
    }

    @Override
    public T getBaseValue() {
        return attribute.getDefaultValue();
    }
}
