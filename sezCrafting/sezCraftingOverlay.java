package net.runelite.client.plugins.microbot.sezCrafting;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.globval.WidgetIndices;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.sezCrafting.state.*;
import net.runelite.client.plugins.microbot.sezCrafting.sezCraftingScript;

import javax.inject.Inject;
import java.awt.*;

public class sezCraftingOverlay extends OverlayPanel {
    @Inject
    sezCraftingOverlay(sezCraftingPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        setNaughty();
    }
    @Override
    public Dimension render(Graphics2D graphics) {
        try {
            panelComponent.setPreferredSize(new Dimension(200, 300));
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("sezCrafting")
                    .color(Color.GREEN)
                    .build());

            panelComponent.getChildren().add(LineComponent.builder().build());

            panelComponent.getChildren().add(LineComponent.builder()

                    .left(Microbot.status)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Profit: " + state.jewelryProfitSaved())
                    .build());

        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return super.render(graphics);
    }
}
