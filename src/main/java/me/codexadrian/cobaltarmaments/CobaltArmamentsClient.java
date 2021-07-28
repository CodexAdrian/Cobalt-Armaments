package me.codexadrian.cobaltarmaments;

import me.codexadrian.cobaltarmaments.render.EnergyBlastRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class CobaltArmamentsClient implements ClientModInitializer {
    public static RenderLayer renderLayer = RenderLayer.of("energy_blast", VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().shader(new RenderPhase.Shader(() -> CobaltArmamentsClient.shaderThing)).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)/*.depthTest(ALWAYS_DEPTH_TEST).writeMaskState(COLOR_MASK)*/.build(false));
    public static Shader shaderThing;
    @Override
    public void onInitializeClient() {
        for (Item item : CobaltArmaments.EMPOWERABLE_ITEMS) {
            FabricModelPredicateProviderRegistry.register(item, new Identifier(CobaltArmaments.MODID, "empowered"), (itemStack, clientLevel, livingEntity, i) -> CobaltArmaments.getIfEmpowered(itemStack) ? 1 : 0);
        }

        EntityRendererRegistry.INSTANCE.register(CobaltArmaments.ENERGY_BLAST_ENTITY, EnergyBlastRenderer::new);

    }
}


