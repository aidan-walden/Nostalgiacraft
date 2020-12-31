package me.aidanwalden.nostalgiacraft.mixin;

import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper{

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getFontRenderer();

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(NostalgiacraftCore.showWatermark && !client.options.debugEnabled && !client.options.hudHidden) {
            //if(Session.AccountType.byName(client.getSession().getUsername()) != null) {
            drawStringWithShadow(matrices, this.getFontRenderer(), "Minecraft " + SharedConstants.getGameVersion().getName(), 2, 2, 16777215);
            /*}
            else {
                drawStringWithShadow(matrices, this.getFontRenderer(), "Minecraft " + SharedConstants.getGameVersion().getName() + "    Unlicensed Copy :(", 2, 2, 16777215);
                drawStringWithShadow(matrices, this.getFontRenderer(), "(Or logged in from another location)", 2, 12, 16777215);
                drawStringWithShadow(matrices, this.getFontRenderer(), "Purchase at minecraft.net", 2, 22, 16777215);
            }*/
        }
    }

    @Inject(method = "renderHeldItemTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        if(!NostalgiacraftCore.doHotbarTooltip) ci.cancel();
    }
}
