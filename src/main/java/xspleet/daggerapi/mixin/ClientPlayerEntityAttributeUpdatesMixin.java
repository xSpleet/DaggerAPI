package xspleet.daggerapi.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.mixin.MixinClientAttributeHolder;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.base.Self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityAttributeUpdatesMixin implements Self<ClientPlayerEntity>, MixinClientAttributeHolder {

    @Unique
    private final Map<Attribute<?>, Long> daggerAPI$attributeUpdateTimes = new HashMap<>();
    @Unique
    private final List<Attribute<?>> daggerAPI$attributesToUpdate = new ArrayList<>();

    @Override
    public void DaggerAPI$addAttributeToUpdate(Attribute<?> attribute) {
        daggerAPI$attributesToUpdate.add(attribute);
        daggerAPI$attributeUpdateTimes.put(attribute, -1L);
    }

    @Override
    public boolean DaggerAPI$updatesAttribute(Attribute<?> attribute) {
        return daggerAPI$attributeUpdateTimes.containsKey(attribute);
    }

    @Override
    public void DaggerAPI$removeAttributeToUpdate(Attribute<?> attribute) {
        daggerAPI$attributesToUpdate.remove(attribute);
        daggerAPI$attributeUpdateTimes.remove(attribute);
    }

    @Override
    public void DaggerAPI$updateAttribute(Attribute<?> attribute, long tick) {
        if(daggerAPI$attributeUpdateTimes.containsKey(attribute)) {
            DaggerLogger.warn("Updating attribute " + attribute.getName() + " at tick " + tick + " for player " + self().getName().getString() + ", but this attribute is not tracked for updates.");
        }
        daggerAPI$attributeUpdateTimes.put(attribute, tick);
    }

    @Override
    public long DaggerAPI$getAttributeUpdateTime(Attribute<?> attribute) {
        return daggerAPI$attributeUpdateTimes.getOrDefault(attribute, -1L);
    }

    @Override
    public List<Attribute<?>> DaggerAPI$getAttributesToUpdate() {
        return daggerAPI$attributesToUpdate;
    }

    @Override
    public void DaggerAPI$reset(Attribute<?> attribute) {
        daggerAPI$attributeUpdateTimes.put(attribute, 0L);
    }

    @Override
    public void DaggerAPI$removeAllAttributesToUpdate() {
        daggerAPI$attributesToUpdate.clear();
        daggerAPI$attributeUpdateTimes.clear();
    }
}
