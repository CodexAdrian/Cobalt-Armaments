package me.codexadrian.cobaltarmaments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public interface CobaltTool extends EnergyHolder {

    default EnergyHandler getEnergyStorage(ItemStack stack) {
        return Energy.of(stack);
    }

    @Override
    default double getMaxStoredPower() {
        return ItemConfigs.ITEM_ENERGY;
    }

    @Override
    default EnergyTier getTier() {
        return EnergyTier.HIGH;
    }

    default TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.getItem() instanceof CobaltTool) {
            boolean currentStatus = CobaltArmaments.getIfEmpowered(stack);
            if (user.isSneaking()) {
                stack.getOrCreateNbt().putBoolean("Empowered", !currentStatus);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    default boolean attemptEnergyDrain(ItemStack itemStack, int modifier) {
        EnergyHandler handler = this.getEnergyStorage(itemStack);
        if (handler.getEnergy() >= ItemConfigs.EXPEND_ENERGY_AMOUNT * modifier) {
            handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT * modifier);
            return true;
        }
        return false;
    }

    default boolean isBarVisible(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergy() > 0;
    }

    default int getBarWidth(ItemStack itemStack) {
        return (int) (getEnergyStorage(itemStack).getEnergy() / getMaxStoredPower() * 13.0);
    }

    default int getBarColor(ItemStack itemStack) {
        return 0xc300ff;
    }

    default void appendTooltip(ItemStack itemStack, @Nullable World level, List<Text> list, TooltipContext tooltipFlag) {
        LiteralText text = new LiteralText("");
        text.append(new LiteralText(((int) this.getEnergyStorage(itemStack).getEnergy()) / 1000 + "kLF/" + ((int) this.getMaxStoredPower()) / 1000 + "kLF"));
        list.add(text.formatted(Formatting.GRAY));
        Text empowered = CobaltArmaments.getIfEmpowered(itemStack) ? new TranslatableText("cobaltarmaments.empowered_indicator").formatted(Formatting.AQUA, Formatting.BOLD) : new TranslatableText("cobaltarmaments.depowered_indicator").formatted(Formatting.BLUE, Formatting.BOLD);
        list.add(empowered);
    }

    //improved from Item.raycast();
    static BlockHitResult getPlayerPOVHitResult(World level, PlayerEntity player, RaycastContext.FluidHandling fluid) {
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        Vec3d eyePos = player.getEyePos();
        double yawOffsetZ = Math.cos(Math.toRadians(-yaw) - Math.PI);
        double yawOffsetX = Math.sin(Math.toRadians(-yaw) - Math.PI);
        double pitchOffsetZ = -Math.cos(Math.toRadians(pitch));
        double pitchOffsetX = Math.sin(Math.toRadians(-pitch));
        double xOffset = yawOffsetX * pitchOffsetZ;
        double zOffset = yawOffsetZ * pitchOffsetZ;
        Vec3d vec3d2 = eyePos.add(xOffset * 5.0D, pitchOffsetX * 5.0D, zOffset * 5.0D);
        return level.raycast(new RaycastContext(eyePos, vec3d2, RaycastContext.ShapeType.OUTLINE, fluid, player));
    }

    default boolean veinMine(ItemStack stack, World world, BlockState state, Tag<Block> blockTag, BlockPos pos, LivingEntity miner) {
        if (state.isIn(blockTag)) {
            return this.veinMine(stack, world, state, pos, miner);
        }
        return attemptEnergyDrain(stack, 1);
    }

    default boolean veinMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (miner instanceof PlayerEntity player) {
            List<BlockPos> cachedPositions = new ArrayList<>();
            cachedPositions.add(pos);
            AtomicInteger index = new AtomicInteger();
            int limit = 32;
            while (index.get() < limit) {
                List<BlockPos> newCachedPositions = new ArrayList<>();
                for (BlockPos logPos : cachedPositions) {
                    BlockBox box = BlockBox.create(logPos.add(1, 1, 1), logPos.add(-1, -1, -1));
                    newCachedPositions.addAll(BlockPos.stream(box).filter(blockPos -> world.getBlockState(blockPos).isOf(state.getBlock()) && world.canPlayerModifyAt(player, blockPos)).filter(blockPos -> index.getAndIncrement() < limit).map(blockPos -> playerBreak(world, player, stack, blockPos)).toList());
                }
                if (newCachedPositions.isEmpty()) break;
                cachedPositions = newCachedPositions;
            }
        }
        return attemptEnergyDrain(stack, 1);
    }

    default BlockPos playerBreak(World world, PlayerEntity player, ItemStack stack, BlockPos pos) {
        //TODO trick mc into thinking its the player
        world.breakBlock(pos, !player.isCreative());
        attemptEnergyDrain(stack, 1);
        return pos;
    }
}

