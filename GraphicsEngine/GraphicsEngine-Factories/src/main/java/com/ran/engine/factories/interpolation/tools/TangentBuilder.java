package com.ran.engine.factories.interpolation.tools;

import com.ran.engine.rendering.algebraic.function.DoubleFunction;
import com.ran.engine.rendering.algebraic.matrix.DoubleMatrix;
import com.ran.engine.rendering.algebraic.vector.ThreeDoubleVector;

public class TangentBuilder {

    private static final TangentBuilder INSTANCE = new TangentBuilder();
    private static final DoubleMatrix Z_HALF_PI_ROTATION = RotationCreator.getInstance().createRotation(
            new ThreeDoubleVector(0.0, 0.0, 1.0), Math.PI / 2.0);

    public static TangentBuilder getInstance() {
        return INSTANCE;
    }

    public Result buildTangent(ThreeDoubleVector point,
                               double tangentAngle,
                               Double forwardRotationAngle,
                               Double backRotationAngle) {
        ThreeDoubleVector a = new ThreeDoubleVector(Z_HALF_PI_ROTATION.multiply(
                new ThreeDoubleVector(point.getX(), point.getY(), 0.0).getDoubleVector())).normalized();
        ThreeDoubleVector b = point.multiply(a).normalized();
        ThreeDoubleVector n = a.multiply(-Math.sin(tangentAngle)).add(b.multiply(Math.cos(tangentAngle)));

        DoubleFunction<DoubleMatrix> forwardRotation = null;
        DoubleFunction<DoubleMatrix> backRotation = null;

        if (forwardRotationAngle != null) {
            forwardRotation = new DoubleFunction<>(
                    u -> RotationCreator.getInstance().createRotation(n, u * forwardRotationAngle),
                    0.0, 1.0);
        }
        if (backRotationAngle != null) {
            backRotation = new DoubleFunction<>(
                    u -> RotationCreator.getInstance().createRotation(n, -u * backRotationAngle),
                    0.0, 1.0);
        }
        return new Result(forwardRotation, backRotation);
    }

    public static class Result {
        private final DoubleFunction<DoubleMatrix> forwardRotation;
        private final DoubleFunction<DoubleMatrix> backRotation;

        public Result(DoubleFunction<DoubleMatrix> forwardRotation, DoubleFunction<DoubleMatrix> backRotation) {
            this.forwardRotation = forwardRotation;
            this.backRotation = backRotation;
        }

        public DoubleFunction<DoubleMatrix> getForwardRotation() {
            return forwardRotation;
        }

        public DoubleFunction<DoubleMatrix> getBackRotation() {
            return backRotation;
        }
    }

}