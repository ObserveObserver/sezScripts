package net.runelite.client.plugins.microbot.fighterGuild.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

import java.util.Arrays;
import java.util.List;


@Getter
@RequiredArgsConstructor
public enum armorToKill {

    BLACK("black");

    private final String armor;

    @Override
    public String toString() {
        return armor;
    }
}

