package xspleet.daggerapi.base;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.exceptions.*;
import xspleet.daggerapi.models.JSONModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class ErrorLogger
{
    private static final Map<String, List<String>> errors = new HashMap<>();

    public static void validateItems() throws IOException {
        if(!errors.isEmpty())
        {
            String now = LocalDateTime.now()
                    .toString()
                    .replace(':', '-')
                    .replace('T', '_')
                    .split("\\.")[0];
            Path path = FabricLoader.getInstance().getGameDir();
            Path newFile = Files.createFile(Path.of(path.toString(), "daggerapi/"
                    + now
                    + "-error.log"));
            Path newConciseFile = Files.createFile(Path.of(path.toString(), "daggerapi/"
                    + now
                    + "-concise-error.log"));

            PrintWriter writer = new PrintWriter(Files.newBufferedWriter(newFile));
            PrintWriter conciseWriter = new PrintWriter(Files.newBufferedWriter(newConciseFile));

            errors.forEach(
                    (itemName, itemModelErrors) ->
                    {
                        writer.println(itemName + ":");
                        for (String error : itemModelErrors) {
                            writer.println();
                            writer.printf("%s\n", error);
                        }
                        writer.println();
                        conciseWriter.println(itemName + ":");
                        for (String error : itemModelErrors) {
                            conciseWriter.println();
                            conciseWriter.printf("%s\n", cutAfter(error, 11));
                        }
                        conciseWriter.println();
                    }
            );

            writer.close();
            conciseWriter.close();

            throw new DaggerAPIException();
        }
    }

    public static String cutAfter(String string, int lines)
    {
        String[] split = string.split("\\n");
        if(split.length <= lines)
            return String.join("\n", split);
        else
            return String.join("\n", Arrays.copyOfRange(split, 0, lines)) + "\n...";
    }

    public static void log(String itemName, NoSuchTriggerException exception, JSONModel model)
    {
        errors.putIfAbsent(itemName, new ArrayList<String>());
        errors.get(itemName).add("No such Trigger: '" + exception.getName() + "' in \n" + DaggerAPI.JSON_PARSER.toJson(model));
    }

    public static void log(String itemName, NoSuchConditionException exception, JSONModel model)
    {
        errors.putIfAbsent(itemName, new ArrayList<String>());
        errors.get(itemName).add("No such Condition: '" + exception.getName() + "' in \n" + DaggerAPI.JSON_PARSER.toJson(model));
    }

    public static void log(String itemName, NoSuchActionException exception, JSONModel model)
    {
        errors.putIfAbsent(itemName, new ArrayList<String>());
        errors.get(itemName).add("No such Action: '" + exception.getName() + "' in \n" + DaggerAPI.JSON_PARSER.toJson(model));
    }

    public static void log(String itemName, NoSuchOperationException exception, JSONModel model)
    {
        errors.putIfAbsent(itemName, new ArrayList<String>());
        errors.get(itemName).add("No such Operation: '" + exception.getName() + "' in \n" + DaggerAPI.JSON_PARSER.toJson(model));
    }

    public static void log(String itemName, NoSuchAttributeException exception, JSONModel model)
    {
        errors.putIfAbsent(itemName, new ArrayList<String>());
        errors.get(itemName).add("No such Entity Attribute: '" + exception.getName() + "' in \n" + DaggerAPI.JSON_PARSER.toJson(model));
    }
}
