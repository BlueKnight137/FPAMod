package io.github.blueknight137.fpamod.util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spline {

    private final List<SplineSegment> splineSegments;
    private final int segmentCount;

    private SplineSegment findSegment(float x) {
        int start = 0;
        int end = segmentCount-1;
        int mid = (end + start)/2;
        SplineSegment midSegment = splineSegments.get(mid);
        while (start <= end) {
            if(midSegment.startPoint <= x && midSegment.endPoint >= x) {
                return midSegment;
            } else if(x < midSegment.startPoint) {
                end = mid-1;
            } else {
                start = mid+1;
            }
            mid = (end + start)/2;
            midSegment = splineSegments.get(mid);
        }
        throw new IllegalArgumentException("Argument is out of range!");
    }

    private Spline(List<SplineSegment> splineSegments) {
        this.splineSegments = splineSegments;
        this.segmentCount = splineSegments.size();
    }

    // https://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
    private static float[] progonka(float mainDiag, float[] subDiag, float[] supDiag, float[] b) {
        int n = b.length;
        if(n == 0) {
            return new float[]{};
        }
        if(n == 1) {
            return new float[]{b[0]/mainDiag};
        }
        float[] x = new float[n];
        float[] f = new float[n-1];
        float[] g = new float[n];

        f[0] = - supDiag[0] / mainDiag;
        g[0] = b[0] / mainDiag;

        for(int i = 1; i < n; i++) {
            if(i < n-1) {
                f[i] = - supDiag[i] / (mainDiag + subDiag[i - 1] * f[i - 1]);
            }
            g[i] = (b[i] - g[i-1] * subDiag[i-1]) / (mainDiag + subDiag[i-1] * f[i-1]);
        }

        x[n-1] = g[n-1];

        for(int i = n-2; i >= 0; i--) {
            x[i] = g[i] + f[i] * x[i+1];
        }

        return x;
    }

    public static Spline constant(List<Float> basePoints, List<Float> values) {
        int n = basePoints.size();
        List<SplineSegment> splineSegments = new ArrayList<>(n-1);

        for(int i = 0; i < n-1; i++) {
            final float startPoint = basePoints.get(i);
            final float endPoint = basePoints.get(i+1);
            final float a = values.get(i);
            splineSegments.add(
                    new SplineSegment(
                            startPoint,
                            endPoint,
                            a
                    )
            );
        }

        return new Spline(splineSegments);
    }

    public static Spline linear(List<Float> basePoints, List<Float> values) {
        int n = basePoints.size();
        List<SplineSegment> splineSegments = new ArrayList<>(n-1);

        for(int i = 0; i < n-1; i++) {
            final float startPoint = basePoints.get(i);
            final float endPoint = basePoints.get(i+1);
            final float a = values.get(i);
            final float b = (values.get(i) - values.get(i+1))/(basePoints.get(i)-basePoints.get(i+1));
            splineSegments.add(
                    new SplineSegment(
                            startPoint,
                            endPoint,
                            a,
                            b
                    )
            );
        }

        return new Spline(splineSegments);
    }



    public static Spline quadratic(List<Float> basePoints, List<Float> values, float startGradient) {
        int n = basePoints.size();
        List<SplineSegment> splineSegments = new ArrayList<>(n-1);

        float[] gradients = new float[n];
        gradients[0] = startGradient;

        for (int i = 0; i < n-1; i++) {
            gradients[i+1] = (values.get(i) - values.get(i+1))/(basePoints.get(i)-basePoints.get(i+1)) - gradients[i];
        }

        for(int i = 0; i < n-1; i++) {
            final float startPoint = basePoints.get(i);
            final float endPoint = basePoints.get(i+1);
            final float a = values.get(i);
            final float b = gradients[i];
            final float c = -gradients[i+1]/(basePoints.get(i)-basePoints.get(i+1));
            splineSegments.add(
                    new SplineSegment(
                            startPoint,
                            endPoint,
                            a,
                            b,
                            c
                    )
            );
        }

        return new Spline(splineSegments);
    }

    public static Spline clampedCubic(List<Float> basePoints, List<Float> values, float startGradient, float endGradient) {
        int n = basePoints.size();
        float[] h = new float[n-1];

        for(int i = 0; i < n-1; i++) {
            h[i] = basePoints.get(i+1) - basePoints.get(i);
        }

        float[] subDiag = new float[n-1];
        float[] supDiag = new float[n-1];

        supDiag[0] = 1;
        subDiag[n-2] = 1;

        for(int i = 0; i < n-2; i++) {
            subDiag[i] = h[i]/(h[i]+h[i+1]);
            supDiag[i+1] = 1 - subDiag[i];
        }

        float[] b = new float[n];
        b[0] = 3 * (startGradient - ((values.get(0) - values.get(1)) / h[0])) / h[0];
        b[n-1] = 3 * ((values.get(n-2) - values.get(n-1)) / h[n-2] - endGradient) / h[n-2];

        Float[] divDiff2 = values.toArray(new Float[]{});
        Float[] divDiff3 = values.toArray(new Float[]{});
        for(int i = 1; i < 3; i++) {
            for(int j = n-1; j >= i; j--) {
                if(i == 1) {
                    divDiff2[j] = (divDiff2[j-1] - divDiff2[j]) / (basePoints.get(j - i) - basePoints.get(j));
                }
                divDiff3[j] = (divDiff3[j-1] - divDiff3[j]) / (basePoints.get(j - i) - basePoints.get(j));
            }
        }

        for(int i = 2; i < n; i++) {
            b[i-1] = 3 * divDiff3[i];
        }

        float[] quadraticParameters = progonka(2, subDiag, supDiag, b);

        List<SplineSegment> splineSegments = new ArrayList<>(n-1);
        for(int i = 0; i < n-1; i++) {
            final float startPoint = basePoints.get(i);
            final float endPoint = basePoints.get(i+1);
            final float p1 = values.get(i);
            final float p2 = divDiff2[i+1] - (h[i]/3)*(2*quadraticParameters[i]+quadraticParameters[i+1]);
            final float p3 = quadraticParameters[i];
            final float p4 = (quadraticParameters[i+1] - quadraticParameters[i]) / (3 * h[i]);
            splineSegments.add(
                    new SplineSegment(
                            startPoint,
                            endPoint,
                            p1,
                            p2,
                            p3,
                            p4
                    )
            );
        }

        return new Spline(splineSegments);
    }

    public static Spline naturalCubic(List<Float> basePoints, List<Float> values) {
        int n = basePoints.size();
        float[] h = new float[n-1];

        for(int i = 0; i < n-1; i++) {
            h[i] = basePoints.get(i+1) - basePoints.get(i);
        }

        float[] subDiag = new float[Math.max(n-3,0)];
        float[] supDiag = new float[Math.max(n-3,0)];

        for(int i = 0; i < n-3; i++) {
            subDiag[i] = h[i + 1] / (h[i + 1] + h[i + 2]);
            supDiag[i] = 1 - h[i] / (h[i] + h[i + 1]);
        }


        Float[] divDiff2 = values.toArray(new Float[]{});
        Float[] divDiff3 = values.toArray(new Float[]{});
        for(int i = 1; i < 3; i++) {
            for(int j = n-1; j >= i; j--) {
                if(i == 1) {
                    divDiff2[j] = (divDiff2[j-1] - divDiff2[j]) / (basePoints.get(j - i) - basePoints.get(j));
                }
                divDiff3[j] = (divDiff3[j-1] - divDiff3[j]) / (basePoints.get(j - i) - basePoints.get(j));
            }
        }

        float[] b = new float[Math.max(n-2,0)];
        for(int i = 0; i < n-2; i++) {
            b[i] = 3 * divDiff3[i+2];
        }

        float[] quadraticParametersPartial = progonka(2, subDiag, supDiag, b);
        float[] quadraticParameters = new float[n];
        quadraticParameters[0] = 0;
        quadraticParameters[n-1] = 0;

        if (n - 2 >= 0) {
            System.arraycopy(quadraticParametersPartial, 0, quadraticParameters, 1, n - 2);
        }

        List<SplineSegment> splineSegments = new ArrayList<>(n-1);
        for(int i = 0; i < n-1; i++) {
            final float startPoint = basePoints.get(i);
            final float endPoint = basePoints.get(i+1);
            final float p1 = values.get(i);
            final float p2 = divDiff2[i+1] - (h[i]/3)*(2*quadraticParameters[i]+quadraticParameters[i+1]);
            final float p3 = quadraticParameters[i];
            final float p4 = (quadraticParameters[i+1] - quadraticParameters[i]) / (3 * h[i]);
            splineSegments.add(
                    new SplineSegment(
                            startPoint,
                            endPoint,
                            p1,
                            p2,
                            p3,
                            p4
                    )
            );
        }

        return new Spline(splineSegments);
    }

    public float evaluate(float x) {
        SplineSegment splineSegment = findSegment(x);

        return splineSegment.evaluate(x);
    }

    private static class SplineSegment {
        private final float startPoint;
        private final float endPoint;
        private final int degree;
        private final List<Float> parameters;


        public SplineSegment(float startPoint, float endPoint, Float... parameters) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.parameters = Arrays.stream(parameters).toList();
            this.degree = this.parameters.size()-1;
        }

        public float evaluate(float x) {
            float value = parameters.get(degree);
            float length = x - startPoint;
            for(int j = degree-1; j >= 0; j--) {
                value = value * length + parameters.get(j);
            }
            return value;
        }
    }
}
