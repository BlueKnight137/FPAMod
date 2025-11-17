package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.util.math.Spline;

import java.util.List;

public class LinearTweener implements Tweener {

    private final Spline spline;

    public LinearTweener(List<Float> timeStamps, List<Float> values) {
        spline = Spline.linear(timeStamps, values);
    }

    @Override
    public float tween(float progress) {
        return spline.evaluate(progress);
    }
}
