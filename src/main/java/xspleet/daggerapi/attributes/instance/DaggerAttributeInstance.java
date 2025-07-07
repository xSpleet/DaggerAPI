package xspleet.daggerapi.attributes.instance;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.*;

public class DaggerAttributeInstance<T> implements AttributeInstance<T>
{
    private final TreeMap<AttributeOperation<T>, Set<AttributeModifier<T>>> temporaryModifiers = new TreeMap<>(Comparator.comparingInt(AttributeOperation::getPrecedence));
    private final Set<AttributeModifier<T>> allModifiers = new HashSet<>();
    private final Attribute<T> attribute;
    private boolean dirty = true;
    private boolean updated = true;
    private T value;

    public DaggerAttributeInstance(Attribute<T> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getAttributeName() {
        return attribute.getName();
    }

    @Override
    public boolean hasModifier(AttributeModifier<T> modifier) {
        return allModifiers.contains(modifier);
    }

    @Override
    public boolean hasModifier(UUID modifierId) {
        return allModifiers.stream().anyMatch(x -> x.getUUID().equals(modifierId));
    }

    @Override
    public void addTemporaryModifier(AttributeModifier<T> modifier) {
        temporaryModifiers.putIfAbsent(modifier.getOperation(), new HashSet<>());
        temporaryModifiers.get(modifier.getOperation()).add(modifier);
        allModifiers.add(modifier);
        updated = true;
        setDirty();
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
        updated = true;
        setDirty();
    }

    @Override
    public T getValue() {
        if(!updated)
            return value;
        T computedValue = attribute.getDefaultValue();
        for(AttributeOperation<T> operation : temporaryModifiers.keySet()) {
            Set<AttributeModifier<T>> modifiers = temporaryModifiers.getOrDefault(operation, Collections.emptySet());
            T valuePreviousGroup = computedValue;
            for (AttributeModifier<T> modifier : modifiers) {
                computedValue = operation.apply(computedValue, valuePreviousGroup, modifier.getValue());
            }
        }
        updated = false;
        value = computedValue;
        return attribute.clamp(computedValue);
    }

    @Override
    public T getBaseValue() {
        return attribute.getDefaultValue();
    }

    public void setDirty() {
        dirty = true;
    }

    @Override
    public void clean() {
        dirty = false;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
