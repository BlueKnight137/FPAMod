package io.github.blueknight137.fpamod.render;

import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static io.github.blueknight137.fpamod.FPAMod.*;

public class AnimationTypes {

    public static final AnimationType NONE =
            new AnimationType(
                    Identifier.of(MODID, "none"),
                    (previousType, renderContext, animationContext) -> true,
                    (renderContext, animationContext) -> 1f,
                    -1
            );

    public static final AnimationType IDLE =
            new AnimationType(
                    Identifier.of(MODID, "idle"),
                    (previousType, renderContext, animationContext) -> true,
                    (renderContext, animationContext) -> ((renderContext.player().age + renderContext.tickProgress()) % 20) / 20,
                    0
            );

    public static final AnimationType CRIT =
            new AnimationType(
                    Identifier.of(MODID, "crit"),
                    (previousType, renderContext, animationContext) ->
                                    animationContext.criting() &&
                                    renderContext.hand() == Hand.MAIN_HAND,
                    (renderContext, animationContext) -> renderContext.getActualAttackCooldown(),
                    6
            );

    public static final AnimationType ATTACK =
            new AnimationType(
                    Identifier.of(MODID, "attack"),
                    (previousType, renderContext, animationContext) ->
                                    animationContext.attacking() && renderContext.hand() == Hand.MAIN_HAND,
                    (renderContext, animationContext) -> renderContext.getActualAttackCooldown(),
                    5
            );

    public static final AnimationType MINE =
            new AnimationType(
                    Identifier.of(MODID, "mine"),
                    (previousType, renderContext, animationContext) ->
                                    animationContext.mining() && renderContext.hand() == Hand.MAIN_HAND,
                    (renderContext, animationContext) -> renderContext.swingProgress(),
                    4
            );

    public static final AnimationType INTERACT_WITH_BLOCK =
            new AnimationType(
                    Identifier.of(MODID, "interact_with_block"),
                    (previousType, renderContext, animationContext) ->
                                    (animationContext.interactingWithBlock()) && renderContext.hand() == animationContext.actingHand(),
                    (renderContext, animationContext) -> renderContext.swingProgress(),
                    4
            );

    public static final AnimationType USING_ITEM =
            new AnimationType(
                    Identifier.of(MODID, "using_item"),
                    (previousType, renderContext, animationContext) ->
                                    (animationContext.usingItem()) && renderContext.hand() == animationContext.actingHand(),
                    (renderContext, animationContext) -> renderContext.swingProgress(),
                    4
            );

    public static final AnimationType MISS =
            new AnimationType(
                    Identifier.of(MODID, "miss"),
                    (previousType, renderContext, animationContext) ->
                                    (animationContext.missing()) && renderContext.hand() == Hand.MAIN_HAND,
                    (renderContext, animationContext) -> renderContext.getActualAttackCooldown(),
                    3
            );

    public static final AnimationType EQUIP =
            new AnimationType(
                    Identifier.of(MODID, "equip"),
                    (previousType, renderContext, animationContext) ->
                            renderContext.player().getAttackCooldownProgress(0f) < 1 &&
                                    renderContext.hand() == Hand.MAIN_HAND &&
                                    (previousType == NONE || previousType.identifier.equals(Identifier.of(MODID, "equip"))),
                    (renderContext, animationContext) -> renderContext.getActualAttackCooldown(),
                    2
            );

    public static final AnimationType UNEQUIP =
            new AnimationType(
                    Identifier.of(MODID, "unequip"),
                    (previousType, renderContext, animationContext) -> (renderContext.lastEquipItemProgress() > 1f-renderContext.equipProgress()) &&
                            renderContext.hand() == Hand.MAIN_HAND &&
                            (previousType == IDLE && animationContext.idling() || previousType.identifier.equals(Identifier.of(MODID, "unequip"))),
                    (renderContext, animationContext) -> renderContext.equipProgress(),
                    2
            );

    public static final AnimationType INSPECT =
            new AnimationType(
                    Identifier.of(MODID, "inspect"),
                    (previousType, renderContext, animationContext) -> animationContext.itemInspectManager().isInspecting() && renderContext.hand() == Hand.MAIN_HAND,
                    (renderContext, animationContext) -> animationContext.itemInspectManager().getInspectProgress(renderContext.tickProgress()),
                    1
            );

    private static void register(AnimationType animationType) {
        animationTypeClientRegistry.register(animationType);
    }

    public static void registerAnimationTypes() {
        Arrays.stream(AnimationTypes.class.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()) && f.getType() == AnimationType.class)
                .map(f -> {
                    try {
                        return (AnimationType) f.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(AnimationTypes::register);
    }

}
