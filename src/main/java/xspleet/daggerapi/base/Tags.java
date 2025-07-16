package xspleet.daggerapi.base;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;

public class Tags
{
    public static final TagKey<Item> ARTIFACTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(DaggerAPI.MOD_ID, "artifacts"));
    public static final TagKey<Item> ACTIVE_ARTIFACTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(DaggerAPI.MOD_ID, "active_artifacts"));
}
