package xspleet.daggerapi.networking;

import net.minecraft.util.Identifier;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;

public class NetworkingConstants
{
    public static void init()
    {
        DaggerLogger.debug(LoggingContext.STARTUP, "Initializing DaggerAPI Networking Constants");
    }

    public static final Identifier SYNC_ATTRIBUTES_PACKET_ID;
    public static final Identifier SYNC_DEV_MODE_PACKET_ID;
    public static final Identifier JOIN_CHECK_ARTIFACTS_PACKET_ID;
    public static final Identifier JOIN_REQUEST_ARTIFACT_HASHES_PACKET_ID;

    static {
        SYNC_ATTRIBUTES_PACKET_ID = Identifier.of("daggerapi", "sync_attributes");
        SYNC_DEV_MODE_PACKET_ID = Identifier.of("daggerapi", "sync_dev_mode");
        JOIN_CHECK_ARTIFACTS_PACKET_ID = Identifier.of("daggerapi", "join_check_artifacts");
        JOIN_REQUEST_ARTIFACT_HASHES_PACKET_ID = Identifier.of("daggerapi", "join_request_artifact_hashes");
    }
}
