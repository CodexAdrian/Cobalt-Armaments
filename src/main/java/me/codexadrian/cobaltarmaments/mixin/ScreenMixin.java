package me.codexadrian.cobaltarmaments.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.cobaltarmaments.tools.PoweredDiggerItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow public abstract List<Component> getTooltipFromItem(ItemStack itemStack);

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("INVOKE"), cancellable = true)
    private void imgToolTip(PoseStack poseStack, ItemStack itemStack, int i, int j, CallbackInfo ci) {
        if(itemStack.getItem() instanceof PoweredDiggerItem poweredItem) {
            List<Component> lines = getTooltipFromItem(itemStack);
            Optional<TooltipComponent> data = itemStack.getTooltipImage();
            List<TooltipComponent> list = lines.stream().map(Component::getVisualOrderText).map(TooltipComponent::)
        }
    }
}
