package net.runelite.client.plugins.microbot.sezCooking;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.fighterGuild.fighterGuildConfig;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = "[sezCooking]",
        description = "hunter",
        tags = {"hunter", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class sezCookingPlugin extends Plugin {
    @Inject
    private sezCookingConfig config;
    @Inject
    public static Client client;
    @Provides
    sezCookingConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(sezCookingConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private sezCookingOverlay sezCookingOverlay;
    @Inject
    sezCookingScript sezCookingScript;

    public static long startTimeMillis = System.currentTimeMillis();



    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(sezCookingOverlay);
        }
        client = Microbot.getClient();
        sezCookingScript.main(client, config);
    }


    protected void shutDown() {
        sezCookingScript.shutdown();
        overlayManager.remove(sezCookingOverlay);
    }
}
