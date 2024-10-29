package xspleet.daggerapi.base.actions;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.DaggerData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionalAction
{
    private Predicate<DaggerData> condition;
    private final List<Consumer<DaggerData>> actions;

    public ConditionalAction()
    {
        condition = ConditionProviders.alwaysTrue();
        actions = new ArrayList<>();
    }

    public void addCondition(Predicate<DaggerData> condition)
    {
        this.condition = this.condition.and(condition);
    }

    public void addAction(Consumer<DaggerData> action)
    {
        actions.add(action);
    }

    public void actOn(DaggerData data)
    {
        if(condition.test(data))
            for(Consumer<DaggerData> action: actions)
                action.accept(data);
    }
}