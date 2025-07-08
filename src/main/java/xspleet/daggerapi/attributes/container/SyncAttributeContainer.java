package xspleet.daggerapi.attributes.container;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.instance.DaggerAttributeInstance;
import xspleet.daggerapi.collections.registration.Mapper;

import java.util.HashMap;
import java.util.Map;

public class SyncAttributeContainer extends DaggerAttributeContainer
{
    protected SyncAttributeContainer(HashMap<Attribute<?>, AttributeInstance<?>> attributeInstances)
    {
        super(attributeInstances);
    }

    protected Map<Attribute<?>, AttributeInstance<?>> getAttributeInstances()
    {
        return attributeInstances;
    }

    @Override
    public void acceptSyncContainer(SyncAttributeContainer syncContainer) {
        throw new IllegalStateException("SyncAttributeContainer cannot accept another SyncAttributeContainer");
    }

    public PacketByteBuf toPacketByteBuf()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeMap(
            getAttributeInstances(),
            (byteBuf, attribute) -> byteBuf.writeString(Mapper.getNameOf(attribute)),
            (byteBuf, instance) -> instance.write(byteBuf)
        );
        return buf;
    }

    public SyncAttributeContainer fromPacketByteBuf(PacketByteBuf buf)
    {
        HashMap<Attribute<?>, AttributeInstance<?>> attributeInstances =
                buf.readMap(
                        byteBuf -> Mapper.getEntityAttribute(byteBuf.readString(32767)),
                        byteBuf -> DaggerAttributeInstance.read(byteBuf)
                );
        return new SyncAttributeContainer(attributeInstances);
    }
}
