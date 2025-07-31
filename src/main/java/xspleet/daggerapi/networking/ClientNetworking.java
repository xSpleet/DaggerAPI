package xspleet.daggerapi.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeTracker;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.pack.Tags;
import xspleet.daggerapi.config.ClientDevModeConfig;

import java.util.HashMap;
import java.util.Map;

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
                            if(player instanceof ClientAttributeTracker clientHolder) {
                                for(var attribute : syncContainer.getSyncData().keySet()) {
                                    if(clientHolder.updatesAttribute(attribute)) {
                                        clientHolder.updateAttribute(attribute, player.getWorld().getTime());
                                    }
                                }
                            }
                            holder.acceptSyncContainer(syncContainer);
                            DaggerLogger.debug(LoggingContext.SYNC, "Received attribute sync packet for player: {}", player.getName().getString());
                        } else {
                            DaggerLogger.error(LoggingContext.SYNC, "Received attribute sync packet but player is null.");
                        }
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SYNC_DEV_MODE_PACKET_ID,
                (client, handler, packet, sender) -> {
                    boolean devMode = packet.readBoolean();
                    client.execute(() -> {
                        ClientDevModeConfig.CLIENT_DEV_MODE = devMode && ClientDevModeConfig.CLIENT_DEV_MODE;
                        DaggerLogger.debug(LoggingContext.SYNC, "Received dev mode sync packet: " + devMode);
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_REQUEST_ARTIFACT_HASHES_PACKET_ID,
                (client, handler, packet, sender) ->{
                    client.execute(() -> {
                        PacketByteBuf buf = PacketByteBufs.create();
                        Map<Identifier, Integer> hashes = new HashMap<>();

                        var taggedArtifacts = Registries.ITEM.getEntryList(Tags.ARTIFACTS).orElse(null);
                        if(taggedArtifacts != null) {
                            Map<Identifier, Integer> finalHashes = hashes;
                            taggedArtifacts.forEach(entry -> {
                                if (entry.hasKeyAndValue()) {
                                    var item = entry.value();
                                    if(!(item instanceof ArtifactItem artifact)) {
                                        return;
                                    }
                                    var id = artifact.getIdentifier();
                                    var behavior = artifact.getBehavior();
                                    var hash = behavior.hashCode();
                                    finalHashes.put(id, hash);
                                }
                            });
                        } else {
                            hashes = new HashMap<>();
                        }

                        var taggedActiveArtifacts = Registries.ITEM.getEntryList(Tags.ACTIVE_ARTIFACTS).orElse(null);
                        if(taggedActiveArtifacts != null) {
                            Map<Identifier, Integer> finalHashes = hashes;
                            taggedActiveArtifacts.forEach(entry -> {
                                if (entry.hasKeyAndValue()) {
                                    var item = entry.value();
                                    if(!(item instanceof ArtifactItem artifact) || !artifact.isActive()) {
                                        return;
                                    }
                                    var id = artifact.getIdentifier();
                                    var behavior = artifact.getBehavior();
                                    var hash = behavior.hashCode();
                                    finalHashes.put(id, hash);
                                }
                            });
                        }

                        buf.writeMap(
                                hashes,
                                PacketByteBuf::writeIdentifier,
                                PacketByteBuf::writeVarInt
                        );

                        ClientPlayNetworking.send(
                                NetworkingConstants.JOIN_CHECK_ARTIFACTS_PACKET_ID,
                                buf
                        );
                    });
                }
        );
    }
}
