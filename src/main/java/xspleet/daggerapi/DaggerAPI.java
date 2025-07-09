package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.base.ErrorLogger;
import xspleet.daggerapi.artifact.builder.ArtifactItemBuilder;
import xspleet.daggerapi.collections.Attributes;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.events.ActiveArtifactActivation;
import xspleet.daggerapi.models.ItemModel;
import xspleet.daggerapi.networking.NetworkingConstants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "daggerapi";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int SYMBOLS_PER_LINE = 40;

	public static final Gson JSON_PARSER = new GsonBuilder()
			.setFieldNamingPolicy(
					FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setPrettyPrinting()
			.create();

	private static final String RESOURCES = "../src/main/resources/";

	@Override
	public void onInitialize() {
		Mapper.registerMapper();
		ActiveArtifactActivation.register();
		NetworkingConstants.init();

		HudRenderCallback.EVENT.register(
				((drawContext, v) -> {
					var client = MinecraftClient.getInstance();
					if(client != null && client.player instanceof AttributeHolder holder) {
						var canWalkOnWater = holder.getAttributeInstance(Attributes.CAN_WALK_ON_WATER);
						var jumpHeight = holder.getAttributeInstance(Attributes.JUMP_HEIGHT);

						drawContext.drawTextWithShadow(
								client.textRenderer,
								"Can walk on water: " + canWalkOnWater.getValue(),
								10, 10, 0xFFFFFF);

						drawContext.drawTextWithShadow(
								client.textRenderer,
								"Jump height: " + jumpHeight.getValue(),
								10, 20, 0xFFFFFF);
					} else {
						LOGGER.warn("Client or player is null, cannot render attributes.");
					}
				})
		);

		ClientPlayNetworking.registerGlobalReceiver(
				NetworkingConstants.SYNC_ATTRIBUTES_PACKET_ID,
				(client, handler, packet, sender) -> {
					var syncContainer = DaggerAttributeContainer.readFromPacket(packet);
					client.execute(() -> {
						var player = client.player;
						if (player instanceof AttributeHolder holder) {
							holder.acceptSyncContainer(syncContainer);
							LOGGER.info("Received attribute sync packet for player: {}", player.getName().getString());
						} else {
							LOGGER.warn("Received attribute sync packet but player is null.");
						}
					});
				}
		);

		Path file = Path.of(RESOURCES, "data/test_item/item.json");
		try {
			ItemModel itemModel = JSON_PARSER.fromJson(Files.newBufferedReader(file), ItemModel.class);

            LOGGER.info("\n{}", JSON_PARSER.toJson(itemModel));
			ArtifactItemBuilder.build(itemModel);

			ErrorLogger.validateItems();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}