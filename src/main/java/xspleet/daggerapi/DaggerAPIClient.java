package xspleet.daggerapi;

import net.fabricmc.api.ClientModInitializer;
import xspleet.daggerapi.config.ClientDevModeConfig;
import xspleet.daggerapi.networking.ClientNetworking;
import xspleet.daggerapi.events.HudEvents;
import xspleet.daggerapi.commands.ClientCommands;
import xspleet.daggerapi.events.KeyBindRegistration;
import xspleet.daggerapi.networking.NetworkingConstants;

public class DaggerAPIClient implements ClientModInitializer {

    @Override
    public void onInitializeClient()
    {
        NetworkingConstants.init();
        ClientDevModeConfig.init();
        ClientNetworking.init();
        HudEvents.init();
        KeyBindRegistration.register();
        ClientCommands.registerCommands();
    }
}
