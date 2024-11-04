package xspleet.daggerapi.base.actions;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.Action;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.base.DaggerData;

import java.util.function.Consumer;
import java.util.function.Predicate;

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
    public void actOn(DaggerData data) {
        conditionalAction.actOn(data);
    }
}
