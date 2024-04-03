package com.lx862.quitgame.mixin;

import com.lx862.quitgame.CharacterRenderer;
import com.lx862.quitgame.NewEpicSplashText;
import com.lx862.quitgame.QuitGame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    @Shadow private boolean doBackgroundFade;
    @Shadow private long backgroundFadeStart;
    @Unique
    private static NewEpicSplashText splash;
    @Unique
    private double mouseX;
    @Unique
    private double mouseY;
    @Unique
    public double startX = 0;
    @Unique
    public double startY = 0;
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if(splash == null) {
            SplashTextRenderer splashTextRenderer = MinecraftClient.getInstance().getSplashTextLoader().get();
            if(splashTextRenderer != null) {
                splash = new NewEpicSplashText(((SplashTextRendererAccessorMixin)splashTextRenderer).getText());
            } else {
                splash = new NewEpicSplashText("MISSINGNO");
            }
        }
        startX = (width / 2.0F) + 123F;
        startY = 78;
        for(CharacterRenderer characterRenderer : new ArrayList<>(splash.chars)) {
            characterRenderer.setStartPos(startX, startY);
        }
        positionCharacters(true);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void hideWidget(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for(String keyword : QuitGame.keywords) {
            if(splash.startsWith(keyword)) {
                QuitGame.unlocked = true;
            }
        }

        if(QuitGame.unlocked) {
            QuitGame.unlockedAlpha = MathHelper.clamp(QuitGame.unlockedAlpha + (delta / 20), 0.0F, 1.0F);
        }

        for(Element element : children()) {
            if(element instanceof ButtonWidget) {
                TextContent msg = ((ButtonWidget)element).getMessage().getContent();
                if(element instanceof PressableTextWidget) continue; // Copyright
                if(msg instanceof TranslatableTextContent) {
                    if(((TranslatableTextContent) msg).getKey().contains("quit")) {
                        continue;
                    }
                }

                ((ButtonWidget) element).visible = QuitGame.unlocked;
                ((ButtonWidget) element).setAlpha(QuitGame.unlockedAlpha);
            }
        }
    }


    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        float alpha = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 2000.0F : 1.0F;
        QuitGame.scale = 1.8F * 100.0F / (float)(textRenderer.getWidth(splash.text) + 32);

        float scale = (float)QuitGame.scale - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * (0.1F * ((float)QuitGame.scale / 1.8F)));
        positionCharacters(false);

        context.getMatrices().push();
        context.getMatrices().translate(startX, startY, 0);
        context.getMatrices().scale(scale, scale, scale);
        for(CharacterRenderer characterRenderer : new ArrayList<>(splash.chars)) {
            context.getMatrices().push();
            characterRenderer.render(context, delta / 3f, (int)(alpha * 255) << 24, textRenderer);
            context.getMatrices().pop();
        }

        context.getMatrices().pop();
//
//        for(CharacterRenderer characterRenderer : new ArrayList<>(QuitGame.splash.chars)) {
//            characterRenderer.renderBoundary(context, delta / 3f, textRenderer);
//        }
    }

    public void positionCharacters(boolean absolute) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        QuitGame.scale = 1.8F * (100.0F / (float)(textRenderer.getWidth(splash.text) + 32));

        double strLength = textRenderer.getWidth(splash.text) * QuitGame.scale;
        double xSoFar = 0 - ((strLength / 2) / QuitGame.scale);
        double ySoFar = (splash.chars.size() * (8 * (QuitGame.rotAngle / 90.0))) / 2;

        for(CharacterRenderer characterRenderer : new ArrayList<>(splash.chars)) {
            if(absolute) {
                characterRenderer.setExactPos(xSoFar, ySoFar);
            } else {
                characterRenderer.setTargetPos(xSoFar, ySoFar);
            }
            xSoFar += textRenderer.getWidth(String.valueOf(characterRenderer.getChar()));
            ySoFar -= (characterRenderer.width * 1.6) * (QuitGame.rotAngle / 90.0);
        }

        for(CharacterRenderer characterRenderer : new ArrayList<>(splash.chars)) {
            if(characterRenderer.isDragging()) {
                int idx = getMouseCharIndex();
                if(idx != -1) {
                    splash.reorder(characterRenderer, idx);
                }
            }
        }
    }

    @Unique
    private int getMouseCharIndex() {
        int i = 0;
        for(CharacterRenderer characterRenderer : new ArrayList<>(splash.chars)) {
            if(characterRenderer.hovered(mouseX, mouseY, true)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(CharacterRenderer characterRenderer : splash.chars) {
            if(characterRenderer.hovered(mouseX, mouseY, false)) {
                characterRenderer.dragged();
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(CharacterRenderer characterRenderer : splash.chars) {
            characterRenderer.released();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
