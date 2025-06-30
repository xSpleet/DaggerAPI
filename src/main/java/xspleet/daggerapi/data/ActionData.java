package xspleet.daggerapi.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.models.On;

public class ActionData implements DaggerContext
{
    private final DaggerContext data;

    private @Nullable Entity triggerer;
    private PlayerEntity triggered;
    private World triggeredWorld;

    public ActionData(DaggerContext data)
    {
        this.data = data;
    }

    @Override
    public ActionData addData(String key, String value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public String getData(String key) {
        return data.getData(key);
    }

    public Entity getActEntity(On on) {
        return switch (on) {
            case TRIGGERER -> triggerer;
            case TRIGGERED -> triggered;
            default -> null;
        };
    }

    public World getActWorld(On on) {
        return switch (on) {
            case TRIGGERER -> triggerer != null ? triggerer.getEntityWorld() : null;
            case TRIGGERED -> triggered != null ? triggered.getEntityWorld() : null;
            case WORLD -> triggeredWorld;
            default -> null;
        };
    }

    public ConditionData toConditionData() {
        ConditionData conditionData = new ConditionData(data);
        conditionData.setTriggerer(triggerer)
                      .setTriggered(triggered)
                    .setWorld(triggeredWorld);
        return conditionData;
    }

    public ActionData setTriggerer(@Nullable Entity triggerer) {
        this.triggerer = triggerer;
        return this;
    }

    public ActionData setTriggered(PlayerEntity triggered) {
        this.triggered = triggered;
        return this;
    }
}
