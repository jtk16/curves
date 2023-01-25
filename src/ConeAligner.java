
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConeAligner {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static double[] YellowyCrCbMin = new double[] {0, 120, 0};
    public static double[] YellowyCrCbMax = new double[] {255, 175, 100};

    public static JFrame finalFrame = new JFrame();
    public static JFrame initialFrame = new JFrame();
    public static JFrame binaryFrame = new JFrame();

    public static String printArr(double[] arr) {
        String out = "{";
        for (int i = 0; i < arr.length - 1; i++) {
            out += arr[i] + ", ";
        }
        return out + arr[arr.length-1] + "}";
    }
    public static void main(String[] poop) {
        VideoCapture camera = new VideoCapture(0);

        long previousTime = System.currentTimeMillis();
        int frameRate = 30;

        Mat img = new Mat();

        while (true) {
            if (1000d/frameRate > System.currentTimeMillis()-previousTime) {

                camera.read(img);
                showMat(img, initialFrame);
                Imgproc.resize(img, img, new Size(80, (int) Math.round((80 / img.size().width) * img.size().height)));


                Mat binary = new Mat();
                Mat kernel = Mat.ones(5, 5, CvType.CV_32F);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2YCrCb);
                System.out.println(printArr(img.get(40, 20)));
                Core.inRange(img, new Scalar(YellowyCrCbMin[0], YellowyCrCbMin[1], YellowyCrCbMin[2]), new Scalar(YellowyCrCbMax[0], YellowyCrCbMax[1], YellowyCrCbMax[2]), binary);
                Imgproc.morphologyEx(binary, binary, Imgproc.MORPH_CLOSE, kernel);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_YCrCb2BGR);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();

                Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_GRAY2BGR);

                if (contours.size() >= 1) {
                    int biggestContourIndex = 0;
                    double biggestContourSize = Imgproc.boundingRect(contours.get(0)).area();
                    for (int i = 1; i < contours.size(); i++) {
                        double currentArea = Imgproc.boundingRect(contours.get(i)).area();
                        if (currentArea >= biggestContourSize) {
                            biggestContourIndex = i;
                            biggestContourSize = currentArea;
                        }
                        //Imgproc.drawContours(img, contours, i, new Scalar(0, 0, 255), 1);
                    }

                    Rect biggestContourBoundingBox = Imgproc.boundingRect(contours.get(biggestContourIndex));
                    Imgproc.drawContours(img, contours, biggestContourIndex, new Scalar(0, 0, 255), 1);

                    Point boundingBoxCentroid = new Point(biggestContourBoundingBox.x + biggestContourBoundingBox.width/2, biggestContourBoundingBox.height/2);
                    Imgproc.circle(img, new Point(boundingBoxCentroid.x, boundingBoxCentroid.y), 3, new Scalar(100, 255, 100), -1);
                }
                Imgproc.cvtColor(binary, binary, Imgproc.COLOR_GRAY2BGR);
                Imgproc.circle(binary, new Point(40, 20), 3, new Scalar(255, 100, 100));
                showMat(binary, binaryFrame);





                showMat(img, finalFrame);

                previousTime = System.currentTimeMillis();
            }
        }

    }

    public static void showMat(Mat img, JFrame originalFrame) {
        Imgproc.resize(img, img, new Size(640, (int) Math.round((640 / img.size().width) * img.size().height)));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            originalFrame.getContentPane().removeAll();
            originalFrame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            originalFrame.pack();
            originalFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
