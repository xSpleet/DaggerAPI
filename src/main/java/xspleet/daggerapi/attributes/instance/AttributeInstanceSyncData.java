package xspleet.daggerapi.attributes.instance;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;

import java.util.List;
import java.util.UUID;

public record AttributeInstanceSyncData(List<UUID> removedModifiers, List<AttributeModifier<?>> addedModifiers) {
}
