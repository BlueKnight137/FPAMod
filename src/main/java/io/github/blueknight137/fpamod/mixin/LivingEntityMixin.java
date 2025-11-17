package io.github.blueknight137.fpamod.mixin;

import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.Swing;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"))
    private void onSwing(Hand hand, CallbackInfo ci){
        if((Object) this instanceof ClientPlayerEntity player) {
            Swing.EVENT.invoker().swing(player, hand);
        }
    }
}
