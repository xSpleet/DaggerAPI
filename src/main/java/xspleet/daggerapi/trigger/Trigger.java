package xspleet.daggerapi.trigger;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.data.collection.TriggerData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;

import java.util.*;

public class Trigger
{
    private final String name;
    private final Set<PlayerEntity> listeners = new HashSet<>();
    private boolean worldful = false;
    private boolean hasTriggerer = false;
    private final Set<DaggerKey<?>> providedData = new HashSet<>();

    public Trigger(String name) {
        this.name = name;
        providedData.addAll(Set.of(
                DaggerKeys.TRIGGER,
                DaggerKeys.TRIGGERED
        ));
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
            TrinketsApi.getTrinketComponent(player).ifPresent(component -> component.forEach(
                    (slot, stack) -> {
                        if (stack.getItem() instanceof ArtifactItem artifact) {
                            if (artifact.hasTrigger(this)) {
                                int slotIndex = slot.index();
                                artifacts.add(new Pair<>(slotIndex, artifact));
                            }
                        }
                    }
            ));

            artifacts.stream().sorted(Comparator.comparingInt(Pair::getLeft))
                    .map(Pair::getRight)
                    .forEach(item -> {
                        if(player instanceof ServerPlayerEntity) {
                            item.receiveTrigger(data);
                        }
                    });
        }
    }

    public Trigger setHasTriggerer() {
        hasTriggerer = true;
        providedData.add(DaggerKeys.TRIGGERER);
        return this;
    }

    public Trigger setWorldful() {
        worldful = true;
        providedData.add(DaggerKeys.WORLD);
        return this;
    }

    public Trigger addProvidedData(DaggerKey<?> key) {
        providedData.add(key);
        return this;
    }

    public Set<DaggerKey<?>> getProvidedData() {
        return Collections.unmodifiableSet(providedData);
    }

    public boolean isWorldful() {
        return worldful;
    }

    public boolean hasTriggerer() {
        return hasTriggerer;
    }
}
