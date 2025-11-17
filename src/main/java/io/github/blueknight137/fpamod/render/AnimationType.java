package io.github.blueknight137.fpamod.render;

import io.github.blueknight137.fpamod.Identifiable;
import io.github.blueknight137.fpamod.events.FPAHandRenderEvents.FirstPersonRenderContext;
import io.github.blueknight137.fpamod.render.AnimationManager.PlayerActionContext;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class AnimationType implements Identifiable {
    public final Identifier identifier;
    private final TriPredicate<AnimationType, FirstPersonRenderContext, PlayerActionContext> predicate;
    private final BiFunction<FirstPersonRenderContext, PlayerActionContext, Float> progressFunction;
    public final int priority;

    public AnimationType(Identifier identifier, TriPredicate<AnimationType, FirstPersonRenderContext, PlayerActionContext> predicate, BiFunction<FirstPersonRenderContext, PlayerActionContext, Float> progressFunction, int priority) {
        this.identifier = identifier;
        this.predicate = predicate;
        this.progressFunction = progressFunction;
        this.priority = priority;
    }

    public AnimationType(Identifier id, TriPredicate<AnimationType, FirstPersonRenderContext, PlayerActionContext> predicate, BiFunction<FirstPersonRenderContext, PlayerActionContext, Float> progressFunction) {
        this(id, predicate, progressFunction, 0);
    }

    public boolean isApplicable(AnimationType previousType, FirstPersonRenderContext renderContext, PlayerActionContext playerActionContext) {
        return predicate.test(previousType, renderContext, playerActionContext);
    }

    public float getProgress(FirstPersonRenderContext renderContext, PlayerActionContext playerActionContext) {
        return Math.clamp(progressFunction.apply(renderContext, playerActionContext),0f,1f);
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @FunctionalInterface
    public interface TriPredicate<A, B, C> {
        boolean test(A a, B b, C c);
    }
}
