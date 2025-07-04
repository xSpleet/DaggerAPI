package xspleet.daggerapi.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.trigger.Trigger;

import java.util.Set;

public class TriggerData implements DaggerContext {
    private final DaggerContext data;

    public TriggerData(DaggerContext data) {
        this.data = data;
    }

    public TriggerData() {
        this.data = new DaggerData();
    }

    @Override
    public <T> TriggerData addData(DaggerKey<T> key, T value) {
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
