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
import xspleet.daggerapi.server.ServerDevModeConfig;

import java.io.*;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "daggerapi";

	public static final int SYMBOLS_PER_LINE = 40;

	public static final Gson JSON_PARSER = new GsonBuilder()
			.setFieldNamingPolicy(
					FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setPrettyPrinting()
			.create();

	@Override
	public void onInitialize() {
		DaggerLogger.debug("Started DaggerAPI in debug mode");
		Mapper.registerMapper();
		ActiveArtifactActivation.registerActivation();
		ServerDevModeConfig.init();

		ItemModel itemModel = new ItemModel();
		DaggerLogger.debug("\n{}", JSON_PARSER.toJson(itemModel));
		Item item = ArtifactItemBuilder.build(itemModel);
		ErrorLogger.validate();

		ArgumentTypeRegistry.registerArgumentType(
				new Identifier(MOD_ID, "attribute"),
				AttributeArgumentType.class,
				ConstantArgumentSerializer.of(AttributeArgumentType::new)
		);
    }
}