import java.awt.*;

public abstract class Curve {
    Point[] points;

    double minX;
    double maxX;

    double scaleFactor;
    Point endpoint;

    /**
     * @return the output of the curve at a value (t) between 0 and 1
     */
    abstract double evaluate(double t);
}
