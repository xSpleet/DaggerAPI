package xspleet.daggerapi.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.trigger.Trigger;

import java.util.HashMap;
import java.util.Map;

public class DaggerData implements DaggerContext
{
    protected Map<DaggerKey, Object> data = new HashMap<>();

    @Override
    public <T> DaggerData addData(DaggerKey<T> key, T value) {
        data.put(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        return key.getItem(data.get(key));
    }
}
