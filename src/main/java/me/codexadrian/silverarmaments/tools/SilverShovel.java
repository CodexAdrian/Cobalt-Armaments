package me.codexadrian.silverarmaments.tools;

import me.codexadrian.silverarmaments.SilverArmaments;
import me.codexadrian.silverarmaments.SilverTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilverShovel extends ShovelItem implements SilverTool, AreaBreakTool{

    public SilverShovel(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return SilverTool.super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        this.areaUseOnBlock(context, super::useOnBlock);
        return super.useOnBlock(context);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return attemptEnergyDrain(stack, 2);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return attemptEnergyDrain(stack, 1);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        SilverTool.super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return SilverArmaments.hasEnoughEnergy(stack) ?  super.getMiningSpeedMultiplier(stack, state) : 1;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return SilverTool.super.isBarVisible(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return SilverTool.super.getBarWidth(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return SilverTool.super.getBarColor();
    }

    @Override
    public Tag<Block> getEffectiveBlocks() {
        return BlockTags.SHOVEL_MINEABLE;
    }

}
