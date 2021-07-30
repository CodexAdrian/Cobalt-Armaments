package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.tools.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.EnergyBlastEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.Energy;

import java.util.ArrayList;

public class CobaltArmaments implements ModInitializer {

	public static final String MODID = "cobaltarmaments";

	public static final ArrayList<Item> EMPOWERABLE_ITEMS = new ArrayList<>();
	public static final Item.Settings TOOL_PROPERTIES = new Item.Settings().maxCount(1).group(ItemGroup.TOOLS);
	public static final Item.Settings ARMOR_PROPERTIES =new Item.Settings().maxCount(1).group(ItemGroup.COMBAT);
	public static final Item COBALT_INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
	public static final CobaltToolMaterial COBALT_TOOL_MATERIAL = new CobaltToolMaterial(4, ItemConfigs.ITEM_ENERGY, 15, 9, 15, () -> Ingredient.ofItems(COBALT_INGOT));
	public static final ArmorItem COBALT_HELMET = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.HEAD, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_CHESTPLATE = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.CHEST, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_LEGGINGS = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.LEGS, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_BOOTS = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.FEET, ARMOR_PROPERTIES);
	public static final CobaltPickaxe COBALT_PICKAXE = new CobaltPickaxe(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final CobaltHammer COBALT_HAMMER = new CobaltHammer(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final CobaltShovel COBALT_SHOVEL = new CobaltShovel(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final CobaltAxe COBALT_AXE = new CobaltAxe(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final CobaltHoe COBALT_HOE = new CobaltHoe(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final CobaltSword COBALT_SWORD = new CobaltSword(COBALT_TOOL_MATERIAL,15 , 5, TOOL_PROPERTIES);
	public static final EntityType<EnergyBlastEntity> ENERGY_BLAST_ENTITY = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EnergyBlastEntity::new).dimensions(EntityDimensions.changing(1, 1)).build();
	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MODID, "cobalt_ingot"), COBALT_INGOT);
		Registry.register(Registry.ITEM, new Identifier(MODID, "cobalt_helmet"), COBALT_HELMET);
		Registry.register(Registry.ITEM, new Identifier(MODID, "cobalt_chestplate"), COBALT_CHESTPLATE);
		Registry.register(Registry.ITEM, new Identifier(MODID, "cobalt_leggings"), COBALT_LEGGINGS);
		Registry.register(Registry.ITEM, new Identifier(MODID, "cobalt_boots"), COBALT_BOOTS);
		Registry.register(Registry.ENTITY_TYPE, new Identifier(MODID, "energy_blast"), ENERGY_BLAST_ENTITY);
		registerEmpowerable("cobalt_hammer", COBALT_HAMMER);
		registerEmpowerable("cobalt_pickaxe", COBALT_PICKAXE);
		registerEmpowerable("cobalt_shovel", COBALT_SHOVEL);
		registerEmpowerable("cobalt_axe", COBALT_AXE);
		registerEmpowerable("cobalt_hoe", COBALT_HOE);
		registerEmpowerable("cobalt_sword", COBALT_SWORD);
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
