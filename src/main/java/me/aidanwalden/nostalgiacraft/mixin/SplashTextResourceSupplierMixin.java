package me.aidanwalden.nostalgiacraft.mixin;

import me.aidanwalden.nostalgiacraft.NostalgiacraftCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Calendar;
import java.util.Date;

@Environment(EnvType.CLIENT)
@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin {

    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    public void mixin(CallbackInfoReturnable<String> cir) {
        if(NostalgiacraftCore.doNotchSplash) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) { //Bring back "Happy Birthday, Notch!" splash message that displays on his birthday
                cir.setReturnValue("Happy Birthday, Notch!");
            }
            else if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) { //Bring back "Happy Birthday, ez!" splash message that displays on her birthday
                cir.setReturnValue("Happy Birthday, ez!");
            }
        }
    }
}
