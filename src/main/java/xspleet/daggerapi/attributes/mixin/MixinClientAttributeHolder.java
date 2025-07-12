package xspleet.daggerapi.attributes.mixin;

import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.ClientAttributeHolder;

import java.util.List;

public interface MixinClientAttributeHolder extends ClientAttributeHolder
{
    public void DaggerAPI$addAttributeToUpdate(Attribute<?> attribute);
    public boolean DaggerAPI$updatesAttribute(Attribute<?> attribute);
    public void DaggerAPI$removeAttributeToUpdate(Attribute<?> attribute);
    public void DaggerAPI$updateAttribute(Attribute<?> attribute, long tick);
    public long DaggerAPI$getAttributeUpdateTime(Attribute<?> attribute);
    public List<Attribute<?>> DaggerAPI$getAttributesToUpdate();
    public void DaggerAPI$reset(Attribute<?> attribute);
    public void DaggerAPI$removeAllAttributesToUpdate();

    default void updateAttribute(Attribute<?> attribute, long tick) {
        DaggerAPI$updateAttribute(attribute, tick);
    }

    default long getAttributeUpdateTime(Attribute<?> attribute) {
        return DaggerAPI$getAttributeUpdateTime(attribute);
    }

    default void reset(Attribute<?> attribute) {
        DaggerAPI$reset(attribute);
    }

    default void addAttributeToUpdate(Attribute<?> attribute) {
        DaggerAPI$addAttributeToUpdate(attribute);
    }

    default boolean updatesAttribute(Attribute<?> attribute) {
        return DaggerAPI$updatesAttribute(attribute);
    }

    default void removeAttributeToUpdate(Attribute<?> attribute) {
        DaggerAPI$removeAttributeToUpdate(attribute);
    }

    default void removeAllAttributesToUpdate() {
        DaggerAPI$removeAllAttributesToUpdate();
    }

    default List<Attribute<?>> getAttributesToUpdate() {
        return DaggerAPI$getAttributesToUpdate();
    }
}
