package net.runelite.client.plugins.microbot.sezCrafting.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JewelryType {

    RING("ring"),
    NECKLACE("necklace"),
    AMULET("amulet"),
    BRACELET("bracelet");

    private final String jewelry;

    @Override
    public String toString() {
        return jewelry;
    }
}



