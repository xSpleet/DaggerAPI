package xspleet.daggerapi.base;

import java.util.ArrayList;
import java.util.List;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

public class TrinketsUtil 
{
	static public boolean hasArtifact(LivingEntity livingEntity, Item item)
	{
		return TrinketsApi.getTrinketComponent(livingEntity).isPresent() && TrinketsApi.getTrinketComponent(livingEntity).get().isEquipped(item);
	}
	
	static public ItemEntity dropArtifact(PlayerEntity playerEntity, Item item)
	{
		ItemStack stack = TrinketsApi.getTrinketComponent(playerEntity).get().getEquipped(item).get(0).getRight();
		SlotReference ref = TrinketsApi.getTrinketComponent(playerEntity).get().getEquipped(item).get(0).getLeft();
		ref.inventory().setStack(ref.index(), ItemStack.EMPTY);
		return playerEntity.dropStack(stack);
	}
}
