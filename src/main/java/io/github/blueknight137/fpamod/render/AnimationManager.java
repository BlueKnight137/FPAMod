package io.github.blueknight137.fpamod.render;

import io.github.blueknight137.fpamod.events.FPAClientPlayerEvents.*;
import io.github.blueknight137.fpamod.events.FPAHandRenderEvents.FirstPersonRenderContext;
import io.github.blueknight137.fpamod.events.FPAHandRenderEvents.HandRender;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public class AnimationManager {

    private final ItemInspectManager itemInspectManager;
    private final AnimationResourceManager animationResourceManager;
    private final AnimationTypeClientRegistry animationTypeClientRegistry;
    private final Map<Hand, AnimationType> lastAnimation;
    private final Map<Hand, Item> previousItem;

    /**
     * Used for more accurate tracking of hand swinging.
     * <p>
     * When the hand is swung and the player changes items the client will not properly update handSwinging to be false.
     * We could technically fix this issue in the client, but we do not want to modify the game's behavior if it is not necessary.
     * Also, it is difficult to track the cause of the swing without this.
     */
    private PlayerAction playerAction = PlayerAction.IDLE;

    private Hand actingHand = Hand.MAIN_HAND;

    private void handleSwing(ClientPlayerEntity player, Hand hand) {
        if(hand == Hand.MAIN_HAND) {
            if(player.getAttackCooldownProgress(0) < 1 && playerAction == PlayerAction.IDLE) {
                playerAction = PlayerAction.MISSING;
                actingHand = Hand.MAIN_HAND;
            }
        }
    }

    /**
     * See {@link net.minecraft.entity.player.PlayerEntity#attack(Entity)} for the crit conditions.
     * This just mimics the logic in the game.
     */
    private boolean isCriting(PlayerEntity player, Entity target) {
        float h = player.getAttackCooldownProgress(0.5F);
        return h > 0.9
                && player.fallDistance > 0.0
                && !player.isOnGround()
                && !player.isClimbing()
                && !player.isTouchingWater()
                && !player.hasStatusEffect(StatusEffects.BLINDNESS)
                && !player.hasVehicle()
                && target instanceof LivingEntity
                && !player.isSprinting();
    }

    private void handleAttack(PlayerEntity player, Entity target) {
        playerAction = isCriting(player, target) ? PlayerAction.CRITING : PlayerAction.ATTACKING;
        actingHand = Hand.MAIN_HAND;
    }

    private void handleMine() {
        playerAction = PlayerAction.MINING;
        actingHand = Hand.MAIN_HAND;
    }

    private void handleInteractBlock(Hand hand) {
        playerAction = PlayerAction.INTERACTING_WITH_BLOCK;
        actingHand = hand;
    }

    private void handleInteractItem(Hand hand) {
        playerAction = PlayerAction.USING_ITEM;
        actingHand = hand;
    }

    public AnimationManager(ItemInspectManager itemInspectManager, AnimationResourceManager animationResourceManager, AnimationTypeClientRegistry animationTypeClientRegistry) {
        this.itemInspectManager = itemInspectManager;
        this.animationResourceManager = animationResourceManager;
        this.animationTypeClientRegistry = animationTypeClientRegistry;
        lastAnimation = new HashMap<>();
        lastAnimation.put(Hand.MAIN_HAND, AnimationTypes.NONE);
        lastAnimation.put(Hand.OFF_HAND, AnimationTypes.NONE);
        previousItem = new HashMap<>();
        previousItem.put(Hand.MAIN_HAND, Items.AIR);
        previousItem.put(Hand.OFF_HAND, Items.AIR);
        HandRender.START.register(this::handleRenderFirstPerson);
        Swing.EVENT.register(this::handleSwing);
        Attack.EVENT.register(this::handleAttack);
        Mine.EVENT.register(this::handleMine);
        InteractBlock.EVENT.register(this::handleInteractBlock);
        InteractItem.EVENT.register(this::handleInteractItem);
    }

    private AnimationType decideType(FirstPersonRenderContext renderData, PlayerActionContext playerActionContext) {
        for (Iterator<AnimationType> it = animationTypeClientRegistry.getIterableAnimationTypes(); it.hasNext(); ) {
            var type = it.next();
            if(type.isApplicable(lastAnimation.get(renderData.hand()), renderData, playerActionContext)) {
                return type;
            }
        }
        return AnimationTypes.NONE;
    }

    private void resetHand(Hand hand, Item item) {
        previousItem.put(hand, item);
        lastAnimation.put(hand, AnimationTypes.NONE);
        resetHandAction();
    }

    private void resetHandAction() {
        playerAction = PlayerAction.IDLE;
        actingHand = Hand.MAIN_HAND;
    }

    public boolean handleRenderFirstPerson(FirstPersonRenderContext renderContext){
        Item item = renderContext.item().getItem();
        Hand hand = renderContext.hand();
        if(item != previousItem.get(hand)) {
            resetHand(hand, item);
            itemInspectManager.endInspect();
        }
        if (playerAction.hasEnded(renderContext) && renderContext.hand() == actingHand) {
            resetHandAction();
        } else if(playerAction != PlayerAction.IDLE) {
            itemInspectManager.endInspect();
        }
        PlayerActionContext playerActionContext = new PlayerActionContext(itemInspectManager, playerAction, actingHand);
        AnimationSet animationSet = animationResourceManager.getAnimationForItem(item);
        if(animationSet == null) return true;
        AnimationType animationType = decideType(renderContext, playerActionContext);
        if(renderContext.hand() == Hand.MAIN_HAND && animationType != AnimationTypes.INSPECT) {
            itemInspectManager.endInspect();
        }
        lastAnimation.put(hand, animationType);
        if(animationType != AnimationTypes.NONE && animationSet.hasAnimationFor(animationType)) {
            animationSet.apply(renderContext.matrixStack(), animationType, renderContext.hand(), animationType.getProgress(renderContext, playerActionContext));
            return false;
        }
        return true;
    }

    public record PlayerActionContext(ItemInspectManager itemInspectManager, PlayerAction playerAction, Hand actingHand) {
        /**
         * @return true if the player is doing nothing.
         */
        public boolean idling() {
            return playerAction == PlayerAction.IDLE;
        }

        /**
         * @return true if the player is swinging from an attack but is not targeting anything.
         */
        public boolean missing() {
            return playerAction == PlayerAction.MISSING;
        }

        /**
         * @return true if the player is attacking an entity.
         */
        public boolean attacking() {
            return playerAction == PlayerAction.ATTACKING;
        }

        /**
         * @return true if the player is attacking an entity with crit.
         */
        public boolean criting() {
            return playerAction == PlayerAction.CRITING;
        }

        /**
         * @return true if the player is mining a block.
         */
        public boolean mining() {
            return playerAction == PlayerAction.MINING;
        }

        /**
         * @return true if the player is interacting with a block.
         */
        public boolean interactingWithBlock() {
            return playerAction == PlayerAction.INTERACTING_WITH_BLOCK;
        }

        /**
         * @return true if the player is using an item.
         */
        public boolean usingItem() {
            return playerAction == PlayerAction.USING_ITEM;
        }
    }

    public enum PlayerAction {
        /**
         * The player is doing nothing.
         */
        IDLE(renderContext -> false),

        /**
         * The player is swinging from an attack but is not targeting anything.
         */
        MISSING(renderContext -> renderContext.getActualAttackCooldown(0, 1) >= 1 && !renderContext.player().handSwinging),

        /**
         * The player is attacking an entity.
         */
        ATTACKING(renderContext -> renderContext.getActualAttackCooldown(0, 1) >= 1 && !renderContext.player().handSwinging),

        /**
         * The player is attacking an entity with crit.
         */
        CRITING(renderContext -> renderContext.getActualAttackCooldown(0, 1) >= 1 && !renderContext.player().handSwinging),

        /**
         * The player is mining a block.
         */
        MINING(renderContext -> !renderContext.player().handSwinging),

        /**
         * The player is interacting with a block.
         */
        INTERACTING_WITH_BLOCK(renderContext -> !renderContext.player().handSwinging),

        /**
         * The player is using an item.
         */
        USING_ITEM(renderContext -> !renderContext.player().handSwinging);

        Predicate<FirstPersonRenderContext> endPredicate;

        PlayerAction(Predicate<FirstPersonRenderContext> endPredicate) {
            this.endPredicate = endPredicate;
        }

        boolean hasEnded(FirstPersonRenderContext renderContext) {
            return endPredicate.test(renderContext);
        }
    }
}
