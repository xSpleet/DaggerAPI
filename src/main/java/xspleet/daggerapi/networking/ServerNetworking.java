package xspleet.daggerapi.networking;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.api.pack.Tags;
import xspleet.daggerapi.config.ServerDevModeConfig;
import xspleet.daggerapi.util.PlayerOwned;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerNetworking
{
    public static final int TIMEOUT = 5000;
    public static final Map<UUID, Integer> playerHashTimeout = new HashMap<>();
    public static void init()
    {
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBoolean(ServerDevModeConfig.SERVER_DEV_MODE);
                    ServerPlayNetworking.send(handler.getPlayer(), NetworkingConstants.SYNC_DEV_MODE_PACKET_ID, buf);

                    var player = handler.getPlayer();
                    playerHashTimeout.put(player.getUuid(), TIMEOUT / 50); // Convert milliseconds to ticks (20 ticks = 1 second)
                    ServerPlayNetworking.send(player, NetworkingConstants.JOIN_REQUEST_ARTIFACT_HASHES_PACKET_ID, PacketByteBufs.empty());
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.JOIN_CHECK_ARTIFACTS_PACKET_ID,
                (server, player, handler, buf, responseSender) -> {
                    Map<Identifier, Integer> hashes = buf.readMap(
                        PacketByteBuf::readIdentifier,
                        PacketByteBuf::readVarInt
                    );

                    var taggedArtifacts = Registries.ITEM.getEntryList(Tags.ARTIFACTS).orElse(null);
                    var taggedActiveArtifacts = Registries.ITEM.getEntryList(Tags.ACTIVE_ARTIFACTS).orElse(null);

                    if(taggedArtifacts != null)
                    {
                        for(var entry : taggedArtifacts) {
                            if(entry.hasKeyAndValue()) {
                                if(entry.value() instanceof ArtifactItem artifact) {
                                    var id = artifact.getIdentifier();
                                    var behavior = artifact.getBehavior();
                                    var hash = behavior.hashCode();
                                    if(!hashes.containsKey(id) || hashes.get(id) != hash) {
                                        handler.disconnect(Text.literal("Actifact behavior mismatch for " + id.toString() + ". Please update your client."));
                                        return;
                                    }
                                    hashes.remove(id);
                                }
                            }
                        }
                    }
                    if(taggedActiveArtifacts != null)
                    {
                        for(var entry : taggedActiveArtifacts) {
                            if(entry.hasKeyAndValue()) {
                                if(entry.value() instanceof ArtifactItem artifact && artifact.isActive()) {
                                    var id = artifact.getIdentifier();
                                    var behavior = artifact.getBehavior();
                                    var hash = behavior.hashCode();
                                    if(!hashes.containsKey(id) || hashes.get(id) != hash) {
                                        handler.disconnect(Text.literal("Actifact behavior mismatch for " + id.toString() + ". Please update your client."));
                                        return;
                                    }
                                    hashes.remove(id);
                                }
                            }
                        }
                    }
                    if (!hashes.isEmpty()) {
                        handler.disconnect(Text.literal("Artifact packs are out of sync. Please update your client."));
                    }

                    playerHashTimeout.remove(player.getUuid());
                }
        );

        ServerTickEvents.END_SERVER_TICK.register(
                server ->
                playerHashTimeout.entrySet().removeIf(entry -> {
                    UUID uuid = entry.getKey();
                    int timeout = entry.getValue();
                    if (timeout <= 0) {
                        server.getPlayerManager().getPlayerList().stream().filter(p -> p.getUuid().equals(uuid)).findAny().ifPresent(player -> player.networkHandler.disconnect(Text.literal("Artifact behavior check timed out. Please try to rejoin the server.")));
                        return true;
                    } else {
                        entry.setValue(timeout - 1);
                        return false;
                    }
                })
        );
    }

    public static void sendParticlePacket(World world, Identifier particleTypeId, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeIdentifier(particleTypeId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(velocityX);
        buf.writeDouble(velocityY);
        buf.writeDouble(velocityZ);

        for(var player : PlayerLookup.tracking((ServerWorld) world, new BlockPos((int) x, (int) y, (int) z))) {
            ServerPlayNetworking.send(player, NetworkingConstants.SEND_PARTICLE_PACKET_ID, buf);
        }
    }
}
