package me.codexadrian.cobaltarmaments.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CobaltHammer extends PoweredDiggerItem {

    public CobaltHammer(Properties properties) {
        super(properties, BlockTags.MINEABLE_WITH_PICKAXE, 15 , 5);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        return attemptEnergyDrain(itemStack, 1);
    }

    public static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
        float f = player.getXRot();
        float g = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float h = Mth.cos(-g * 0.017453292F - 3.1415927F);
        float i = Mth.sin(-g * 0.017453292F - 3.1415927F);
        float j = -Mth.cos(-f * 0.017453292F);
        float k = Mth.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        double d = 5.0D;
        Vec3 vec32 = vec3.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
        return level.clip(new ClipContext(vec3, vec32, ClipContext.Block.OUTLINE, fluid, player));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(this.blocks);
    }
}

