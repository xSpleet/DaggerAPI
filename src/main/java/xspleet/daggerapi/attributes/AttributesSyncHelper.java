package xspleet.daggerapi.attributes;

import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class AttributesSyncHelper
{
    public static void acceptSyncContainer(AttributeHolder holder)
    {
        if(holder instanceof ClientPlayerEntity)
        {

        }
    }
}
