package net.runelite.client.plugins.microbot.sezCrafting;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = "[sezCrafting]",
        description = "hunter",
        tags = {"hunter", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class sezCraftingPlugin extends Plugin {
    @Inject
    private sezCraftingConfig config;
    @Inject
    public static Client client;
    @Provides
    sezCraftingConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(sezCraftingConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private sezCraftingOverlay sezCraftingOverlay;
    @Inject
    sezCraftingScript sezCraftingScript;

    public static long startTimeMillis = System.currentTimeMillis();



    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(sezCraftingOverlay);
        }

        client = Microbot.getClient();
        sezCraftingScript.main(client, Microbot.getItemManager(), config);
    }


    protected void shutDown() {
        sezCraftingScript.shutdown();
        overlayManager.remove(sezCraftingOverlay);
    }
}
