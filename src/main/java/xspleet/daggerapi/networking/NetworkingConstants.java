package xspleet.daggerapi.networking;

import net.minecraft.util.Identifier;
import xspleet.daggerapi.DaggerAPI;

public class NetworkingConstants
{
    public static void init()
    {
        DaggerAPI.LOGGER.debug("Initializing DaggerAPI Networking Constants");
    }
    public static final Identifier SYNC_ATTRIBUTES_PACKET_ID = Identifier.of("daggerapi", "sync_attributes");
}
