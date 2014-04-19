package videoCapture;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;



public class VidPanel extends JPanel{
	BufferedImage img;
	
	public VidPanel(BufferedImage image){
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