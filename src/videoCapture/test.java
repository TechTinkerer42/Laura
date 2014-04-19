package videoCapture;

import java.io.IOException;

import org.opencv.highgui.Highgui;

public class test extends VideoHelper {
	private static String extension = ".mp4";
	private static String filename  = "vid1680";
	private static final String readfolder = "videos/";
	private static final String savefolder = "videos/saved";
	
	public static void main(String args[]){
		Opticalflow optflow = new Opticalflow();
		
		try {
			optflow.run(filename + extension);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//vid.ScreenCap(10, readfolder + filename + extension);
		 
		
	}
}
