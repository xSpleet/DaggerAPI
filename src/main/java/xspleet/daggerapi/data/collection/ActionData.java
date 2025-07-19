package xspleet.daggerapi.data.collection;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.api.models.On;

public class ActionData implements DaggerContext
{
    private final DaggerContext data;

    public ActionData(DaggerContext data)
    {
        this.data = data;
    }

    public Entity getActEntity(On on) {
        return switch (on) {
            case TRIGGERER -> getData(DaggerKeys.TRIGGERER);
            case TRIGGERED -> getData(DaggerKeys.TRIGGERED);
            default -> null;
        };
    }

    public World getActWorld(On on) {
        return on == On.WORLD
                ? getData(DaggerKeys.WORLD)
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
