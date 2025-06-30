package xspleet.daggerapi.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.trigger.Trigger;

import java.util.Set;

public class TriggerData implements DaggerContext
{
    private final DaggerContext data;

    private final Set<PlayerEntity> listeners;
    private Trigger trigger;
    private @Nullable Entity triggerer;
    private World triggeredWorld;

    public TriggerData(DaggerContext data, Set<PlayerEntity> listeners) {
        this.data = data;
        this.listeners = listeners;
    }

    public TriggerData() {
        this.data = new DaggerData();
        listeners = Set.of();
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public TriggerData setTrigger(Trigger trigger) {
        this.trigger = trigger;
        data.addData("trigger", trigger.getName());
        return this;
    }

    @Override
    public TriggerData addData(String key, String value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public String getData(String key) {
        return data.getData(key);
    }

    public Set<PlayerEntity> getListeners() {
        return listeners;
    }

    public @Nullable Entity getTriggerer() {
        return triggerer;
    }

    public TriggerData setTriggerer(Entity triggerer) {
        this.triggerer = triggerer;
        return this;
    }

    public World getTriggeredWorld() {
        return triggeredWorld;
    }

    public TriggerData setTriggeredWorld(World triggeredWorld) {
        this.triggeredWorld = triggeredWorld;
        return this;
    }
}
