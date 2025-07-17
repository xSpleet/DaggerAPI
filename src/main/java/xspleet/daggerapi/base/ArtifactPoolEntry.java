package xspleet.daggerapi.base;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootPoolEntryType;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.artifact.builder.ArtifactRarity;

import java.util.List;
import java.util.function.Consumer;

public class ArtifactPoolEntry extends LootPoolEntry {
    private final List<ArtifactItem> items;
    private final List<ArtifactRarity> rarities;

    public ArtifactPoolEntry(List<ArtifactItem> items) {
        this(items, new LootCondition[0]);
    }

    protected ArtifactPoolEntry(List<ArtifactItem> items, LootCondition[] conditions) {
        super(conditions);
        this.items = items;
        this.rarities = items.stream()
                .map(ArtifactItem::getArtifactRarity)
                .distinct()
                .toList();
    }

    @Override
    public LootPoolEntryType getType() {
        return DaggerAPI.ARTIFACT_POOL_ENTRY_TYPE;
    }

    @Override
    public boolean expand(LootContext context, Consumer<LootChoice> choiceConsumer) {
        for(var condition: this.conditions)
        {
            if (!condition.test(context)) {
                return false;
            }
        }

        int weight = context.getRandom().nextInt(100);
        DaggerLogger.debug(LoggingContext.GENERIC, "ArtifactPoolEntry: Generated weight {}", weight);
        int sumWeight = 0;
        for(ArtifactRarity rarity: rarities)
        {
            sumWeight += rarity.getWeight();
            if(weight < sumWeight)
            {
                // We found a rarity that matches the weight
                List<ArtifactItem> filteredItems = items.stream()
                        .filter(item -> item.getArtifactRarity() == rarity)
                        .toList();
                int size = filteredItems.size();
                if (size > 0) {
                    int index = context.getRandom().nextInt(size);
                    ArtifactItem selectedItem = filteredItems.get(index);
                    ItemStack stack = new ItemStack(selectedItem, 1);
                    DaggerLogger.debug(LoggingContext.GENERIC, "ArtifactPoolEntry: Selected item {} with rarity {}", selectedItem, rarity);
                    choiceConsumer.accept(new LootChoice() {
                        @Override
                        public int getWeight(float luck) {
                            return 1;
                        }

                        @Override
                        public void generateLoot(Consumer<ItemStack> lootConsumer, LootContext context) {
                            lootConsumer.accept(stack);
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

    public List<ArtifactItem> getItems() {
        return items;
    }
}
