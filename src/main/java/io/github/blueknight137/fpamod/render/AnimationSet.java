package io.github.blueknight137.fpamod.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {

    private final Map<AnimationType, Animation> typeAnimationMap = new HashMap<>();

    public void upsertAnimation(AnimationType type, Animation animation) {
        typeAnimationMap.put(type, animation);
    }

    public void apply(MatrixStack matrixStack, AnimationType animationType, Hand hand, float progress) {
        typeAnimationMap.get(animationType).applyTransformation(matrixStack, hand, progress);
    }

    public boolean hasAnimationFor(AnimationType animationType) {
        return typeAnimationMap.containsKey(animationType) && typeAnimationMap.get(animationType) != null;
    }
}
