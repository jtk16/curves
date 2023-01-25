import java.awt.*;

import static java.lang.Math.pow;

public class VariantDegreeBezier extends Curve {

    /**
     * Must be length of 4
     */
    public Point[] points;

    private double minX;
    private double maxX;

    public VariantDegreeBezier(Point[] points) throws ArrayIndexOutOfBoundsException {
        if (points.length != 4) {
            throw new ArrayIndexOutOfBoundsException("curve does not have a length of 4");
        }

        this.points = points;
        this.endpoint = points[3];

        minX = points[0].x;
        maxX = points[0].x;

        for (int i = 1; i < points.length; i++) {
            minX = Math.min(minX, points[i].x);
            maxX = Math.max(maxX, points[i].x);
        }
    }

    public static void main(String[] args) {
        Point[] points = new Point[] {
                new Point(100, 100),
                new Point(100, 300),
                new Point(300, 100),
                new Point(300, 300)
        };

        VariantDegreeBezier bezier = new VariantDegreeBezier(points);

    }

    public double evaluate(double t) {
        double y = (pow(1 - t, 3) * points[0].y) +
                   (3 * t * pow(1 - t, 2) * points[1].y) +
                   (3 * (1 - t) * pow(t, 2) * points[2].y) +
                   (pow(t, 3) * points[3].y);
        return y;
    }

}
