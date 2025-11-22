package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.Keyframe;
import io.github.blueknight137.fpamod.render.RenderData;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerVector3;
import org.joml.Vector3f;

import java.util.List;

public class FullTweening extends Tweening {

    private final TweenerVector3 positionTweener;
    private final TweenerVector3 rotationTweener;
    private final TweenerVector3 scaleTweener;

    public FullTweening(Keyframe start, Keyframe end, List<Float> arguments, TweenerType tweenerType) {
        super(start, end);
        List<Float> timeStamps = keyframes.stream().map(Keyframe::getTimestamp).toList();
        List<Vector3f> positions = keyframes.stream().map(Keyframe::getPosition).toList();
        List<Vector3f> rotations = keyframes.stream().map(Keyframe::getRotation).toList();
        List<Vector3f> scales = keyframes.stream().map(Keyframe::getScale).toList();

        positionTweener = new TweenerVector3(
                tweenerType,
                arguments,
                timeStamps,
                positions
        );

        rotationTweener = new TweenerVector3(
                tweenerType,
                arguments,
                timeStamps,
                rotations
        );

        scaleTweener = new TweenerVector3(
                tweenerType,
                arguments,
                timeStamps,
                scales
        );
    }

    @Override
    public void applyTransition(RenderData data, Keyframe lastKeyframe, float progress) {
         data
                 .setPosition(positionTweener.tween(progress))
                 .setRotation(rotationTweener.tween(progress))
                 .setScale(scaleTweener.tween(progress));
    }
}
