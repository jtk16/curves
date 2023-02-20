import java.awt.*;

public class CurveSequence {

    public Curve[] curves;
    public double length;
    public double minX;
    public double maxX;

    public CurveSequence(Curve[] curves) {

        this.curves = curves;

        this.minX = this.curves[0].minX;
        this.maxX = this.curves[0].maxX;

        for (int i = 1; i < curves.length; i++) {
            this.minX = Math.min(this.minX, this.curves[i].minX);
            this.maxX = Math.max(this.maxX, this.curves[i].maxX);
            System.out.println(curves[i].minX);
        }

        this.length = this.maxX - this.minX;
        System.out.println("minX: " + this.minX + ", maxX: " + this.maxX + ", length: " + this.length);
    }

    public static void main(String[] args) {
        Point[] points = new Point[] {
            new Point(100, 100),
            new Point(100, 300),
            new Point(300, 100),
            new Point(300, 300),
            new Point(325, 250),
            new Point(350, 275),
            new Point(375, 200),
        };

        Curve[] curves = new Curve[]{
                new VariantDegreeBezier(new Point[]{
                        points[0], points[1], points[2], points[3]
                }),
                new LagrangianInterpolation(new Point[]{
                        points[3], points[4], points[5], points[6]
                })
        };

        CurveSequence sequence = new CurveSequence(
                curves
        );

        System.out.println(sequence.evaluate(0.7273));
    }

    public double evaluate(double t) {
        for (Curve curve : curves) {
            double minT = (curve.minX - minX)/length;
            double maxT = (curve.maxX - minX)/length;

            if (t >= minT && t <= maxT) {
                double tx = this.minX + (this.maxX - this.minX) * t;
                System.out.println("at time = " + t + ", x = " + (this.minX + (this.maxX - this.minX) * t));
                System.out.println((tx - curve.minX) / (curve.maxX - curve.minX));
                return curve.evaluate((tx - curve.minX) / (curve.maxX - curve.minX));
            }
        }

        throw new ArrayIndexOutOfBoundsException("could not locate a point at t = " + t + ", x = " + (this.minX + (this.maxX - this.minX) * t));
    }

}
