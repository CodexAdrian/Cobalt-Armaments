package me.codexadrian.silverarmaments.tools;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import me.codexadrian.silverarmaments.SilverArmaments;
import me.codexadrian.silverarmaments.SilverTool;
import me.codexadrian.silverarmaments.render.SilverQuiverModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SilverQuiver extends TrinketItem implements SilverTool{
    private SilverQuiverModel model;
    private static final Identifier texture = new Identifier(SilverArmaments.MODID, "textures/quiver/quiver.png");
    public SilverQuiver(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        SilverTool.super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return SilverTool.super.isBarVisible(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return SilverTool.super.getBarWidth(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return SilverTool.super.getBarColor();
    }

}