package examples;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;

/**
 * Snapshot class
 * <p>contains general input image accessors and mutators.</p>
 * <p>image processing functions are done here</p>
 * 
 * 
 * @author dolphin
 *
 */
public class Snapshot {
	private static BufferedImage image;
	private Mat mat;
	private MatToBufferedImage conv;	//do you need this?
	private static Histogram hist;
	
	
	//TODO: FEED THEM THE KIDS
	//constructor
	public Snapshot(){
		
	}
	
	public void LoadImage(String filename) throws IOException{
		//loads image from src file
		image = ImageIO.read(new File(filename));
		hist = new Histogram(filename, filename);
	}
	/**
	 * Initialize a histogram with the given input image and assign it into local variable.
	 * @param img a BufferedImage type variable
	 */
	public static void LoadImage(BufferedImage img){
		image = img;
		hist = new Histogram(img);
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	/**
	 * <p>Loads image available in the class. If none is available, return error.  </p>
	 * <p>If specific coords are given, a cropped image will be produced </p>
	 * @param xstart
	 * @param xend (optional)
	 * @param ystart
	 * @param yend (optional)
	 * 
	 * @return
	 **/
	public static void ViewImage(){

		JFrame jf = new JFrame();
		if(image == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		jf.setSize(image.getWidth(), image.getHeight());
		jf.setTitle("Image");
		ColorPanel panel = new ColorPanel(image);
		jf.setContentPane(panel);
		jf.setVisible(true);
	}
	
	public static void ViewImage(int xstart, int xend, int ystart, int yend){

		JFrame jf = new JFrame();
		if(image == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		image = image.getSubimage(xstart, ystart, xend-xstart, yend-ystart);
		jf.setSize(image.getWidth(), image.getHeight());
		jf.setTitle("Image");
		ColorPanel panel = new ColorPanel(image);
		jf.setContentPane(panel);
		jf.setVisible(true);
	}
	
	public static void ViewImage(int xstart, int ystart){
		JFrame jf = new JFrame();
		if(image == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		image = image.getSubimage(xstart, ystart, image.getWidth(), image.getHeight());
		jf.setSize(image.getWidth(), image.getHeight());
		jf.setTitle("Image");
		ColorPanel panel = new ColorPanel(image);
		jf.setContentPane(panel);
		jf.setVisible(true);
	}
	

	
	
	
	public void SaveImage(String filename){
		
	}
	
	public Mat getMatFromImage(BufferedImage img){
		Mat mat = new Mat();
		byte[] data = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		
		return mat;
	}
	


/**
 * <p> ColorPanel class <p>
 * <p> overrides the original paintcomponent method to draw BufferedImage.
 *  allows BufferedImage to be set onto the panel						</p>
 * @author dolphin
 *
 */
public static class ColorPanel extends JPanel{
	BufferedImage img;
	
	public ColorPanel(BufferedImage image){
		img = image;
	}
	
	public void setImage(BufferedImage image){
		img = image;
	}
 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, null, 0,0);
	}
}
}

