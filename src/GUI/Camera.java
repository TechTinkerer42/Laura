package GUI;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class Camera extends JFrame{
	private JTextField textField;
	private JButton Home;
	private JButton Camera;
	private JButton LogOut;
	private JButton About;
	
	public Camera() {
		setTitle("ANPR");
		getContentPane().setLayout(null);
		
		Home = new JButton("Home");
		Home.setBounds(7, 7, 73, 26);
		Home.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Home);
		
		Camera = new JButton("Camera");
		Camera.setBounds(90, 7, 73, 26);
		Camera.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(Camera);
		
		About= new JButton("About");
		About.setBounds(247, 7, 63, 37);
		About.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(About);
		
		textField = new JTextField();
		textField.setBounds(699, 7, 109, 36);
		textField.setText("Signed in as -");
		textField.setFont(new Font("Gabriola", Font.PLAIN, 17));
		textField.setColumns(10);
		getContentPane().add(textField);
		
		LogOut = new JButton("Log out");
		LogOut.setBounds(737, 48, 71, 37);
		LogOut.setFont(new Font("Gabriola", Font.PLAIN, 16));
		getContentPane().add(LogOut);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(419, 89, 1, 499);
		separator.setOrientation(SwingConstants.VERTICAL);
		getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(7, 338, 801, 2);
		getContentPane().add(separator_1);
		
		JPanel panel = new JPanel();
		panel.setBounds(7, 89, 402, 238);
		getContentPane().add(panel);
	}
	
	void addHomeListener(ActionListener listenForHomeButton){
		Home.addActionListener(listenForHomeButton); 
			}
	
//
//	void addDatabaseListener(ActionListener listenForDatabaseButton){
//		Database.addActionListener(listenForDatabaseButton); 
//			}
	

	void addCameraListener(ActionListener listenForCameraButton){
		Camera.addActionListener(listenForCameraButton); 
			}

	void addLogOutListener(ActionListener listenForLogOutButton){
		LogOut.addActionListener(listenForLogOutButton); 
			}
	
	void addAboutListener(ActionListener listenForAboutButton){
		About.addActionListener(listenForAboutButton); 
			}
}
