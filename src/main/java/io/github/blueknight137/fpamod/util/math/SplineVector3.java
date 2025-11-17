package io.github.blueknight137.fpamod.util.math;

import org.joml.Vector3f;

public class SplineVector3 {

    private final Spline x;
    private final Spline y;
    private final Spline z;

    public SplineVector3(Spline x, Spline y, Spline z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f evaluate(float t) {
        return new Vector3f(x.evaluate(t), y.evaluate(t), z.evaluate(t));
    }
}
