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
	private JPanel panelimg;
	private JButton btnManagement;
	private JButton btnLogin;
	
	Image img;
	

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
		
		// 메인 화면에 사원정보를 보여주는 메소드
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
				// 결재 화면을 생성
				ApprovalFrame window = new ApprovalFrame(frmMain);
				window.setVisible(true);
			}
		});
		btnApproval.setBounds(152, 51, 97, 23);
		frmMain.getContentPane().add(btnApproval);
		
		JButton btnCommute = new JButton("출결");
		btnCommute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 출결 화면을 생성
				Attendance window = new Attendance();
				window.frame.setVisible(true);
			}
		});
		btnCommute.setBounds(290, 51, 97, 23);
		frmMain.getContentPane().add(btnCommute);
		
		JButton btnSchedule = new JButton("일정");
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 일정 화면을 생성
				ScheduleFrame window = new ScheduleFrame();
				window.frame.setVisible(true);
			}
		});
		btnSchedule.setBounds(152, 111, 97, 23);
		frmMain.getContentPane().add(btnSchedule);
		
		JButton btnMessenger = new JButton("메신저");
		btnMessenger.setVisible(false);
		btnMessenger.setBounds(152, 229, 97, 23);
		frmMain.getContentPane().add(btnMessenger);
		
		btnManagement = new JButton("관리");
		btnManagement.setVisible(false);
		btnManagement.setBounds(152, 172, 97, 23);
		frmMain.getContentPane().add(btnManagement);
		
		btnLogin = new JButton("로그아웃");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 로그아웃 버튼 클릭시 로그인 화면을 세팅한 후 보여주고 메인 화면은 감춤
				MainStart.classLogin.setIdPassword("", "", "로그인을 해주세요");
				MainStart.classLogin.frmLogin.setVisible(true);				
				frmMain.setVisible(false);
			}
		});
		btnLogin.setBounds(290, 111, 97, 23);
		frmMain.getContentPane().add(btnLogin);
		
		JButton btnClose = new JButton("닫기");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 닫기 버튼 클릭시 프로그램 종료
				System.exit(0);
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
		
		panelimg = new JPanel() {
			// 이미지 파일을 가져오기 위해 paint 메소드를 오버라이드
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		panelimg.setBounds(27, 22, 70, 76);
		frmMain.getContentPane().add(panelimg);		
	}
	
	// 메인 화면에 사원정보를 보여주는 메소드
	public void setEmployeeInfo() {
		lblDepartment.setText(MainStart.dep_name);
		lblPosition.setText(MainStart.pos_name);
		lblName.setText(MainStart.emp_name);
		// 사원 이미지를 보여주는 메소드
		setEmployeeImage();
		
		// 매니저 코드가 1인 경우 관리 버튼을 보여줌
		if (MainStart.man_code.equals("1")) {
			btnManagement.setVisible(true);
		}
	}
	
	// 사원 이미지를 보여주는 메소드
	private boolean setEmployeeImage() {		
		File f = new File(MainStart.emp_image);
		if (!f.exists()) {
			return false;
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage(MainStart.emp_image);		

		panelimg.repaint();
		
		return true;
	}
}
