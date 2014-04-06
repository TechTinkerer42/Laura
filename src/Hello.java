/**
 * Canny edge detection sample
 * @author Adamwgoh
 * 
 */
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Hello
{
	public static BufferedImage matToBufferedImage(Mat matrix) {  
		int cols = matrix.cols();  
		int rows = matrix.rows();  
		int elemSize = (int)matrix.elemSize();  
		//convert Mat into bytesize data
		byte[] data = new byte[cols * rows * elemSize];  
		int type;  
		matrix.get(0, 0, data);  
		switch (matrix.channels()) {  
		case 1:  
			type = BufferedImage.TYPE_BYTE_GRAY;  
			break;  
		case 3:  
			type = BufferedImage.TYPE_3BYTE_BGR;  
			// bgr to rgb  
			byte b;  
			for(int i=0; i<data.length; i=i+3) {  
				b = data[i];  
				data[i] = data[i+2];  
				data[i+2] = b;  
			}  
			break;  
		default:  
			return null;  
		}  
		BufferedImage image2 = new BufferedImage(cols, rows, type);  
		image2.getRaster().setDataElements(0, 0, cols, rows, data);  
		return image2;  
	}  
	 
	    public static void main(String[] args) {
	    		//loads core opencv native library
	    		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    		
	    		//create a new Mat (opencv basic structure). Reads image with imread method from Highgui library.
	    		Mat m = Highgui.imread("car0652inv.JPG", Highgui.CV_LOAD_IMAGE_COLOR);
	    		
	    		//create a new Mat object as output object
	    		Mat aa = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		Mat nonEQ = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		Mat bb = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		//converts the color of the input image to gray using cvtColor
	    		Imgproc.cvtColor(m, aa,Imgproc.COLOR_RGB2GRAY);
	    		nonEQ = aa;
	    		
	    		//TODO: histEQ may give lots of noise to the picture. to EQ or not to EQ?
	    		Imgproc.equalizeHist(aa, aa);
	    		
	    		//TODO: apparently Gaussian Blur makes no difference EDIT: It makes a difference when done with different gaussian 
	    		//Imgproc.GaussianBlur(aa, aa, new Size(7,7), 0,0);
	    		
	    		Imgproc.Laplacian(aa, aa, aa.depth(), 3, 1, 0);
	    		
	    		//applies canny edge detector.  Canny(Mat input, Mat output, int low_threshold value, int high_threshold value)
	    		//Imgproc.Canny(aa, aa, 100, 150);
	    		Mat dx = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		Mat dy = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		Mat dxy = new Mat(m.width(), m.height(), CvType.CV_64FC2);
	    		Mat img_threshold = new Mat(m.width(), m.height(), CvType.CV_8U);
	    		//cvtype.CV_32FC1
	    		Imgproc.Sobel(aa, dx, aa.depth(), 1, 0);
	    		Imgproc.Sobel(aa, dy, aa.depth(), 0, 1);
	    		
	    		//adds Sobel-x and Sobel-y together and store in Mat dxy
	    		//(src1, alpha, src2, beta, gamma, dst)
	    		Core.addWeighted(dx, 0.5, dy, 0.5, 0, dxy);
	    		
	    		
	    		
	    		//Imgproc.threshold(src, dst, thresh, maxval, type);
	    		double threshold = Imgproc.threshold(dxy, img_threshold, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
	    		
	    		//TODO: if you're using canny, this can be the lower and higher threshold, canny takes more brain power
	    		//double high_thres = Otsu's threshold;
	    		//double lower thres = 0.5 * threshold;
	    		
	    		//getting structuring element for morphological
	    		Mat elem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
	    		
	    		//morphological transformation
	    		//morphologyEx(img_threshold, img_threshold, CV_MOP_CLOSE (3), element);
	    		Imgproc.morphologyEx(img_threshold, img_threshold, 3, elem);
	    		
	    		//finding contours
//	    		List<MatOfPoint> contours = null;
//	    		Imgproc.findContours(img_threshold, contours, new Mat(),
//	    							 0, //CV_RETR_EXTERNAL(0) retrieve the external contours
//	    							 1); //CV_CHAIN_APPROX_NONE(1) all pixels of each contour
//	    		
	    		/**
	    		 * //Start to iterate to each contour found
					vector<vector<Point> >::iterator itc= contours.begin();
					vector<RotatedRect> rects;
					//Remove patch that has no inside limits of aspect ratio and area.
					while (itc!=contours.end()) {
					//Create bounding rect of object
					RotatedRect mr= minAreaRect(Mat(*itc));
					if( !verifySizes(mr)){
						itc= contours.erase(itc);
					}else{
						++itc;
					rects.push_back(mr);
	    		 */
	    		List<RotatedRect> rects;
	    	//	Iterator itr = rects.iterator();
	    		
//	    		while(itr.hasNext()){
//	    			//create bounding rect of object
//	    			RotatedRect mr = Imgproc.minAreaRect((MatOfPoint2f) (itr.next()));
//	    			
//	    			if( !Imgproc.(mr)){
//	    				
//	    			}else{}
//	    		}
//	    		
	    		new LoadImage("dx.JPG", dx);
	    		new LoadImage("dy.JPG", dy);
	    		new LoadImage("dxy.JPG", dxy);
	    		new LoadImage("nonEQ.JPG", nonEQ);
	    		
	    		new LoadImage("GaussianNEQ.JPG", aa);
	    		//new LoadImage("Laplacian.JPG", bb);
	    		new LoadImage("morphed.JPG", img_threshold);
//	    		//convert dx into image and output
//	    		new LoadImage("dxtest0061nonEQ.JPG", dx);
//	    		new LoadImage("dytest049nonEQ.JPG", dy);
//	    		new LoadImage("dxytest049nonEQ.JPG", dxy);
//	    		
	    		
//	    		//resize the picture by half
//	    		Imgproc.resize(aa, aa, new Size(aa.width()*0.5, aa.height()*0.5));
//	    		//output image
//	    		new LoadImage("sobeltest061nonEQ.JPG", aa);
	    		
	    }
}

