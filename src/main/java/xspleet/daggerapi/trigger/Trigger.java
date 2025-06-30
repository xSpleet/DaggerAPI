package xspleet.daggerapi.trigger;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.data.TriggerData;

import java.util.*;

public class Trigger
{
    private final String name;
    private final Map<ArtifactItem, Set<PlayerEntity>> listeners = new HashMap<>();
    private boolean worldful = false;
    private boolean hasTriggerer = false;

    public Trigger(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addListenerType(ArtifactItem listenerType)
    {
        listeners.putIfAbsent(listenerType, new HashSet<>());
    }

    public void addListener(ArtifactItem listenerType, PlayerEntity listener)
    {
        if(!listeners.containsKey(listenerType))
            throw new NoSuchElementException();

        listeners.get(listenerType).add(listener);
    }

    public void removeListener(ArtifactItem listenerType, PlayerEntity listener)
    {
        if(!listeners.containsKey(listenerType))
            throw new NoSuchElementException();

        listeners.get(listenerType).remove(listener);
    }

    public void trigger(TriggerData data)
    {
        for(Map.Entry<ArtifactItem, Set<PlayerEntity>> entry: listeners.entrySet()) {
            TriggerData triggerData = new TriggerData(data, entry.getValue())
                    .setTrigger(this)
                    .setTriggerer(data.getTriggerer())
                    .setTriggeredWorld(data.getTriggeredWorld());

            entry.getKey().receiveTrigger(triggerData);
        }
    }

    public Trigger setHasTriggerer() {
        hasTriggerer = true;
        return this;
    }

    public Trigger setWorldful() {
        worldful = true;
        return this;
    }

    public boolean isWorldful() {
        return worldful;
    }

    public boolean hasTriggerer() {
        return hasTriggerer;
    }
}
