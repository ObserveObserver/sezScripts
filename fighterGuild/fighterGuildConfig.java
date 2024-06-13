package net.runelite.client.plugins.microbot.fighterGuild;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.fighterGuild.enums.salamanderMode;
import net.runelite.client.plugins.microbot.fighterGuild.enums.armorToKill;

@ConfigGroup("fighterGuild")
public interface fighterGuildConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Mode",
            name = "Mode",
            description = "Choose your mode of hunter",
            position = 0,
            section = generalSection
    )
    default armorToKill armorToKill() {
        return armorToKill.BLACK;
    }
}