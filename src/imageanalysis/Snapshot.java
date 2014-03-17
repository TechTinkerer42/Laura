package examples;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage; 
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

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
	private Histogram hist;
	
	
	//TODO: FEED THEM THE KIDS
	//constructor
	public Snapshot(){
		
	}
	
	public void SetImage(String filename) throws IOException{
		//loads image from src file
		image = ImageIO.read(new File(filename));
	}
	/**
	 * Initialize a histogram with the given input image and assign it into local variable.
	 * @param img a BufferedImage type variable
	 */
	public static void SetImage(BufferedImage img){
		image = img;
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
	public static void ViewImage(String name){

		JFrame jf = new JFrame();
		if(image == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		jf.setSize(image.getWidth(), image.getHeight());
		jf.setTitle(name);
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
		jf.pack();
		jf.setVisible(true);
	}
	
	public static void ViewImage(BufferedImage img, String title){
		JFrame jf = new JFrame();
		if(img == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		jf.setSize(img.getWidth(), img.getHeight());
		jf.setTitle(title);
		ColorPanel panel = new ColorPanel(img);
		jf.setContentPane(panel);
		jf.pack();
		jf.setVisible(true);
	}
	
	
	//TODO: do this
	public void SaveImage(String filename){
		
	}
	
	public Mat getMatFromImage(BufferedImage img){
		Mat mat = new Mat();
		byte[] data = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		
		return mat;
	}
	
	
	/**
	 * Scales image to a given input size
	 * @param src	image to be resized
	 * @param w		desired size of image width
	 * @param h		desired size of image height
	 * @return		resized image in BufferedImage type
	 */
	public static BufferedImage getScaledImage(BufferedImage src, int w, int h){
	    int finalw = w;
	    int finalh = h;
	    double factor = 1.0d;
	    if(src.getWidth() > src.getHeight()){
	        factor = ((double)src.getHeight()/(double)src.getWidth());
	        finalh = (int)(finalw * factor);                
	    }else{
	        factor = ((double)src.getWidth()/(double)src.getHeight());
	        finalw = (int)(finalh * factor);
	    }   

	    BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(src, 0, 0, finalw, finalh, null);
	    g2.dispose();
	    return resizedImg;
	}

	/**
	 * Scales image to a given input size
	 * @param src	image to be resized
	 * @param scale a double typed scaling number for the desired resized image.
	 * @return		resized image in BufferedImage type
	 */
	public static BufferedImage getScaledImage(BufferedImage src, double scale){
	    int finalw = (int) (src.getWidth()*scale);
	    int finalh = (int) (src.getHeight()*scale);
	    double factor = 1.0d;
	    if(src.getWidth() > src.getHeight()){
	        factor = ((double)src.getHeight()/(double)src.getWidth());
	        finalh = (int)(finalw * factor);                
	    }else{
	        factor = ((double)src.getWidth()/(double)src.getHeight());
	        finalw = (int)(finalh * factor);
	    }   

	    BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(src, 0, 0, finalw, finalh, null);
	    g2.dispose();
	    return resizedImg;
		
	}
	
	public static BufferedImage getCropImage(BufferedImage img, int xstart, int xend, int ystart, int yend){

		if(img == null){
			//TODO: better catch this dude with a proper warrant/exception
			System.err.println("No image found!");
		}
		
		img = img.getSubimage(xstart, ystart, xend-xstart, yend-ystart);
		return img;
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

