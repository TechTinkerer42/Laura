package examples;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.ui.RefineryUtilities;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Preprocess extends Snapshot{
	
	private static BufferedImage img;
	private MatToBufferedImage conv = new MatToBufferedImage();
	
//	public Preprocess(BufferedImage img){
//		this.img = getImage();
//		this.mat = getMatFromImage(this.img);
//	}

	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat m = Highgui.imread("test_039.JPG");
		System.out.println("width: " + m.width() + "height: " + m.height());
		
		/**
		 * Vertical Projection
		 */
		Mat bw = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Imgproc.cvtColor(m, bw, Imgproc.COLOR_RGB2GRAY);
		Mat vertEdges = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Mat morphelem = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Mat threshold = new Mat(m.width(), m.height(), CvType.CV_8UC1);
		//Imgproc.equalizeHist(m, EQ);
		Imgproc.Sobel(bw, threshold, m.depth(), 1, 0);
		Imgproc.GaussianBlur(threshold, threshold, new Size(5,5), 0);
		Imgproc.threshold(threshold, threshold, 150, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		
		morphelem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
		Imgproc.morphologyEx(threshold, vertEdges, 3, morphelem);
	

		
		
		/**
		 * 
		 * 
		 * here onwards are for displaying images only
		 */
		MatToBufferedImage conv = new MatToBufferedImage();
		//change Mat here only.
		img = conv.getImage(vertEdges);
		img = getScaledImage(img, img.getWidth(), img.getHeight());
		SetImage(img);
		ViewImage("Pre-processed");
		
		Histogram demo = new Histogram("Vertical Projection Histogram", img);
		int[] img_arr = demo.getImgArr();//get image array to retrieve peak and its side band coords;
		Coordinates peak_coord = demo.getPeakCoord(img_arr);
		Coordinates band_coord = demo.getBandCoords(img_arr, 0.55);
		System.out.println("Peak coord: " + peak_coord.toString());
		System.out.println("band coord: " + band_coord.toString());
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);	//show vertical projection histogram
		
		//get Band image to produce vertical projection
		BufferedImage vert_img = conv.getImage(m);
		BufferedImage crop_img = getCropImage(vert_img, 0, vert_img.getWidth(), band_coord.getX(), band_coord.getY());
		SetImage(crop_img);
		ViewImage("Vertical Projection");
		
		
		/**
		 * Horizontal Projection
		 */
		
		Mat hori = m.submat(band_coord.getX(), band_coord.getY(), 0, m.width());
		
		
		bw = new Mat();
		Mat horiEdges = new Mat(hori.width(), hori.height(), CvType.CV_64FC3);
		morphelem = new Mat(hori.width(), hori.height(), CvType.CV_64FC2);
		threshold = new Mat(hori.width(), hori.height(), CvType.CV_8UC1);
		
		Imgproc.cvtColor(hori, bw, Imgproc.COLOR_RGB2GRAY);
		Imgproc.Sobel(bw, threshold, m.depth(), 0, 2);
		Imgproc.GaussianBlur(threshold, threshold, new Size(3,3), 0);
		Imgproc.threshold(threshold, threshold, 150, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		
		morphelem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		Imgproc.morphologyEx(threshold, horiEdges, 3, morphelem);
		BufferedImage banad = conv.getImage(horiEdges);
		SetImage(banad);
		ViewImage("Horizontal Projection");
		
		/**
		 * 
		 * 
		 * here onwards are for displaying images only
		 */
		
		Histogram hori_hist = new Histogram("Horizontal Projection Histogram", banad, "horizontal");
		int[] hori_imgarr = hori_hist.getImgArr();
		Coordinates horipeak_coord = hori_hist.getPeakCoord(hori_imgarr);
		Coordinates horiband_coord = hori_hist.getBandCoords(hori_imgarr, 0.1);
		System.out.println("Hori_Peak coord: " + horipeak_coord.toString());
		System.out.println("Hori_band coord: " + horiband_coord.toString());
		hori_hist.pack();
		RefineryUtilities.centerFrameOnScreen(hori_hist);
		hori_hist.setVisible(true);	//show vertical projection histogram
		
		//get Band image to produce horizontal projection
		BufferedImage band = conv.getImage(m);
		BufferedImage horiband = getCropImage(band, horiband_coord.getX(), horiband_coord.getY(), band_coord.getX(), band_coord.getY());
		SetImage(horiband);
		ViewImage("Horizontal Projection");
		
		
//		//start from difference between X and Y of band_coord, to Yb1/band_coord's Y coord
//		BufferedImage image = conv.getImage(m);
//		image = getScaledImage(image, img.getWidth(), img.getHeight());
//		LoadImage(image);
//		//DONE: choice of y_start matters, may need to find a better way to get a more constant value 
//		ViewImage(0, img.getWidth(), band_coord.getX(), band_coord.getY());	//cropped image
		
	}
	



}
