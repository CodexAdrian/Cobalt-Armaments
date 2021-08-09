package me.codexadrian.silverarmaments.render;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import me.codexadrian.silverarmaments.SilverArmaments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class QuiverRenderer implements TrinketRenderer {
    private SilverQuiverModel model;
    private static final Identifier texture = new Identifier(SilverArmaments.MODID, "textures/quiver/quiver.png");

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(contextModel instanceof PlayerEntityModel<?> playerModel) {
            matrices.push();
            TrinketRenderer.translateToChest(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) playerModel, (AbstractClientPlayerEntity) entity);
            matrices.translate(.5, -0.4, -0.3);
            getModel().render(matrices, vertexConsumers.getBuffer(getModel().getLayer(texture)), light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1);
            matrices.pop();
        }
    }

    @Environment(EnvType.CLIENT)
    private SilverQuiverModel getModel() {
        if (this.model == null) {
            // Vanilla 1.17 uses EntityModels, EntityModelLoader and EntityModelLayers
            this.model = new SilverQuiverModel(SilverQuiverModel.createData().createModel());
        }
        return model;
    }
}
