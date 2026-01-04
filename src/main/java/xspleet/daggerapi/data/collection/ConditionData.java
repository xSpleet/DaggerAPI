package xspleet.daggerapi.data.collection;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import xspleet.daggerapi.api.models.OnModel;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;

public class ConditionData implements DaggerContext
{
    private final DaggerContext data;

    public ConditionData(DaggerContext data)
    {
        this.data = data;
    }

    public ConditionData(){
        this.data = new DaggerData();
    }

    public Entity getTestEntity(OnModel on) {
        return switch (on) {
            case SOURCE -> getData(DaggerKeys.TRIGGER_SOURCE);
            case TRIGGERED -> getData(DaggerKeys.TRIGGERED);
            case SELF -> getData(DaggerKeys.PLAYER);
            case WORLD -> null;
        };
    }

    public World getTestWorld(OnModel on) {
        return on == OnModel.WORLD
                ? getData(DaggerKeys.WORLD)
                : getTestEntity(on) != null
                    ? getTestEntity(on).getWorld()
                    : null;
    }

    @Override
    public <T> ConditionData addData(DaggerKey<T> key, T value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        return data.getData(key);
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return data.hasData(key);
    }
}

