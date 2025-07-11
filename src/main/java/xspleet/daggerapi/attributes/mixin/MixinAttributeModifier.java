package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.UUID;

public interface MixinAttributeModifier<T> extends AttributeModifier<T> {

    public T DaggerAPI$getValue();
    public AttributeOperation<T> DaggerAPI$getOperation();
    public UUID DaggerAPI$getUUID();
    public String DaggerAPI$getName();
    public String DaggerAPI$getArtifactName();
    public void DaggerAPI$setArtifactName(String artifactName);

    public default T getValue() {
        return DaggerAPI$getValue();
    }

    public default AttributeOperation<T> getOperation() {
        return DaggerAPI$getOperation();
    }

    public default UUID getUUID() {
        return DaggerAPI$getUUID();
    }

    public default String getName() {
        return DaggerAPI$getName();
    }

    public default String getArtifactName() {
        return DaggerAPI$getArtifactName();
    }

    public default void setArtifactName(String artifactName) {
        DaggerAPI$setArtifactName(artifactName);
    }

}
