package me.codexadrian.silverarmaments;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import me.codexadrian.silverarmaments.render.EnergyBlastRenderer;
import me.codexadrian.silverarmaments.render.QuiverRenderer;
import me.codexadrian.silverarmaments.render.SilverQuiverModel;
import me.codexadrian.silverarmaments.tools.SilverQuiver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class SilverArmamentsClient implements ClientModInitializer {
    public static RenderLayer renderLayer = RenderLayer.of("energy_blast", VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().shader(new RenderPhase.Shader(() -> SilverArmamentsClient.shaderThing)).transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)/*.depthTest(ALWAYS_DEPTH_TEST).writeMaskState(COLOR_MASK)*/.build(false));
    public static Shader shaderThing;

    @Override
    public void onInitializeClient() {
        for (Item item : SilverArmaments.EMPOWERABLE_ITEMS) {
            FabricModelPredicateProviderRegistry.register(item, new Identifier(SilverArmaments.MODID, "empowered"), (itemStack, clientLevel, livingEntity, i) -> SilverArmaments.getIfEmpowered(itemStack) ? 1 : 0);
        }
        TrinketRendererRegistry.registerRenderer(SilverArmaments.SILVER_QUIVER, new QuiverRenderer());
        EntityRendererRegistry.INSTANCE.register(SilverArmaments.ENERGY_BLAST_ENTITY, EnergyBlastRenderer::new);
    }
}


