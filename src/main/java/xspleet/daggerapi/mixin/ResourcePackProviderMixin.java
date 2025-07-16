package xspleet.daggerapi.mixin;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xspleet.daggerapi.base.DaggerResourcePackProvider;
import xspleet.daggerapi.base.Self;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public class ResourcePackProviderMixin implements Self<ResourcePackManager>
{
    @Shadow
    @Final
    private Set<ResourcePackProvider> providers;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void daggerapi$init(CallbackInfo ci) {
        var providers = new LinkedHashSet<>(this.providers);
        providers.add(new DaggerResourcePackProvider(ResourceType.CLIENT_RESOURCES));
        providers.add(new DaggerResourcePackProvider(ResourceType.SERVER_DATA));
        ((ResourcePackManagerAccessor)this).daggerapi$setProviders(providers);
    }
}
