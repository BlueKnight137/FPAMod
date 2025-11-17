package io.github.blueknight137.fpamod.mixin;

import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.Attack;
import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.InteractBlock;
import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.InteractItem;
import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.Mine;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow public abstract boolean isBreakingBlock();

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        Attack.EVENT.invoker().attack(player, target);
    }

    @Inject(method = "attackBlock", at = @At("HEAD"))
    private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        Mine.EVENT.invoker().mine();
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"))
    private void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        Mine.EVENT.invoker().mine();
    }

    @Inject(method = "interactBlock", at = @At("HEAD"))
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        InteractBlock.EVENT.invoker().interactBlock(hand);
    }

    @Inject(method = "interactItem", at = @At("HEAD"))
    private void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        InteractItem.EVENT.invoker().interactItem(hand);
    }
}
