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
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.event.MouseMotionAdapter;

public class Attendance extends JDialog {
	static JFrame tmpFrame;

	private JLabel lblNowDate;
	private JLabel lblDept;
	private JLabel lblPosi;
	private JLabel lblNm;
	private JPanel panel;
	private JButton btnManagement;
	private Statement stmt = MainStart.connectDataBase();
	int xx, xy;

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
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - xx, y - xy);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		setTitle("출결");
		setBounds(100, 100, 661, 571);
		setLocationRelativeTo(frame);
		this.setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblStart_1 = new JLabel("");
		lblStart_1.setForeground(Color.WHITE);
		lblStart_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblStart_1.setBounds(211, 277, 195, 21);
		getContentPane().add(lblStart_1);

		JLabel lblEnd_1 = new JLabel("");
		lblEnd_1.setForeground(Color.WHITE);
		lblEnd_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblEnd_1.setBounds(211, 326, 195, 18);
		getContentPane().add(lblEnd_1);

		JButton btnStart = new JButton("출근 등록");
		btnStart.setBackground(new Color(255, 255, 224));
		btnStart.setFont(new Font("굴림체", Font.BOLD, 12));
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

		btnStart.setBounds(78, 410, 110, 30);
		getContentPane().add(btnStart);

		JButton btnEnd = new JButton("퇴근 등록");
		btnEnd.setBackground(new Color(255, 255, 224));
		btnEnd.setFont(new Font("굴림체", Font.BOLD, 12));
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
		btnEnd.setBounds(269, 410, 110, 30);
		getContentPane().add(btnEnd);

		JButton buttonClose = new JButton("닫 기");
		buttonClose.setBackground(new Color(255, 255, 224));
		buttonClose.setFont(new Font("굴림체", Font.BOLD, 12));
		buttonClose.setBounds(457, 495, 110, 30);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Attendance.this.dispose();
			}
		});
		getContentPane().add(buttonClose);

		JButton btnLog = new JButton("출결 로그");
		btnLog.setBackground(new Color(255, 255, 224));
		btnLog.setFont(new Font("굴림체", Font.BOLD, 12));
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AttendanceLog window = new AttendanceLog(Attendance.this);
//				window.setBounds(100, 100, 0, 0);
				window.setVisible(true);
			}
		});
		btnLog.setBounds(457, 410, 110, 30);
		getContentPane().add(btnLog);
	
//		JLabel lblCloseX = new JLabel("X");
//		lblCloseX.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				Attendance.this.dispose();
//			}
//		});
//		lblCloseX.setHorizontalAlignment(SwingConstants.CENTER);
//		lblCloseX.setForeground(Color.WHITE);
//		lblCloseX.setFont(new Font("Dialog", Font.BOLD, 18));
//		lblCloseX.setBounds(622, 8, 42, 20);
//		getContentPane().add(lblCloseX);

		lblNowDate = new JLabel("현재 날짜");
		lblNowDate.setForeground(Color.WHITE);
		lblNowDate.setFont(new Font("굴림", Font.BOLD, 24));
		lblNowDate.setBounds(78, 212, 223, 29);
		getContentPane().add(lblNowDate);

		JLabel lblStart = new JLabel("출근시간");
		lblStart.setForeground(Color.WHITE);
		lblStart.setFont(new Font("굴림", Font.BOLD, 16));
		lblStart.setBounds(78, 277, 79, 15);
		getContentPane().add(lblStart);

		JLabel lblEnd = new JLabel("퇴근시간");
		lblEnd.setForeground(Color.WHITE);
		lblEnd.setFont(new Font("굴림", Font.BOLD, 16));
		lblEnd.setBounds(78, 326, 79, 15);
		getContentPane().add(lblEnd);

		lblDept = new JLabel("New label");
		lblDept.setForeground(Color.WHITE);
		lblDept.setBounds(219, 85, 80, 20);
		getContentPane().add(lblDept);
		lblDept.setText("1");

		lblPosi = new JLabel("New label");
		lblPosi.setForeground(Color.WHITE);
		lblPosi.setBounds(219, 115, 80, 20);
		getContentPane().add(lblPosi);
		lblPosi.setText("2");

		lblNm = new JLabel("New label");
		lblNm.setForeground(Color.WHITE);
		lblNm.setBounds(219, 145, 80, 20);
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
		panel.setBounds(78, 41, 119, 124);
		getContentPane().add(panel);
		
		JLabel lblBackImg = new JLabel("lblBackImg");
		lblBackImg.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 661, 571);
		getContentPane().add(lblBackImg);
		
		

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
