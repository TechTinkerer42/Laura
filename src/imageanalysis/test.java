package examples;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class test extends Snapshot {

	private static BufferedImage img;
	private static String extension = ".jpg";
	private static String filename = "test_047possibeplate 1result";
	private static final int numberofcandidates = 5;
	private static final String readfolder = "images/";
	private static final String savefolder = "images/results/";
	
	public static void main(String args[]) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src = Highgui.imread(savefolder + "possibleplates/positives/" + filename + extension);
		

		displayImage(src, "hello");
		System.out.println(src);
		Mat img = OCR.SketchContours(src);
		System.out.println(img.toString());
		displayImage(img, "hello");

	}
}
