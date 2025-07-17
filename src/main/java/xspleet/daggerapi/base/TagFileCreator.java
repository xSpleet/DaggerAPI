package xspleet.daggerapi.base;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.models.TagModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagFileCreator
{
    public static void createTags(Map<Boolean, List<ArtifactItem>> items)
    {
        var activeArtifacts = items.get(true);
        var artifacts = items.get(false);
        var artifactTag = new Identifier(DaggerAPI.MOD_ID, "artifact");
        var activeArtifactTag = new Identifier(DaggerAPI.MOD_ID, "active_artifact");

        var tagDataPack = FabricLoader.getInstance().getGameDir().resolve("daggerapi/packs/tags");
        if(!tagDataPack.toFile().exists()) {
            try {
                Files.createDirectories(tagDataPack);
            } catch (IOException e) {
                DaggerLogger.error( LoggingContext.STARTUP, "Failed to create tag data pack directory: {}", e.getMessage());
                return;
            }
        }

        var dataFolder = tagDataPack.resolve("data/daggerapi/tags/items");

        if(!dataFolder.toFile().exists()) {;
            try {
                Files.createDirectories(dataFolder);
            } catch (IOException e) {
                DaggerLogger.error(LoggingContext.STARTUP, "Failed to create data folder for tags: {}", e.getMessage());
                return;
            }
        }

        createTagFile(artifacts, dataFolder, "artifacts");
        createTagFile(activeArtifacts, dataFolder, "active_artifacts");

        //create pack.mcmeta file
        var packMetaFile = tagDataPack.resolve("pack.mcmeta");
        try (var writer = Files.newBufferedWriter(packMetaFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            var packMeta = DaggerAPI.JSON_PARSER.toJson(
                    Map.of("pack", Map.of("pack_format", 15, "description", "DaggerAPI Artifact Tags - Service Pack")));
            writer.write(packMeta);
        } catch (IOException e) {
            DaggerLogger.error(LoggingContext.STARTUP, "Failed to write pack.mcmeta file: {}", e.getMessage());
        }
    }

    private static void createTagFile(List<ArtifactItem> artifacts, Path tagDataPack, String tagName)
    {
        var artifactTagFile = tagDataPack.resolve(tagName + ".json");

        try (var writer = Files.newBufferedWriter(artifactTagFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            var tagModel = new TagModel(false, artifacts
                    .stream()
                    .map(item -> item.getIdentifier().toString())
                    .toList());
            writer.write(DaggerAPI.JSON_PARSER.toJson(tagModel, TagModel.class));
        } catch (IOException e) {
            DaggerLogger.error(LoggingContext.STARTUP, "Failed to write artifact tag file: {}", e.getMessage());
        }
    }
}
