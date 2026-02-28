package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.api.pack.ArtifactPackParser;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.artifact.generation.ArtifactPoolEntrySerializer;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.commands.AttributeArgumentType;
import xspleet.daggerapi.commands.ServerCommands;
import xspleet.daggerapi.api.collections.VariablePaths;
import xspleet.daggerapi.events.ActiveArtifactActivation;
import xspleet.daggerapi.config.ServerDevModeConfig;
import xspleet.daggerapi.events.WakeUpEventTriggerRegistration;
import xspleet.daggerapi.networking.ServerNetworking;
import xspleet.daggerapi.trigger.PlayerEntityAllowSleepingHandler;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "daggerapi";

	public static final int SYMBOLS_PER_LINE = 40;

	public static final Gson JSON_PARSER = new GsonBuilder()
			.setFieldNamingPolicy(
					FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setPrettyPrinting()
			.create();

	public static final LootPoolEntryType ARTIFACT_POOL_ENTRY_TYPE = Registry.register(
			Registries.LOOT_POOL_ENTRY_TYPE,
			new Identifier(MOD_ID, "artifact_pool_entry"),
			new LootPoolEntryType(new ArtifactPoolEntrySerializer())
	);

	public static final boolean DEBUG_MODE = FabricLoader.getInstance().isDevelopmentEnvironment();

	public static final RegistryKey<ItemGroup> ARTIFACT_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier(DaggerAPI.MOD_ID, "item_group"));
	public static final ItemGroup ARTIFACT_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(Items.NETHER_STAR))
			.displayName(Text.translatable("daggerapi.itemgroup.artifacts"))
			.build();

	@Override
	public void onInitialize() {
		DaggerLogger.debug(LoggingContext.STARTUP, "Started DaggerAPI in debug mode");
		Mapper.registerMapper();
		VariablePaths.registerVariablePaths();
		ArtifactPackParser.readPacks();

		ActiveArtifactActivation.registerActivation();
		ServerDevModeConfig.init();
		ServerNetworking.init();
		ServerCommands.registerCommands();

		ArgumentTypeRegistry.registerArgumentType(
				new Identifier(MOD_ID, "attribute"),
				AttributeArgumentType.class,
				ConstantArgumentSerializer.of(AttributeArgumentType::new)
		);

		EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register(new PlayerEntityAllowSleepingHandler());
		EntitySleepEvents.STOP_SLEEPING.register(new WakeUpEventTriggerRegistration());
		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> true);
	}
}