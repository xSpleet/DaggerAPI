package xspleet.daggerapi.base;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.GroupEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
import xspleet.daggerapi.artifact.builder.ArtifactRarity;
import xspleet.daggerapi.artifact.builder.ErrorLogger;
import xspleet.daggerapi.models.GenerationModel;
import xspleet.daggerapi.models.ItemModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtifactPackParser
{
    public static void readPacks() {
        var packsDir = FabricLoader.getInstance().getGameDir().resolve("daggerapi/packs");

        if (!packsDir.toFile().exists()) {
            DaggerLogger.info("No artifact pack folder found in {}", packsDir);
            DaggerLogger.info("Creating artifact pack folder in {}", packsDir);

            if (!packsDir.toFile().mkdirs()) {
                DaggerLogger.error("Failed to create artifact pack folder in {}", packsDir);
            }

            return;
        }

        Map<String, List<ArtifactItem>> itemGenerationInfo = new HashMap<>();
        Map<Boolean, List<ArtifactItem>> itemsByActivation = new HashMap<>();
        itemsByActivation.put(false, new ArrayList<>());
        itemsByActivation.put(true, new ArrayList<>());


        DaggerLogger.info("Reading artifact packs from {}", packsDir);

        for(var file : packsDir.toFile().listFiles()) {
            if(file.isDirectory()) {
                DaggerLogger.info("Found artifact pack: {}", file.getName());

                var packName = file.getName();
                var packFile = file.toPath().resolve("pack.mcmeta").toFile();

                if (!packFile.exists()) {
                    DaggerLogger.warn("Artifact pack {} does not contain a pack.mcmeta file. Skipping.", packName);
                    continue;
                }

                var data = file.toPath().resolve("data/daggerapi").toFile();
                if (!data.exists() || !data.isDirectory()) {
                    DaggerLogger.warn("Artifact pack {} does not contain a data/daggerapi folder. Skipping.", packName);
                    continue;
                }

                var behaviors = data.toPath().resolve("behaviors").toFile();
                if (!behaviors.exists() || !behaviors.isDirectory()) {
                    DaggerLogger.warn("Artifact pack {} does not contain a data/daggerapi/behaviors folder. Skipping.", packName);
                    continue;
                }

                Map<String, ArtifactItem> registeredItems = new HashMap<>();

                for(var behaviorFile : behaviors.listFiles()) {
                    if(behaviorFile.isFile() && behaviorFile.getName().endsWith(".json")) {
                        DaggerLogger.info("Found behavior file in artifact pack {}: {}", packName, behaviorFile.getName());

                        try {
                            var itemModel = DaggerAPI.JSON_PARSER.fromJson(Files.newBufferedReader(behaviorFile.toPath()), ItemModel.class);
                            var item = ArtifactItemBuilder.build(itemModel);
                            var id = Identifier.of(DaggerAPI.MOD_ID, packName + "/" + itemModel.getName());
                            item = Registry.register(Registries.ITEM, id, item);
                            itemsByActivation.get(itemModel.isActive()).add(item);
                            DaggerLogger.info("Registered artifact item {} with ID {}", item.getName(), id);
                            registeredItems.put(itemModel.getName(), item);
                        }
                        catch (IOException e) {
                            DaggerLogger.error("Failed to read behavior file {} in artifact pack {}: {}", behaviorFile.getName(), packName, e.getMessage());
                        }

                    }
                    else {
                        DaggerLogger.warn("Found unknown file type in artifact pack {}: {}", packName, behaviorFile.getName());
                    }
                }

                if(!ErrorLogger.validate(packName)){
                    DaggerLogger.error("Errors found in artifact pack {}. Skipping item registration.", packName);
                    continue;
                }

                var gens = data.toPath().resolve("gen.json").toFile();
                if(!gens.exists() || !gens.isFile()) {
                    DaggerLogger.warn("Artifact pack {} does not contain a data/daggerapi/gen.json file. Skipping.", packName);
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(gens.toPath())) {
                    var genData = DaggerAPI.JSON_PARSER.fromJson(reader, GenerationModel.class);
                    for (var entry: genData.getGeneration()) {
                        var lootTableId = entry.getLoottable();
                        var items = entry.getEntries();

                        if (lootTableId == null || items == null || items.isEmpty()) {
                            DaggerLogger.warn("Invalid generation entry in artifact pack {}: {}", packName, genData.getGeneration().indexOf(entry));
                            continue;
                        }

                        itemGenerationInfo.computeIfAbsent(lootTableId, k -> new java.util.ArrayList<>()).addAll(
                                registeredItems.entrySet()
                                        .stream()
                                        .filter(e -> items.contains(e.getKey()))
                                        .map(Map.Entry::getValue)
                                        .toList()
                        );
                    }
                } catch (IOException e) {
                    DaggerLogger.error("Failed to read gen.json in artifact pack {}: {}", packName, e.getMessage());
                }

            }
            else {
                DaggerLogger.warn("Found unknown file type in artifact pack folder: {}", file.getName());
            }

        }

        if(ErrorLogger.hasErrors())
            throw new RuntimeException("DaggerAPI: There were problems found with your artifacts, check the logs for more details.");

        DaggerLogger.info("Finished reading artifact packs. Registering loottables...");

        var loottablesToModify = itemGenerationInfo.entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        LootTableEvents.MODIFY.register(
                (resourceManager, lootManager, identifier, builder, lootTableSource) -> {
                    if(loottablesToModify.contains(identifier.toString())) {
                        var items = itemGenerationInfo.get(identifier.toString());

                        var pool = LootPool.builder()
                                .with(new ArtifactPoolEntry(items));

                        builder.pool(pool.build());
                    }
                }
        );

        TagFileCreator.createTags(itemsByActivation);
        //create new data pack with the tags active_artifacts and artifact

        DaggerLogger.info("Registered {} loottables with artifact items.", loottablesToModify.size());
    }
}
