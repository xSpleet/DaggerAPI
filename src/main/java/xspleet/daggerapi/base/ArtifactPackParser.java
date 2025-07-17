package xspleet.daggerapi.base;

import io.netty.handler.logging.LogLevel;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.loot.LootPool;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
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
            DaggerLogger.info(LoggingContext.PARSING, "No artifact pack folder found in {}", packsDir);
            DaggerLogger.info(LoggingContext.PARSING, "Creating artifact pack folder in {}", packsDir);

            if (!packsDir.toFile().mkdirs()) {
                DaggerLogger.error(LoggingContext.PARSING, "Failed to create artifact pack folder in {}", packsDir);
            }
            return;
        }

        Map<String, List<ArtifactItem>> itemGenerationInfo = new HashMap<>();
        Map<Boolean, List<ArtifactItem>> itemsByActivation = new HashMap<>();
        itemsByActivation.put(false, new ArrayList<>());
        itemsByActivation.put(true, new ArrayList<>());


        DaggerLogger.info(LoggingContext.PARSING, "Reading artifact packs from {}", packsDir.toString());

        for(var file : packsDir.toFile().listFiles()) {
            if(file.isDirectory()) {
                var packName = file.getName();
                var packFile = file.toPath().resolve("pack.mcmeta").toFile();

                if(packName.equalsIgnoreCase("tags"))
                    continue;

                DaggerLogger.setCurrentPack(packName);
                DaggerLogger.debug(LoggingContext.PARSING, "Found artifact pack: {}", file.getName());

                if (!packFile.exists()) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.WARN, "Artifact pack does not contain a pack.mcmeta file. Skipping.");
                    continue;
                }

                var data = file.toPath().resolve("data/daggerapi").toFile();
                if (!data.exists() || !data.isDirectory()) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.WARN, "Artifact pack does not contain a data/daggerapi folder. Skipping.");
                    continue;
                }

                var behaviors = data.toPath().resolve("behaviors").toFile();
                if (!behaviors.exists() || !behaviors.isDirectory()) {
                    DaggerLogger.report(LoggingContext.PARSING, LogLevel.WARN, "Artifact pack does not contain a data/daggerapi/behaviors folder. Skipping.");
                    continue;
                }

                Map<String, ArtifactItem> registeredItems = new HashMap<>();

                for(var behaviorFile : behaviors.listFiles()) {
                    if(behaviorFile.isFile() && behaviorFile.getName().endsWith(".json")) {
                        DaggerLogger.debug(LoggingContext.PARSING, "Found behavior file in artifact pack {}: {}", packName, behaviorFile.getName());

                        try {
                            var itemModel = DaggerAPI.JSON_PARSER.fromJson(Files.newBufferedReader(behaviorFile.toPath()), ItemModel.class);
                            var item = ArtifactItemBuilder.build(itemModel);
                            var id = Identifier.of(DaggerAPI.MOD_ID, packName + "/" + itemModel.getName());
                            item = Registry.register(Registries.ITEM, id, item);
                            itemsByActivation.get(itemModel.isActive()).add(item);
                            DaggerLogger.debug(LoggingContext.PARSING, "Registered artifact item {} with ID {}", item.getName(), id);
                            registeredItems.put(itemModel.getName(), item);
                        }
                        catch (IOException e) {
                            DaggerLogger.error(LoggingContext.PARSING, "Failed to read behavior file {}: {}", behaviorFile.getName(), e.getMessage());
                        }

                    }
                    else {
                        DaggerLogger.warn(LoggingContext.PARSING, "Found unknown file type: {}", behaviorFile.getName());
                    }
                }

                if(DaggerLogger.hasErrors(packName)){
                    DaggerLogger.error(LoggingContext.PARSING, "Errors found in artifact pack. Skipping item registration.", packName);
                    continue;
                }

                var gens = data.toPath().resolve("gen.json").toFile();
                if(!gens.exists() || !gens.isFile()) {
                    DaggerLogger.warn(LoggingContext.PARSING,"Artifact pack does not contain a data/daggerapi/gen.json file. Skipping.");
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(gens.toPath())) {
                    var genData = DaggerAPI.JSON_PARSER.fromJson(reader, GenerationModel.class);
                    for (var entry: genData.getGeneration()) {
                        var lootTableId = entry.getLoottable();
                        var items = entry.getEntries();

                        if (lootTableId == null || items == null || items.isEmpty()) {
                            DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Invalid generation entry number {}", genData.getGeneration().indexOf(entry));
                            continue;
                        }

                        itemGenerationInfo.computeIfAbsent(lootTableId, k -> new java.util.ArrayList<>());
                        for (var itemName : items) {
                            var item = registeredItems.get(itemName);
                            if (item == null) {
                                DaggerLogger.report(LoggingContext.PARSING, LogLevel.ERROR, "Item {} not found in artifact pack {}, but referenced in generation entry {}", itemName, packName, genData.getGeneration().indexOf(entry));
                                continue;
                            }
                            itemGenerationInfo.get(lootTableId).add(item);
                        }
                    }
                } catch (IOException e) {
                    DaggerLogger.error(LoggingContext.PARSING, "Failed to read gen.json in artifact pack {}: {}", packName, e.getMessage());
                }

            }
            else {
                DaggerLogger.warn(LoggingContext.PARSING, file.getName());
            }

        }

        if(DaggerLogger.hasErrors()) {
            DaggerLogger.dumpAll(LogLevel.WARN);
            throw new RuntimeException("DaggerAPI: There were problems found with your artifacts, check the logs for more details.");
        }

        DaggerLogger.dumpAll(LogLevel.WARN);
        DaggerLogger.removeCurrentPack();

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

        DaggerLogger.info(LoggingContext.STARTUP, "Registered {} artifact items", itemsByActivation.get(true).size() + itemsByActivation.get(false).size());
        DaggerLogger.info(LoggingContext.STARTUP, "Registered {} loottables with artifact items.", loottablesToModify.size());
        DaggerLogger.info(LoggingContext.STARTUP, "Finished reading artifact packs.");
    }
}
