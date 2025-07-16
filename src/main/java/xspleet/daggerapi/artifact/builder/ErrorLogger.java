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
    private static final LocalDateTime nowTime = LocalDateTime.now();
    private static boolean errorsLogged = false;

    public static void log(String itemName, String place, DaggerAPIException exception) {
        log(itemName, place, exception.getMessage());
    }

    public static void log(String itemName, String place, String message) {
        DaggerLogger.error("Error in {} at {}: {}", itemName, place, message);
        errors.putIfAbsent(itemName, new ArrayList<>());
        errors.get(itemName).add(place + " : " + message);
    }

    public static boolean validate(String packName) {
        if(!errors.isEmpty())
        {
            errorsLogged = true;
            try {
                String now = nowTime
                        .toString()
                        .replace(':', '-')
                        .replace('T', '_')
                        .split("\\.")[0];
                Path path = FabricLoader.getInstance().getGameDir();
                Path folder = Path.of(path.toString(), "daggerapi/logs");

                if(!Files.exists(folder))
                    Files.createDirectories(folder);

                var newFile = folder.resolve(now + "_errors.txt");
                if(!newFile.toFile().exists())
                    Files.createFile(newFile);

                PrintWriter writer = new PrintWriter(Files.newBufferedWriter(newFile));

                writer.println("=====================================");
                writer.println("Errors in pack " + packName + ":");
                errors.forEach(
                        (itemName, itemModelErrors) ->
                        {
                            writer.println("----------------------------------");
                            writer.println("\t" + itemName + ":");
                            for (String error : itemModelErrors) {
                                writer.printf("\t\t > %s\n", error);
                            }
                            writer.println("----------------------------------");
                        }
                );

                writer.println("=====================================");
                writer.close();
                errors.clear();
                return false;
            }
            catch (IOException e) {
                throw new RuntimeException("DaggerAPI: There were problems found with your artifacts, but no log file could be created! Reason: " + e.getMessage());
            }
        }
        return true;
    }

    public static String placeOf(String type, int place) {
        return type + "[" + place + "]";
    }

    public static String placeOf(String type, int place, String innerType, int innerPlace) {
        return type + "[" + place + "]" + "{ " + innerType + "[" + innerPlace + "] }";
    }

    public static boolean hasErrors() {
        return errorsLogged;
    }
}
