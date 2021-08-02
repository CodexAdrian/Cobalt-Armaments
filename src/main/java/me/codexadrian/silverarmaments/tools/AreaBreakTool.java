package me.codexadrian.silverarmaments.tools;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public interface AreaBreakTool {
    default int getRange() {
        return 1;
    }
    Tag<Block> getEffectiveBlocks();
}
