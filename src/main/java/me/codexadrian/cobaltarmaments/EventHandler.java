package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.tools.CobaltHammer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import static me.codexadrian.cobaltarmaments.tools.CobaltHammer.getPlayerPOVHitResult;

public class EventHandler {
    public static void init() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, blockPos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandItem();
            if(stack.getItem() instanceof CobaltHammer) {
                EnergyHandler handler = Energy.of(stack);
                handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT);
                if (!level.isClientSide()) {
                    BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        Direction direction = hitResult.getDirection();
                        int bound = CobaltArmaments.getIfEmpowered(stack) ? 2 : 1;
                        BlockPos anchorPoint = direction == Direction.DOWN || direction == Direction.UP ? blockPos : blockPos.offset(0, bound - 1, 0);
                        for (int a = -bound; a <= bound; a++) {
                            for (int b = -bound; b <= bound; b++) {
                                if (anchorPoint.equals(blockPos) && a == 0 && b == 0) continue;

                                BlockPos target = null;

                                if (direction == Direction.UP || direction == Direction.DOWN)
                                    target = anchorPoint.offset(a, 0, b);
                                if (direction == Direction.NORTH || direction == Direction.SOUTH)
                                    target = anchorPoint.offset(a, b, 0);
                                if (direction == Direction.EAST || direction == Direction.WEST)
                                    target = anchorPoint.offset(0, a, b);

                                ServerLevel serverLevel = (ServerLevel) level;
                                if (serverLevel.mayInteract(player, target) && handler.getEnergy() > 0 && !serverLevel.getBlockState(target).isAir()) {
                                    if (serverLevel.getBlockState(target).getDestroySpeed(serverLevel, target) > 0)
                                        serverLevel.destroyBlock(target, true);
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