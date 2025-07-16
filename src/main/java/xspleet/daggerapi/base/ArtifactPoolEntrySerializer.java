package xspleet.daggerapi.base;

import com.google.gson.*;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.exceptions.ParseException;

import java.util.ArrayList;

public class ArtifactPoolEntrySerializer extends LootPoolEntry.Serializer<ArtifactPoolEntry> {
    @Override
    public void addEntryFields(JsonObject json, ArtifactPoolEntry entry, JsonSerializationContext context) {
        var items = entry.getItems();
        JsonArray itemsArray = new JsonArray();

        for(var item : items) {
            itemsArray.add(Registries.ITEM.getId(item).toString());
        }

        json.add("items", itemsArray);
    }

    @Override
    public ArtifactPoolEntry fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
        var itemsArray = json.getAsJsonArray("items");
        if(itemsArray == null) {
            throw new IllegalArgumentException("Items array is missing in ArtifactPoolEntry JSON");
        }

        var items = new ArrayList<ArtifactItem>();

        for(var item : itemsArray) {
            var id = new Identifier(item.getAsString());
            var artifactItem = Registries.ITEM.get(id);
            if (!(artifactItem instanceof ArtifactItem)) {
                throw new JsonParseException("Item with ID " + id + " is not an ArtifactItem");
            }

            items.add((ArtifactItem) artifactItem);
        }

        if(items.isEmpty()) {
            throw new JsonParseException("No valid ArtifactItems found in the provided JSON");
        }

        return new ArtifactPoolEntry(items, conditions);
    }
}
