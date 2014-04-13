package examples;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvANN_MLP;

public class OCR extends Snapshot{
	public static final boolean VERTICAL = true;
	public static final boolean HORIZONTAL = false;
	public static final int numCharacters = 30;
	public static boolean isTrained = false;
	public static CvANN_MLP ann;
	
	public static int charSize;
	
	public OCR(){
		charSize = 20;
	}
	
	public static Mat SketchContours(Mat src) throws IOException{
		Mat img = src;
		
		Mat img_threshold = new Mat();
		Mat img_contours = new Mat();
		Mat img_morph = new Mat();
		
		//img.convertTo(img, CvType.CV_8UC1);
		Imgproc.cvtColor(img, img_threshold, Imgproc.COLOR_RGB2GRAY);
		Mat detected = new Mat();
		
		Imgproc.threshold(img_threshold, img_threshold, 120, 255, Imgproc.THRESH_BINARY);
		//img_threshold.copyTo(img_morph);
		//Mat morphelem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		//Imgproc.morphologyEx(img_morph, img_morph, 3, morphelem);
		//img_morph.copyTo(img_contours);
		img_threshold.copyTo(img_contours);
		displayImage(img_contours, "img_contours");
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		contours = segmentation(img_contours);
		img_threshold.copyTo(detected);
		Imgproc.cvtColor(detected, detected, Imgproc.COLOR_GRAY2RGB);
		
				Imgproc.drawContours(detected, contours, -1, new Scalar(255,255,0));
				for(MatOfPoint contour : contours){
					Rect box = Imgproc.boundingRect(contour);
					Core.rectangle(detected, box.tl(), box.br(), new Scalar(0,0,255));
					
					Mat characters = new Mat(img_threshold, box);
					
					System.out.println("characters w: " + characters.width() + ",characters h:" + characters.height() + characters.toString());
					try {
						if(verifyplatesize(characters)){
							characters.convertTo(characters, CvType.CV_32F);
							characters = preprocessChar(characters);
							displayImage(characters, "Windowcap");
							
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
		return detected;
		
		//return detecteds;
	}
	
	
	public static List<MatOfPoint> segmentation(Mat img){
		Mat threshold = new Mat();
		Mat img_contours = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		//Imgproc.threshold(img, threshold, 60, 255, Imgproc.THRESH_BINARY);
		img.copyTo(img_contours);
		Imgproc.findContours(img_contours, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);	//new Mat() as a dummy mat for optional Mat
		
		return contours;	
	}
	
	public static boolean verifyplatesize(Mat src)
	   {
	     //Char sizes 45x77
		 double aspect = 45.0f/77.0f;
	     double charAspect = (float)src.cols()/(float)src.rows();
	     double error = 0.35;
	     double minHeight =15;
	     double maxHeight =28;
	     //We have a different aspect ratio for number 1, and it can be
	     //~0.2
	     double minAspect = 0.2;
	     double maxAspect = aspect+aspect*error;
	     //area of pixels
	     double area= Core.countNonZero(src);
	     //bb area
	     double bbArea = src.cols()*src.rows();
	     //% of pixel in area
	     double percPixels = area/bbArea;
	    
	     if(percPixels < 0.8 && charAspect > minAspect && charAspect <
	     maxAspect && src.rows() >= minHeight && src.rows () < maxHeight)
	    	 return true;
	     else
	    	 return false;
	}
	
	/**
	 * 
	 * @param img
	 * @param t	true being rows and false being cols
	 * @return
	 */
	private static Mat ProjectedHistogram(Mat img, boolean t){
		int sz = (t)?img.rows() : img.cols();
		Mat mhist = new Mat();
		mhist = Mat.zeros(1, sz, CvType.CV_32F);
		
		for(int j = 0; j < sz; j++){
			Mat data = (t)?img.row(j) :img.col(j);
			mhist.put(1, j, (float) Core.countNonZero(data)); 
			
		}
		//normailize histogram
		double minval, maxval;
		MinMaxLocResult minmaxlocR = Core.minMaxLoc(mhist);
		minval = minmaxlocR.minVal;
		maxval = minmaxlocR.maxVal;
		if(maxval > 0)			mhist.convertTo(mhist, -1, 1.0f/maxval, 0);
		
		return mhist;
	}
	
	private static Mat features(Mat src, int sizeData){
		//Histogram features
		Mat verthist = ProjectedHistogram(src, VERTICAL);
		Mat horihist = ProjectedHistogram(src, HORIZONTAL);
		
		//Low data feature
		Mat lowData = new Mat();
		Imgproc.resize(src, lowData, new Size(sizeData, sizeData));
		int numofCols = verthist.cols() + horihist.cols() + lowData.cols()*lowData.cols();
		Mat out = Mat.zeros(1, numofCols, CvType.CV_32F);
		
		//Assign values to feature
		int j = 0;
		for(int i=0; i<verthist.cols(); i++){
			out.put(1, j, verthist.get(1, i)); 
			j++;
		}
		
		for(int i=0; i<horihist.cols(); i++){
			out.put(1, j, horihist.get(1, i)); 
			j++;
		}
		
		for(int x=0; x<lowData.cols(); x++){
			for(int y=0; y<lowData.rows(); y++){
				out.put(1, j,  lowData.get(x,y));
				j++;
			}
		}
		
		return out;
	}
	
	public boolean getTrainingfiles(String pathname){
		
		return false;
	}
	
	public void train(Mat TrainData, Mat classes, int nlayers){
		Mat layerSizes = new Mat(1,3,CvType.CV_32SC1);
		layerSizes.put(1,0, TrainData.cols());
		layerSizes.put(1,1, nlayers);
		layerSizes.put(1,2, numCharacters);
		
		CvANN_MLP ann = new CvANN_MLP();
		ann.create(layerSizes, CvANN_MLP.SIGMOID_SYM, 1, 1);
		
		//Preparing trainclasses by creating a mat with n trained data by m classes
		Mat trainClasses = new Mat();
		trainClasses.create(TrainData.rows(), numCharacters, CvType.CV_32FC1);
		for(int i=0; i< trainClasses.rows(); i++){
			for(int k=0; k < trainClasses.cols(); k++){
				//If class of data i is same as a k class
				if(k == classes.get(1, i, classes.get(1, i)) ){
					trainClasses.put(i,k, 1);
				}else{
					trainClasses.put(i,k, 0);
				}
			}
			
			Mat weights = new Mat(1, TrainData.rows(), CvType.CV_32FC1, Scalar.all(1) );
			
			//learn classifier
			ann.train(TrainData, trainClasses, weights);
			isTrained = true;
		}
	}
	
	public static double classify(Mat src){
		int result = -1;
		Mat output = new Mat(1, numCharacters, CvType.CV_32FC1);
		ann.predict(src, output);
		Point maxLoc;
		double maxVal;
		MinMaxLocResult minmaxlocR = Core.minMaxLoc(output);
		maxVal = minmaxlocR.maxVal;
		maxLoc = minmaxlocR.maxLoc;
		
		return maxLoc.x;
		
	}
	
	private static Mat preprocessChar(Mat char_in){
		//remap image to a new Mat
		int h = char_in.rows();
		int w = char_in.cols();
		
		Mat transform_mat = Mat.eye(new Size(2,3), CvType.CV_32F);
		int m = Math.max(w, h);
		
		transform_mat.put(0, 2, m/2 - w/2);
		transform_mat.put(1, 2, m/2 - h/2);
		
		Mat warpImage = new Mat(new Size(m,m), char_in.type());
		System.out.println("char_in: " + char_in.toString());
		Imgproc.warpAffine(char_in, warpImage, transform_mat, warpImage.size(), Imgproc.INTER_LINEAR, Imgproc.BORDER_CONSTANT, new Scalar(0));
		
		Mat out = new Mat();
		Imgproc.resize(warpImage, out, new Size(charSize, charSize)) ;
		
		return out;
	}

}
