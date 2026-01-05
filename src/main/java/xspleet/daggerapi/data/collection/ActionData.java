package xspleet.daggerapi.data.collection;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;

public class ActionData implements DaggerContext
{
    private final DaggerContext data;

    public ActionData(DaggerContext data)
    {
        this.data = data;
    }

    public Entity getActEntity(DaggerKey<?> on) {
        return Entity.class.isAssignableFrom(on.type()) ? (Entity) data.getData(on) : null;
    }

    public World getActWorld(DaggerKey<?> on) {
        return World.class.isAssignableFrom(on.type())
                ? (World) data.getData(on)
                : getActEntity(on) != null
                    ? getActEntity(on).getWorld()
                    : null;
    }

    @Override
    public <T> ActionData addData(DaggerKey<T> key, T value) {

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
