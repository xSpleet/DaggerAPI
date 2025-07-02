package xspleet.daggerapi.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.trigger.Trigger;

import java.util.Set;

public class TriggerData implements DaggerContext
{
    private final DaggerContext data;
    private final Set<PlayerEntity> listeners;

    public TriggerData(DaggerContext data, Set<PlayerEntity> listeners) {
        this.data = data;
        this.listeners = listeners;
    }

    public TriggerData() {
        this.data = new DaggerData();
        listeners = Set.of();
    }

    public Set<PlayerEntity> getListeners() {
        return listeners;
    }

    public TriggerData addListeners(Set<PlayerEntity> listeners) {
        this.listeners.addAll(listeners);
        return this;
    }

    @Override
    public <T> TriggerData addData(DaggerKey<T> key, T value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        return data.getData(key);
    }
}
