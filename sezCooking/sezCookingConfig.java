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
            keyName = "Items To Cook",
            name = "Items To Cook",
            description = "Raw items, in a list: i.e.: raw shark,raw tuna",
            position = 1,
            section = generalSection
    )

    default String cookingItems() {
        return "raw shark";
    }

}
