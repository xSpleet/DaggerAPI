package xspleet.daggerapi.trigger.actions;

import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.TriggerData;

public class WeightedConditionalAction extends ConditionalAction
{
    private final int weight;
    private final ConditionalAction conditionalAction;

    public WeightedConditionalAction(int weight)
    {
        conditionalAction = new ConditionalAction();
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }

    @Override
    public void addCondition(Condition condition)
    {
        conditionalAction.addCondition(condition);
    }

    @Override
    public void addAction(Action action) {
        conditionalAction.addAction(action);
    }

    @Override
    public void actOn(TriggerData data) {
        conditionalAction.actOn(data);
    }
}
