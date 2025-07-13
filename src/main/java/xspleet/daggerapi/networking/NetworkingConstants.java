package xspleet.daggerapi.networking;

import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.DaggerLogger;

public class NetworkingConstants
{
    public static void init()
    {
        DaggerLogger.debug("Initializing DaggerAPI Networking Constants");
    }

    public static final Identifier SYNC_ATTRIBUTES_PACKET_ID = Identifier.of("daggerapi", "sync_attributes");
    public static final Identifier SYNC_DEV_MODE_PACKET_ID = Identifier.of("daggerapi", "sync_dev_mode");
}
