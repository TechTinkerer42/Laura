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
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.w3c.dom.css.RGBColor;

import examples.MatToBufferedImage;
import examples.Snapshot;

public class Opticalflow extends Snapshot{
	//maximum number of points for the algorithm
	private final static int MAX_CORNERS 	 = 500;
	private final static int WINDOW_SIZE 	 = 15;
	private final static double QUALITY 	 = 0.01;
	private final static double MIN_DISTANCE = 10;
	private final static int FLOWSTEP	     = 50;
	private final static String savefolder	 = "videos/saved";
	private final static String readfolder	 = "videos/";
	private final static String filename  	 = "vid1776";
	private final static String extension 	 = ".mp4";

	
	private static Mat prevgrey;//CvType.CV_8UC1
	private static Mat nextgrey;//CvType.CV_8UC1
	
	//CvPoint2D32F Arrays are 32 bit points to track features
	private static MatOfPoint prevpointS;
	private static MatOfPoint2f prevpoint;
	private static MatOfPoint2f nextpoint;
	private static MatOfByte  featuresfound;
	private static MatOfFloat feature_errors;

	
	private final static boolean onCamera = true;

	
	public Opticalflow(){
	}
	
	
	public static void main(String args[]) throws IOException, InterruptedException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoHelper vid = new VideoHelper();
		
		if(onCamera){
			vid.onCamera();
		}else{
			vid.loadfile(readfolder + filename + extension);
		}
		Mat frame1 = new Mat();
		frame1 = vid.grabframe();
		//Core.flip(frame1, frame1, 1);
		//displayImage(prevgrey, "current frame");

		BufferedImage img;
		Mat diff = new Mat();
		Mat result = new Mat();
		MatToBufferedImage conv = new MatToBufferedImage();
		JFrame jf = new JFrame();
		
		boolean hasNext = true;
		double startTime = System.nanoTime();
		double timediff;
		int count = 0;
		double fps;
		
		init(frame1);
		prevgrey = frame1;
		jf.setSize(prevgrey.width(), prevgrey.height());
		jf.setTitle("optical flow");
		VidPanel panel;
		Imgproc.cvtColor(prevgrey, prevgrey, Imgproc.COLOR_RGB2GRAY);
	
		while(hasNext){
			
			nextgrey = vid.grabframe();
			//Core.flip(nextgrey, nextgrey, 1);
			if(nextgrey.empty()){
				System.out.println("end of video");
				
				hasNext = false;
				jf.setVisible(false);
				vid.close();
				return;
			}
			
			Imgproc.cvtColor(nextgrey, nextgrey, Imgproc.COLOR_RGB2GRAY);
			Thread.sleep(10);//to prevent fps from running too fast
			
			Core.absdiff( prevgrey, nextgrey, diff );
			double n = Core.norm(diff, Core.NORM_L2);
			System.out.println("the n value is " + n);
			
			prevpoint = findFeatures(prevgrey);
			//displayImage(prevgrey, "hello");
			result = calculateOptflow(prevgrey, nextgrey, prevpoint );
			
			//prevgrey = OptflowFarneBack(prevgrey, nextgrey);
			
			img = conv.getImage(result);
			panel = new VidPanel(img);
			jf.setContentPane(panel);
			jf.setVisible(true);
			
			prevgrey = nextgrey;
			timediff = (System.nanoTime() - startTime) / 1e9;
			count++;
			fps = count/timediff;
			System.out.println(fps);
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
		
	}
	
	
	public static Mat drawOptFlowMap(Mat flow, Mat cflowmap, int step, Scalar color){
		
		for(int y = 0; y < cflowmap.rows(); y += step){
			for(int x = 0; x < cflowmap.cols(); x += step){
			    Point fxy = new Point(flow.get(y, x));
			   // System.out.println("point fxt. X is: " + fxy.x + ", Y is: " + fxy.y);
			    Core.line(cflowmap, new Point(x,y), new Point(Math.round(x+fxy.x), Math.round(y+fxy.y)),
			         color);
			    //Core.circle(cflowmap, new Point(x,y), 2, color, -1);
			}
		}
		
		return cflowmap;
		
	}
	
	/**
	 * <p> Dense optical flow </p>
	 * @param prevgray
	 * @param gray
	 * @return
	 */
	public static Mat OptflowFarneBack(Mat prevgray, Mat gray){
		
		 Mat cflow = new Mat(prevgray.size(), CvType.CV_8UC3);
		 Mat flow = new Mat(prevgray.size(), CvType.CV_8UC3);
		// System.out.println("prevgrey " + prevgray.toString());
		// System.out.println("cflow " + cflow.toString());
		// Video.calcOpticalFlowFarneback(prev, next, cflow, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags)
		Video.calcOpticalFlowFarneback(prevgray, gray, flow, 0.5, 3, WINDOW_SIZE, 3, 5, 1.5, 0);
        //Imgproc.cvtColor(prevgray, cflow, Imgproc.COLOR_GRAY2BGR);
        drawOptFlowMap(flow, cflow, FLOWSTEP, new Scalar(0, 255, 0));
        //cflow.assignTo(cflow, prevgrey.type());
         return cflow;
	}
	
	public static MatOfPoint2f findFeatures(Mat src){
		MatOfPoint2f features;
		Mat img = src;
		assert(img.channels() == 1 || img.type() == CvType.CV_8U);
		
		prevpoint.alloc((int) (MAX_CORNERS*(prevpoint.elemSize1())));
		prevpointS.alloc((int) (MAX_CORNERS*(prevpointS.elemSize1())));
		
		//System.out.println("prevpointS is" + prevpointS.toString());
		
		//Video.buildOpticalFlowPyramid(src, imgpyramid, new Size(WINDOW_SIZE, WINDOW_SIZE), 1);
		Imgproc.goodFeaturesToTrack(img, prevpointS, MAX_CORNERS, QUALITY, MIN_DISTANCE);
		
		//System.out.println("prevpointS stats: " + prevpointS.toString());
		features = new MatOfPoint2f(prevpointS.toArray());
		//Imgproc.cornerSubPix(src, prevpoint, new Size(WINDOW_SIZE*2+1, WINDOW_SIZE*2+1), new Size(-1,-1),
		//					new TermCriteria(TermCriteria.MAX_ITER | TermCriteria.EPS, 20,0.3));
		
		return 	features;
	}

	
	public static Mat calculateOptflow(Mat prevframe, Mat nextframe, MatOfPoint2f prevpt){
		  Video.calcOpticalFlowPyrLK(prevframe, nextframe, prevpt, nextpoint, featuresfound, 
									feature_errors, new Size(WINDOW_SIZE, WINDOW_SIZE), 5);
		
		  Mat dots = new Mat(prevframe.size(), CvType.CV_16U);
		  // Draw optical flow vectors
          Point[] prevpoints = prevpt.toArray();
          Point[] nextpoints = nextpoint.toArray();
          
          
	      for (int i = 0; i < MAX_CORNERS && i < prevpoints.length; i++) {  
	    	
	        Point point1 = new Point(Math.round(prevpoints[i].x), Math.round(prevpoints[i].y));
	        Point point2 = new Point(Math.round(nextpoints[i].x), Math.round(nextpoints[i].y));
	        //System.out.println("point1: " + point1.toString() + ", point2 " + point2.toString());
	        if(point2.x < 0 || point2.y < 0){
	        	Core.line(prevframe, point1, point2, new Scalar(0, 255, 0), 1, 8, 0);
	        }else{
	        	Core.line(prevframe, point1, point2, new Scalar(255, 0, 0), 1, 8, 0);
	        }
	      }
	      
	    prevpt.release();
	    nextpoint.release();
	    prevpointS.release();
	    featuresfound.release();
	    feature_errors.release();
	    
	    return prevframe;
	}
	private static int checkDirection(Point[] prevpoints, Point[] nextpoints){

	      for (int i = 0; i < MAX_CORNERS && i < prevpoints.length; i++) { 
	    	  //check at left
	    	  
	    	  //check at right
	    	  
	      }
		
		return -1;
	}
	
}
