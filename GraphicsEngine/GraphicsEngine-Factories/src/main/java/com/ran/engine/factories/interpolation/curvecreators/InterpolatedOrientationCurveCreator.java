package com.ran.engine.factories.interpolation.curvecreators;

import com.ran.engine.factories.interpolation.tools.CurvesDeformationCreator;
import com.ran.engine.factories.interpolation.tools.OrientationArcsBuilder;
import com.ran.engine.factories.interpolation.tools.TimeMomentsUtil;
import com.ran.engine.rendering.algebraic.common.Pair;
import com.ran.engine.rendering.algebraic.function.DoubleFunction;
import com.ran.engine.rendering.algebraic.function.DoubleMultifunction;
import com.ran.engine.rendering.algebraic.quaternion.Quaternion;

import java.util.ArrayList;
import java.util.List;

public class InterpolatedOrientationCurveCreator extends AbstractOrientationCurveCreator {

    private static final InterpolatedOrientationCurveCreator INSTANCE = new InterpolatedOrientationCurveCreator();

    public static InterpolatedOrientationCurveCreator getInstance() {
        return INSTANCE;
    }

    @Override
    public DoubleFunction<Quaternion> interpolateCurve(List<Quaternion> quaternions,
                                                       SimpleInputParameters parameters, int degree) {
        validateVerticesList(quaternions);

        double t0 = parameters.getT0();
        double t1 = parameters.getT1();
        int k = quaternions.size();

        CurvesDeformationCreator deformationCreator = CurvesDeformationCreator.getInstance();
        OrientationArcsBuilder orientationArcsBuilder = OrientationArcsBuilder.getInstance();
        TimeMomentsUtil timeMomentsUtil = TimeMomentsUtil.getInstance();
        
        List<DoubleFunction<Quaternion>> orientationsOnSegments = new ArrayList<>(k - 1);
        List<Pair<Double, Double>> rotationAngles = new ArrayList<>(k - 2);
        OrientationArcsBuilder.Result currentOrientationArcsBuildingResult =
                orientationArcsBuilder.buildArcsBetweenQuaternionsOnThreeDimensionalSphere(
                        quaternions.get(0), quaternions.get(1), quaternions.get(2));
        orientationsOnSegments.add(currentOrientationArcsBuildingResult.getFirstRotation());
        rotationAngles.add(currentOrientationArcsBuildingResult.getAngles());
        
        for (int i = 1; i < k - 2; i++) {
            OrientationArcsBuilder.Result nextOrientationArcsBuildingResult =
                    orientationArcsBuilder.buildArcsBetweenQuaternionsOnThreeDimensionalSphere(
                            quaternions.get(i), quaternions.get(i + 1), quaternions.get(i + 2));
            DoubleFunction<Quaternion> deformedFunction = deformationCreator.deformCurves(
                    currentOrientationArcsBuildingResult.getSecondRotation(),
                    nextOrientationArcsBuildingResult.getFirstRotation(), degree);
            orientationsOnSegments.add(deformedFunction);
            rotationAngles.add(nextOrientationArcsBuildingResult.getAngles());
            currentOrientationArcsBuildingResult = nextOrientationArcsBuildingResult;
        }
        orientationsOnSegments.add(currentOrientationArcsBuildingResult.getSecondRotation());
        
        List<Double> timeMoments = timeMomentsUtil.countTimeMoments(rotationAngles, t0, t1, k);
        List<DoubleFunction<Quaternion>> orientationCurveSegments = new ArrayList<>(k - 1);
        for (int i = 0; i < k - 1; i++) {
            double startTime = timeMoments.get(i);
            double endTime = timeMoments.get(i + 1);
            DoubleFunction<Quaternion> currentRotation = orientationsOnSegments.get(i);
            Quaternion currentQuaternion = quaternions.get(i);
            DoubleFunction<Quaternion> curveSegmentWithoutAligning = new DoubleFunction<>(
                    point -> currentRotation.apply(point).multiply(currentQuaternion), 0.0, 1.0
            );
            DoubleFunction<Quaternion> alignedCurveSegment =
                    curveSegmentWithoutAligning.superposition(timeMomentsUtil.buildAligningFunction(startTime, endTime));
            orientationCurveSegments.add(alignedCurveSegment);
        }
        return DoubleMultifunction.makeMultifunction(orientationCurveSegments);
    }
    
}