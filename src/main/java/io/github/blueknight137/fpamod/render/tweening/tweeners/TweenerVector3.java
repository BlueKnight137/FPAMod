package io.github.blueknight137.fpamod.render.tweening.tweeners;

import org.joml.Vector3f;

import java.util.List;

public class TweenerVector3 {

    private final Tweener xTweener;
    private final Tweener yTweener;
    private final Tweener zTweener;

    public TweenerVector3(TweenerType tweenerType, List<Float> args, List<Float> timeStamps, List<Float> xs, List<Float> ys, List<Float> zs) {
        xTweener = tweenerType.constructTweener(args, timeStamps, xs);
        yTweener = tweenerType.constructTweener(args, timeStamps, ys);
        zTweener = tweenerType.constructTweener(args, timeStamps, zs);
    }

    public TweenerVector3(TweenerType tweenerType, List<Float> args, List<Float> timeStamps, List<Vector3f> vectors) {
        this(
                tweenerType,
                args,
                timeStamps,
                vectors.stream().map(Vector3f::x).toList(),
                vectors.stream().map(Vector3f::y).toList(),
                vectors.stream().map(Vector3f::z).toList()
        );
    }

    public Vector3f tween(float progress) {
        float x = xTweener.tween(progress);
        float y = yTweener.tween(progress);
        float z = zTweener.tween(progress);
        return new Vector3f(x, y, z);
    }

}
