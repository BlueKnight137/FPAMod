package io.github.blueknight137.fpamod.events;

import io.github.blueknight137.fpamod.mixin.LivingEntityAccessor;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public final class FPAHandRenderEvents {

    public record FirstPersonRenderContext(HeldItemRenderer heldItemRenderer, MatrixStack matrixStack, AbstractClientPlayerEntity player, ItemStack item, Hand hand, float tickProgress, float swingProgress, float equipProgress, float lastEquipItemProgress) {

        /***
         * This method does almost the same thing as [{@link net.minecraft.entity.player.PlayerEntity#getAttackCooldownProgress(float)}] except
         * it fixes an issue with the attack speed modifier only applying to the player one tick after swapping to an item.
         * This causes the animations to not be smooth and act weird, because the attack speed changes one tick into the animation.
         * @param baseTime the same as in the original method
         * @param delay the amount of ticks the progress should be delayed by, used to counteract minecraft's built in
         *              animations lasting one tick longer than the attack cooldown
         * @return the actual correct attack cooldown optionally with delay
         */
        public float getActualAttackCooldown(float baseTime, int delay) {
            var modifiers = item().getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).modifiers();
            float lastAttackedTicks = ((LivingEntityAccessor) player()).getLastAttackedTicks() - delay;
            for(var modifier : modifiers) {
                if(modifier.attribute().matchesKey(EntityAttributes.ATTACK_SPEED.getKey().get())) {
                    float attackCooldownProgressPerTick = (float) (1.0 / (4.0 + modifier.modifier().value()) * 20.0);
                    return MathHelper.clamp((lastAttackedTicks + baseTime) / attackCooldownProgressPerTick, 0f, 1f);
                }
            }
            return MathHelper.clamp((lastAttackedTicks + baseTime) / (1f/4f*20f), 0f, 1f);
        }

        public float getActualAttackCooldown(float baseTime) {
            return getActualAttackCooldown(baseTime, 0);
        }

        public float getActualAttackCooldown() {
            return getActualAttackCooldown(tickProgress(), 0);
        }
    }

    public interface HandRender {
        Event<HandRender> START = EventFactory.createArrayBacked(HandRender.class,
                (listeners) -> firstPersonRenderData -> {
                   for (var listener : listeners) {
                       if(!listener.start(firstPersonRenderData)) {
                           return false;
                       }
                   }

                   return true;
                });

        boolean start(FirstPersonRenderContext renderData);
    }
}
