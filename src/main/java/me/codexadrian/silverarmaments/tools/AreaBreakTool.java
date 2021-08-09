package me.codexadrian.silverarmaments.tools;

import me.codexadrian.silverarmaments.SilverArmaments;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

public interface AreaBreakTool {
    default int getRange(ItemStack stack) {
        return SilverArmaments.getIfEmpowered(stack) ? 1 : 0;
    }
    Tag<Block> getEffectiveBlocks();
}
