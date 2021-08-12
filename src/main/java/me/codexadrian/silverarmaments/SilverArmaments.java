package me.codexadrian.silverarmaments;

import me.codexadrian.silverarmaments.tools.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.EnergyBlastEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.Energy;

import java.util.ArrayList;

public class SilverArmaments implements ModInitializer {

	public static final String MODID = "silverarmaments";

	public static final ArrayList<Item> EMPOWERABLE_ITEMS = new ArrayList<>();
	public static final Item.Settings TOOL_PROPERTIES = new Item.Settings().maxCount(1).group(ItemGroup.TOOLS);
	public static final Item.Settings ARMOR_PROPERTIES =new Item.Settings().maxCount(1).group(ItemGroup.COMBAT);
	public static final Item SILVER_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item LAZULI_FLUX_CRYSTAL = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final SilverQuiver SILVER_QUIVER = new SilverQuiver(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final Item REDSTONE_ROD = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final SilverToolMaterial SILVER_TOOL_MATERIAL = new SilverToolMaterial(4, ItemConfigs.ITEM_ENERGY, 15, 1, 15, () -> Ingredient.ofItems(SILVER_INGOT));
	public static final ArmorItem SILVER_HELMET = new ArmorItem(new SilverArmorMaterial(), EquipmentSlot.HEAD, ARMOR_PROPERTIES);
	public static final ArmorItem SILVER_CHESTPLATE = new ArmorItem(new SilverArmorMaterial(), EquipmentSlot.CHEST, ARMOR_PROPERTIES);
	public static final ArmorItem SILVER_LEGGINGS = new ArmorItem(new SilverArmorMaterial(), EquipmentSlot.LEGS, ARMOR_PROPERTIES);
	public static final ArmorItem SILVER_BOOTS = new ArmorItem(new SilverArmorMaterial(), EquipmentSlot.FEET, ARMOR_PROPERTIES);
	public static final SilverPickaxe SILVER_PICKAXE = new SilverPickaxe(SILVER_TOOL_MATERIAL, ItemConfigs.PICKAXE_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverHammer SILVER_HAMMER = new SilverHammer(SILVER_TOOL_MATERIAL, ItemConfigs.HAMMER_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverShovel SILVER_SHOVEL = new SilverShovel(SILVER_TOOL_MATERIAL, ItemConfigs.SHOVEL_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverAxe SILVER_AXE = new SilverAxe(SILVER_TOOL_MATERIAL, ItemConfigs.AXE_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverHoe SILVER_HOE = new SilverHoe(SILVER_TOOL_MATERIAL, ItemConfigs.HOE_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverSword SILVER_SWORD = new SilverSword(SILVER_TOOL_MATERIAL, ItemConfigs.SWORD_DAMAGE, 5, TOOL_PROPERTIES);
	public static final SilverBow SILVER_BOW = new SilverBow(TOOL_PROPERTIES);
	public static final EntityType<EnergyBlastEntity> ENERGY_BLAST_ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EnergyBlastEntity::new).dimensions(EntityDimensions.changing(1, 1)).build();
	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_ingot"), SILVER_INGOT);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_flux_crystal"), LAZULI_FLUX_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier(MODID, "redstone_rod"), REDSTONE_ROD);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_helmet"), SILVER_HELMET);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_chestplate"), SILVER_CHESTPLATE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_leggings"), SILVER_LEGGINGS);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_boots"), SILVER_BOOTS);
		Registry.register(Registry.ENTITY_TYPE, new Identifier(MODID, "energy_blast"), ENERGY_BLAST_ENTITY);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_quiver"), SILVER_QUIVER);
		Registry.register(Registry.ITEM, new Identifier(MODID, "lazuli_silver_bow"), SILVER_BOW);
		registerEmpowerable("lazuli_silver_hammer", SILVER_HAMMER);
		registerEmpowerable("lazuli_silver_pickaxe", SILVER_PICKAXE);
		registerEmpowerable("lazuli_silver_shovel", SILVER_SHOVEL);
		registerEmpowerable("lazuli_silver_axe", SILVER_AXE);
		registerEmpowerable("lazuli_silver_hoe", SILVER_HOE);
		registerEmpowerable("lazuli_silver_sword", SILVER_SWORD);
		EventHandler.init();
	}

	public static boolean getIfEmpowered(ItemStack stack) {
		if(stack.getNbt() != null) {
			return stack.getNbt().contains("Empowered") && stack.getNbt().getBoolean("Empowered");
		} else return false;
	}

	public static boolean hasEnoughEnergy(ItemStack stack) {
		return Energy.of(stack).getEnergy() > ItemConfigs.EXPEND_ENERGY_AMOUNT;
	}

	public void registerEmpowerable(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
		EMPOWERABLE_ITEMS.add(item);
	}

}
