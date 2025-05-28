package com.lx862.quitgame.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public class SplashTextRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int screenWidth, TextRenderer textRenderer, float alpha, CallbackInfo ci) {
        ci.cancel();
    }
}
