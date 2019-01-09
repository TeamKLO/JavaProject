package projectpackage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

// 로그인 화면
public class LoginFrame {
	// 로그인 화면에서 사용하는 JFrame 필드. 다른 클래스에서도 사용할 수있도록 public
	public JFrame frmLogin;

	private JTextField txtId;
	private JPasswordField pwfPw;
	private JLabel lblLoginMessage;
	private JButton btnLogin;

	private Statement stmt = MainStart.connectDataBase();
	int xx,xy;
	private JLabel lblBackImg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame window = new LoginFrame();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginFrame() {
		// frame 초기 설정
		initialize();

		// 로그인 정보 세팅
		setIdPassword("10002", "1234", "로그인을 해주세요");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	// frame 초기 설정
	private void initialize() {
		frmLogin = new JFrame();
		
		frmLogin.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		frmLogin.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				frmLogin.setLocation(x - xx, y - xy);
			}
		});
		frmLogin.getContentPane().setBackground(new Color(173, 216, 230));
		frmLogin.setBackground(Color.WHITE);
		frmLogin.setTitle("로그인");
		frmLogin.setBounds(100, 100, 381, 471);
		// frame이 생성될 때 위치는 모니터의 중앙
		frmLogin.setLocationRelativeTo(null);
		frmLogin.setUndecorated(true);
		
		// frame이 close 될 때의 설정
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel lblId = new JLabel("사원번호");
		lblId.setFont(new Font("굴림체", Font.BOLD, 15));
		lblId.setForeground(Color.WHITE);
		lblId.setHorizontalAlignment(SwingConstants.LEFT);
		lblId.setBounds(87, 217, 72, 20);
		frmLogin.getContentPane().add(lblId);

		JLabel lblPw = new JLabel("패스워드");
		lblPw.setFont(new Font("굴림체", Font.BOLD, 15));
		lblPw.setForeground(Color.WHITE);
		lblPw.setHorizontalAlignment(SwingConstants.LEFT);
		lblPw.setBounds(87, 283, 72, 20);
		frmLogin.getContentPane().add(lblPw);

		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		txtId.setBounds(171, 213, 134, 30);
		frmLogin.getContentPane().add(txtId);
		txtId.setColumns(10);

		JButton btnClose = new JButton("닫기");
		btnClose.setFont(new Font("굴림체", Font.BOLD, 12));
		btnClose.setForeground(Color.BLACK);
		btnClose.setBackground(new Color(255, 255, 224));
		btnClose.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnClose.doClick();
				}
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 닫기 버튼을 클릭시 프로그램을 완전히 종료
				System.exit(0);
			}
		});
		btnClose.setBounds(218, 350, 110, 30);
		frmLogin.getContentPane().add(btnClose);

		btnLogin = new JButton("로그인");
		btnLogin.setFont(new Font("굴림체", Font.BOLD, 12));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setBackground(new Color(255, 255, 224));
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 로그인 버튼을 클릭하면 메소드를 사용해서 결과를 얻음
				if (isLogin()) {
					// 정상적인 로그인의 경우 메인 화면인 MainFrame 클래스의 인스턴스가 있는지 판별
					if (MainStart.classMain != null) {
						// 이미 메인 화면이 생성되었다면 새로 생성하지 않고 사원정보만 바꿈
						MainStart.classMain.setEmployeeInfo();
					} else {
						// 메인 화면이 없다면 새로 생성
						MainStart.classMain = new MainFrame();
					}

					// 메인 화면은 보이고, 로그인 화면은 감춤
					MainStart.classMain.frmMain.setVisible(true);
					frmLogin.setVisible(false);
				}

			}
		});
		btnLogin.setBounds(55, 350, 110, 30);
		frmLogin.getContentPane().add(btnLogin);

		lblLoginMessage = new JLabel("로그인 해주세요");
		lblLoginMessage.setFont(new Font("굴림체", Font.BOLD, 18));
		lblLoginMessage.setForeground(new Color(255, 255, 255));
		lblLoginMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginMessage.setBounds(55, 398, 276, 43);
		frmLogin.getContentPane().add(lblLoginMessage);

		pwfPw = new JPasswordField();
		pwfPw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		pwfPw.setBounds(171, 279, 134, 30);
		frmLogin.getContentPane().add(pwfPw);
		
		JLabel lblCloseX = new JLabel("X");
		lblCloseX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		lblCloseX.setFont(new Font("Euphemia", Font.BOLD, 18));
		lblCloseX.setForeground(Color.WHITE);
		lblCloseX.setHorizontalAlignment(SwingConstants.CENTER);
		lblCloseX.setBounds(344, 7, 43, 20);
		frmLogin.getContentPane().add(lblCloseX);
		
		lblBackImg = new JLabel("BackImg");
		lblBackImg.setIcon(new ImageIcon("C:\\Users\\KITRI\\git\\JavaProject\\JavaProject\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 381, 471);
		frmLogin.getContentPane().add(lblBackImg);
	}

	// 로그인 정보 세팅
	public void setIdPassword(String strId, String strPw, String strLoginMessage) {
		this.txtId.setText(strId);
		this.pwfPw.setText(strPw);
		this.lblLoginMessage.setText(strLoginMessage);
	}

	// 로그인이 정상인지 아닌지 판별
	private boolean isLogin() {
		boolean retValue = false;
		String strId = txtId.getText();
		// JPasswordField에서 password를 String으로 얻어서 처리
		String strPw = new String(pwfPw.getPassword());

		// ID 혹은 PW가 공백이라면 볼 것도 없음
		if (strId.equals("") || strPw.equals("")) {
//			JOptionPane.showMessageDialog(frmLogin, "사번이나 비번이 틀렸습니다");
			lblLoginMessage.setText("사번이나 비번이 틀렸습니다");
			return retValue;
		}

		// ID와 PW에 해당하는 사원의 정보를 가져오는 쿼리
		String query = "select a.emp_no, emp_password, emp_name, emp_image, " + "c.dep_code, c.dep_name, "
				+ "e.man_code, g.pos_code, g.pos_name " + "from EMPLOYEE a, BELONG_DEPARTMENT b, DEPARTMENT c, "
				+ "MANAGER d, MANAGEMENT_LIST e, " + "EMPLOYEE_POSITION f, POSITION g "
				+ "where a.emp_no = b.emp_no and b.dep_code = c.dep_code and "
				+ "a.emp_no = d.emp_no and d.man_code = e.man_code and "
				+ "a.emp_no = f.emp_no and f.pos_code = g.pos_code " + " and a.emp_no = " + txtId.getText()
				+ " and emp_password = '" + strPw + "'";

		try {
			ResultSet result = stmt.executeQuery(query);

			// 결과값이 있다면 정상 로그인
			// MainStart의 정적변수에 사원 정보를 넣음
			while (result.next()) {
				MainStart.emp_no = result.getString(1);
				MainStart.emp_name = result.getString(3);
				MainStart.emp_image = result.getString(4);
				MainStart.dep_code = result.getString(5);
				MainStart.dep_name = result.getString(6);
				MainStart.man_code = result.getString(7);
				MainStart.pos_code = result.getString(8);
				MainStart.pos_name = result.getString(9);

				retValue = true;
			}

			if (retValue) {
//				JOptionPane.showMessageDialog(frmLogin, "로그인 했습니다");
				lblLoginMessage.setText("로그인 했습니다");
			} else {
//				JOptionPane.showMessageDialog(frmLogin, "사번이나 비번이 틀렸습니다");
				lblLoginMessage.setText("사번이나 비번이 틀렸습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retValue;
	}
}
