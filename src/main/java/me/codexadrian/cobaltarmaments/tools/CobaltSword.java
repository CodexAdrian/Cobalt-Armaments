package me.codexadrian.cobaltarmaments.tools;

import me.codexadrian.cobaltarmaments.CobaltArmaments;
import me.codexadrian.cobaltarmaments.CobaltTool;
import me.codexadrian.cobaltarmaments.ItemConfigs;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
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
import team.reborn.energy.EnergyHolder;

import java.util.Iterator;
import java.util.List;

public class CobaltSword extends SwordItem implements CobaltTool {

    public CobaltSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return CobaltTool.super.use(world, user, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && CobaltArmaments.hasEnoughEnergy(stack) && CobaltArmaments.getIfEmpowered(stack)) {
            Box box = new Box(target.getBlockPos());
            List<LivingEntity> livingEntityList = attacker.world.getTargets(LivingEntity.class, TargetPredicate.createAttackable(), target, box.expand(6, 1, 6));
            Iterator<LivingEntity> iterator = livingEntityList.iterator();
            while (iterator.hasNext() && CobaltArmaments.hasEnoughEnergy(stack)) {
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
        CobaltTool.super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return CobaltTool.super.isBarVisible(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return CobaltTool.super.getBarWidth(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return CobaltTool.super.getBarColor(stack);
    }
}

