package me.codexadrian.silverarmaments;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public interface SilverTool extends EnergyHolder {

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
        if (stack.getItem() instanceof SilverTool) {
            boolean currentStatus = SilverArmaments.getIfEmpowered(stack);
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

    default int getBarColor() {
        return 0x273bd6;
    }

    default void appendTooltip(ItemStack itemStack, @Nullable World level, List<Text> list, TooltipContext tooltipFlag) {
        LiteralText text = new LiteralText("");
        text.append(new LiteralText(((int) this.getEnergyStorage(itemStack).getEnergy()) / 1000 + "kLF/" + ((int) this.getMaxStoredPower()) / 1000 + "kLF"));
        list.add(text.formatted(Formatting.GRAY));
        Text empowered = SilverArmaments.getIfEmpowered(itemStack) ? new TranslatableText("silverarmaments.empowered_indicator").formatted(Formatting.AQUA, Formatting.BOLD) : new TranslatableText("silverarmaments.depowered_indicator").formatted(Formatting.BLUE, Formatting.BOLD);
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
                    Set<BlockPos> logList = BlockPos.stream(box).filter(blockPos -> world.getBlockState(blockPos).isOf(state.getBlock())).filter(blockPos -> index.getAndIncrement() < limit).map(blockPos -> playerBreak(world, player, stack, blockPos).toImmutable()).collect(Collectors.toSet());
                    newCachedPositions.addAll(logList);
                }
                if (newCachedPositions.isEmpty()) break;
                cachedPositions = newCachedPositions;
            }
        }
        return attemptEnergyDrain(stack, 1);
    }

    default BlockPos playerBreak(World world, PlayerEntity player, ItemStack stack, BlockPos pos) {
        if(!world.isClient() && world.canPlayerModifyAt(player, pos)) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockState state = world.getBlockState(pos);
            world.removeBlock(pos, false);
            serverWorld.spawnParticles(DustParticleEffect.DEFAULT, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 3);
            state.getBlock().afterBreak(world, player, pos, state, world.getBlockEntity(pos), player.getMainHandStack());
            state.getBlock().onBreak(world, pos, state, player);
            attemptEnergyDrain(stack, 1);
        }
        return pos;
    }

    default void areaUseOnBlock(ItemUsageContext context, Consumer<ItemUsageContext> consumer) {
        if(SilverArmaments.getIfEmpowered(context.getStack())) {
            BlockBox box = BlockBox.create(context.getBlockPos().add(1, 0, 1), context.getBlockPos().add(-1, 0, -1));
            BlockPos.stream(box).map(blockPos -> new BlockHitResult(context.getHitPos(), context.getSide(), blockPos.toImmutable(), false)).map(blockHitResult -> new ItemUsageContext(Objects.requireNonNull(context.getPlayer()), context.getHand(), blockHitResult)).forEach(consumer);
        }
    }
}

