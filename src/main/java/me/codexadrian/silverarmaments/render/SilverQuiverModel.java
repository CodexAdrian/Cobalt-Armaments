package me.codexadrian.silverarmaments.render;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class SilverQuiverModel extends Model {


	public SilverQuiverModel(ModelPart part) {
		super(RenderLayer::getEntityCutout);
		group = part.getChild("quiver");

		/*
		texWidth = 64;
		texHeight = 64;

		group = new ModelRenderer(this);
		group.setPos(-8.0F, 16.0F, 8.0F);
		group.texOffs(6, 7).addBox(5.0F, -16.5F, -3.0F, 0.0F, 2.0F, 6.0F, 0.0F, true);
		group.texOffs(0, 13).addBox(-5.0F, -10.5F, -3.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
		group.texOffs(0, 38).addBox(-5.0F, -16.5F, -3.0F, 10.0F, 8.0F, 0.0F, 0.0F, false);
		group.texOffs(21, 28).addBox(-5.0F, -16.5F, 3.0F, 10.0F, 9.0F, 0.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(2.0F, -5.5F, 4.5F);
		group.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F,x 0.7854F);
		cube_r1.texOffs(1, 48).addBox(-16.0F, -4.0F, -1.0F, 7.0F, 4.0F, 0.0F, 0.0F, false);
		cube_r1.texOffs(0, 56).addBox(-9.0F, -4.0F, -3.0F, 9.0F, 4.0F, 4.0F, 0.0F, false);
		*/
	}

	private final ModelPart group;
	//private final ModelPart cube_r1;

	public static TexturedModelData createData() {
		ModelData data = new ModelData();
		var builder = ModelPartBuilder.create();
		builder.cuboid("cube", 5.0F, -16.5F, -3.0F, 0, 2, 6, 6, 7);
		builder.cuboid("cube", -5.0F, -10.5F, -3.0F, 0, 3, 6, 0, 13 );
		builder.cuboid("cube", -5.0F, -16.5F, -3.0F, 10, 8, 0,  0, 38);
		builder.cuboid("cube", -5.0F, -16.5F, 3.0F, 10, 9, 0, 21, 28);

		var quiver = data.getRoot().addChild("quiver", builder, ModelTransform.pivot(-8.0F, 16.0F, 8.0F));

		var cubeBuilder = ModelPartBuilder.create();
		cubeBuilder.cuboid("cube", -16.0F, -4.0F, -1, 7, 4, 0, 1, 48);
		cubeBuilder.cuboid("cube", -9.0F, -4.0F, -3.0F, 9, 4, 4, 0, 56);

		quiver.addChild("cube_r1", cubeBuilder, ModelTransform.of(2.0F, -5.5F, 4.5F, 0F, 0F, 0.7854F));
		return TexturedModelData.of(data, 64, 64);
	}


	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		group.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}