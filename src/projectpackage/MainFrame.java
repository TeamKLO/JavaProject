package projectpackage;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;

public class MainFrame {

	public JFrame frmMain;
	
	private JLabel lblDepartment;
	private JLabel lblPosition;
	private JLabel lblName;
	private JPanel panel;
	private JButton btnManagement;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
		
		setEmployeeInfo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMain = new JFrame();
		frmMain.setTitle("메인");
		frmMain.setBounds(100, 100, 450, 300);
		frmMain.setLocationRelativeTo(null);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.getContentPane().setLayout(null);
		
		JButton btnApproval = new JButton("결재");
		btnApproval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApprovalFrame window = new ApprovalFrame(frmMain);
				window.setVisible(true);
			}
		});
		btnApproval.setBounds(152, 51, 97, 23);
		frmMain.getContentPane().add(btnApproval);
		
		JButton btnCommute = new JButton("출결");
		btnCommute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Attendance window = new Attendance(frmMain);
				window.setVisible(true);
			}
		});
		btnCommute.setBounds(290, 51, 97, 23);
		frmMain.getContentPane().add(btnCommute);
		
		JButton btnSchedule = new JButton("일정");
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ScheduleFrame window = new ScheduleFrame();
				window.frame.setVisible(true);
			}
		});
		btnSchedule.setBounds(152, 111, 97, 23);
		frmMain.getContentPane().add(btnSchedule);
		
		JButton btnMessenger = new JButton("메신저");
		btnMessenger.setVisible(false);
		btnMessenger.setBounds(290, 111, 97, 23);
		frmMain.getContentPane().add(btnMessenger);
		
		btnManagement = new JButton("관리");
		btnManagement.setVisible(false);
		btnManagement.setBounds(152, 172, 97, 23);
		frmMain.getContentPane().add(btnManagement);
		
		JButton btnClose = new JButton("닫기");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmMain.dispose();
			}
		});
		btnClose.setBounds(290, 172, 97, 23);
		frmMain.getContentPane().add(btnClose);
		
		lblDepartment = new JLabel("New label");
		lblDepartment.setForeground(Color.BLACK);
		lblDepartment.setBounds(40, 119, 57, 15);
		frmMain.getContentPane().add(lblDepartment);
		lblDepartment.setText("1");
		
		lblPosition = new JLabel("New label");
		lblPosition.setBounds(40, 148, 57, 15);
		frmMain.getContentPane().add(lblPosition);
		lblPosition.setText("2");
		
		lblName = new JLabel("New label");
		lblName.setBounds(40, 180, 57, 15);
		frmMain.getContentPane().add(lblName);
		lblName.setText("3");
		
	}
	
	private void setEmployeeInfo() {
		lblDepartment.setText(MainStart.dep_name);
		lblPosition.setText(MainStart.pos_name);
		lblName.setText(MainStart.emp_name);
		setEmployeeImage();
		
		if (MainStart.man_code.equals("1")) {
			btnManagement.setVisible(true);
		}
	}
	
	private boolean setEmployeeImage() {
		Image img;
		File f = new File(MainStart.emp_image);
		if (!f.exists()) {
			return false;
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage(MainStart.emp_image);
		
		panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		panel.setBounds(27, 22, 70, 76);
		frmMain.getContentPane().add(panel);
		
		return true;
	}
}
