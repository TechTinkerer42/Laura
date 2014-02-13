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
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Preprocess extends Snapshot{
	
	private static  Mat mat;
	private static BufferedImage img;
	private MatToBufferedImage conv = new MatToBufferedImage();
	
//	public Preprocess(BufferedImage img){
//		this.img = getImage();
//		this.mat = getMatFromImage(this.img);
//	}

	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat m = Highgui.imread("test_032.jpg");
		Mat bw = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Imgproc.cvtColor(m, bw, Imgproc.COLOR_RGB2GRAY);
		Mat vertEdges = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Mat morphelem = new Mat(m.width(), m.height(), CvType.CV_64FC2);
		Mat threshold = new Mat(m.width(), m.height(), CvType.CV_8UC1);
		//Imgproc.equalizeHist(m, EQ);
		Imgproc.Sobel(bw, vertEdges, m.depth(), 1, 0);
		
		morphelem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
		Imgproc.morphologyEx(vertEdges, vertEdges, 3, morphelem);
//		
		//Imgproc.threshold(vertEdges, threshold, 150, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

		
		
		/**
		 * 
		 * 
		 * here onwards are for displaying images only
		 */
		MatToBufferedImage conv = new MatToBufferedImage();
		//change Mat here only.
		img = conv.getImage(vertEdges);
		img = getScaledImage(img, img.getWidth(), img.getHeight());
		LoadImage(img);
		ViewImage();
		
		Histogram demo = new Histogram("Vertical Projection", img);
		int[] img_arr = demo.getImgArr();//get image array to retrieve peak and its side band coords;
		Coordinates peak_coord = demo.getPeakCoord(img_arr);
		Coordinates band_coord = demo.getBandCoords(img_arr, 0.55);
		System.out.println("Peak coord: " + peak_coord.toString());
		System.out.println("band coord: " + band_coord.toString());
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
		//start from difference between X and Y of band_coord, to Yb1/band_coord's Y coord
		BufferedImage image = conv.getImage(m);
		image = getScaledImage(image, img.getWidth(), img.getHeight());
		LoadImage(image);
		//TODO: choice of y_start matters, may need to find a better way to get a more constant value 
		ViewImage(0, img.getWidth(), (peak_coord.getY()-band_coord.getX()), band_coord.getY());
		//ViewImage(0, img.getWidth(), band_coord.getX(), band_coord.getY());
	}
	
	private static BufferedImage getScaledImage(BufferedImage src, int w, int h){
	    int finalw = w;
	    int finalh = h;
	    double factor = 1.0d;
	    if(src.getWidth() > src.getHeight()){
	        factor = ((double)src.getHeight()/(double)src.getWidth());
	        finalh = (int)(finalw * factor);                
	    }else{
	        factor = ((double)src.getWidth()/(double)src.getHeight());
	        finalw = (int)(finalh * factor);
	    }   

	    BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(src, 0, 0, finalw, finalh, null);
	    g2.dispose();
	    return resizedImg;
	}


}
