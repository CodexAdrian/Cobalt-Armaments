package me.codexadrian.silverarmaments.render;

import me.codexadrian.silverarmaments.SilverArmamentsClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.EnergyBlastEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.system.CallbackI;

public class EnergyBlastRenderer extends EntityRenderer<EnergyBlastEntity> {

    public EnergyBlastRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnergyBlastEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        var buffer = vertexConsumers.getBuffer(SilverArmamentsClient.renderLayer);
        long gameTime = entity.world.getTime();
        BlockPos origin = entity.getORIGIN();
        BlockPos current = entity.getBlockPos();
        int xLength = origin.getX() - current.getX();
        int yLength = origin.getY() - current.getY();
        int zLength = origin.getZ() - current.getZ();

        int finalLength = (xLength * xLength + zLength * zLength) + yLength * yLength;

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(entity.getYaw()));
        for (int i = 0; i < finalLength * 100/12; i++) {
            float radialModifier =  (float) (Math.sin(gameTime + (i) + 1.3) * .3) * .5F;
            matrices.push();
            float zVal = -i * .12F;
            matrices.translate(0, 0, zVal);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - this.dispatcher.camera.getYaw())); // y axis
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-this.dispatcher.camera.getPitch())); // x axis
            var model = matrices.peek().getModel();
            buffer.vertex(model, -0.5F + radialModifier, 1  - radialModifier, 0).texture(0, 0).color(0xc3, 0, 0xff, 0xff).next();
            buffer.vertex(model, -0.5F + radialModifier, 0 + radialModifier, 0).texture(0, 1).color(0xc3, 0, 0xff, 0xff).next();
            buffer.vertex(model, 0.5F - radialModifier, 0 + radialModifier, 0).texture(1, 1).color(0xc3, 0, 0xff, 0xff).next();
            buffer.vertex(model, 0.5F - radialModifier, 1  - radialModifier, 0).texture(1, 0).color(0xc3, 0, 0xff, 0xff).next();
            matrices.pop();
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        //curse you mojang
    }

    @Override
    public Identifier getTexture(EnergyBlastEntity entity) {
        return null;
    }
}
