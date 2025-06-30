package xspleet.daggerapi.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xspleet.daggerapi.trigger.Trigger;

import java.util.HashMap;
import java.util.Map;

public class DaggerData implements DaggerContext
{
    protected @Nullable PlayerEntity player = null;
    protected @Nullable World world = null;
    protected Map<String, String> data = new HashMap<>();

    public DaggerData setPlayer(PlayerEntity player) {
        this.player = player;
        return this;
    }

    public @Nullable PlayerEntity getPlayer() {
        return player;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public DaggerData setWorld(World world) {
        this.world = world;
        return this;
    }

    public @Nullable World getWorld() {
        return world;
    }

    public boolean hasWorld() {
        return world != null;
    }

    public DaggerData setTrigger(Trigger trigger) {
        addData("trigger", trigger.getName());
        return this;
    }

    @Override
    public DaggerData addData(String key, String value) {
        data.put(key, value);
        return this;
    }

    @Override
    public String getData(String key) {
        return data.get(key);
    }
}
