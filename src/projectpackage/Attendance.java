package projectpackage;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Calendar;
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
	private Statement stmt;

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

	public void clock() {
		Thread clock = new Thread() {
			public void run() {
				try {
					for (;;) {
						Calendar cal = new GregorianCalendar();
						int day = cal.get(Calendar.DAY_OF_MONTH);
						int month = cal.get(Calendar.MONTH) + 1;
						int year = cal.get(Calendar.YEAR);

//						int second = cal.get(Calendar.SECOND);
//						int minute = cal.get(Calendar.MINUTE);
//						int hour = cal.get(Calendar.HOUR);

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

	/**
	 * Create the dialog.
	 */
<<<<<<< HEAD
	public Attendance(JFrame frame) {
		setBounds(100, 100, 549, 467);
		getContentPane().setLayout(null);
		setLocationRelativeTo(frame);

		JLabel lblStart_1 = new JLabel("");
		lblStart_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblStart_1.setBounds(326, 104, 152, 21);
		getContentPane().add(lblStart_1);

		JLabel lblEnd_1 = new JLabel("");
		lblEnd_1.setFont(new Font("굴림", Font.BOLD, 16));
		lblEnd_1.setBounds(326, 156, 152, 18);
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

=======
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
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
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
>>>>>>> branch 'master' of https://github.com/TeamKLO/JavaProject.git
			}
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnStart.setBounds(45, 285, 97, 43);
		getContentPane().add(btnStart);

		JButton btnEnd = new JButton("퇴근 등록");
		btnEnd.addMouseListener(new MouseAdapter() {
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

				lblEnd_1.setText(am1 + "  " + hour + "시" + minute + "분" + second + "초");

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
				AttendanceLog window = new AttendanceLog();
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

}
