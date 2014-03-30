package examples;

import java.util.List;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class OCR {


	public List<MatOfPoint> segmentation(Mat img){
		Mat threshold = new Mat();
		Mat img_contours = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		
		Imgproc.threshold(img, threshold, 60, 255, Imgproc.THRESH_BINARY_INV);
		threshold.copyTo(img_contours);
		
		Imgproc.findContours(img_contours, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);	//new Mat() as a dummy mat for optional Mat
		
		return contours;
		
	}
}
