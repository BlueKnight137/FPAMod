package io.github.blueknight137.fpamod.mixin;

import io.github.blueknight137.fpamod.events.FPAHandRenderEvents.FirstPersonRenderContext;
import io.github.blueknight137.fpamod.events.FPAHandRenderEvents.HandRender;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void swingArm(float swingProgress, float equipProgress, MatrixStack matrices, int armX, Arm arm);

    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light);


    @Shadow private float lastEquipProgressMainHand;

    @Shadow private float lastEquipProgressOffHand;

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = Shift.AFTER), cancellable = true)
    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if(!HandRender.START.invoker().start(
                new FirstPersonRenderContext(
                        (HeldItemRenderer)(Object)this,
                        matrices,
                        player,
                        item,
                        hand,
                        tickProgress,
                        swingProgress,
                        equipProgress,
                        hand == Hand.MAIN_HAND ? lastEquipProgressMainHand : lastEquipProgressOffHand))) {
            ci.cancel();
            renderItem(
                    player, item, ItemDisplayContext.NONE, matrices, vertexConsumers, light
            );
            matrices.pop();
        }
    }
}
