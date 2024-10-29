package xspleet.daggerapi.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DaggerData
{
    private @Nullable PlayerEntity player;
    private @Nullable World world;

    private Map<String, String> data;

    public DaggerData()
    {
        data = new HashMap<>();
        player = null;
        world = null;
    }

    public DaggerData setPlayer(PlayerEntity player)
    {
        this.player = player;
        return this;
    }

    public @Nullable PlayerEntity getPlayer()
    {
        return player;
    }

    public @Nullable World getWorld()
    {
        return world;
    }
    public DaggerData setWorld(World world)
    {
        this.world = world;
        return this;
    }

    public DaggerData addData(String key, String value)
    {
        data.put(key, value);
        return this;
    }
}
