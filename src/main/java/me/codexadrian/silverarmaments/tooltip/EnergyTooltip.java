package me.codexadrian.silverarmaments.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;

public class EnergyTooltip implements TooltipComponent {
    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
        TooltipComponent.super.drawItems(textRenderer, x, y, matrices, itemRenderer, z, textureManager);
    }
}
