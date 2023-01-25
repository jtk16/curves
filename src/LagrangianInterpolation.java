import java.awt.*;

public class LagrangianInterpolation extends Curve {

    public Point[] points;

    private double minX;
    private double maxX;

    public LagrangianInterpolation(Point[] points) {
        this.points = points;

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
                new Point(200, 300),
                new Point(300, 200)
        };

        LagrangianInterpolation func = new LagrangianInterpolation(points);
        System.out.println(func.P(287));
    }

    public double P(double x) {
        double sum = 0;
        for (int j = 0; j < points.length; j++) {
            sum += Pj(x, j);
        }

        return sum;
    }

    private double Pj(double x, int j) {
        double product = points[j].y;

        for (int k = 0; k < points.length; k++) {
            if (k != j && points[k].x != points[j].x) {
                product *= (x - points[k].x) / (points[j].x - points[k].x);
            }
        }

        return product;
    }

    @Override
    double evaluate(double t) {
        return P(minX + (maxX - minX) * t);
    }
}
