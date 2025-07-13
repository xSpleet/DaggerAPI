package xspleet.daggerapi.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.networking.NetworkingConstants;

public class ClientNetworking
{
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(
                NetworkingConstants.SYNC_ATTRIBUTES_PACKET_ID,
                (client, handler, packet, sender) -> {
                    var syncContainer = DaggerAttributeContainer.readFromPacket(packet);
                    client.execute(() -> {
                        var player = client.player;
                        if (player instanceof AttributeHolder holder) {
                            if(player instanceof ClientAttributeHolder clientHolder) {
                                for(var attribute : syncContainer.getSyncData().keySet()) {
                                    if(clientHolder.updatesAttribute(attribute)) {
                                        clientHolder.updateAttribute(attribute, player.getWorld().getTime());
                                    }
                                }
                            }
                            holder.acceptSyncContainer(syncContainer);
                            DaggerLogger.debug("Received attribute sync packet for player: {}", player.getName().getString());
                        } else {
                            DaggerLogger.error("Received attribute sync packet but player is null.");
                        }
                    });
                }
        );
    }
}
