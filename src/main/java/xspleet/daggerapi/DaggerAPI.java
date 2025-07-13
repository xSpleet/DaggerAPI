package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.artifact.builder.ErrorLogger;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.commands.AttributeArgumentType;
import xspleet.daggerapi.events.ActiveArtifactActivation;
import xspleet.daggerapi.models.ConfigModel;
import xspleet.daggerapi.models.ItemModel;
import xspleet.daggerapi.networking.NetworkingConstants;

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

	public static boolean SERVER_DEV_MODE = false;
	private static final String RESOURCES = "../src/main/resources/";

	@Override
	public void onInitialize() {
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			var configs = FabricLoader.getInstance().getConfigDir();
			var config = configs.resolve("daggerapi.json").toFile();
			if(!config.exists()) {
				DaggerLogger.warn("DaggerAPI config file not found at " + config.getAbsolutePath() + ". Creating default one");
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(config))) {
					var defaultConfig = new ConfigModel();
					JSON_PARSER.toJson(defaultConfig, writer);
					DaggerLogger.warn("Default DaggerAPI config created at " + config.getAbsolutePath());
				} catch (IOException e) {
					DaggerLogger.error("Failed to create DaggerAPI config file: " + e.getMessage());
				}
			}
			try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
				var json = JSON_PARSER.fromJson(reader, ConfigModel.class);
				DaggerLogger.debug("DaggerAPI config loaded: " + json);
				if(json.isDevMode()) {
					DaggerLogger.warn("DaggerAPI is running in development mode. This is not recommended for production servers.");
				}
				DaggerAPI.SERVER_DEV_MODE = json.isDevMode();
			} catch (IOException e) {
				DaggerLogger.error("Failed to read DaggerAPI config file: " + e.getMessage());
			}

			ServerPlayConnectionEvents.JOIN.register(
					(handler, sender, server) -> {
						PacketByteBuf buf = PacketByteBufs.create();
						buf.writeBoolean(DaggerAPI.SERVER_DEV_MODE);
						ServerPlayNetworking.send(handler.getPlayer(), NetworkingConstants.SYNC_DEV_MODE_PACKET_ID, buf);
					}
			);
		}

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
    }
}