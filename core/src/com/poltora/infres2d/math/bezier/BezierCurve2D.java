package com.poltora.infres2d.math.bezier;

import com.badlogic.gdx.math.Vector2;
import com.poltora.infres2d.math.PascalTriangle;

public class BezierCurve2D {
    private static final PascalTriangle BINOMIAL_COEFS = new PascalTriangle(3);

    private Vector2[] points;

    public BezierCurve2D(int power) {
        points = new Vector2[power + 1];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vector2();
        }
    }

    public BezierCurve2D(Vector2... points) {
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
}
