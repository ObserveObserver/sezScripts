package net.runelite.client.plugins.microbot.fighterGuild.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum salamanderMode {
    NONE("None", 0, 0),
    GREEN_SALAMANDER("Green Salamander", 9341, 9004),
    RED_SALAMANDER("Red Salamander",8990 ,8986 ),
    BLACK_SALAMANDER("Black Salamander", 9000, 8996 );


    private final String name;
    private final int openTrap;
    private final int fullTrap;

    @Override
    public String toString() {
        return name;
    }
    public int getOpenTrap() { return openTrap; }
    public int getFullTrap() { return fullTrap; }
}
