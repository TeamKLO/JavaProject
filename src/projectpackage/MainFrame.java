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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;

// 메인 화면
public class MainFrame {
	// 메인 화면에서 사용하는 JFrame 필드. 다른 클래스에서도 사용할 수있도록 public
	public JFrame frmMain;

	private JLabel lblDepartment;
	private JLabel lblPosition;
	private JLabel lblName;
	private JPanel panelimg;
	private JButton btnManagement;
	private JButton btnLogin;
	private JButton btnApproval;
	int xx,xy;

	// 사원 이미지 파일을 대입할 필드
	Image img;
	private JLabel lblBackImg;

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
		// frame 초기 설정
		initialize();

		// 메인 화면에 사원정보를 보여줌
		setEmployeeInfo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	// frame 초기 설정
	private void initialize() {
		frmMain = new JFrame();
		
		frmMain.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		frmMain.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				frmMain.setLocation(x - xx, y - xy);
			}
		});
		frmMain.setBackground(Color.WHITE);
		frmMain.setTitle("메인");
		frmMain.setBounds(100, 100, 661, 571);
		// frame이 생성될 때 위치는 모니터의 중앙
		frmMain.setLocationRelativeTo(null);
		// frame이 close 될 때의 설정
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.getContentPane().setLayout(null);
		frmMain.setUndecorated(true);

		btnApproval = new JButton("결 재");
		btnApproval.setForeground(Color.BLACK);
		btnApproval.setBackground(new Color(255, 255, 224));
		btnApproval.setFont(new Font("굴림체", Font.BOLD, 12));
		btnApproval.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnApproval.doClick();
				}
			}
		});
		btnApproval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 결재 화면을 생성
				// modal의 owner로서 JFrame을 argument로 넘김
				ApprovalFrame window = new ApprovalFrame(frmMain);
				window.setVisible(true);
			}
		});
		btnApproval.setBounds(93, 309, 110, 30);
		frmMain.getContentPane().add(btnApproval);

		JButton btnCommute = new JButton("출 결");
		btnCommute.setForeground(Color.BLACK);
		btnCommute.setBackground(new Color(255, 255, 224));
		btnCommute.setFont(new Font("굴림체", Font.BOLD, 12));
		btnCommute.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// 출결 화면을 생성

				Attendance window = new Attendance(frmMain);

				window.setVisible(true);


			}
		});
		btnCommute.setBounds(457, 309, 110, 30);
		frmMain.getContentPane().add(btnCommute);

		JButton btnSchedule = new JButton("일 정");
		btnSchedule.setForeground(Color.BLACK);
		btnSchedule.setBackground(new Color(255, 255, 224));
		btnSchedule.setFont(new Font("굴림체", Font.BOLD, 12));
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 일정 화면을 생성
				ScheduleFrame window = new ScheduleFrame();
				window.frame.setVisible(true);
			}
		});
		btnSchedule.setBounds(275, 309, 110, 30);
		frmMain.getContentPane().add(btnSchedule);

		JButton btnMessenger = new JButton("메신저");
		btnMessenger.setForeground(Color.BLACK);
		btnMessenger.setBackground(new Color(255, 255, 224));
		btnMessenger.setFont(new Font("굴림체", Font.BOLD, 12));
		btnMessenger.setVisible(false);
		btnMessenger.setBounds(275, 405, 110, 30);
		frmMain.getContentPane().add(btnMessenger);

		btnManagement = new JButton("관 리");
		btnManagement.setForeground(Color.BLACK);
		btnManagement.setBackground(new Color(255, 255, 224));
		btnManagement.setFont(new Font("굴림체", Font.BOLD, 12));
		btnManagement.setVisible(false);
		btnManagement.setBounds(93, 405, 110, 30);
		btnManagement.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EmployeeFrame employeeFrame = new EmployeeFrame();
				employeeFrame.setVisible(true);
				
			}
		});
		frmMain.getContentPane().add(btnManagement);

		btnLogin = new JButton("로그아웃");
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setBackground(new Color(255, 255, 224));
		btnLogin.setFont(new Font("굴림체", Font.BOLD, 12));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 로그아웃 버튼 클릭시 로그인 화면을 세팅한 후 보여주고 메인 화면은 감춤
				MainStart.classLogin.setIdPassword("", "", "로그인을 해주세요");
				MainStart.classLogin.frmLogin.setVisible(true);
				frmMain.setVisible(false);
			}
		});
		btnLogin.setBounds(457, 405, 110, 30);
		frmMain.getContentPane().add(btnLogin);

		JButton btnClose = new JButton("닫 기");
		btnClose.setForeground(Color.BLACK);
		btnClose.setBackground(new Color(255, 255, 224));
		btnClose.setFont(new Font("굴림체", Font.BOLD, 12));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 닫기 버튼 클릭시 프로그램 종료
				System.exit(0);
			}
		});
		btnClose.setBounds(457, 495, 110, 30);
		frmMain.getContentPane().add(btnClose);
		
//		JLabel lblCloseX = new JLabel("X");
//		lblCloseX.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				System.exit(0);
//			}
//		});
//		
//		lblCloseX.setFont(new Font("Euphemia", Font.BOLD, 18));
//		lblCloseX.setForeground(Color.WHITE);
//		lblCloseX.setHorizontalAlignment(SwingConstants.CENTER);
//		lblCloseX.setBounds(622, 8, 42, 20);
//		frmMain.getContentPane().add(lblCloseX);

		lblDepartment = new JLabel("New label");
		lblDepartment.setFont(new Font("굴림체", Font.BOLD, 15));
		lblDepartment.setForeground(Color.WHITE);
		lblDepartment.setBounds(234, 85, 80, 20);
		frmMain.getContentPane().add(lblDepartment);
		lblDepartment.setText("1");

		lblPosition = new JLabel("New label");
		lblPosition.setForeground(Color.WHITE);
		lblPosition.setFont(new Font("굴림체", Font.BOLD, 15));
		lblPosition.setBounds(234, 115, 80, 20);
		frmMain.getContentPane().add(lblPosition);
		lblPosition.setText("2");

		lblName = new JLabel("New label");
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("굴림체", Font.BOLD, 15));
		lblName.setBounds(234, 145, 80, 20);
		frmMain.getContentPane().add(lblName);
		lblName.setText("3");

		panelimg = new JPanel() {
			// 이미지 파일을 가져오기 위해 paint 메소드를 오버라이드
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		panelimg.setBounds(93, 41, 119, 124);
		frmMain.getContentPane().add(panelimg);
		
		lblBackImg = new JLabel("lblBackImg");
		lblBackImg.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 661, 571);
		frmMain.getContentPane().add(lblBackImg);
		
		
		
		
		
		
		
		
	}

	// 메인 화면에 사원정보를 보여줌
	public void setEmployeeInfo() {
		lblDepartment.setText(MainStart.dep_name);
		lblPosition.setText(MainStart.pos_name);
		lblName.setText(MainStart.emp_name);
		// 사원 이미지를 보여줌
		setEmployeeImage();

		// 매니저 코드가 1인 경우 관리 버튼을 보여줌
		if (MainStart.man_code.equals("1")) {
			btnManagement.setVisible(true);
		} else {
			btnManagement.setVisible(false);
		}
	}

	// 사원 이미지를 보여줌
	private boolean setEmployeeImage() {
		// 노파심에 적어보는 emp_image가 null인 경우
		if (MainStart.emp_image == null) {
			return false;
		}
		
		// 파일이 존재하는가
		File f = new File(MainStart.emp_image);
		if (!f.exists()) {
			return false;
		}

		// Toolkit을 이용 Image를 얻음
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage(MainStart.emp_image);

		// 이미지를 표현할 JPanel의 repaint메소드를 실행
		panelimg.repaint();

		return true;
	}
}
