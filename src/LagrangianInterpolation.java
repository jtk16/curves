import java.awt.*;

public class LagrangianInterpolation extends Curve {

    public Point[] points;

    public double minX;
    public double maxX;

    public LagrangianInterpolation(Point[] points) {
        this.points = points;

        super.minX = points[0].x;
        super.maxX = points[0].x;

        for (int i = 1; i < points.length; i++) {
            super.minX = Math.min(super.minX, points[i].x);
            super.maxX = Math.max(super.maxX, points[i].x);
        }

        this.minX = super.minX;
        this.maxX = super.maxX;

        System.out.println("lagrange min: " + super.minX + ", bezier max: " + super.maxX);
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
        return P(this.minX + (this.maxX - this.minX) * t);
    }
}
