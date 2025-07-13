package xspleet.daggerapi.base;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
import xspleet.daggerapi.artifact.builder.ErrorLogger;
import xspleet.daggerapi.models.ItemModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
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

                Map<String, Item> registeredItems = new HashMap<>();

                for(var behaviorFile : behaviors.listFiles()) {
                    if(behaviorFile.isFile() && behaviorFile.getName().endsWith(".json")) {
                        DaggerLogger.info("Found behavior file in artifact pack {}: {}", packName, behaviorFile.getName());

                        try {
                            var itemModel = DaggerAPI.JSON_PARSER.fromJson(Files.newBufferedReader(behaviorFile.toPath()), ItemModel.class);
                            var item = ArtifactItemBuilder.build(itemModel);
                            var id = Identifier.of(packName, itemModel.getName());
                            registeredItems.put(itemModel.getName(), item);
                            DaggerLogger.info("Registered item {} from artifact pack {}", id.toString(), packName);
                        }
                        catch (IOException e) {
                            DaggerLogger.error("Failed to read behavior file {} in artifact pack {}: {}", behaviorFile.getName(), packName, e.getMessage());
                        }

                    }
                    else {
                        DaggerLogger.warn("Found unknown file type in artifact pack {}: {}", packName, behaviorFile.getName());
                    }
                }

                var gens = data.toPath().resolve("gen").toFile();
                if(!gens.exists() || !gens.isDirectory()) {
                    DaggerLogger.warn("Artifact pack {} does not contain a data/daggerapi/gen folder. Skipping.", packName);
                    continue;
                }

                for(var genFile : gens.listFiles()) {
                    if(genFile.isFile() && genFile.getName().endsWith(".json")) {
                        DaggerLogger.info("Found generation file in artifact pack {}: {}", packName, genFile.getName());

                    }
                    else {
                        DaggerLogger.warn("Found unknown file type in artifact pack {}: {}", packName, genFile.getName());
                    }
                }

            }
            else {
                DaggerLogger.warn("Found unknown file type in artifact pack folder: {}", file.getName());
            }
        }
    }
}
