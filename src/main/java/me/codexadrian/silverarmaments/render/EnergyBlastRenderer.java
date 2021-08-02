package me.codexadrian.silverarmaments.render;

import me.codexadrian.silverarmaments.SilverArmamentsClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.EnergyBlastEntity;
import net.minecraft.util.Identifier;

public class EnergyBlastRenderer extends EntityRenderer<EnergyBlastEntity> {

    public EnergyBlastRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnergyBlastEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        var model = matrices.peek().getModel();
        var buffer = vertexConsumers.getBuffer(SilverArmamentsClient.renderLayer);
        buffer.vertex(model, -0.5F, 1, 0).texture(0, 0).color(0xc3,0, 0xff, 0xff).next();
        buffer.vertex(model, -0.5F, 0, 0).texture(0, 1).color(0xc3,0, 0xff, 0xff).next();
        buffer.vertex(model, 0.5F, 0, 0).texture(1, 1).color(0xc3,0, 0xff, 0xff).next();
        buffer.vertex(model, 0.5F, 1, 0).texture(1, 0).color(0xc3,0, 0xff, 0xff).next();
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        //curse you mojang
    }

    @Override
    public Identifier getTexture(EnergyBlastEntity entity) {
        return null;
    }
}
