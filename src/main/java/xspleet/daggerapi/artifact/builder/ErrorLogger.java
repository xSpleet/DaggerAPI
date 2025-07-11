package xspleet.daggerapi.artifact.builder;

import net.fabricmc.loader.api.FabricLoader;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.exceptions.DaggerAPIException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class ErrorLogger
{
    private static final Map<String, List<String>> errors = new HashMap<>();

    public static void log(String itemName, String place, DaggerAPIException exception) {
        log(itemName, place, exception.getMessage());
    }

    public static void log(String itemName, String place, String message) {
        DaggerLogger.error("Error in {} at {}: {}", itemName, place, message);
        errors.putIfAbsent(itemName, new ArrayList<>());
        errors.get(itemName).add(place + " : " + message);
    }

    public static void validate(){
        if(!errors.isEmpty())
        {
            try {
                String now = LocalDateTime.now()
                        .toString()
                        .replace(':', '-')
                        .replace('T', '_')
                        .split("\\.")[0];
                Path path = FabricLoader.getInstance().getGameDir();
                Path folder = Path.of(path.toString(), "daggerapi");
                if(!Files.exists(folder))
                    Files.createDirectories(folder);
                Path newFile = Files.createFile(Path.of(path.toString(), "daggerapi/"
                        + now
                        + "-error.log"));

                PrintWriter writer = new PrintWriter(Files.newBufferedWriter(newFile));

                errors.forEach(
                        (itemName, itemModelErrors) ->
                        {
                            writer.println(itemName + ":");
                            for (String error : itemModelErrors) {
                                writer.printf("\t > %s\n", error);
                            }
                            writer.println("=====================================");
                        }
                );
                writer.close();
                throw new RuntimeException("DaggerAPI: There were problems found with your artifacts. See " + newFile + " for details");
            }
            catch (IOException e)
            {
                throw new RuntimeException("DaggerAPI: There were problems found with your artifacts, but no log file could be created! Reason: " + e.getMessage());
            }
        }
    }

    public static String placeOf(String type, int place) {
        return type + "[" + place + "]";
    }

    public static String placeOf(String type, int place, String innerType, int innerPlace) {
        return type + "[" + place + "]" + "{ " + innerType + "[" + innerPlace + "] }";
    }
}
