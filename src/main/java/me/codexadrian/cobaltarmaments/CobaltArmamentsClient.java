package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.tools.PoweredDiggerItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CobaltArmamentsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Item item : CobaltArmaments.EMPOWERABLE_ITEMS) {
            FabricModelPredicateProviderRegistry.register(item, new ResourceLocation(CobaltArmaments.MODID, "empowered"), (itemStack, clientLevel, livingEntity, i) -> CobaltArmaments.getIfEmpowered(itemStack) ? 1 : 0);
        }
    }
}
