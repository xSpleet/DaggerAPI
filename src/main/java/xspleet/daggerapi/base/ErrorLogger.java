package xspleet.daggerapi.base;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import xspleet.daggerapi.DaggerAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ErrorLogger
{
    public static void validateItems() throws IOException {
        Path path = FabricLoader.getInstance().getConfigDir();
        DaggerAPI.LOGGER.info(path.toString());
        FileUtils.touch(new File(path.toString() + "/daggerapi/error.log"));
    }
}
