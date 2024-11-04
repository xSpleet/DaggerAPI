package xspleet.daggerapi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xspleet.daggerapi.base.ActiveArtifactActivation;
import xspleet.daggerapi.base.ErrorLogger;
import xspleet.daggerapi.base.ItemBuilder;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.collections.Operations;
import xspleet.daggerapi.models.ItemModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DaggerAPI implements ModInitializer {
	public static final String MOD_ID = "daggerapi";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int SYMBOLS_PER_LINE = 40;

	private static final String RESOURCES = "../src/main/resources/";

	@Override
	public void onInitialize() {
		Mapper.registerMapper();
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
		File file = new File(RESOURCES + "data/test_item/item.json");
		try {
			ItemModel itemModel = gson.fromJson(new FileReader(file), ItemModel.class);
			String modelFileName = RESOURCES + "assets/daggerapi/models/item/" + itemModel.name + ".json";
			String modelFolderName = RESOURCES + "assets/daggerapi/models/item";
			String texturesFolderName = RESOURCES + "assets/daggerapi/textures/item";
			String textureCopyFileName = texturesFolderName + "/" + itemModel.name + ".png";
			String textureFileName = RESOURCES + "data/" + itemModel.name + "/item.png";

			FileUtils.cleanDirectory(new File(modelFolderName));
			FileUtils.cleanDirectory(new File(texturesFolderName));

			LOGGER.info(gson.toJson(itemModel));
			ItemBuilder.build(itemModel);
			ErrorLogger.validateItems();
			/*File model = new File(modelFileName);
			FileUtils.touch(model);
			FileWriter fileWriter = new FileWriter(modelFileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.printf("""
					{
					  "parent": "minecraft:item/generated",
					  "textures": {
					    "layer0": "daggerapi:item/""" + itemModel.name + "\"\n" +
					"""
					  }
					}
					""");
			printWriter.close();
			Path copied = Paths.get(textureCopyFileName);
			Path originalPath = Paths.get(textureFileName);
			Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);*/
		} catch (FileNotFoundException e) {
			LOGGER.info("File not found!");
			LOGGER.info("Searching in: " + file.getAbsolutePath());
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}