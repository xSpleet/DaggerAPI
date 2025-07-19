package xspleet.daggerapi.artifact;

import xspleet.daggerapi.artifact.modifiers.ArtifactAttributeModifier;
import xspleet.daggerapi.trigger.Trigger;
import xspleet.daggerapi.trigger.actions.ConditionalAction;
import xspleet.daggerapi.trigger.actions.WeightedConditionalAction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record ArtifactBehavior(Map<Trigger, List<ConditionalAction>> events,
                               Map<Trigger, List<WeightedConditionalAction>> weightedEvents,
                               Set<Trigger> triggers,
                               List<ArtifactAttributeModifier> attributeModifiers,
                               ArtifactRarity artifactRarity,
                               int cooldown) {
}
