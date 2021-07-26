package me.codexadrian.cobaltarmaments;

import dev.technici4n.fasttransferlib.api.energy.EnergyApi;
import dev.technici4n.fasttransferlib.api.energy.base.SimpleItemEnergyIo;
import me.codexadrian.cobaltarmaments.tools.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class CobaltArmaments implements ModInitializer {

	public static final String MODID = "cobaltarmaments";
	public static final ArrayList<Item> EMPOWERABLE_ITEMS = new ArrayList<>();
	public static final Item.Properties TOOL_PROPERTIES = new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS);
	public static final Item.Properties ARMOR_PROPERTIES =new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT);
	public static final Item COBALT_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
	public static final ArmorItem COBALT_HELMET = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.HEAD, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_CHESTPLATE = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.CHEST, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_LEGGINGS = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.LEGS, ARMOR_PROPERTIES);
	public static final ArmorItem COBALT_BOOTS = new ArmorItem(new CobaltArmorMaterial(), EquipmentSlot.FEET, ARMOR_PROPERTIES);
	public static final PoweredDiggerItem COBALT_PICKAXE = new PoweredDiggerItem(TOOL_PROPERTIES, BlockTags.MINEABLE_WITH_PICKAXE, 15 , 5);
	public static final CobaltHammer COBALT_HAMMER = new CobaltHammer(TOOL_PROPERTIES);
	public static final CobaltShovel COBALT_SHOVEL = new CobaltShovel(TOOL_PROPERTIES, 15, 5);
	public static final CobaltAxe COBALT_AXE = new CobaltAxe((TOOL_PROPERTIES), 15, 5);
	public static final CobaltHoe COBALT_HOE = new CobaltHoe(TOOL_PROPERTIES, 15, 5);

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cobalt_ingot"), COBALT_INGOT);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cobalt_helmet"), COBALT_HELMET);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cobalt_chestplate"), COBALT_CHESTPLATE);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cobalt_leggings"), COBALT_LEGGINGS);
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, "cobalt_boots"), COBALT_BOOTS);
		registerEmpowerable("cobalt_hammer", COBALT_HAMMER);
		registerEmpowerable("cobalt_pickaxe", COBALT_PICKAXE);
		registerEmpowerable("cobalt_shovel", COBALT_SHOVEL);
		registerEmpowerable("cobalt_axe", COBALT_AXE);
		registerEmpowerable("cobalt_hoe", COBALT_HOE);
		EventHandler.init();
	}

	public static boolean getIfEmpowered(ItemStack stack) {
		if(stack.getTag() != null) {
			return stack.getTag().contains("Empowered") && stack.getTag().getBoolean("Empowered");
		} else return false;
	}

	public void registerEmpowerable(String name, Item item) {
		Registry.register(Registry.ITEM, new ResourceLocation(MODID, name), item);
		EMPOWERABLE_ITEMS.add(item);
	}


}
