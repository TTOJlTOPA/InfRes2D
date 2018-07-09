package com.poltora.infres2d.math.bezier;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.poltora.infres2d.math.PascalTriangle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BezierCurve2D {
    private static final PascalTriangle BINOMIAL_COEFS = new PascalTriangle(3);

    private Vector2[] points;
    private List<Vector2> approxPoints;

    public BezierCurve2D(int power) {
        approxPoints = new LinkedList<>();
        points = new Vector2[power + 1];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vector2();
        }
    }

    public BezierCurve2D(Vector2... points) {
        approxPoints = new LinkedList<>();
        this.points = new Vector2[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new Vector2(points[i]);
        }
    }

    public Vector2 calculate(float t) {
        Vector2 result = new Vector2(0.0f, 0.0f);
        float oneMinusT = 1.0f - t;
        float coef;
        int power = points.length - 1;

        for (int i = 0; i < points.length; i++) {
            coef = (float) (BINOMIAL_COEFS.get(power, i) * Math.pow(oneMinusT, power - i) * Math.pow(t, i));
            result.add(coef * points[i].x, coef * points[i].y);
        }

        return result;
    }

    public void calcSimpleApprox(int splitNum) {
        float step = 1.0f / splitNum;

        approxPoints.clear();

        for (int i = 0; i < splitNum; i++) {
            approxPoints.add(calculate(i * step));
        }
    }

    private Vector2 calcMidpoint(Vector2 firstPoint, Vector2 secondPoint) {
        return new Vector2((firstPoint.x + secondPoint.x) / 2.0f, (firstPoint.y + secondPoint.y) / 2.0f);
    }

    private Vector2[] calcMidpoints(Vector2... points) {
        Vector2[] midpoints = new Vector2[points.length - 1];

        for (int i = 0; i < midpoints.length; i++) {
            midpoints[i] = calcMidpoint(points[i], points[i + 1]);
        }

        return midpoints;
    }

    private boolean isFlat(float intervalLeft, float intervalRight, float accuracy, Vector2... points) {
        float distance;
        float step = (intervalRight - intervalLeft) / (points.length - 1);

        for (int i = 0; i < points.length; i++) {
            distance = points[i].dst(calculate(intervalLeft + i * step));
            if (distance > accuracy) {
                return false;
            }
        }

        return true;
    }

    private void calcSplitsDeCasteljau(float intervalLeft, float intervalRight, float accuracy, Vector2... points) {
        Vector2[] left = new Vector2[points.length];
        Vector2[] right = new Vector2[points.length];
        Vector2[] currentMidpoints = points;

        if (isFlat(intervalLeft, intervalRight, accuracy, points)) {
            approxPoints.addAll(Arrays.asList(points));
            return;
        }

        left[0] = points[0];
        right[right.length - 1] = points[points.length - 1];
        for (int i = 1; i < points.length; i++) {
            currentMidpoints = calcMidpoints(currentMidpoints);
            left[i] = currentMidpoints[0];
            right[right.length - i - 1] = currentMidpoints[currentMidpoints.length - 1];
        }

        calcSplitsDeCasteljau(intervalLeft, (intervalLeft + intervalRight) / 2.0f, accuracy, left);
        calcSplitsDeCasteljau((intervalLeft + intervalRight) / 2.0f, intervalRight, accuracy, right);
    }

    public void calcDeCasteljauApprox(float accuracy) {
        approxPoints.clear();
        calcSplitsDeCasteljau(0.0f, 1.0f, accuracy, points);
    }

    public void draw(ShapeRenderer renderer) {
        Vector2 prev;
        Vector2 next;
        Iterator<Vector2> iter = approxPoints.iterator();

        prev = iter.next();
        while (iter.hasNext()) {
            next = iter.next();
            renderer.line(prev, next);
            prev = next;
        }
    }
}
