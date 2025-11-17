package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.util.math.Spline;

import java.util.List;

public class ClampedCubicTweener implements Tweener {

    private final Spline spline;

    public ClampedCubicTweener(List<Float> timeStamps, List<Float> values, float startGradient, float endGradient) {
        spline = Spline.clampedCubic(timeStamps, values, startGradient, endGradient);
    }

    @Override
    public float tween(float progress) {
        return spline.evaluate(progress);
    }
}
