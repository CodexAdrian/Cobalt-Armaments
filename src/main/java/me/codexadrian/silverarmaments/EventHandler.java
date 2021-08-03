package me.codexadrian.silverarmaments;

import me.codexadrian.silverarmaments.tools.AreaBreakTool;
import me.codexadrian.silverarmaments.tools.ExpandingAreaBreakTool;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import static me.codexadrian.silverarmaments.SilverTool.getPlayerPOVHitResult;

public class EventHandler {
    public static void init() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, blockPos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            Item item = stack.getItem();
            if (item instanceof ExpandingAreaBreakTool || (item instanceof AreaBreakTool && SilverArmaments.getIfEmpowered(stack))) {
                AreaBreakTool areaTool = (AreaBreakTool) item;
                EnergyHandler handler = Energy.of(stack);
                handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT);
                if (!level.isClient()) {
                    BlockHitResult hitResult = getPlayerPOVHitResult(level, player, RaycastContext.FluidHandling.ANY);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        Direction direction = hitResult.getSide();
                        int bound = item instanceof ExpandingAreaBreakTool expandedTool ? SilverArmaments.getIfEmpowered(stack) ? expandedTool.getExpandedRange() : areaTool.getRange() : areaTool.getRange();
                        BlockPos anchorPoint = direction == Direction.DOWN || direction == Direction.UP ? blockPos : blockPos.add(0, bound - 1, 0);
                        BlockBox box;
                        switch (direction) {
                            case UP, DOWN -> box = BlockBox.create(anchorPoint.add(bound, 0, bound), anchorPoint.add(-bound, 0, -bound));
                            case EAST, WEST -> box = BlockBox.create(anchorPoint.add(0, bound, bound), anchorPoint.add(0, -bound, -bound));
                            case NORTH, SOUTH -> box = BlockBox.create(anchorPoint.add(bound, bound, 0), anchorPoint.add(-bound, -bound, 0));
                            default -> throw new IllegalStateException("Unexpected value: " + direction);
                        }

                        SilverTool tool = (SilverTool) item;
                        BlockPos.stream(box).filter(blockPos1 -> level.getBlockState(blockPos1).isIn(areaTool.getEffectiveBlocks()) && level.getBlockState(blockPos1).getHardness(level, blockPos1) > 0).forEach(blockPos1 -> {
                            if (tool.attemptEnergyDrain(stack, 1) || player.isCreative())
                                tool.playerBreak(level, player, stack, blockPos1);
                        });
                    }
                }
            }
            return true;
        });
    }
}
