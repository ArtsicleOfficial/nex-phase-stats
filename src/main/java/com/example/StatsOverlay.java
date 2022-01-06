package com.example;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;

public class StatsOverlay extends OverlayPanel {
    public PhaseStats[] stats;
    public int currentPhase = 0;
    public StatsOverlay(ExamplePlugin plugin, PhaseStats[] stats) {
        super(plugin);

        this.stats = stats;

        setPosition(OverlayPosition.TOP_CENTER);
    }

    public String getStat(PhaseStats stat) {
        if(stat.hits + stat.misses == 0) {
            return "0/0 (0%)";
        }
        return stat.hits + "/" + (stat.hits + stat.misses) + " (" + Math.round((100.0*stat.hits/(stat.misses+stat.hits))) + "%)";
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        int number = 0;
        for(PhaseStats stat : stats) {
            this.getPanelComponent().getChildren().add(LineComponent.builder()
                    .left("P" + (number+1)).right(getStat(stats[number]))
                            .leftColor((currentPhase == number) ? new Color(200,255,200) : (number % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY))
                    .build());
            number++;
        }



        return super.render(graphics);
    }
}
