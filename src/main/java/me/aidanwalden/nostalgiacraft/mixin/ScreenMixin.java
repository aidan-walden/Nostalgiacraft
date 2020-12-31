package me.aidanwalden.nostalgiacraft.mixin;

import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements TickableElement, Drawable {

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow @Nullable protected MinecraftClient client;

    @Inject(method = "renderOrderedTooltip", at = @At(value = "HEAD"), cancellable = true)
    private void mixin(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, CallbackInfo ci) {
        if(NostalgiacraftCore.doOldTooltip) {
            if(!lines.isEmpty()) {
                int i = 0;
                Iterator iter = lines.iterator();

                while(iter.hasNext()) {
                    OrderedText orderedText = (OrderedText)iter.next();
                    int j = client.textRenderer.getWidth(orderedText);
                    if (j > i) {
                        i = j;
                    }
                }

                int k = x + 12;
                int l = y - 12;
                int n = 8;
                if (lines.size() > 1) {
                    n += 2 + (lines.size() - 1) * 10;
                }

                if (k + i > this.width) {
                    k -= 28 + i;
                }

                if (l + n + 6 > this.height) {
                    l = this.height - n - 6;
                }
                matrices.translate(0.0D, 0.0D, 400.0D);
                this.fillGradient(matrices, k - 3, l - 3, k + i + 3, l + (11 * lines.size()), -1073741824, -1073741824);
                for(int s = 0; s < lines.size(); ++s) {
                    OrderedText orderedText2 = (OrderedText)lines.get(s);
                    if (orderedText2 != null) {
                        client.textRenderer.drawWithShadow(matrices, (OrderedText)orderedText2, (float)k, (float)l, -1);
                    }

                    if (s == 0) {
                        l += 2;
                    }

                    l += 10;
                }
                ci.cancel();
            }
        }
    }
}