import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;



public class DisplayVid {
	
	public static void main(String[] args) throws IOException{
	
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat frame1 = new Mat();
		Mat frame2 = new Mat();
		Mat mhi = new Mat();
		Mat framediff = new Mat();
		VideoCapture cap = new VideoCapture();
		BufferedImage img = null;
		MatToBufferedImage conv = new MatToBufferedImage();
		int count = 0;
		
		cap.open(0);
		//cap.open("goalvid.mp4");
		
		if(!cap.isOpened()){
			System.err.println("Error opening file!");
		}else{
			System.out.println("Camera ok");
		}
		
			cap.read(frame1);
			cap.read(frame2);
			Core.absdiff(frame1, frame2, framediff);
			Imgproc.cvtColor(frame1, frame2, Imgproc.COLOR_RGB2GRAY);
			Imgproc.cvtColor(frame1, frame1, Imgproc.COLOR_RGB2GRAY);
			conv.SetImage(framediff);
			img = conv.getImage();

			JFrame jf = new JFrame();
			jf.setSize(img.getWidth()-100, img.getHeight()-100);
			jf.setTitle("Ronaldo Goal");
			ColorPanel panel = new ColorPanel(img);
			jf.setContentPane(panel);
			jf.setVisible(true);
			double timestamp = (System.nanoTime()) / 1e9;
			mhi = Mat.zeros(new Size(img.getWidth(), img.getHeight()), CvType.CV_32FC1);
			
			while(true){
				if(count % 2 == 0){
				cap.read(frame1);
				
					Imgproc.cvtColor(frame1, frame1, Imgproc.COLOR_RGB2GRAY);
					Core.absdiff(frame1, frame2, framediff);
					//Imgproc.threshold(framediff, framediff, Imgproc.THRESH_OTSU, 1, Imgproc.THRESH_BINARY);
					
					//Video.updateMotionHistory(framediff, mhi, timestamp, 1);
					img = conv.getImage(framediff);
					panel.setImage(img);
					panel.repaint();
					count++;
				}else if(count % 2 != 0){
					cap.read(frame2);
					Imgproc.cvtColor(frame2, frame2, Imgproc.COLOR_RGB2GRAY);
					Core.absdiff(frame1, frame2, framediff);
					//Imgproc.threshold(framediff, framediff, Imgproc.THRESH_OTSU, 1, Imgproc.THRESH_BINARY);
					
					//Video.updateMotionHistory(framediff, mhi, timestamp, 1);
					img = conv.getImage(framediff);
					panel.setImage(img);
					panel.repaint();
					count++;
				}
			}
			
	
	
	}
}

class ColorPanel extends JPanel{
	BufferedImage theCat;
	
	public ColorPanel(BufferedImage image){
		theCat = image;
	}
	
	public void setImage(BufferedImage image){
		theCat = image;
	}
 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(theCat, null, 50,50);
	}
}




