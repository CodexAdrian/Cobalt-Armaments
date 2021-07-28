package me.codexadrian.cobaltarmaments.mixin;

import com.mojang.datafixers.util.Pair;
import me.codexadrian.cobaltarmaments.CobaltArmaments;
import me.codexadrian.cobaltarmaments.CobaltArmamentsClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Map<String, Shader> shaders;

    @Inject(method = "loadShaders", at = @At("TAIL"))
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) {
        Optional<Pair<Shader, Consumer<Shader>>> list = Optional.empty();
        try {
            list = Optional.of(Pair.of(new Shader(resourceManager, "rendertype_energy_blast", VertexFormats.POSITION_TEXTURE_COLOR), (shaderInstance) -> CobaltArmamentsClient.shaderThing = shaderInstance));
        } catch (Exception e) {
            list.ifPresent(pair -> pair.getFirst().close());
            throw new RuntimeException("could not reload shaders", e);
        }

        list.ifPresent(pair -> {
            Shader shaderInstance = pair.getFirst();
            this.shaders.put(shaderInstance.getName(), shaderInstance);
            pair.getSecond().accept(shaderInstance);
        });
    }
}