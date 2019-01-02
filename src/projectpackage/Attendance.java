package projectpackage;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Color;

public class Attendance {
	
	private JLabel labelCal;

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Attendance window = new Attendance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
		
	public void clock()
	{
		Thread clock = new Thread()
				{
			public void run()
			{
				try {
					for(;;) {
					Calendar cal = new GregorianCalendar();
					int day = cal.get(Calendar.DAY_OF_MONTH);
					int month = cal.get(Calendar.MONTH);
					int year = cal.get(Calendar.YEAR);
					
					int second = cal.get(Calendar.SECOND);
					int minute = cal.get(Calendar.MINUTE);
					int hour = cal.get(Calendar.HOUR);
					
					labelCal.setText("Time "+hour+":"+minute+":"+second+"    Date "+year+"/"+month+"/"+day);
					
					sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				};
				clock.start();
		
		

		
	}
	

	/**
	 * Create the application.
	 */
	public Attendance() {
		initialize();
		clock();
		frame.setLocationRelativeTo(null);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(150, 150, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 462);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		labelCal = new JLabel("Clock");
		labelCal.setFont(new Font("돋움", Font.BOLD, 24));
		labelCal.setBounds(25, 33, 397, 57);
		panel.add(labelCal);
		
				
		JLabel labelStart = new JLabel("\uCD9C\uADFC\uC2DC\uAC04 :");
		labelStart.setFont(new Font("돋움", Font.PLAIN, 15));
		labelStart.setBounds(161, 117, 80, 40);
		panel.add(labelStart);
		
		JLabel labelEnd = new JLabel("\uD1F4\uADFC\uC2DC\uAC04 :");
		labelEnd.setFont(new Font("돋움", Font.PLAIN, 15));
		labelEnd.setBounds(161, 176, 80, 40);
		panel.add(labelEnd);
		
		JLabel labelStratView = new JLabel("New label");
		labelStratView.setFont(new Font("돋움", Font.PLAIN, 15));
		labelStratView.setBounds(294, 117, 100, 40);
		panel.add(labelStratView);
		
		JLabel labelEndView = new JLabel("New label");
		labelEndView.setFont(new Font("돋움", Font.PLAIN, 15));
		labelEndView.setBounds(294, 176, 100, 40);
		panel.add(labelEndView);
		
		JButton buttonStart = new JButton("\uCD9C\uADFC");
		buttonStart.setBounds(37, 362, 100, 50);
		panel.add(buttonStart);
		
		JButton buttonEnd = new JButton("\uD1F4\uADFC");
		buttonEnd.setBounds(167, 362, 100, 50);
		panel.add(buttonEnd);
		
		JButton buttonClose = new JButton("닫기");			
		buttonClose.setBounds(294, 362, 100, 50);
		panel.add(buttonClose);
		
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		
		JButton buttonLog = new JButton("\uCD9C\uACB0\uB85C\uADF8");
		buttonLog.setBounds(294, 274, 100, 50);
		panel.add(buttonLog);
		
		buttonLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AttendanceLog window = new AttendanceLog();
				window.setVisible(true);
			}
		});
	}
}


