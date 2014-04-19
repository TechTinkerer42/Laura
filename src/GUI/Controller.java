package GUI;



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;


import videoCapture.*;

import com.mysql.jdbc.Connection;
import imageanalysis.*;
import imageanalysis.Snapshot;


import com.xuggle.*;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.ToolFactory;

import net.miginfocom.swing.MigLayout;
import java.util.Timer;


public class Controller {

	private Login Login;
	private Home Home;
	private Model Model;
	private Camera Camera;
	private About About;
	private static Connection conn;
	
	private static String urlpath;
	private static String vidpath;
	private static String platepath = "platepaths/";
	
	private String plateS;
	private boolean hasImage = false;
	
	//private Settings Settings;
	Email Email = new Email();
	Settings settings;
	RequestAccount RequestAccount = new RequestAccount();
	Vehicle Vehicle = new Vehicle(conn);
	
	BufferedImage img;
	
	public Controller(Login Login, Home Home, Model Model, Camera Camera, Email Email, About About, Settings Settings,RequestAccount RequestAccount, Connection conn) {
        this.Login = Login;
        this.Home = Home;
        this.conn = conn;
        this.Camera = Camera;
        this.Model = Model;
        this.About = About;
        this.RequestAccount=RequestAccount;
        settings = new Settings(this.conn);
      
        
        
        
        this.Login.addLoginListener(new LogInListener());
        this.Login.addRequestListener(new RequestListener());
        this.RequestAccount.addRequestCancelListener(new RequestCancelListener());
        this.Login.addLoginListener(new AdminListener());
       
        
        //Home Action Listeners
        this.Home.addAddListener(new AddListener());
        this.Home.addHomeListener(new HomeListener());
       // this.Home.addDatabaseListener(new DatabaseListener());
        this.Home.addCameraListener(new CameraListener());
        this.Home.addLogOutListener(new LogoutListener());
        this.Home.addBrowseListener(new BrowseListener());
        this.Home.addClearListener(new ClearListener());
       // this.Home.addDeleteListener(new DeleteListener());
        this.Home.addAboutListener(new AboutListener());
       // this.Home.addHelpListener(new HelpListener());
       this.Home.addUserListener(new UserListener());
       this.Home.addExecuteListener(new ExecuteListener());
       this.Home.addAdminListener(new AdminListener());
        
        //this.Home.addAddListener(new AddListener());
              
        //Database Action Listeners
        /*this.DataView.addAddListener(new AddListener());
        this.DataView.addHomeListener(new HomeListener());
        this.DataView.addDatabaseListener(new DatabaseListener());
        this.DataView.addCameraListener(new CameraListener());
        this.DataView.addLogOutListener(new LogoutListener());
        this.DataView.addBrowseListener(new DataBrowseListener());
        this.DataView.addEmailListener(new EmailListener());
        this.DataView.addDataExportListener(new DataExportListener());
        this.DataView.addAboutListener(new AboutListener());
        this.DataView.addDataExecuteListener(new DataExecuteListener());
        //this.Home.addAddListener(new AddListener());*/
        
        
        //Camera Action Listeners
        this.Camera.addHomeListener(new HomeListener());
      //  this.Camera.addDatabaseListener(new DatabaseListener());
        this.Camera.addCameraListener(new CameraListener());
        this.Camera.addLogOutListener(new LogoutListener());
        this.Camera.addAboutListener(new AboutListener());
        this.Camera.addUserListener(new UserListener());
        
        
        //About Action Listeners
        this.About.addHomeListener(new HomeListener());
        
        //Camera Action Listeners
        this.Camera.addBrowseVidListener(new BrowseVidListener());
}//end of constructor
	
	class AdminListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			if(!(Controller.this.Login.getStatusID() == 3)){
				Home.Admin.setVisible(false);
			}
		}
	}
	
	
	 class LogInListener implements ActionListener {


		public void actionPerformed(ActionEvent arg0) {
			try {
				if (Controller.this.Login.loginAuthentication()) {
					Login.setVisible(false);
					Home.setVisible(true);
					if(Controller.this.Login.getStatusID() == 3){
						//Home.Admin.setText("YOU DID IT");
						Home.Admin.setVisible(true);
					}
				} else {
					Login.setVisible(true);
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			/*Login.setVisible(false);
			Home.setVisible(true);*/

		}
		 
	 class RequestListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						RequestAccount window = new RequestAccount();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	 }
	
	 class RequestCancelListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Login.setVisible(true);
			RequestAccount.frame.setVisible(false);
			
		}
	 }
	 
//	 class OpenListener implements ActionListener {
//
//	
//		public void actionPerformed(ActionEvent arg0) {
//			
//			String p = urlpath ;
//			ImageIcon icon = new ImageIcon(p);
//			Image img = icon.getImage();
//			img = img.getScaledInstance(381, 386, Image.SCALE_DEFAULT);
//			
//			BufferedImage buffimg = new BufferedImage(381, 386, BufferedImage.TYPE_INT_ARGB);
//			
//			Graphics g = buffimg.createGraphics();
//			g.drawImage(img, 0, 0, 381, 386, null);
//			ImageIcon newicon = new ImageIcon(img);
//			
//			Home.pic.setIcon(newicon);
//		}
//		 
//	 }

	 class AddListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			DefaultTableModel model= (DefaultTableModel)Home.gettblImage().getModel();		
			if(!Home.getManuallyInputPlate().trim().equals("")|| !Home.getRecResult().trim().equals("") ){
				//TODO: plate path here, time here is also wrong. Need the time of image capture.
				model.addRow(new Object[]{Model.getDate(),Model.getTime(), Home.getManuallyInputPlate(), platepath + Home.getManuallyInputPlate() + ".jpg"});
				try {
					System.out.println("saving image.... " +Snapshot.SaveImage(img, Home.getManuallyInputPlate(), platepath));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				Home.displayErrorMessage("Please Insert the Number Plate Before Clicking Add Button");
			}
		}
	 }

	 class HomeListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			Home.setVisible(true);
			Camera.setVisible(false);
			About.setVisible(false);
			}	 
	 }
	 
	 class ExecuteListener implements ActionListener{
		 ArrayList<Character> characters = new ArrayList<Character>();
		 public void actionPerformed(ActionEvent e){
			 if(hasImage){
				 try {
					
					img = Preprocess.run(urlpath);
					//get image from here and paste onto there
					ImageIcon newicon = new ImageIcon(img);
					Home.RecResult.setIcon(newicon);
					
//					OCR ocr = new OCR();
//					characters = ocr.getPlateCharacter(img);
//					char[] chars = new char[characters.size()];
//					plateS = new String(chars);
//					if(plateS.isEmpty()){
//						JOptionPane.showMessageDialog(null, "plate characters not detected. Manual Input is available, however");
//					}
//					
//					Home.setManuallyInputPlate(plateS);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }else{
				 JOptionPane.showMessageDialog(null,"Can't execute! No image loaded");	
			 }
			 System.out.println("Execute pressed");
		 }
	 }
	 
	/* class DatabaseListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
		
		
			Home.setVisible(false);
			Camera.setVisible(false);
		}
		 
	 }*/
	 
	 class CameraListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			Camera.setVisible(true);
			Home.setVisible(false);
		}
	 }
	 
	 class LogoutListener implements ActionListener{

	
		public void actionPerformed(ActionEvent e) {
			Login.setVisible(true);
			Home.setVisible(false);
		
			Camera.setVisible(false);
			
			
			
		}
		 
	 }
	 
	 class BrowseVidListener implements ActionListener{

		 
		 public void actionPerformed(ActionEvent e){
			 	String path;
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				path = f.getAbsolutePath();
	            //file you want to play

				vidpath = path;
				try {
					Opticalflw(path);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Runnable runner = new OptflowRunnable();
//				Thread worker = new Thread(runner);
//				worker.run();
				
//					
//					if(!optflow.WrongDirImg.empty()){
//						BufferedImage img = conv.getImage(optflow.WrongDirImg);
//					}e1.printStackTrace();
				
	           
				
//				try {
//					URL mediaURL = new File(path).toURI().toURL();
//					Playback player = new Playback(mediaURL.getPath(), "Camera1");
//					Camera.video.add(player);
//				} catch (MalformedURLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
	 
					
				

	          //  Camera.video.add(controls,BorderLayout.SOUTH);
		 }
		 
		void Opticalflw(String path) throws IOException, InterruptedException {
			Opticalflow optflow = new Opticalflow();
			optflow.run(path);
		}
	 }
	 
	
	 class BrowseListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String path;
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);
			File f = chooser.getSelectedFile();
			path = f.getAbsolutePath();
			urlpath = path;
			hasImage = true;
			Home.path.setText(path);
			
			ImageIcon icon = new ImageIcon(path);
			Image img = icon.getImage();
			img = img.getScaledInstance(381, 386, Image.SCALE_DEFAULT);
			
			BufferedImage buffimg = new BufferedImage(381, 386, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = buffimg.createGraphics();
			g.drawImage(img, 0, 0, 381, 386, null);
			ImageIcon newicon = new ImageIcon(img);
			
			Home.pic.setIcon(newicon);
			//path.split("\\");			
			//DataView.path.setText(path);	
		}
	 }
	 
	 class ClearListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {	
			Home.setManuallyInputPlate("");
		} 
	 }
	 
	 class DeleteListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			DefaultTableModel model= (DefaultTableModel)Home.gettblImage().getModel();
			int numRows = Home.gettblImage().getRowCount();
			//for (int i=numRows-1;i>0;i++) {
			model.removeRow(numRows-1); 
			Home.gettblImage().revalidate();	
		}		 
	 }
	 
	/* class DataBrowseListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
				String path;
				
			

				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				path = f.getAbsolutePath();
				DataView.path.setText(path);
				
			}
		 
			
		}*/
		  
	 class EmailListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		//	Email Email = new Email();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Email window = new Email();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}	
	}
 
	 class ExportListener implements ActionListener {

		
		public void actionPerformed(ActionEvent arg0) {
			
			JFileChooser schooser = new JFileChooser();
			schooser.showSaveDialog(null);
			 int rVal = schooser.showSaveDialog(null);
			JTextComponent filename = null;
			JTextComponent dir = null;
			if (rVal == JFileChooser.APPROVE_OPTION) {
		        filename.setText(schooser.getSelectedFile().getName());
		        dir.setText(schooser.getCurrentDirectory().toString());
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		        filename.setText("You pressed cancel");
		        dir.setText("");
		      }
		    }
			
		}
		 
	 class AboutListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {	
				String file = "GRP - Help and About.txt";
				FileReader r = new FileReader(file);
				/*BufferedReader reader = new BufferedReader(r);
				
				
				String inputFile = "";
			    String textFieldReadable = reader.readLine();

			    while (textFieldReadable != null){
			        inputFile += textFieldReadable;
			        textFieldReadable = reader.readLine();                    
			    }		*/	
				
				About.about.read(r, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			About.setVisible(true);
			Home.setVisible(false);
		
			Camera.setVisible(false);	
		} 
	 }
	
	 class UserListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("works");
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Settings window = new Settings(conn);
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		}
		 
		 
	 }
	
	/* class DataExportListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			
			DataView.gettblImage().setModel(DataView.getnumberplate());
			
			
		}
		 
		 
	 }
	 
	 class DataExecuteListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		
			
			DataView.getpath();
			DataView.getnumberplatetable().setModel(DataView.getnumberplate());
			
		}
		 
		 
	 }*/
	 
	 public class OptflowRunnable implements Runnable{

		@Override
		public void run() {
			
			Opticalflow optflow = new Opticalflow();
			try {
				optflow.run(vidpath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 }
}










	

