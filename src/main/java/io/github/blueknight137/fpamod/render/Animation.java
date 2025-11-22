package io.github.blueknight137.fpamod.render;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.blueknight137.fpamod.FPAMod;
import io.github.blueknight137.fpamod.data.AnimationDeserializer;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerTypes;
import io.github.blueknight137.fpamod.render.tweening.tweenings.FullTweening;
import io.github.blueknight137.fpamod.render.tweening.tweenings.Tweening;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@JsonDeserialize(using = AnimationDeserializer.class)
public class Animation {

    private final Keyframe start;
    private final Keyframe end;
    private Keyframe last;
    private final List<Tweening> tweenings;
    private final Tweening fallbackTweening;
    private final Transformation initialTransformation;

    private void ensureProgressValue(float progress) {
        if(0f > progress || 1f < progress) {
            throw new IllegalArgumentException("Progress must be between 0 and 1 (inclusive)!");
        }
    }

    private List<Tweening> getApplicableTweenings(float progress) {
        var applicableTweenings = tweenings.stream().filter(transition -> transition.isApplicable(progress)).toList();
        return applicableTweenings.isEmpty() ? List.of(fallbackTweening) : applicableTweenings;
    }

    private void maybePropagateKeyFrame(float progress) {
        if(last.getTimestamp() > progress) {
            last = start;
        }
        while (last != end && last.next.getTimestamp() < progress) {
            last = last.next;
        }
    }

    public void applyTransformation(MatrixStack matrixStack, Hand hand, float progress) {
        ensureProgressValue(progress);
        maybePropagateKeyFrame(progress);
        var tweenings = getApplicableTweenings(progress);
        RenderData renderData = new RenderData();
        for(var tweening : tweenings) {
            tweening.applyTransition(renderData, last, progress);
        }
        /* Applying a matrix that reflects onto the plane parallel to the x-axis will flip the order of vertex of all the faces.
         * This is an issue because culling will become inverted, faces that should be rendered will not be rendered and vice versa.
         * So we first flip the vertices, apply the transformations, then flip it again. This way when we flip it back
         * the transformations will be flipped but the vertices will still be in the right order.
         */
        if(hand == Hand.OFF_HAND) {
            matrixStack.multiplyPositionMatrix(new Matrix4f().reflect(1, 0, 0, 0));
        }
        renderData.applyTransformation(matrixStack);
        if(hand == Hand.OFF_HAND) {
            matrixStack.multiplyPositionMatrix(new Matrix4f().reflect(1, 0, 0, 0));
        }
        initialTransformation.applyMCStyle(matrixStack);
    }

    protected Animation(Keyframe start, Keyframe end, List<Tweening> tweenings, Transformation initialTransformation) {
        this.start = start;
        this.last = start;
        this.end = end;
        this.tweenings = tweenings;
        this.fallbackTweening = new FullTweening(start, end, List.of(), TweenerTypes.STATIC);
        this.initialTransformation = initialTransformation;
    }

    public static class Builder {
        Keyframe start = null;
        private final List<Keyframe> keyframes = new ArrayList<>();
        private final List<Tweening> tweenings = new ArrayList<>();
        private Transformation initialTransformation = new Transformation();

        public Builder addKeyFrame(Keyframe keyFrame) {
            if(start == null) {
                start = keyFrame;
                keyframes.add(keyFrame);
            } else {
                if(keyframes.getLast().getTimestamp() > keyFrame.getTimestamp()) {
                    FPAMod.LOGGER.error("Invalid keyframe order detected! Keyframe with timestamp [{}] is not in order compared to the other keyframes!", keyFrame.getTimestamp());
                    throw new IllegalArgumentException("KeyFrame is before the last KeyFrame!");
                }
                keyframes.getLast().next = keyFrame;
                keyframes.add(keyFrame);
            }
            return this;
        }

        public Builder addTweening(BiFunction<Keyframe, Keyframe, Tweening> tweeningFactory, String startTag, String endTag) {
            for(int i = 0; i < keyframes.size(); i++) {
                if(!keyframes.get(i).hasTag(startTag)) continue;
                Keyframe startKeyframe = keyframes.get(i);
                boolean foundEnd = false;
                for(int j = i+1; j < keyframes.size() && !foundEnd; j++) {
                    if(!keyframes.get(j).hasTag(endTag)) continue;
                    Keyframe endKeyframe = keyframes.get(j);
                    tweenings.add(tweeningFactory.apply(startKeyframe, endKeyframe));
                    foundEnd = true;
                }
                if(!foundEnd) {
                    FPAMod.LOGGER.error("Invalid tweening definition! Could not find end tag [{}] for start tag [{}]!", endTag, startTag);
                    throw new IllegalArgumentException("KeyFrame corresponding to the endTag of a Tweening is missing!");
                }
            }
            return this;
        }

        public Builder setInitialTransformation(Transformation transformation) {
            this.initialTransformation = transformation;
            return this;
        }

        public Animation build() {
            Keyframe start = this.start;
            if(start.getTimestamp() != 0f) {
                Keyframe previousStart = start;
                start = new Keyframe(start.getTransformation(), 0f);
                start.next = previousStart;
            }
            Keyframe end = keyframes.getLast();
            if(end.getTimestamp() != 1f) {
                Keyframe newEnd = new Keyframe(end.getTransformation(), 1f);
                end.next = newEnd;
                end = newEnd;
            }
            return new Animation(start, end, tweenings, initialTransformation);
        }
    }
}
