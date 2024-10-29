package xspleet.daggerapi.base.artifact;

import xspleet.jdagapi.base.*;
import xspleet.jdagapi.base.actions.ConditionalAction;
import xspleet.jdagapi.base.actions.WeightedConditionalAction;

import java.util.ArrayList;

public class BuildableArtifactItem extends ActiveArtifactItem
{
    public BuildableArtifactItem(Settings settings) {
        super(settings);
    }

    public void addAttributeModifier(ArtifactAttributeModifier attributeModifier)
    {
        attributeModifiers.add(attributeModifier);
    }

    public void addEvent(Trigger trigger, ConditionalAction action)
    {
        if(action instanceof WeightedConditionalAction weightedAction)
        {
            if(!weightedEvents.containsKey(trigger))
                weightedEvents.put(trigger, new ArrayList<>());
            weightedEvents.get(trigger).add(weightedAction);
        }
        else
        {
            if(!events.containsKey(trigger))
                events.put(trigger, new ArrayList<>());
            events.get(trigger).add(action);
        }

        trigger.addListener(this);
    }
}
