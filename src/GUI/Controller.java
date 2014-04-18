package GUI;



import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import com.mysql.jdbc.Connection;

public class Controller {

	private Login Login;
	private Home Home;
	private Model Model;
	private Camera Camera;
	private About About;
	private static Connection conn;
	private static String urlpath;
	//private Settings Settings;
	Email Email = new Email();
	Settings settings;
	RequestAccount RequestAccount = new RequestAccount();
	 Vehicle Vehicle = new Vehicle(conn);
	
	
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
       this.Home.addOpenListener(new OpenListener());
        
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
        
        
        //About Action Listeners
        this.About.addHomeListener(new HomeListener());
}

	 class LogInListener implements ActionListener {


		public void actionPerformed(ActionEvent arg0) {
		
			try {
				if (Controller.this.Login.loginAuthentication()) {
					Login.setVisible(false);
					Home.setVisible(true);
					
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
	 
	 class OpenListener implements ActionListener {

	
		public void actionPerformed(ActionEvent arg0) {
			
			String p = urlpath ;
		 Home.pic.insertIcon(new ImageIcon(urlpath));
		}
		 
	 }

	 class AddListener implements ActionListener {


	
		public void actionPerformed(ActionEvent arg0) {
			DefaultTableModel model= (DefaultTableModel)Home.gettblImage().getModel();
		
			
			if(!Home.getManuallyInputPlate().trim().equals("")|| !Home.getRecResult().trim().equals("") ){
				
				//System.out.println("this works");
				
		model.addRow(new Object[]{Model.getDate(),Model.getTime(), Home.getManuallyInputPlate(), "please"});
	
					
			
			}
			else
			{
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
	
	 class BrowseListener implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			String path;
			
		

			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(null);
			File f = chooser.getSelectedFile();
			path = f.getAbsolutePath();
			urlpath = path;
			Home.path.setText(path);
			//path.split("\\");
			
			
			//DataView.path.setText(path);
			
		}
	 
	 
	 }
	 
	 class ClearListener implements ActionListener{

		
		public void actionPerformed(ActionEvent arg0) {
			
			Home.setManuallyInputPlate();
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
}










	

