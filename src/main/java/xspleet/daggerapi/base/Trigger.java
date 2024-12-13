package xspleet.daggerapi.base;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.artifact.ArtifactItem;

import java.util.ArrayList;
import java.util.List;

public class Trigger
{
    private final String name;
    private final List<ArtifactItem> listeners;

    public Trigger(String name)
    {
        this.name = name;
        listeners = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public void addListener(ArtifactItem listener)
    {
        listeners.add(listener);
    }

    public void trigger(DaggerData data)
    {
        data.setTrigger(this);
        for(ArtifactItem listener: listeners)
            listener.receiveTrigger(this, data);
    }
}
