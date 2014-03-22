package examples;

import java.io.File;

import org.jfree.ui.RefineryUtilities;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

public class SVMachine {
	private Mat svm_trainingimages;
	private Mat svm_trainingdata;
	private Mat svm_classes;
	//Set SVM Parameters
	CvSVMParams svm_params = new CvSVMParams();
	CvSVM classifier;
	
	
	private void init(Mat svm_trainingdata, Mat svm_classes){
		svm_params.set_kernel_type(CvSVM.LINEAR);
		CvSVM classifier = new CvSVM(svm_trainingdata, svm_classes, new Mat(), new Mat(), svm_params);
		
		this.svm_trainingdata = svm_trainingdata;
		this.svm_classes = svm_classes;
	}
	
	
	
	public boolean predict(Mat possibleplate){
		int response = (int) classifier.predict(possibleplate);
		if(response == 1)
			return true;
		
		return false;
	}
	
	
	public void getTrainingfiles(String path){
		for (File file : new File(path + "positives/").listFiles()) {
	        Mat img = Highgui.imread(file.getAbsolutePath());
	        img.reshape(1, 1);	//convert to 1 row of m features
	        img.convertTo(img, CvType.CV_32FC1);
	        
	        svm_trainingimages.push_back(img);
	        svm_classes.push_back(Mat.ones(new Size(1, 1), CvType.CV_32FC1));
	    }
	
	    for (File file : new File(path + "negatives/").listFiles()) {
	        Mat img = Highgui.imread(file.getAbsolutePath());
	        img.reshape(1, 1);	//convert to 1 row of m features
	        img.convertTo(img, CvType.CV_32FC1);
	        
	        svm_trainingimages.push_back(img);
	        svm_classes.push_back(Mat.zeros(new Size(1, 1), CvType.CV_32FC1));
	    }
	
	    svm_trainingimages.copyTo(svm_trainingdata);
	    svm_trainingdata.convertTo(svm_trainingdata, CvType.CV_32FC1);
	    svm_trainingimages.copyTo(svm_classes);
	};
}
