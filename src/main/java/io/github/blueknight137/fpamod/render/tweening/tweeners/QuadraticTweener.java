package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.util.math.Spline;

import java.util.List;

public class QuadraticTweener implements Tweener {

    private final Spline spline;

    public QuadraticTweener(List<Float> timeStamps, List<Float> values, float startGradient) {
        spline = Spline.quadratic(timeStamps, values, startGradient);
    }

    @Override
    public float tween(float progress) {
        return spline.evaluate(progress);
    }
}
