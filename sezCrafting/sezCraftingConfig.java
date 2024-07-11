package net.runelite.client.plugins.microbot.sezCrafting;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.fletching.enums.FletchingMaterial;
import net.runelite.client.plugins.microbot.sezCrafting.enums.GemStones;
import net.runelite.client.plugins.microbot.sezCrafting.enums.JewelryType;

@ConfigGroup("fighterGuild")
public interface sezCraftingConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Gem to Use",
            name = "Gem to Use",
            description = "Gemstone to use in Crafting",
            position = 1,
            section = generalSection
    )

    default GemStones gemStones()
    {
        return GemStones.SAPPHIRE;
    }

    @ConfigItem(
            keyName = "Jewelry Type",
            name = "Jewelry Type ",
            description = "Kind of Jewelry to Craft",
            position = 2,
            section = generalSection
    )

    default JewelryType jewelry() { return JewelryType.NECKLACE; }

}
