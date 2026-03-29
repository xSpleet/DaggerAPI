package xspleet.daggerapi.trigger.actions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.trigger.Condition;
import xspleet.daggerapi.data.collection.ActionData;
import xspleet.daggerapi.data.collection.ConditionData;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.api.models.TriggeredBy;
import xspleet.daggerapi.api.models.TriggeredIn;
import xspleet.daggerapi.data.collection.TriggerData;

public class ConditionalAction
{
    protected Condition condition;
    protected Action action;
    protected TriggeredBy triggeredBy;
    protected TriggeredIn triggeredIn;

    public ConditionalAction()
    {
        condition = conditionData -> true;
        action = actionData -> {};
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
        this.action = action.andThen(action);
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
            var triggerSource = data.getData(DaggerKeys.TRIGGER_SOURCE);

            isTriggered &= switch (triggeredBy)
            {
                case ONLY_SELF -> triggered.equals(triggerSource);
                case OTHER_PLAYER -> triggerSource instanceof PlayerEntity && !triggered.equals(triggerSource);
                case OTHER_LIVING -> triggerSource instanceof LivingEntity && !triggered.equals(triggerSource);
                case OTHER_ENTITY -> triggerSource != null && !triggered.equals(triggerSource);
                case ANY_PLAYER -> triggerSource instanceof PlayerEntity;
                case ANY_LIVING -> triggerSource instanceof LivingEntity;
                case ANY_ENTITY -> triggerSource != null;
                case ANY -> true;
            };
        }

        return isTriggered;
    }
}