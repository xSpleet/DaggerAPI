package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.UUID;

public interface MixinAttributeModifier<T> extends AttributeModifier<T> {

    T DaggerAPI$getValue();
    AttributeOperation<T> DaggerAPI$getOperation();
    UUID DaggerAPI$getUUID();
    String DaggerAPI$getName();
    String DaggerAPI$getArtifactName();
    void DaggerAPI$setArtifactName(String artifactName);

    default T getValue() {
        return DaggerAPI$getValue();
    }

    default AttributeOperation<T> getOperation() {
        return DaggerAPI$getOperation();
    }

    default UUID getUUID() {
        return DaggerAPI$getUUID();
    }

    default String getName() {
        return DaggerAPI$getName();
    }

    default String getArtifactName() {
        return DaggerAPI$getArtifactName();
    }

    default void setArtifactName(String artifactName) {
        DaggerAPI$setArtifactName(artifactName);
    }

}
