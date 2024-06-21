package net.runelite.client.plugins.microbot.sezCooking;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("fighterGuild")
public interface sezCookingConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Mode",
            name = "Mode",
            description = "Placeholder",
            position = 0,
            section = generalSection
    )
    default int returnBlah() {
        return 0;
    }
}
