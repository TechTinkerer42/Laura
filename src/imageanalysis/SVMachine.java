package imageanalysis;

import java.io.File;

import org.jfree.ui.RefineryUtilities;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

public class SVMachine {
	private static Mat svm_trainingimages;
	private static Mat svm_trainingdata;
	private static Mat svm_traininglabels;
	private static Mat svm_classes;
	//Set SVM Parameters
	private static CvSVMParams svm_params;
	private CvSVM classifier;
	
	public SVMachine(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		svm_params = new CvSVMParams();
		//init all Mats
		svm_trainingdata = new Mat();
		svm_trainingimages = new Mat();
		svm_traininglabels = new Mat();
		svm_classes = new Mat();
	}
	
	public void init(){
		svm_params.set_svm_type(CvSVM.C_SVC);
		svm_params.set_kernel_type(CvSVM.LINEAR);
		svm_params.set_degree(0);
		svm_params.set_gamma(1);
		svm_params.set_C(1);
		svm_params.set_nu(0);
		svm_params.set_p(0);
		svm_params.set_term_crit(new TermCriteria(TermCriteria.MAX_ITER, 1000, 0.01));
		
	}
	
	public void close(){
		classifier.clear();
		svm_trainingdata = new Mat();
		svm_traininglabels = new Mat();
		svm_classes = new Mat();
	}
	
	
	
	public boolean predict(Mat possibleplate){
		int response = (int) classifier.predict(possibleplate);
		if(response == 1)
			return true;
		
		return false;
	}
	
	
	public void getTrainingfiles(String path){
		svm_trainingimages = new Mat();
		
		File[] positives = new File(path + "positives/").listFiles();
		File[] negatives = new File(path + "negatives/").listFiles();
		
		for (int i = 1; i<positives.length; i++) {
			if(!positives[i].isHidden()){
		        Mat img = Highgui.imread(positives[i].getAbsolutePath(), 0);
		        img = img.reshape(1, 1);	//convert to 1 row of m features
		        img.convertTo(img, CvType.CV_32FC1);
		        svm_trainingimages.push_back(img);
		        svm_classes.push_back(Mat.ones(new Size(1, 1), CvType.CV_32FC1));
			}
	    }
	
	    for (int i = 1; i<negatives.length; i++){
	    	if(!negatives[i].isHidden()){
		        Mat img = Highgui.imread(negatives[i].getAbsolutePath(), 0);
		        img = img.reshape(1, 1);	//convert to 1 row of m features
		        img.convertTo(img, CvType.CV_32FC1);
		        svm_trainingimages.push_back(img);
		        svm_classes.push_back(Mat.zeros(new Size(1, 1), CvType.CV_32FC1));
		    }
	    }
	
	    svm_trainingimages.copyTo(svm_trainingdata);
	    svm_trainingdata.convertTo(svm_trainingdata, CvType.CV_32FC1);
	    svm_classes.copyTo(svm_traininglabels);
	    System.out.println(svm_trainingimages.toString());
	    System.out.println(svm_trainingdata.toString());
	    System.out.println(svm_classes.toString());
	    classifier = new CvSVM(svm_trainingdata, svm_traininglabels, new Mat(), new Mat(), svm_params);
	}
	

	
	
}
