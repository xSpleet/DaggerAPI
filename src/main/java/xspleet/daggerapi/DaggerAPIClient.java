package xspleet.daggerapi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.client.ClientDevModeConfig;
import xspleet.daggerapi.client.ClientNetworking;
import xspleet.daggerapi.client.HudEvents;
import xspleet.daggerapi.commands.ClientCommands;
import xspleet.daggerapi.events.KeyBindRegistration;
import xspleet.daggerapi.models.ConfigModel;
import xspleet.daggerapi.networking.NetworkingConstants;

import java.io.*;

import static xspleet.daggerapi.DaggerAPI.JSON_PARSER;

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
