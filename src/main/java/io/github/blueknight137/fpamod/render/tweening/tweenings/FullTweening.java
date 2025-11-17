package io.github.blueknight137.fpamod.render.tweening.tweenings;

import io.github.blueknight137.fpamod.render.KeyFrame;
import io.github.blueknight137.fpamod.render.RenderData;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerType;
import io.github.blueknight137.fpamod.render.tweening.tweeners.TweenerVector3;
import org.joml.Vector3f;

import java.util.List;

public class FullTweening extends Tweening {

    private final TweenerVector3 positionTweener;
    private final TweenerVector3 rotationTweener;
    private final TweenerVector3 scaleTweener;

    public FullTweening(KeyFrame start, KeyFrame end, List<Float> arguments, TweenerType tweenerType) {
        super(start, end);
        List<Float> timeStamps = keyFrames.stream().map(KeyFrame::getTimeStamp).toList();
        List<Vector3f> positions = keyFrames.stream().map(KeyFrame::getPosition).toList();
        List<Vector3f> rotations = keyFrames.stream().map(KeyFrame::getRotation).toList();
        List<Vector3f> scales = keyFrames.stream().map(KeyFrame::getScale).toList();

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
    public void applyTransition(RenderData data, KeyFrame lastKeyFrame, float progress) {
         data
                 .setPosition(positionTweener.tween(progress))
                 .setRotation(rotationTweener.tween(progress))
                 .setScale(scaleTweener.tween(progress));
    }
}
