package videoCapture;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.View;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.w3c.dom.css.RGBColor;

import examples.MatToBufferedImage;
import examples.Snapshot;

public class Opticalflow extends Snapshot{
	//maximum number of points for the algorithm
	private final static int MAX_CORNERS = 500;
	private final static int WINDOW_SIZE = 10;
	private final static double QUALITY = 0.01;
	private final static double MIN_DISTANCE = 10;
	private final static String savefolder = "videos/saved";
	private final static String readfolder = "videos/";
	private final static String filename   = "videoviewdemo";
	private final static String extension  = ".mp4";

	
	private static Mat prevgrey;//CvType.CV_8UC1
	private static Mat nextgrey;//CvType.CV_8UC1
	private static Mat prevpyramid;//CvType.CV_32FC1
	private static Mat nextpyramid;//CvType.CV_32FC1
	private static Mat eigenMat;//CvType.CV_32FC1
	private static Mat tempMat;//CvType.CV_32FC1
	
	//CvPoint2D32F Arrays are 32 bit points to track features
	private static MatOfPoint prevpointS;
	private static MatOfPoint2f prevpoint;
	private static MatOfPoint2f nextpoint;
	private static MatOfByte  featuresfound;
	private static MatOfFloat feature_errors;
	private static ArrayList<Mat> imgpyramid;

	
	public Opticalflow(){
	}
	
	
	public static void main(String args[]) throws IOException, InterruptedException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoHelper vid = new VideoHelper();
		vid.loadfile(readfolder + filename + extension);
		//vid.view(readfolder + filename + extension, "opticalflow");
		Mat frame1 = new Mat();
		frame1 = vid.grabframe();
		//displayImage(prevgrey, "current frame");

		BufferedImage img;
		MatToBufferedImage conv = new MatToBufferedImage();
		JFrame jf = new JFrame();
		
		boolean hasNext = true;
		
		init(frame1);
		prevgrey = frame1;
		while(hasNext){
			
			nextgrey = vid.grabframe();
			Thread.sleep(10);//to prevent fps from running to fast
			if(nextgrey.empty()){
				System.out.println("nope");
				
				hasNext = false;
				jf.setVisible(false);
				vid.close();
				return;
			}
			
			
			findFeatures(prevgrey);
			calculateOptflow();
			img = conv.getImage(prevgrey);
			jf.setSize(img.getWidth(), img.getHeight());
			jf.setTitle("optical flow");
			VidPanel panel = new VidPanel(img);
			jf.setContentPane(panel);
			jf.setVisible(true);
			prevgrey = nextgrey;
		}
		System.exit(1);
	}
	
	public static void init(Mat src){
		//initiate all variables for calculation
		
		prevpointS = new MatOfPoint();
		prevpoint = new MatOfPoint2f();//MAX_COUNT
		nextpoint = new MatOfPoint2f();//MAX_COUNT
		featuresfound = new MatOfByte();
		feature_errors = new MatOfFloat();
		prevgrey = new Mat(src.size(), CvType.CV_8UC1);
		nextgrey = new Mat(src.size(), CvType.CV_8UC1);
		imgpyramid = new ArrayList<Mat>(2);
		prevpyramid = new Mat(src.size(), CvType.CV_8U);
		nextpyramid = new Mat(src.size(), CvType.CV_8U);
		eigenMat = new Mat(src.size(), CvType.CV_32FC1);
		tempMat  = new Mat(src.size(), CvType.CV_32FC1);
		
	}
	
	public static void findFeatures(Mat src){
			Mat img = src;
			Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
			
		prevpoint.alloc((int) (MAX_CORNERS*(prevpoint.elemSize1())));
		prevpointS.alloc((int) (MAX_CORNERS*(prevpointS.elemSize1())));
		
		System.out.println("prevpointS is" + prevpointS.toString());
		//Video.buildOpticalFlowPyramid(src, imgpyramid, new Size(WINDOW_SIZE, WINDOW_SIZE), 1);
		try {
			displayImage(img, "image");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Imgproc.goodFeaturesToTrack(img, prevpointS, MAX_CORNERS, QUALITY, MIN_DISTANCE, new Mat(src.size(), CvType.CV_8UC1), 3, false, 0.4);

		System.out.println("prevpointS stats: " + prevpointS.toString());
		System.out.println(prevpointS.elemSize());
		Point[] points = prevpointS.toArray();
		prevpoint.fromArray(points);
		System.out.println("prevpoint stats: " + prevpoint.toString());
		Imgproc.cornerSubPix(src, prevpoint, new Size(WINDOW_SIZE*2+1, WINDOW_SIZE*2+1), new Size(-1,-1),
							new TermCriteria(TermCriteria.MAX_ITER | TermCriteria.EPS, 20,0.3));
	}
	
	public static void calculateOptflow(){
		
		/**
		 *  // finds features to track and stores them in array (here: points[0])
    	 *  // void cvGoodFeaturesToTrack(cvArr* image, cvArr* eigImage, cvArr* tempImage, 
    	 *	// CvPoint2D32f* corners, int* cornerCount, double qualityLevel, double minDistance,
    	 *	// cvArr* mask, int blockSize, int useHarris, double k)
    	 *cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.3)
		 **/
		//imgpyramid.get(0).assignTo(imgpyramid.get(0), CvType.CV_8U);
		//imgpyramid.get(1).assignTo(imgpyramid.get(1), CvType.CV_8U);

	//	System.out.println(imgpyramid.get(0).toString());
	//	Imgproc.cvtColor(imgpyramid.get(0), imgpyramid.get(0), Imgproc.COLOR_RGB2GRAY);
	//	Imgproc.cvtColor(imgpyramid.get(1), imgpyramid.get(1), Imgproc.COLOR_RGB2GRAY);
	//	System.out.println(imgpyramid.get(0).toString());
		Video.calcOpticalFlowPyrLK(prevpyramid, nextpyramid, prevpoint, nextpoint, featuresfound, 
									feature_errors, new Size(WINDOW_SIZE, WINDOW_SIZE), 5);
		
		// Draw optical flow vectors
	      for (int i = 0; i < MAX_CORNERS; i++) {
	        
	        Point[] prevpoints = prevpoint.toArray();
	        Point[] nextpoints = nextpoint.toArray();
	        
	        
	        Point point1 = new Point(Math.round(prevpoints[i].x), Math.round(prevpoints[i].y));
	        Point point2 = new Point(Math.round(nextpoints[i].x), Math.round(nextpoints[i].y));
	        System.out.println("point1: " + point1.toString() + ", point2 " + point2.toString());
	        Core.line(prevgrey, point1, point2, new Scalar(255, 0, 0), 1, 8, 0);
	      }

	}
}
