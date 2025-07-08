package xspleet.daggerapi.trigger.actions;

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
        if(isTriggered(data))
        {
            ActionData actionData = new ActionData(data);
            if (condition.test(new ConditionData(actionData)))
                for (Action action : actions)
                    action.accept(actionData);
        }
    }

    private boolean isTriggered(TriggerData data)
    {
        boolean isTriggered = true;
        PlayerEntity triggered = data.getData(DaggerKeys.TRIGGERED);

        if(triggeredIn != null)
        {
            var world = data.getData(DaggerKeys.WORLD);

            isTriggered &= switch (triggeredIn)
            {
                case SAME_WORLD -> world != null && triggered.getWorld() == world;
                case OTHER_WORLD -> world != null && triggered.getWorld() != world;
                case ANY -> true;
            };
        }

        if(triggeredBy != null)
        {
            var triggerer = data.getData(DaggerKeys.TRIGGERER);

            isTriggered &= switch (triggeredBy)
            {
                case ONLY_SELF -> triggered.equals(triggerer);
                case OTHER_PLAYER -> triggerer instanceof PlayerEntity && !triggered.equals(triggerer);
                case OTHER_LIVING -> triggerer instanceof LivingEntity && !triggered.equals(triggerer);
                case OTHER_ENTITY -> triggerer != null && !triggered.equals(triggerer);
                case ANY_PLAYER -> triggerer instanceof PlayerEntity;
                case ANY_LIVING -> triggerer instanceof LivingEntity;
                case ANY_ENTITY -> triggerer != null;
                case ANY -> true;
            };
        }

        return isTriggered;
    }
}