package nl.enjarai.doabarrelroll.mixin;

import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Redirect(
            method = "updateMouse",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;smoothCameraEnabled:Z"
            )
    )
    private boolean smoothCameraEnabled(GameOptions options) {
        return options.smoothCameraEnabled || DoABarrelRollClient.shouldSmooth();
    }

    @ModifyArg(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SmoothUtil;smooth(DD)D"
            ), index = 1

    )
    private double scaleSmoothing(double original) {
        if (DoABarrelRollClient.shouldSmooth()) {
            return original * 4;
        }
        return original;
    }


    @Redirect(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
            )
    )
    private void changeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
        DoABarrelRollClient.updateMouse(player, cursorDeltaX, cursorDeltaY);
    }
}
