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

// 로그인 화면
public class LoginFrame {
	// 로그인 화면에서 사용하는 JFrame 필드. 다른 클래스에서도 사용할 수있도록 public
	public JFrame frmLogin;

	private JTextField txtId;
	private JPasswordField pwfPw;
	private JLabel lblLoginMessage;
	private JButton btnLogin;

	private Statement stmt = MainStart.connectDataBase();

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
		frmLogin.setTitle("로그인");
		frmLogin.setBounds(100, 100, 300, 220);
		// frame이 생성될 때 위치는 모니터의 중앙
		frmLogin.setLocationRelativeTo(null);
		// frame이 close 될 때의 설정
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel lblId = new JLabel("사번");
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblId.setBounds(12, 36, 57, 15);
		frmLogin.getContentPane().add(lblId);

		JLabel lblPw = new JLabel("암호");
		lblPw.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPw.setBounds(12, 74, 57, 15);
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
		txtId.setBounds(91, 33, 181, 21);
		frmLogin.getContentPane().add(txtId);
		txtId.setColumns(10);

		JButton btnClose = new JButton("닫기");
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
		btnClose.setBounds(175, 117, 97, 23);
		frmLogin.getContentPane().add(btnClose);

		btnLogin = new JButton("로그인");
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
		btnLogin.setBounds(38, 117, 97, 23);
		frmLogin.getContentPane().add(btnLogin);

		lblLoginMessage = new JLabel("로그인 해주세요");
		lblLoginMessage.setHorizontalAlignment(SwingConstants.LEFT);
		lblLoginMessage.setBounds(38, 150, 206, 15);
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
		pwfPw.setBounds(91, 71, 181, 21);
		frmLogin.getContentPane().add(pwfPw);
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
