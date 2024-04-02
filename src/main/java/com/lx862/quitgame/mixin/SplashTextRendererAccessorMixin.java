package com.lx862.quitgame.mixin;

import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SplashTextRenderer.class)
public interface SplashTextRendererAccessorMixin {
    @Accessor
    String getText();
}
