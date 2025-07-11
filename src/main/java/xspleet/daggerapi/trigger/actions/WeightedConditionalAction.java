package xspleet.daggerapi.trigger.actions;

import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.TriggerData;

public class WeightedConditionalAction extends ConditionalAction
{
    private final int weight;

    public WeightedConditionalAction(int weight)
    {
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }
}
