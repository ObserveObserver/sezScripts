package net.runelite.client.plugins.microbot.sezCrafting.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GemStones {

    SAPPHIRE("sapphire"),
    EMERALD("emerald"),
    RUBY("ruby"),
    DIAMOND("diamond");

    private final String gem;

    @Override
    public String toString() {
        return gem;
    }
    }



