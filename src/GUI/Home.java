package GUI;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

public class Home extends JFrame{
	private JTextField ManuallyInputPlate;
	JTextField path;
	private JButton Home;
	private JButton Camera;
	private JButton About;
	private JButton LogOut;
	private JLabel Preview;
	private JLabel RecognitionResult;
	private JButton AddToTable;
	private JButton Clear;
	private JButton Browse;
	private JButton Export;
	private JButton Execute;
	private JScrollPane scrollPane;
	private JTable tblImage;
	private JButton button;
	private JLabel lblNewLabel;
	private JButton User;
	private JTable table;
	private JTextPane RecResult;
	private JButton Open;
	JLabel pic;
	
	public Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("ANPR");
		getContentPane().setLayout(null);
		
		Home = new JButton("Home");
		Home.setBounds(7, 7, 73, 28);
		Home.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Home);
		
		Camera = new JButton("Camera");
		Camera.setBounds(90, 7, 79, 28);
		Camera.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Camera);
		
		About = new JButton("About");
		About.setBounds(179, 7, 73, 28);
		About.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(About);
		
		lblNewLabel = new JLabel("Signed in as");
		lblNewLabel.setBounds(683, 11, 61, 28);
		lblNewLabel.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(lblNewLabel);
		
		User = new JButton("User");
		User.setBounds(748, 7, 76, 28);
		User.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(User);
		
		LogOut = new JButton("Log out");
		LogOut.setBounds(748, 50, 76, 28);
		LogOut.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(LogOut);
		
		Preview = new JLabel("Preview");
		Preview.setBounds(19, 55, 39, 30);
		Preview.setFont(new Font("Gabriola", Font.PLAIN, 17));
		getContentPane().add(Preview);
		
		RecognitionResult = new JLabel("Recognition Result");
		RecognitionResult.setBounds(455, 56, 95, 28);
		RecognitionResult.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(RecognitionResult);
		
		RecResult = new JTextPane();
		RecResult.setBounds(444, 96, 234, 28);
		getContentPane().add(RecResult);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(407, 96, 2, 520);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		getContentPane().add(separator_1);
		
		ManuallyInputPlate = new JTextField();
		ManuallyInputPlate.setBounds(504, 135, 95, 34);
		ManuallyInputPlate.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(ManuallyInputPlate);
		ManuallyInputPlate.setColumns(10);
		
		AddToTable = new JButton("Add to Table");
		AddToTable.setBounds(407, 180, 106, 28);
		AddToTable.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(AddToTable);
		
		Clear = new JButton("Clear Field");
		Clear.setBounds(523, 180, 106, 28);
		Clear.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Clear);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(427, 230, 381, 171);
		getContentPane().add(scrollPane);
		
		tblImage = new JTable();
		tblImage.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Date", "Time", "Plate", "Image Location"
			}
		));
		scrollPane.setViewportView(tblImage);
		
		path = new JTextField();
		path.setBounds(7, 512, 369, 34);
		path.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(path);
		path.setColumns(10);
		
		Browse = new JButton("Browse");
		Browse.setBounds(10, 557, 86, 28);
		Browse.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Browse);
		
		Execute = new JButton("Execute");
		Execute.setBounds(105, 557, 86, 28);
		Execute.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Execute);
		
		Export = new JButton("Export");
		Export.setBounds(201, 557, 86, 28);
		Export.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Export);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(427, 416, 381, 171);
		getContentPane().add(scrollPane_1);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Date ", "Time", "Plate", "Image Location"
			}
		));
		scrollPane_1.setViewportView(table);
		
		JButton Search = new JButton("Search");
		Search.setFont(new Font("Gabriola", Font.PLAIN, 16));
		Search.setBounds(639, 182, 106, 28);
		getContentPane().add(Search);
		
		JButton Email = new JButton("Email");
		Email.setFont(new Font("Gabriola", Font.PLAIN, 16));
		Email.setBounds(572, 588, 106, 28);
		getContentPane().add(Email);
		
		 Open = new JButton("Open Image");
		Open.setFont(new Font("Gabriola", Font.PLAIN, 16));
		Open.setBounds(297, 559, 100, 28);
		getContentPane().add(Open);
		
	pic = new JLabel();
		pic.setBounds(10, 126, 381, 366);
		getContentPane().add(pic);
	}
	
	public JTable gettblImage() {
		return tblImage;
	}
	
	public String getManuallyInputPlate(){
		
		return ManuallyInputPlate.getText();
	}
	
public void setManuallyInputPlate(){
		
		ManuallyInputPlate.setText("");
	}
	

public String getpath(){
	
	return path.getText();
}

public String getRecResult(){
	
	return RecResult.getText();
}

public void setRecResult(){
	
	RecResult.setText("");
}

//public void setpic(){
//	
//	Icon icon = null;
//	pic.insertIcon(icon);
//}


/*public String setpath(){
	String s = path.setText(Controller.);
	return s;
	
}*/
	/*public Object settblImage() {
		return tblImage;
	}
	*/
	
	
	
	//Button Listeners
	void addAddListener(ActionListener listenForAddButton){
		AddToTable.addActionListener(listenForAddButton); 
			}
	
	

	void addHomeListener(ActionListener listenForHomeButton){
		Home.addActionListener(listenForHomeButton); 
			}
	

	/*void addDatabaseListener(ActionListener listenForDatabaseButton){
		Database.addActionListener(listenForDatabaseButton); 
			}
	*/

	void addCameraListener(ActionListener listenForCameraButton){
		Camera.addActionListener(listenForCameraButton); 
			}
	
	void addAboutListener(ActionListener listenForAboutButton){
		About.addActionListener(listenForAboutButton); 
			}

	void addLogOutListener(ActionListener listenForLogOutButton){
		LogOut.addActionListener(listenForLogOutButton); 
			}
	
	void addBrowseListener(ActionListener listenForBrowseButton){
		Browse.addActionListener(listenForBrowseButton); 
			}
	
	void addClearListener(ActionListener listenForClearButton){
		Clear.addActionListener(listenForClearButton); 
			}
	
/*	void addDeleteListener(ActionListener listenForDeleteButton){
		Delete.addActionListener(listenForDeleteButton); 
			}*/
	
	/*void addHelpListener(ActionListener listenForHelpButton){
		Help.addActionListener(listenForHelpButton); 
			}
*/
	void addUserListener(ActionListener listenForUserButton){
		User.addActionListener(listenForUserButton); 
			}
	
	void addOpenListener(ActionListener listenForOpenButton){
		Open.addActionListener(listenForOpenButton); 
			}
	
	//error message
	public void displayErrorMessage(String errorMessage) {
		 JOptionPane.showMessageDialog(this, errorMessage);
}
}
