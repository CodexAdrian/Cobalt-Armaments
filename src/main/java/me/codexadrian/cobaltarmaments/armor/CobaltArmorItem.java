package me.codexadrian.cobaltarmaments.armor;

import me.codexadrian.cobaltarmaments.ItemConfigs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

public class CobaltArmorItem extends ArmorItem implements EnergyHolder {
    public CobaltArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @Override
    public double getMaxStoredPower() {
        return ItemConfigs.ITEM_ENERGY;
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
}
