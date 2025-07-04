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
    protected ThreadLocal<HashMap<DaggerKey, Object>> data = ThreadLocal.withInitial(HashMap::new);

    @Override
    public <T> DaggerData addData(DaggerKey<T> key, T value) {
        data.get().put(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        if(!data.get().containsKey(key)) {
            return null;
        }
        return key.getItem(data.get().get(key));
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return data.get().containsKey(key);
    }
}
