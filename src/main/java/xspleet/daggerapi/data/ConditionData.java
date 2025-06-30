package xspleet.daggerapi.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.models.On;

public class ConditionData implements DaggerContext
{
    private final DaggerContext data;

    private @Nullable Entity triggerer;
    private @Nullable PlayerEntity triggered;
    private @Nullable PlayerEntity player;
    private @Nullable World world;

    public ConditionData(DaggerContext data)
    {
        this.data = data;
    }

    public ConditionData(){
        this.data = new DaggerData();
    }

    @Override
    public ConditionData addData(String key, String value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public String getData(String key) {
        return data.getData(key);
    }

    public @Nullable Entity getTriggerer() {
        return triggerer;
    }

    public ConditionData setTriggerer(Entity triggerer) {
        this.triggerer = triggerer;
        return this;
    }

    public @Nullable PlayerEntity getTriggered() {
        return triggered;
    }

    public ConditionData setTriggered(PlayerEntity triggered) {
        this.triggered = triggered;
        return this;
    }

    public Entity getTestEntity(On on) {
        return switch (on) {
            case TRIGGERER -> triggerer;
            case TRIGGERED -> triggered;
            case SELF -> player;
            case WORLD -> null;
        };
    }

    public World getTestWorld(On on) {
        return switch (on) {
            case TRIGGERER -> triggerer != null ? triggerer.getWorld() : null;
            case TRIGGERED -> triggered != null ? triggered.getWorld() : null;
            case SELF -> player != null ? player.getWorld() : null;
            case WORLD -> world;
        };
    }

    public @Nullable PlayerEntity getPlayer() {
        return player;
    }

    public ConditionData setPlayer(PlayerEntity player) {
        this.player = player;
        this.world = player.getWorld();
        return this;
    }

    public ActionData toActionData() {
        ActionData actionData = new ActionData(data);
        actionData.setTriggerer(triggerer)
                  .setTriggered(triggered);
        return actionData;
    }

    public @Nullable World getWorld() {
        return world;
    }

    public ConditionData setWorld(@Nullable World world) {
        this.world = world;
        return this;
    }
}

