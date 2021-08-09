package me.codexadrian.silverarmaments.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class EnergyTooltip implements TooltipComponent {
    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18;
    }
}
