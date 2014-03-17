package examples;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.jfree.ui.RefineryUtilities;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Preprocess extends Snapshot{
	
	private static BufferedImage img;
	private static String filename = "test_056";
	private static String extension = ".JPG";
	private static int numberofcandidates = 3;
	private static Vector<Mat> bands = new Vector<Mat>();
	
	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MatToBufferedImage conv = new MatToBufferedImage();
		
		Mat source = Highgui.imread(filename + extension);
		Mat processed = new Mat(source.width(), source.height(), CvType.CV_64FC2);
		Coordinates[] ycoords = new Coordinates[2];//0 is ypeak, 1 is yband
		Coordinates[] xcoords = new Coordinates[2];//0 is xpeak, 1 is xband
		//System.out.println("width: " + m.width() + "height: " + m.height());	//debugging purposes

		//Preprocess image
		Process(source, processed, 150, "vertical");
		img = conv.getImage(processed);
		displayImage(source, new Coordinates(0, source.width()), new Coordinates(0, source.height()), "Original Image");
		
		//Vertical Projection histogram & Image
		ycoords = displayHistogram(img, "Vertical Projection Histogram", "vertical", 0.55);
		displayImage(source, new Coordinates(0, source.width()), ycoords[1], "vertical projection 0th");
		Mat band = source.submat(ycoords[1].getX(), ycoords[1].getY(), 0, source.width());
		bands.add(band);
		
		
		//produce more possible plates
		if(numberofcandidates > 1){
			for(int i = 1; i < numberofcandidates; i++){
				Coordinates[] coords = displayHistogram(img, "Vertical Projection Histogram " + i + "th", "vertical", 0.55, i);
				band = source.submat(coords[1].getX(), coords[1].getY(), 0, source.width());
				if(bands.add(band)){
					System.out.println("band added");
				}
				displayImage(source, new Coordinates(0, source.width()), coords[1], "vertical projection " + i + "th");
			}
		}
		
	
		//Horizontal Projection
		Mat horizontal;
		
		Iterator it = bands.iterator();
		
		if(it.hasNext()){
			int count = 1;
			while(it.hasNext()){
				horizontal = (Mat) it.next();
				Process(horizontal, processed, 150, "horizontal");
				img = conv.getImage(processed);
				xcoords = displayHistogram(img, "Horizontal Projection " + count + "th" + " Histogram", "horizontal", 0.1);
				displayImage(horizontal, xcoords[1], new Coordinates(0,horizontal.height()), "horizontal projection " + count + "th");
				count++;
			}
		}else{
			horizontal = source.submat(ycoords[1].getX(), ycoords[1].getY(), 0, source.width());
			Process(horizontal, processed, 150, "horizontal");
			img = conv.getImage(processed);
			xcoords = displayHistogram(img, "Horizontal Projection Histogram", "horizontal", 0.1);
			displayImage(horizontal, xcoords[1], new Coordinates(0,horizontal.height()), "horizontal projection");
		}
	}
	
	private static Mat Process(Mat src, Mat dst, double Threshold, String sobeltype){
		Mat bw = new Mat(src.width(), src.height(), CvType.CV_64FC2);
		Imgproc.cvtColor(src, bw, Imgproc.COLOR_RGB2GRAY);
		Mat morphelem = new Mat(src.width(), src.height(), CvType.CV_64FC2);
		Mat threshold = new Mat(src.width(), src.height(), CvType.CV_8UC1);
		if(sobeltype == "vertical"){
			Imgproc.Sobel(bw, threshold, src.depth(), 1, 0);
		}else if(sobeltype == "horizontal"){
			Imgproc.Sobel(bw, threshold, src.depth(), 0, 2);
		}
		Imgproc.GaussianBlur(threshold, threshold, new Size(5,5), 0);
		Imgproc.threshold(threshold, threshold, Threshold, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		morphelem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
		Imgproc.morphologyEx(threshold, dst, 3, morphelem);
		
		return dst;
	}
	
	private static Coordinates[] displayHistogram(BufferedImage img, String title, String orientation, double bandthreshold){
		Coordinates[] coords = new Coordinates[2];
		Histogram hist = new Histogram(title, img, orientation);
		int[] arr = hist.getImgArr();
		Coordinates peak_coord = hist.getPeakCoord(arr);
		Coordinates band_coord;
		if(orientation == "vertical"){
			band_coord = hist.getBandCoords(arr, bandthreshold, "vertical");
		}
		band_coord = hist.getBandCoords(arr, bandthreshold, "horizontal");
		if(peak_coord == null || band_coord == null){
			System.err.println("Error. unable to find peak coordinates or band coordinates");
		}
		System.out.println("peak coordinates are: " + peak_coord.toString());
		System.out.println("band coordinates are: " + band_coord.toString());
		coords[0] = peak_coord;
		coords[1] = band_coord;
		
		hist.pack();
		RefineryUtilities.centerFrameOnScreen(hist);
		hist.setVisible(true);
		
		return coords;
	}
	
	private static Coordinates[] displayHistogram(BufferedImage img, String title, String orientation, double bandthreshold, int newPeak){
		Coordinates[] coords = new Coordinates[2];
		Histogram hist = new Histogram(title, img, orientation);
		int[] arr = hist.getImgArr();
		for(int i = 0; i < newPeak; i++){
			Coordinates peak = hist.getPeakCoord(arr);
			Coordinates band;
			if(orientation == "vertical"){
				band = hist.getBandCoords(arr, bandthreshold, "vertical");
			}
			band = hist.getBandCoords(arr, bandthreshold, "horizontal");
			System.out.println("band to be zeroized: " + band.toString());
			arr = clearPeak(arr, band);
			System.out.println("band Zeroized");
		}
		Coordinates peak_coord = hist.getPeakCoord(arr);
		Coordinates band_coord;
		if(orientation == "vertical"){
			band_coord = hist.getBandCoords(arr, bandthreshold, "vertical");
		}
		band_coord = hist.getBandCoords(arr, bandthreshold, "horizontal");
		if(peak_coord == null || band_coord == null){
			System.err.println("Error. unable to find peak coordinates or band coordinates");
		}
		System.out.println("peak coordinates are: " + peak_coord.toString());
		System.out.println("band coordinates are: " + band_coord.toString());
		coords[0] = peak_coord;
		coords[1] = band_coord;
		
		hist.pack();
		RefineryUtilities.centerFrameOnScreen(hist);
		hist.setVisible(true);
		
		return coords;
	}
	
	private static int[] clearPeak(int[] imgarr, Coordinates band_coord){
		for(int i = band_coord.getX(); i<band_coord.getY(); i++){
			imgarr[i] = 0;
		}
		
		return imgarr;
		
	}
	
	
	private static void displayImage(Mat mat, Coordinates axisX, Coordinates axisY) throws IOException{
		MatToBufferedImage conv = new MatToBufferedImage();
		BufferedImage image = conv.getImage(mat);
		BufferedImage result = getCropImage(image, axisX.getX(), axisX.getY(), axisY.getX(), axisY.getY());
		System.out.println("image width: " + result.getWidth() + ", image height: " + result.getHeight());
		
		SetImage(result);
		System.out.println(SaveImage(filename) + "result");
		ViewImage("Possible Plate");
	}
	
	private static void displayImage(Mat mat, Coordinates axisX, Coordinates axisY, String windowcaption) throws IOException{
		MatToBufferedImage conv = new MatToBufferedImage();
		BufferedImage image = conv.getImage(mat);
		BufferedImage result = getCropImage(image, axisX.getX(), axisX.getY(), axisY.getX(), axisY.getY());
		System.out.println("image width: " + result.getWidth() + ", image height: " + result.getHeight());
		
		SetImage(result);
		System.out.println(SaveImage(filename + "result"));
		ViewImage(windowcaption);
	}


}
