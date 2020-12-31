package me.aidanwalden.nostalgiacraft.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Final
    @Shadow
    private RotatingCubeMapRenderer backgroundRenderer;

    @Final
    @Shadow
    private final boolean doBackgroundFade = false;

    @Shadow
    private long backgroundFadeStart;

    @Final
    @Shadow
    public static Identifier PANORAMA_OVERLAY;

    @Final
    @Shadow
    public static Identifier MINECRAFT_TITLE_TEXTURE;

    @Final
    @Shadow
    public static Identifier EDITION_TITLE_TEXTURE;

    @Nullable
    @Shadow
    private String splashText;

    @Final
    @Shadow
    private boolean isMinceraft;

    @Shadow
    private int copyrightTextWidth;

    @Shadow
    private int copyrightTextX;

    @Shadow
    private boolean areRealmsNotificationsEnabled() {
        return false;
    }

    @Shadow
    private Screen realmsNotificationGui;


    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - backgroundFadeStart) / 1000.0F : 1.0F;
        fill(matrices, 0, 0, this.width, this.height, -1);
        if(NostalgiacraftCore.dirtBackground) {
            renderBackgroundTexture(0);
        }
        else {
            backgroundRenderer.render(delta, MathHelper.clamp(f, 0.0F, 1.0F));
        }
        boolean i = true;
        int j = this.width / 2 - 137;
        boolean k = true;
        this.client.getTextureManager().bindTexture(PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
        drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            this.client.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, g);
            if (this.isMinceraft) {
                this.method_29343(j, 30, (integer, integer2) -> {
                    this.drawTexture(matrices, integer + 0, integer2, 0, 0, 99, 44);
                    this.drawTexture(matrices, integer + 99, integer2, 129, 0, 27, 44);
                    this.drawTexture(matrices, integer + 99 + 26, integer2, 126, 0, 3, 44);
                    this.drawTexture(matrices, integer + 99 + 26 + 3, integer2, 99, 0, 26, 44);
                    this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
                });
            } else {
                this.method_29343(j, 30, (integer, integer2) -> {
                    this.drawTexture(matrices, integer + 0, integer2, 0, 0, 155, 44);
                    this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
                });
            }

            this.client.getTextureManager().bindTexture(EDITION_TITLE_TEXTURE);
            drawTexture(matrices, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
            if (this.splashText != null) {
                RenderSystem.pushMatrix();
                RenderSystem.translatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
                RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                float h = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
                h = h * 100.0F / (float)(this.textRenderer.getWidth(this.splashText) + 32);
                RenderSystem.scalef(h, h, h);
                drawCenteredString(matrices, this.textRenderer, this.splashText, 0, -8, 16776960 | l);
                RenderSystem.popMatrix();
            }

            String string = "Minecraft " + SharedConstants.getGameVersion().getName();
            if(!NostalgiacraftCore.doOldMenuVersion) {
                if (this.client.isDemo()) {
                    string = string + " Demo";
                } else {
                    string = string + ("release".equalsIgnoreCase(this.client.getVersionType()) ? "" : "/" + this.client.getVersionType());
                }

                if (this.client.isModded()) {
                    string = string + I18n.translate("menu.modded");
                }
            }
            if(NostalgiacraftCore.doOldMenuVersion) {
                this.textRenderer.draw(matrices, string, 2, 2, 5263440);
            }
            else {
                drawStringWithShadow(matrices, this.textRenderer, string, 2, this.height - 10, 16777215 | l);
            }
            drawStringWithShadow(matrices, this.textRenderer, "Copyright Mojang AB. Do not distribute!", this.copyrightTextX, this.height - 10, 16777215 | l);
            if (mouseX > this.copyrightTextX && mouseX < this.copyrightTextX + this.copyrightTextWidth && mouseY > this.height - 10 && mouseY < this.height) {
                fill(matrices, this.copyrightTextX, this.height - 1, this.copyrightTextX + this.copyrightTextWidth, this.height, 16777215 | l);
            }

            Iterator var12 = this.buttons.iterator();

            while(var12.hasNext()) {
                AbstractButtonWidget abstractButtonWidget = (AbstractButtonWidget)var12.next();
                abstractButtonWidget.setAlpha(g);
            }

            super.render(matrices, mouseX, mouseY, delta);
            if (this.areRealmsNotificationsEnabled() && g >= 1.0F) {
                this.realmsNotificationGui.render(matrices, mouseX, mouseY, delta);
            }
        }
        ci.cancel();
    }

}
