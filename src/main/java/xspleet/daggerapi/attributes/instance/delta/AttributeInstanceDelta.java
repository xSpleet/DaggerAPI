package xspleet.daggerapi.attributes.instance.delta;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.*;
import java.util.stream.Collectors;

public class AttributeInstanceDelta<T>
{
    List<AttributeInstanceDeltaUnit<T>> deltas = new ArrayList<>();

    public void addModifier(AttributeModifier<T> modifier)
    {
        deltas.add(new AttributeInstanceDeltaUnit<T>(AttributeModifierChangeType.ADD, modifier));
    }

    public void removeModifier(AttributeModifier<T> modifier)
    {
        deltas.add(new AttributeInstanceDeltaUnit<T>(AttributeModifierChangeType.REMOVE, modifier));
    }

    private void collapse()
    {
        Map<UUID, AttributeInstanceDeltaUnit<T>> modifierIndex = new HashMap<>();
        for (AttributeInstanceDeltaUnit<T> delta : deltas) {
            if(modifierIndex.containsKey(delta.modifier.getUUID())) {
                AttributeInstanceDeltaUnit<T> existing = modifierIndex.get(delta.modifier.getUUID());
                if(existing.changeType != delta.changeType) {
                    if(existing.changeType == AttributeModifierChangeType.REMOVE) {
                        modifierIndex.put(delta.modifier.getUUID(), delta);
                    }
                    else {
                        modifierIndex.remove(delta.modifier.getUUID());
                    }
                }
            }
            else {
                modifierIndex.put(delta.modifier.getUUID(), delta);
            }
        }
        deltas = new ArrayList<>(modifierIndex.values());
    }

    public List<AttributeModifier<T>> getAddedModifiers()
    {
        collapse();
        return deltas.stream()
                .filter(delta -> delta.changeType == AttributeModifierChangeType.ADD)
                .map(delta -> delta.modifier)
                .collect(Collectors.toList());
    }

    public List<AttributeModifier<T>> getRemovedModifiers()
    {
        collapse();
        return deltas.stream()
                .filter(delta -> delta.changeType == AttributeModifierChangeType.REMOVE)
                .map(delta -> delta.modifier)
                .collect(Collectors.toList());
    }

    public void clear()
    {
        deltas.clear();
    }

    public boolean hasDeltas()
    {
        return !deltas.isEmpty();
    }
}
