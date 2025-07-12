package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.artifact.builder.ErrorLogger;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.commands.AttributeArgumentType;
import xspleet.daggerapi.commands.ServerCommands;
import xspleet.daggerapi.events.ActiveArtifactActivation;
import xspleet.daggerapi.models.ItemModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "daggerapi";

	public static final int SYMBOLS_PER_LINE = 40;

	public static final Gson JSON_PARSER = new GsonBuilder()
			.setFieldNamingPolicy(
					FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setPrettyPrinting()
			.create();

	public static final boolean DEBUG_MODE = true; // Set to false in production

	private static final String RESOURCES = "../src/main/resources/";

	@Override
	public void onInitialize() {
		DaggerLogger.debug("Started DaggerAPI in debug mode");
		Mapper.registerMapper();
		ActiveArtifactActivation.registerActivation();

		Path file = Path.of(RESOURCES, "data/test_item/item.json");
		try {
			ItemModel itemModel = JSON_PARSER.fromJson(Files.newBufferedReader(file), ItemModel.class);

            DaggerLogger.debug("\n{}", JSON_PARSER.toJson(itemModel));
			Item item = ArtifactItemBuilder.build(itemModel);

			ErrorLogger.validate();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		ArgumentTypeRegistry.registerArgumentType(
				new Identifier(MOD_ID, "attribute"),
				AttributeArgumentType.class,
				ConstantArgumentSerializer.of(AttributeArgumentType::new)
		);
		ServerCommands.registerCommands();
    }
}