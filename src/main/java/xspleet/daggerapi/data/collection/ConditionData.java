package xspleet.daggerapi.data.collection;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
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

    public Entity getTestEntity(DaggerKey<?> on) {
        return Entity.class.isAssignableFrom(on.type()) ? (Entity) data.getData(on) : null;
    }

    public World getTestWorld(DaggerKey<?> on) {
        return World.class.isAssignableFrom(on.type())
                ? (World) data.getData(on)
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

