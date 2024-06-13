package net.runelite.client.plugins.microbot.fighterGuild;

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
        name = PluginDescriptor.Spaghetti + "Fighter Guild",
        description = "hunter",
        tags = {"hunter", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class fighterGuildPlugin extends Plugin {
    @Inject
    private fighterGuildConfig config;
    @Inject
    public static Client client;
    @Provides
    fighterGuildConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(fighterGuildConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private fighterGuildOverlay fighterGuildOverlay;
    @Inject
    fighterGuildScript fighterGuildScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(fighterGuildOverlay);
        }
        System.out.println("testing123123123123123");
        client = Microbot.getClient();
        fighterGuildScript.main(client, config);
    }


    protected void shutDown() {
        fighterGuildScript.shutdown();
        overlayManager.remove(fighterGuildOverlay);
    }
}
