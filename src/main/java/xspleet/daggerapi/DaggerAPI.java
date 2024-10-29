package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.base.ActiveArtifactActivation;
import xspleet.daggerapi.base.ItemBuilder;
import xspleet.daggerapi.models.ItemModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "dagger-api";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int SYMBOLS_PER_LINE = 40;

	@Override
	public void onInitialize() {
		ActiveArtifactActivation.registerActivation();
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
		File file = new File("../src/main/resources/data/test_item/item.json");
		try {
			ItemModel itemModel = gson.fromJson(new FileReader(file), ItemModel.class);
			ItemBuilder builder = new ItemBuilder(itemModel);
			builder.build();
			LOGGER.info(gson.toJson(itemModel));
		} catch (FileNotFoundException e) {
			LOGGER.info("File not found!");
			LOGGER.info("Searching in: " + file.getAbsolutePath());
		}
	}
}