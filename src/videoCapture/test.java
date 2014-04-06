package videoCapture;

import java.io.IOException;

public class test extends VideoHelper {
	private static String extension = ".mp4";
	private static String filename  = "videoviewdemo";
	private static final String readfolder = "videos/";
	private static final String savefolder = "videos/saved";
	
	public static void main(String args[]) throws IOException, InterruptedException{
		VideoHelper vid = new VideoHelper();
		//vid.view(readfolder + filename + extension, "camera1");
		//System.out.println("helo");
		vid.ScreenCap(10, readfolder + filename + extension);
		
		
	}
}
