package me.codexadrian.silverarmaments.tools;

import me.codexadrian.silverarmaments.SilverArmaments;
import me.codexadrian.silverarmaments.SilverTool;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class SilverSword extends SwordItem implements SilverTool {

    public SilverSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return SilverTool.super.use(world, user, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && SilverArmaments.hasEnoughEnergy(stack) && SilverArmaments.getIfEmpowered(stack)) {
            Box box = new Box(target.getBlockPos());
            List<LivingEntity> livingEntityList = attacker.world.getTargets(LivingEntity.class, TargetPredicate.createAttackable(), target, box.expand(6, 1, 6));
            Iterator<LivingEntity> iterator = livingEntityList.iterator();
            while (iterator.hasNext() && SilverArmaments.hasEnoughEnergy(stack)) {
                LivingEntity entity = iterator.next();
                player.attack(entity);
                attemptEnergyDrain(stack, 1);
            }
        }
        return attemptEnergyDrain(stack, 1);
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
}

