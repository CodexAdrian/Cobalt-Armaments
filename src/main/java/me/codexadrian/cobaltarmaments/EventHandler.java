package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.tools.CobaltHammer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
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
            if(stack.getItem() instanceof CobaltHammer) {
                EnergyHandler handler = Energy.of(stack);
                handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT);
                if (!level.isClient()) {
                    BlockHitResult hitResult = getPlayerPOVHitResult(level, player, RaycastContext.FluidHandling.ANY);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        Direction direction = hitResult.getSide();
                        int bound = CobaltArmaments.getIfEmpowered(stack) ? 2 : 1;
                        BlockPos anchorPoint = direction == Direction.DOWN || direction == Direction.UP ? blockPos : blockPos.add(0, bound - 1, 0);
                        for (int a = -bound; a <= bound; a++) {
                            for (int b = -bound; b <= bound; b++) {
                                if (anchorPoint.equals(blockPos) && a == 0 && b == 0) continue;

                                BlockPos target = null;

                                if (direction == Direction.UP || direction == Direction.DOWN)
                                    target = anchorPoint.add(a, 0, b);
                                if (direction == Direction.NORTH || direction == Direction.SOUTH)
                                    target = anchorPoint.add(a, b, 0);
                                if (direction == Direction.EAST || direction == Direction.WEST)
                                    target = anchorPoint.add(0, a, b);

                                ServerWorld serverLevel = (ServerWorld) level;
                                if (serverLevel.canPlayerModifyAt(player, target) && handler.getEnergy() > 0 && !serverLevel.getBlockState(target).isAir()) {
                                    if (serverLevel.getBlockState(target).getHardness(serverLevel, target) > 0)
                                        serverLevel.breakBlock(target, !player.isCreative());
                                    handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        });
    }
}