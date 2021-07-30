package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.tools.CobaltHammer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import static me.codexadrian.cobaltarmaments.CobaltTool.getPlayerPOVHitResult;

public class EventHandler {
    public static void init() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, blockPos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof CobaltHammer hammer) {
                EnergyHandler handler = Energy.of(stack);
                handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT);
                if (!level.isClient()) {
                    BlockHitResult hitResult = getPlayerPOVHitResult(level, player, RaycastContext.FluidHandling.ANY);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        Direction direction = hitResult.getSide();
                        int bound = CobaltArmaments.getIfEmpowered(stack) ? 2 : 1;
                        BlockPos anchorPoint = direction == Direction.DOWN || direction == Direction.UP ? blockPos : blockPos.add(0, bound - 1, 0);
                        BlockBox box;
                        switch (direction) {
                            case UP, DOWN -> box = BlockBox.create(anchorPoint.add(bound, 0, bound), anchorPoint.add(-bound, 0, -bound));
                            case EAST, WEST -> box = BlockBox.create(anchorPoint.add(0, bound, bound), anchorPoint.add(0, -bound, -bound));
                            case NORTH, SOUTH -> box = BlockBox.create(anchorPoint.add(bound, bound, 0), anchorPoint.add(-bound, -bound, 0));
                            default -> throw new IllegalStateException("Unexpected value: " + direction);
                        }

                        BlockPos.stream(box).filter(blockPos1 -> level.getBlockState(blockPos1).isIn(BlockTags.PICKAXE_MINEABLE) && level.getBlockState(blockPos1).getHardness(level, blockPos1) > 0).forEach(blockPos1 -> {
                            if (hammer.attemptEnergyDrain(stack, 1) || player.isCreative())
                                hammer.playerBreak(level, player, stack, blockPos1);
                        });
                    }
                }
            }
            return true;
        });
    }
}
