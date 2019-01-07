package projectpackage;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Attendance extends JDialog {
	static JFrame tmpFrame;

	private JLabel lblNowDate;
	private JLabel lblDept;
	private JLabel lblPosi;
	private JLabel lblNm;
	private JPanel panel;
	private JButton btnManagement;
	private Statement stmt = MainStart.connectDataBase();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Attendance dialog = new Attendance(tmpFrame);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */

	public Attendance(JFrame frame) {
		super(frame, true);
		setTitle("출결");
		setBounds(100, 100, 549, 467);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblStart_1 = new JLabel("");
		lblStart_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblStart_1.setBounds(326, 104, 195, 21);
		getContentPane().add(lblStart_1);

		JLabel lblEnd_1 = new JLabel("");
		lblEnd_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblEnd_1.setBounds(326, 156, 195, 18);
		getContentPane().add(lblEnd_1);

		JButton btnStart = new JButton("출근 등록");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!lblStart_1.getText().equals(""))
					return;

				String am1 = null;
				GregorianCalendar cal = new GregorianCalendar();

				int second = cal.get(Calendar.SECOND);
				int minute = cal.get(Calendar.MINUTE);
				int hour = cal.get(Calendar.HOUR);
				int am = cal.get(Calendar.AM_PM);

				if (hour == 0) {
					hour = hour + 12;
				}
				if (am == 1) {
					am1 = "오후";
				}

				else if (am == 0) {
					am1 = "오전";
				}

				lblStart_1.setText(am1 + "  " + hour + "시" + minute + "분" + second + "초");

				employeeInsert();

			}
		});

		btnStart.setBounds(45, 285, 97, 43);
		getContentPane().add(btnStart);

		JButton btnEnd = new JButton("퇴근 등록");
		btnEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (lblStart_1.getText().equals(""))
					return;

				String am1 = null;
				GregorianCalendar cal = new GregorianCalendar();

				int second = cal.get(Calendar.SECOND);
				int minute = cal.get(Calendar.MINUTE);
				int hour = cal.get(Calendar.HOUR);
				int am = cal.get(Calendar.AM_PM);

				if (hour == 0) {
					hour = hour + 12;
				}
				if (am == 1) {
					am1 = "오후";
				}

				else if (am == 0) {
					am1 = "오전";
				}

				lblEnd_1.setText(am1 + "  " + hour + "시" + minute + "분" + second + "초");
				update();

			}

		});
		btnEnd.setBounds(210, 285, 97, 43);
		getContentPane().add(btnEnd);

		JButton buttonClose = new JButton("닫기");
		buttonClose.setBounds(381, 376, 97, 23);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Attendance.this.dispose();
			}
		});
		getContentPane().add(buttonClose);

		JButton btnLog = new JButton("출결 로그");
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AttendanceLog window = new AttendanceLog(Attendance.this);
				window.setVisible(true);
			}
		});
		btnLog.setBounds(381, 285, 97, 43);
		getContentPane().add(btnLog);

		lblNowDate = new JLabel("현재 날짜");
		lblNowDate.setFont(new Font("굴림", Font.BOLD, 24));
		lblNowDate.setBounds(228, 39, 223, 29);
		getContentPane().add(lblNowDate);

		JLabel lblStart = new JLabel("출근시간");
		lblStart.setFont(new Font("굴림", Font.BOLD, 16));
		lblStart.setBounds(228, 108, 79, 15);
		getContentPane().add(lblStart);

		JLabel lblEnd = new JLabel("퇴근시간");
		lblEnd.setFont(new Font("굴림", Font.BOLD, 16));
		lblEnd.setBounds(228, 158, 79, 15);
		getContentPane().add(lblEnd);

		lblDept = new JLabel("New label");
		lblDept.setForeground(Color.BLACK);
		lblDept.setBounds(45, 110, 57, 15);
		getContentPane().add(lblDept);
		lblDept.setText("1");

		lblPosi = new JLabel("New label");
		lblPosi.setBounds(45, 146, 57, 15);
		getContentPane().add(lblPosi);
		lblPosi.setText("2");

		lblNm = new JLabel("New label");
		lblNm.setBounds(45, 179, 57, 15);
		getContentPane().add(lblNm);
		lblNm.setText("3");

		lblStart_1.setText("");

		clock();
		setEmployeeInfo();

	}

	private void setEmployeeInfo() {
		lblDept.setText(MainStart.dep_name);
		lblPosi.setText(MainStart.pos_name);
		lblNm.setText(MainStart.emp_name);
		setEmployeeImage();

	}

	private boolean setEmployeeImage() {
		Image img;
		File f = new File(MainStart.emp_image);
		if (!f.exists()) {
			return false;
		}

		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage(MainStart.emp_image);

		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		panel.setBounds(45, 24, 70, 76);
		getContentPane().add(panel);

		return true;
	}

	public void employeeInsert() {

		String query = "INSERT INTO COMMUTE VALUES(sysdate," + MainStart.emp_no
				+ ",sysdate, to_date('01/01/01','RR/MM/DD'))";

		try {
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e);

		}

	}

	public void update() {
		String query = "UPDATE COMMUTE SET COM_ENDDATE = sysdate " + "WHERE EMP_NO = " + MainStart.emp_no;
		try {
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e);

		}

	}

	public void clock() {
		Thread clock = new Thread() {
			public void run() {
				try {
					for (;;) {
						Calendar cal = new GregorianCalendar();
						int day = cal.get(Calendar.DAY_OF_MONTH);
						int month = cal.get(Calendar.MONTH) + 1;
						int year = cal.get(Calendar.YEAR);

						lblNowDate.setText(year + "년 " + month + "월 " + day + "일");

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
}
