package xspleet.daggerapi.trigger;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.data.TriggerData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.data.key.DaggerKeys;

import java.util.*;
import java.util.stream.Collectors;

public class Trigger
{
    private final String name;
    private final Set<PlayerEntity> listeners = new HashSet<>();
    private boolean worldful = false;
    private boolean hasTriggerer = false;

    public Trigger(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addListener(PlayerEntity listener)
    {
        listeners.add(listener);
    }

    public void removeListener(PlayerEntity listener)
    {
        listeners.remove(listener);
    }

    public void trigger(TriggerData data)
    {
        data.addData(DaggerKeys.TRIGGER, this);
        for(PlayerEntity player: listeners) {
            data.addData(DaggerKeys.TRIGGERED, player);

            List<Pair<Integer, ArtifactItem>> artifacts = new ArrayList<>();

            //get all the equipped artifacts of the player in the order of the slots
            TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);
            if (component != null) {
                component.forEach(
                        (slot, stack) -> {
                            if (stack.getItem() instanceof ArtifactItem artifact) {
                                if(artifact.hasTrigger(this))
                                {
                                    int slotIndex = slot.index();
                                    artifacts.add(new Pair<>(slotIndex, artifact));
                                }
                            }
                        }
                );
            }

            artifacts.stream().sorted(Comparator.comparingInt(Pair::getLeft))
                    .map(Pair::getRight)
                    .forEach(item -> {
                        item.receiveTrigger(data);
                    });
        }
    }

    public Trigger setHasTriggerer() {
        hasTriggerer = true;
        return this;
    }

    public Trigger setWorldful() {
        worldful = true;
        return this;
    }

    public boolean isWorldful() {
        return worldful;
    }

    public boolean hasTriggerer() {
        return hasTriggerer;
    }
}
