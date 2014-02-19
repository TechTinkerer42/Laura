package examples;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Histogram extends ApplicationFrame {
	//TODO: A properties class that takes in whether to initiate vertical or horizontal dataset
	
	private int[] img_arr;
	
	   public Histogram(final String title, String imgname) throws IOException {
		    super(title);
		    BufferedImage image = ImageIO.read(new File(imgname));
		    IntervalXYDataset dataset = createVertDataset(image);
		    //InteralXYDataset dataset = createRedDataset();
		    JFreeChart chart = createChart(dataset);
		    final ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		    setContentPane(chartPanel);
	   }
	   
	   public Histogram(final String title, BufferedImage img){
		    super(title);
		    BufferedImage image = img;
		    IntervalXYDataset dataset = createVertDataset(image);
		    //InteralXYDataset dataset = createRedDataset();
		    JFreeChart chart = createChart(dataset);
		    final ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		    setContentPane(chartPanel);
	   }
	   //TODO: need proper javadoc for all these constructors
	   public Histogram(BufferedImage img){
		    super("histogram");
		    BufferedImage image = img;
		    IntervalXYDataset dataset = createVertDataset(image);
		    //InteralXYDataset dataset = createRedDataset();
		    JFreeChart chart = createChart(dataset);
		    final ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		    setContentPane(chartPanel);
		   	   
	   }
	   
	   public Histogram(final String title, BufferedImage img, String dset){
		    super(title);
		    BufferedImage image = img;
		    IntervalXYDataset dataset = createVertDataset(image);
		    if(dset == "horizontal"){
		    	dataset = createHoriDataset(image);
		    }else if(dset == "vertical"){
		    	dataset = createVertDataset(image);
		    }

		    JFreeChart chart = createChart(dataset);
		    final ChartPanel chartPanel = new ChartPanel(chart);
		    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		    setContentPane(chartPanel);
	   }
	   
	   //TODO: proper catch for null variable
	   public int[] getImgArr(){

		   return img_arr;
	   }
	 
	  /**
	   * <p>Creates a dataset of vertical projection of the given input image</p>
	   * @param image			an input image to generate a vertical projection of the image
	   * @return dataset		a jfree library's dataset to be used for plottings
	   */
	 private IntervalXYDataset createVertDataset(BufferedImage image){
		 BufferedImage img = image;
		 
		 int[] height = new int[img.getHeight()];
		 int total = img.getWidth()*255;
		 int sum = 0;

		 //from top row to bottom row
		 for(int y = img.getHeight()-1; y>0; y--){
			 for(int x=0; x<img.getWidth()-1; x++){
				 //get pixel value and add into sum
				 sum += (img.getRGB(x, y)  & 0x00ff0000 )>> 16;
			 }
			 double avg = ((double) sum/ (double)total)*100;
			 height[y] = (int) avg;	//assign summation value in percentage to that height's row
			 sum = 0;			//restart value
		 }
		
		img_arr = height; 
		 
		return createDataset(height);
	 }

	 
	  /**
	   * <p>Creates a dataset of horizontal projection of the given input image</p>
	   * @param image			an input image to generate a horizontal projection of the image
	   * @return dataset		a jfree library's dataset to be used for plottings
	   **/
	 private IntervalXYDataset createHoriDataset(BufferedImage image){
		 BufferedImage img = image;

		 int[] width = new int[img.getWidth()];
		 int total = img.getHeight()*255;
		 int sum = 0;

		 //from first row to bottom row
		 for(int x = 0; x < img.getWidth()-1; x++){
			 for(int y=0; y<img.getHeight()-1; y++){
				 //get pixel value and add into sum
				 sum += (img.getRGB(x, y)  & 0x00ff0000 )>> 16;
			 }
			 double avg = ((double) sum/ (double)total)*100;
			 width[x] = (int) avg;	//assign summation value in percentage to that height's row
			 sum = 0;			//restart value
		 }
		 
		 img_arr = width;
		 
		 return createDataset(width);
	 }
	 
	 /**
	  * Creates a XYSeriesCollection dataset from image's value array from given input
	  * @param img	an array input of values for an image
	  * @param legend(optional) legend of the value for the input image array
	  * @return a dataset to be used for plottings
	  */
	 private XYSeriesCollection createDataset(int[] img){
		 XYSeries series = new XYSeries("legend here");
		 for(int y=0; y<img.length; y++)
			 series.add(y, img[y] );
		 
		 XYSeriesCollection dataset = new XYSeriesCollection(series);
		 return dataset;
	 }
	 
	 private XYSeriesCollection createDataset(int[] img, String legend){
		 XYSeries series = new XYSeries("legend here");
		 for(int y=0; y<img.length; y++)
			 series.add(y, img[y] );
		 
		 XYSeriesCollection dataset = new XYSeriesCollection(series);
		 return dataset;
	 }

	 private JFreeChart createChart(IntervalXYDataset dataset) {
	  final JFreeChart chart = ChartFactory.createXYBarChart("histogram"
			  	,"X",false,"Y",dataset,PlotOrientation.VERTICAL,true,true,false);
	  XYPlot plot = (XYPlot) chart.getPlot();
	  return chart;    
	 }
	 
	 public int getMaxVal(int[] arr){
		 int max = 0;
		 
		 for(int i=0; i<arr.length; i++){
			 if(arr[i] > max){
				 max = arr[i];
			 }
		 }
		 return max;
	 }
	 
	 public Coordinates getPeakCoord(int[] arr){
		 int max = getMaxVal(arr);
		 int YPeak;
		 
		 //loop through array to find for y coordinate
		 for(int y = 0; y<arr.length; y++)
			 if(arr[y] == max){
				 System.out.println("max is " + max + ", YPeak is " + y);
				 YPeak = y;	
				 Coordinates peak = new Coordinates(0, YPeak);  
				 return peak;
			 }	
		 Coordinates notfound = new Coordinates(0,0);
		 System.err.println("no coord found");
		 return notfound;
	 }
	 
	 /**
	  * <p> BandCoords is used to calculate the sides of a possible plate location by having a clipping range between a chose peak </p>
	  * @param yValues	The vertical projection accumulate values produced from VertDataset
	  * @param threshold	The threshold value to determine where to find the Band's sides
	  * @return	returns a Coordinate type with its X as Yb0 of a band and its Y as Yb1 of a band
	  */
	 public Coordinates getBandCoords(int[] yValues, double threshold){
		 Coordinates Peak_coord = getPeakCoord(yValues);
		 Coordinates band_coord;
		 int Yb0_value = 0;
		 int Yb1_value = yValues[Peak_coord.getY()];
		 int Yb0 = 0;
		 int Yb1 = 0;
		 
		 //find for Yb0 value, and get its Y coordinate
		 for(int y = Peak_coord.getY(); y>0; y--){
			 if(yValues[y] < threshold*yValues[Peak_coord.getY()]){
				 
					 Yb0_value = yValues[y];
					 Yb0 = Math.max(0, y);
					 System.out.println("Y value is " + Yb0_value + ", Yb0 is " + Yb0);
					 break;
			 }
			 
		 }
		 
		 //find for Yb1 value, and get its Y coordinate
		 for(int y = Peak_coord.getY(); y<yValues.length; y++){
			 if(yValues[y] < threshold*yValues[Peak_coord.getY()] ){
					 Yb1_value = yValues[y];
					 Yb1 = Math.min(yValues.length, y);
					 System.out.println("Y value is " + Yb1_value + ", Yb1 is " + Yb1);
					 break;

			 }
		 }
		 
		 band_coord = new Coordinates(Yb0, Yb1);
		 
		 int diff = band_coord.getY() - band_coord.getX();
		 int NewYb0 = Math.max(0, band_coord.getX() - (int) (0.2*diff));
		 int NewYb1 = Math.min(yValues.length, band_coord.getY() + (int) (0.2*diff));
		 
		 System.out.println("Diff is: " + diff);
		 System.out.println("NewYb0 is " + NewYb0);
		 System.out.println("NewYb1 is " + NewYb1);
		 band_coord = new Coordinates(NewYb0, NewYb1);
		 
		 //TODO: Band Coords may be -ve
		 return band_coord;
	 }
	 
//depereciated, for testing purposes only
/*	 public static void main(final String[] args) throws IOException {	
	  final Histogram demo = new Histogram("Image Histogram", "dx.JPG");
	  demo.pack();
	  RefineryUtilities.centerFrameOnScreen(demo);
	  demo.setVisible(true);
	 }*/
	}
