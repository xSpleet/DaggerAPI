package xspleet.daggerapi.trigger;

import net.minecraft.item.Item;

public record TriggerTrackEntry(Trigger trigger, Item item, long tick) {
}
