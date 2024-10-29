package xspleet.daggerapi.base.actions;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.DaggerData;
import xspleet.jdagapi.providers.ConditionProvider;

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

    public void addCondition(Predicate<PlayerEntity> condition)
    {
        this.condition = this.condition.and(condition);
    }

    public void addAction(Consumer<PlayerEntity> action)
    {
        actions.add(action);
    }

    public void actOn(PlayerEntity player)
    {
        if(condition.test(player))
            for(Consumer<PlayerEntity> action: actions)
                action.accept(player);
    }
}