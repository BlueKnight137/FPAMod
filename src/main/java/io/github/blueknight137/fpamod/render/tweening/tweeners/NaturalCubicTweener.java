package io.github.blueknight137.fpamod.render.tweening.tweeners;

import io.github.blueknight137.fpamod.util.math.Spline;

import java.util.List;

public class NaturalCubicTweener implements Tweener {

    private final Spline spline;

    public NaturalCubicTweener(List<Float> timeStamps, List<Float> values) {
        spline = Spline.naturalCubic(timeStamps, values);
    }

    @Override
    public float tween(float progress) {
        return spline.evaluate(progress);
    }
}
