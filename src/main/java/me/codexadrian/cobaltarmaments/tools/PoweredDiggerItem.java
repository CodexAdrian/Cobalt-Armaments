package me.codexadrian.cobaltarmaments.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.codexadrian.cobaltarmaments.CobaltArmaments;
import me.codexadrian.cobaltarmaments.ItemConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class PoweredDiggerItem extends Item implements EnergyHolder {
    public final Tag<Block> blocks;
    public final float speed;
    public final float attackDamageBaseline;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public PoweredDiggerItem(Properties properties, Tag<Block> tag, float speed, float dmg) {
        super(properties);
        this.blocks = tag;
        this.speed = speed;
        this.attackDamageBaseline = dmg;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9000000953674316D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public double getMaxStoredPower() {
        return ItemConfigs.ITEM_ENERGY;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        float calcSpeed = CobaltArmaments.getIfEmpowered(itemStack) ? this.speed * 4 : this.speed;
        return this.blocks.contains(blockState.getBlock()) && this.getEnergyStorage(itemStack).getEnergy() > ItemConfigs.EXPEND_ENERGY_AMOUNT ? calcSpeed : 1.0F;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        TextComponent text = new TextComponent("");
        text.append(new TextComponent(((int) this.getEnergyStorage(itemStack).getEnergy())/1000 + "kLF/" + ((int) this.getMaxStoredPower())/1000 + "kLF"));
        list.add(text.withStyle(ChatFormatting.GRAY));
        Component empowered = CobaltArmaments.getIfEmpowered(itemStack) ? new TranslatableComponent("cobaltarmaments.empowered_indicator").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD) : new TranslatableComponent("cobaltarmaments.depowered_indicator").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        list.add(empowered);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        return attemptEnergyDrain(itemStack, 2);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        return attemptEnergyDrain(itemStack, 1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (player.getMainHandItem().getItem() instanceof PoweredDiggerItem) {
            boolean currentStatus = CobaltArmaments.getIfEmpowered(stack);
            if (player.isShiftKeyDown()) {
                player.getMainHandItem().getOrCreateTag().putBoolean("Empowered", !currentStatus);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.MEDIUM;
    }

    public EnergyHandler getEnergyStorage(ItemStack stack) {
        return Energy.of(stack);
    }

    public boolean isBarVisible(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergy() > 0;
    }

    public int getBarWidth(ItemStack itemStack) {
        return (int) (getEnergyStorage(itemStack).getEnergy() / getMaxStoredPower() * 13.0);
    }

    public int getBarColor(ItemStack itemStack) {
        return 0xc300ff;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return blockState.is(this.blocks);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public boolean attemptEnergyDrain(ItemStack itemStack, int modifier) {
        EnergyHandler handler = this.getEnergyStorage(itemStack);
        if (handler.getEnergy() >= ItemConfigs.EXPEND_ENERGY_AMOUNT * modifier) {
            handler.extract(ItemConfigs.EXPEND_ENERGY_AMOUNT * modifier);
            return true;
        }
        return false;
    }
}
