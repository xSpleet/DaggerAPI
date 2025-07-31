package xspleet.daggerapi.util;

import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DamageTypeUtil
{
    public static DamageType getDamageType(World world, Identifier id)
    {
        return world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .get(id);
    }

    public static RegistryKey<DamageType> getDamageTypeKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id);
    }

    public static Identifier getDamageTypeId(DamageSource damageSource) {
        return damageSource.getTypeRegistryEntry().getKey().orElseThrow().getValue();
    }
}
