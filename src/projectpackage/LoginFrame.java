package projectpackage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame {

	public JFrame frmLogin;
	private JTextField txtId;
	private JTextField txtPw;
	private JLabel lblLoginMessage;

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("로그인");
		frmLogin.setBounds(100, 100, 300, 220);
		frmLogin.setLocationRelativeTo(null);
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
		txtId.setText("10002");
		txtId.setBounds(91, 33, 181, 21);
		frmLogin.getContentPane().add(txtId);
		txtId.setColumns(10);

		JButton btnClose = new JButton("닫기");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLogin.dispose();
			}
		});
		btnClose.setBounds(175, 117, 97, 23);
		frmLogin.getContentPane().add(btnClose);

		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isLogin()) {

					frmLogin.dispose();
					MainFrame window = new MainFrame();
					window.frmMain.setVisible(true);
				}

			}
		});
		btnLogin.setBounds(38, 117, 97, 23);
		frmLogin.getContentPane().add(btnLogin);

		txtPw = new JTextField();
		txtPw.setText("1234");
		txtPw.setColumns(10);
		txtPw.setBounds(91, 71, 181, 21);
		frmLogin.getContentPane().add(txtPw);

		lblLoginMessage = new JLabel("로그인 해주세요");
		lblLoginMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginMessage.setBounds(38, 150, 206, 15);
		frmLogin.getContentPane().add(lblLoginMessage);
	}

	private boolean isLogin() {
		boolean retValue = false;
		String query = "select a.emp_no, emp_password, emp_name, emp_image, " 
				+ "c.dep_code, c.dep_name, "
				+ "e.man_code, g.pos_code, g.pos_name " 
				+ "from EMPLOYEE a, BELONG_DEPARTMENT b, DEPARTMENT c, "
				+ "MANAGER d, MANAGEMENT_LIST e, " + "EMPLOYEE_POSITION f, POSITION g "
				+ "where a.emp_no = b.emp_no and b.dep_code = c.dep_code and "
				+ "a.emp_no = d.emp_no and d.man_code = e.man_code and "
				+ "a.emp_no = f.emp_no and f.pos_code = g.pos_code";

		try {
			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				if (txtId.getText().equals(result.getString(1)) && txtPw.getText().equals(result.getString(2))) {
					MainStart.emp_no = result.getString(1);
					MainStart.emp_name = result.getString(3);
					MainStart.emp_image = result.getString(4);
					MainStart.dep_code = result.getString(5);
					MainStart.dep_name = result.getString(6);
					MainStart.man_code = result.getString(7);
					MainStart.pos_code = result.getString(8);
					MainStart.pos_name = result.getString(9);

					retValue = true;
					break;
				}
			}

			if (retValue) {
				lblLoginMessage.setText("로그인 성공");
			} else {
				lblLoginMessage.setText("사번이나 비번이 틀렸습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retValue;
	}

}
