import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ConeSleevePipeline {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static double[] YellowyCrCbMin = new double[] {100, 120, 0};
    public static double[] YellowyCrCbMax = new double[] {255, 175, 100};

    public static double[] GreenyCrCbMin = new double[] {0, 0, 0};

    public static double[] GreenyCrCbMax = new double[] {180, 120, 120};

    public static double[] PurpleyCrCbMin = new double[] {0, 140, 140};

    public static double[] PurpleyCrCbMax = new double[] {180, 255, 255};

    public static JFrame finalFrame = new JFrame();
    public static JFrame initialFrame = new JFrame();

    public static JFrame pbinaryFrame = new JFrame();

    public static JFrame gbinaryFrame = new JFrame();

    public static JFrame ybinaryFrame = new JFrame();

    public static JFrame binaryFrame = new JFrame();

    public static state defaultState = state.YELLOW;
    public static String printArr(double[] arr) {
        String out = "{";
        for (int i = 0; i < arr.length - 1; i++) {
            out += arr[i] + ", ";
        }
        return out + arr[arr.length-1] + "}";
    }

    public enum state {
        PURPLE(20),
        YELLOW(0),
        GREEN(-20);

        private final int offset;

        state(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }
    }

    public static void main(String[] poop) {
        VideoCapture camera = new VideoCapture(0);

        long previousTime = System.currentTimeMillis();
        int frameRate = 30;

        Mat img = new Mat();

        while (true) {
            if (1000d/frameRate > System.currentTimeMillis()-previousTime) {

                camera.read(img);
                ConeAligner.showMat(img, initialFrame);
                Imgproc.resize(img, img, new Size(80, (int) Math.round((80 / img.size().width) * img.size().height)));

                Mat yBinary = new Mat();
                Mat pBinary = new Mat();
                Mat gBinary = new Mat();
                Mat Binary = new Mat();

                Mat kernel = Mat.ones(5, 5, CvType.CV_32F);

                Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2YCrCb);

                Core.inRange(img, new Scalar(YellowyCrCbMin[0], YellowyCrCbMin[1], YellowyCrCbMin[2]), new Scalar(YellowyCrCbMax[0], YellowyCrCbMax[1], YellowyCrCbMax[2]), yBinary);
                Core.inRange(img, new Scalar(PurpleyCrCbMin[0], PurpleyCrCbMin[1], PurpleyCrCbMin[2]), new Scalar(PurpleyCrCbMax[0], PurpleyCrCbMax[1], PurpleyCrCbMax[2]), pBinary);
                Core.inRange(img, new Scalar(GreenyCrCbMin[0], GreenyCrCbMin[1], GreenyCrCbMin[2]), new Scalar(GreenyCrCbMax[0], GreenyCrCbMax[1], GreenyCrCbMax[2]), gBinary);

                Imgproc.morphologyEx(yBinary, yBinary, Imgproc.MORPH_CLOSE, kernel);
                Imgproc.morphologyEx(pBinary, pBinary, Imgproc.MORPH_CLOSE, kernel);
                Imgproc.morphologyEx(gBinary, gBinary, Imgproc.MORPH_CLOSE, kernel);

                Core.bitwise_or(yBinary, pBinary, Binary);
                Core.bitwise_or(Binary, gBinary, Binary);

                Imgproc.cvtColor(img, img, Imgproc.COLOR_YCrCb2BGR);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();

                Imgproc.findContours(Binary, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_GRAY2BGR);

                double y = 0;
                double p = 0;
                double g = 0;

                for (int i = 0; i < pBinary.rows(); i++) {
                    for (int j = 0; j < pBinary.cols(); j++) {
                        if (pBinary.get(i, j) != null) {
                            p += pBinary.get(i, j)[0];
                            y += yBinary.get(i, j)[0];
                            g += gBinary.get(i, j)[0];
                        }
                    }
                }

                if (p >= y && p >= g) defaultState = state.PURPLE;
                if (y >= p && y >= g) defaultState = state.YELLOW;
                if (g >= p && g >= y) defaultState = state.GREEN;

                Imgproc.cvtColor(Binary, Binary, Imgproc.COLOR_GRAY2BGR);

                ConeAligner.showMat(Binary, binaryFrame);

                ConeAligner.showMat(img, finalFrame);

                System.out.println(defaultState);

                previousTime = System.currentTimeMillis();
            }
        }

    }

}

