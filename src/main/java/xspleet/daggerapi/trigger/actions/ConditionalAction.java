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

import java.util.ArrayList;
import java.util.List;

public class ConditionalAction
{
    protected Condition condition;
    protected final List<String> conditions = new ArrayList<>();
    protected final List<Action> actions;
    protected final List<String> actionNames = new ArrayList<>();
    protected TriggeredBy triggeredBy;
    protected TriggeredIn triggeredIn;

    public ConditionalAction()
    {
        condition = conditionData -> true;
        actions = new ArrayList<>();
    }

    public Condition getCondition()
    {
        return condition;
    }

    public void addCondition(Condition condition, String name)
    {
        conditions.add(name);
        this.condition = this.condition.and(condition);
    }

    public void addAction(Action action, String name)
    {
        actions.add(action);
        actionNames.add(name);
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