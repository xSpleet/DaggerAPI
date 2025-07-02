package xspleet.daggerapi.trigger.actions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.collections.ConditionProviders;
import xspleet.daggerapi.data.ActionData;
import xspleet.daggerapi.data.ConditionData;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.models.TriggeredBy;
import xspleet.daggerapi.models.TriggeredIn;
import xspleet.daggerapi.data.TriggerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConditionalAction
{
    private Condition condition;
    private final List<Action> actions;
    private TriggeredBy triggeredBy;
    private TriggeredIn triggeredIn;

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

    public ConditionalAction triggeredBy(TriggeredBy triggeredBy) {
        this.triggeredBy = triggeredBy;
        return this;
    }

    public ConditionalAction triggeredIn(TriggeredIn triggeredIn)
    {
        this.triggeredIn = triggeredIn;
        return this;
    }

    public void actOn(TriggerData data) {
        for(PlayerEntity player: getTriggeredPlayers(data))
        {
            ActionData actionData = new ActionData(data)
                    .addData(DaggerKeys.TRIGGERED, player);
            if (condition.test(new ConditionData(actionData)))
                for (Action action : actions)
                    action.accept(actionData);
        }
    }

    private Set<PlayerEntity> getTriggeredPlayers(TriggerData data)
    {
        Set<PlayerEntity> listeners = data.getListeners();

        if(triggeredIn != null)
        {
            var world = data.getData(DaggerKeys.WORLD);

            listeners = switch (triggeredIn)
            {
                case SAME_WORLD -> listeners.stream()
                        .filter(p -> p.getWorld().equals(world)).collect(Collectors.toSet());

                case OTHER_WORLD -> listeners.stream()
                        .filter(p -> !p.getWorld().equals(world)).collect(Collectors.toSet());

                case ANY -> listeners;
            };
        }

        if(triggeredBy != null)
        {
            var triggerer = data.getData(DaggerKeys.TRIGGERER);

            listeners = switch (triggeredBy)
            {
                case ONLY_SELF -> listeners.stream().filter(l -> l.equals(triggerer)).collect(Collectors.toSet());
                case OTHER_PLAYER -> listeners.stream()
                        .filter(l -> !l.equals(triggerer) && triggerer instanceof PlayerEntity)
                        .collect(Collectors.toSet());
                case OTHER_LIVING -> listeners.stream()
                        .filter(l -> !l.equals(triggerer) && triggerer instanceof LivingEntity)
                        .collect(Collectors.toSet());
                case OTHER_ENTITY -> listeners.stream()
                        .filter(l -> !l.equals(triggerer) && triggerer instanceof Entity)
                        .collect(Collectors.toSet());
                case ANY_PLAYER -> triggerer instanceof PlayerEntity ? listeners : Set.of();
                case ANY_LIVING -> triggerer instanceof LivingEntity ? listeners : Set.of();
                case ANY_ENTITY -> triggerer instanceof Entity ? listeners : Set.of();
                case ANY -> listeners;
            };
        }

        return listeners;
    }
}