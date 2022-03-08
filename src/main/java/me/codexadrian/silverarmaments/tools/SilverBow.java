package me.codexadrian.silverarmaments.tools;

import dev.emi.trinkets.api.TrinketsApi;
import me.codexadrian.silverarmaments.SilverArmaments;
import me.codexadrian.silverarmaments.SilverTool;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EnergyBlastEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SilverBow extends BowItem implements SilverTool {

    public SilverBow(Settings settings) {
        super(settings);
    }
/*
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (TrinketsApi.getTrinketComponent(user).isPresent())
            if (TrinketsApi.getTrinketComponent(user).get().isEquipped(SilverArmaments.SILVER_QUIVER) && user instanceof PlayerEntity playerEntity) {
                var trinket = TrinketsApi.getTrinketComponent(user).get();

                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                float f = getPullProgress(i);
//                if (!((double) f < 0.1D)) {
                    if (!world.isClient) {
                        EnergyBlastEntity blast = new EnergyBlastEntity(SilverArmaments.ENERGY_BLAST_ENTITY, world);
                        blast.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, f * 3.0F, 1.0F);
                        world.spawnEntity(blast);
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                //}
            }
    }
*/
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            EnergyBlastEntity blast = new EnergyBlastEntity(SilverArmaments.ENERGY_BLAST_ENTITY, world);
            blast.setORIGIN(user.getBlockPos());
            blast.setPosition(user.getX(), user.getY(), user.getZ());
            blast.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.2F, 1.0F);
            //blast.setVelocity(user.getX(), user.getY(), user.getZ(), 0.5F, 1);
            blast.velocityDirty = true;
            world.spawnEntity(blast);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    //TODO add quiver logic here
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

