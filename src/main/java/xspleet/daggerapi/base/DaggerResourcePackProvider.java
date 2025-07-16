package xspleet.daggerapi.base;

import net.fabricmc.fabric.impl.resource.loader.BuiltinModResourcePackSource;
import net.fabricmc.fabric.impl.resource.loader.FabricModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;

public class DaggerResourcePackProvider implements ResourcePackProvider {
    private final Path packDir;
    private final ResourceType type;

    public DaggerResourcePackProvider(ResourceType type) {
        this.packDir = FabricLoader.getInstance().getGameDir().resolve("daggerapi/packs");
        if(!packDir.toFile().exists() || !packDir.toFile().isDirectory())
            throw new IllegalArgumentException("Provided path is not a directory: " + packDir);
        this.type = type;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        for(var file : packDir.toFile().listFiles()) {
            if(file.isDirectory()) {
                var packName = file.getName();
                var profile = ResourcePackProfile.create(
                            "daggerapi/" + packName,
                            Text.literal("DaggerAPI Artifact Pack: " + packName),
                            true,
                            (name) -> new DirectoryResourcePack("daggerapi/" + packName, file.toPath(), true),
                            type,
                            ResourcePackProfile.InsertionPosition.TOP,
                            ResourcePackSource.FEATURE
                            );
                profileAdder.accept(profile);
            }
        }
    }
}
