package xspleet.daggerapi.artifact.builder;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.artifact.modifiers.ArtifactAttributeModifier;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;

import java.util.ArrayList;

public class BuildableArtifactItem extends ArtifactItem
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

        triggers.add(trigger);
    }
}
