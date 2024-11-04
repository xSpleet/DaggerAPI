package xspleet.daggerapi.base.actions;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.Action;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.base.DaggerData;
import xspleet.daggerapi.collections.ConditionProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalAction
{
    private Condition condition;
    private final List<Action> actions;

    public ConditionalAction()
    {
        condition = ConditionProviders.alwaysTrue();
        actions = new ArrayList<>();
    }

    public Condition getCondition()
    {
        return condition;
    }

    public void addCondition(Condition condition)
    {
        this.condition = this.condition.and(condition);
    }

    public void addAction(Action action)
    {
        actions.add(action);
    }

    public void actOn(DaggerData data)
    {
        if(condition.test(data))
            for(Action action: actions)
                action.accept(data);
    }
}