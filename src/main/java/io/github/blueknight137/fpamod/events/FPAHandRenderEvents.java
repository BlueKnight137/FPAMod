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

        // mojang moment
        public float getActualAttackCooldown() {
            var modifiers = item().getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).modifiers();
            float lastAttackedTicks = ((LivingEntityAccessor) player()).getLastAttackedTicks();
            for(var modifier : modifiers) {
                if(modifier.attribute().matchesKey(EntityAttributes.ATTACK_SPEED.getKey().get())) {
                    float attackCooldownProgressPerTick = (float) (1.0 / (4.0 + modifier.modifier().value()) * 20.0);
                    return MathHelper.clamp((lastAttackedTicks + tickProgress()) / attackCooldownProgressPerTick, 0f, 1f);
                }
            }
            return MathHelper.clamp((lastAttackedTicks + tickProgress()) / (1f/4f*20f), 0f, 1f);
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
