package projectpackage;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseMotionAdapter;

public class AttendanceLog extends JDialog {
	static JFrame tmpFrame;

	private JDateChooser dateChooserSearchStartDate;
	private JDateChooser dateChooserSearchEndDate;
	private JComboBox comboBoxDept;
	private Statement stmt = MainStart.connectDataBase();
	// 테이블
	private DefaultTableModel model;//
	private JTable table; // 사원 목록 테이블
	int xx, xy;

	String[] colTitle = { "작성날짜", "사원이름", "출근시간", "퇴근시간" };

	DefaultTableModel defaultTableModel = new DefaultTableModel(colTitle, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AttendanceLog dialog = new AttendanceLog(tmpFrame);
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
	public AttendanceLog(Window frame) {
		super(frame, Dialog.ModalityType.APPLICATION_MODAL);
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
		setTitle("출결 로그");
//		setBounds(588, 300, 614, 500);
//		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
	

		dateChooserSearchStartDate = new JDateChooser();
		dateChooserSearchStartDate.setDateFormatString("yyyy-MM-dd");
		dateChooserSearchStartDate.setBounds(48, 117, 172, 36);
		getContentPane().add(dateChooserSearchStartDate);

		dateChooserSearchEndDate = new JDateChooser();
		dateChooserSearchEndDate.setDateFormatString("yyyy-MM-dd");
		dateChooserSearchEndDate.setBounds(274, 117, 172, 36);
		getContentPane().add(dateChooserSearchEndDate);

		comboBoxDept = new JComboBox();
		comboBoxDept.setBackground(new Color(255, 255, 224));
		comboBoxDept.setFont(new Font("굴림체", Font.BOLD, 12));
		comboBoxDept.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {

					model.setRowCount(0);
					employeeSelect();
				}
			}
		});
		comboBoxDept.setModel(new DefaultComboBoxModel(new String[] { "전체", "영업부", "인사부", "기획부", "총무부", "개발부" }));
		comboBoxDept.setSelectedIndex(0);
		comboBoxDept.setBounds(48, 58, 172, 36);
		getContentPane().add(comboBoxDept);
		comboBoxDept.setVisible(false);

		//// 테이블
		String[] tableColumn = { "작성날짜", "부서이름", "사원이름", "출근시간", "퇴근시간" };
		model = new DefaultTableModel(tableColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 셀 편집 불가능 오버라이드
				return false;
			}
		};
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(48, 190, 564, 347);

		table.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //가로스크롤

		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) { //// 테이블 가운데 정렬
			tcm.getColumn(i).setCellRenderer(dtcr);
		}

		getContentPane().add(scrollPane);
		

		JButton buttonClose = new JButton("닫 기");
		buttonClose.setBackground(new Color(255, 255, 224));
		buttonClose.setFont(new Font("굴림체", Font.BOLD, 12));
		buttonClose.setBounds(507, 629, 110, 30);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttendanceLog.this.dispose();
			}
		});
		getContentPane().add(buttonClose);
		
//		JLabel lblCloseX = new JLabel("X");
//		lblCloseX.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				AttendanceLog.this.dispose();
//			}
//		});
//		lblCloseX.setHorizontalAlignment(SwingConstants.CENTER);
//		lblCloseX.setForeground(Color.WHITE);
//		lblCloseX.setFont(new Font("Dialog", Font.BOLD, 18));
//		lblCloseX.setBounds(622, 8, 42, 20);
//		getContentPane().add(lblCloseX);

		JLabel lblNewLabel = new JLabel("~");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("굴림체", Font.BOLD, 40));
		lblNewLabel.setBounds(237, 123, 61, 21);
		getContentPane().add(lblNewLabel);

		JButton button = new JButton("검색");
		button.setBackground(new Color(255, 255, 224));
		button.setFont(new Font("굴림체", Font.BOLD, 12));
		button.setForeground(Color.BLACK);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setRowCount(0);
				employeeSelect();
			}
		});
		button.setBounds(481, 117, 127, 36);
		getContentPane().add(button);
		
		JLabel lblBackImg = new JLabel("lblBackImg");
		lblBackImg.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 661, 700);
		getContentPane().add(lblBackImg);
		
		

		scheduleSetDateChooser();
		model.setRowCount(0);
		employeeSelect();

		if (MainStart.man_code.equals("1")) { // 1번 코드일 때만 관리자 메뉴 보임
			comboBoxDept.setVisible(true);
//			label_1.setVisible(true);
//			setBounds(100, 100, 580, 458);
		}
		setBounds(100, 100, 661,700);
		this.setUndecorated(true);
		setLocationRelativeTo(frame);
	}

	/// 테이블 뷰 메소드
	public void employeeSelect() {

		String deptCBM = (String) comboBoxDept.getSelectedItem();
		String a = "to_date('" + ((JTextField) dateChooserSearchStartDate.getDateEditor().getUiComponent()).getText()
					+ " 00:00', 'yyyy-mm-dd hh24:mi') ";
		
	
		String b = "to_date('" + ((JTextField) dateChooserSearchEndDate.getDateEditor().getUiComponent()).getText()
					+ " 23:59', 'yyyy-mm-dd hh24:mi') ";

		ResultSet result;

		String query = "SELECT c.COM_DATE, d.DEP_NAME, e.EMP_NAME, c.COM_STARTDATE, c.COM_ENDDATE"
				+ " FROM COMMUTE c, EMPLOYEE e, DEPARTMENT d, BELONG_DEPARTMENT bd" 
				+ " WHERE e.EMP_NO = c.emp_no"
				+ " AND d.DEP_CODE = bd.DEP_CODE AND e.EMP_NO = bd.EMP_NO" 
				+ " AND c.COM_STARTDATE >= " + a + " ";

		
		
				String[] addString = new String[3];
		if (!MainStart.man_code.equals("1")) {
			addString[0] = " AND d.DEP_NAME = '" + MainStart.dep_name + "'";
		} else {
			addString[0] = "";
		}

		if (!b.isEmpty()) {
			addString[1] = " AND c.COM_ENDDATE <= " + b + " ";
		} else {
			addString[1] = "";
		}

		if (comboBoxDept.getSelectedIndex() > 0) {
			addString[2] = " AND d.DEP_NAME = '" + comboBoxDept.getSelectedItem() + "'";
		} else {
			addString[2] = "";
		}

		query = query + addString[0] + addString[1] + addString[2] + " ORDER BY 1 DESC,2,3";

		try {
			result = stmt.executeQuery(query);
			while (result.next()) {

				Object[] data = { result.getDate(1), result.getString(2), result.getString(3),
						result.getString(4).substring(10), result.getString(5).substring(10) };
				model.addRow(data);

			}
		} catch (Exception e) {
			System.out.println("");
		}

	}

	public void scheduleSetDateChooser() {

		Calendar calS = Calendar.getInstance();
		calS.setTime(new Date());
		calS.add(Calendar.DATE, -14);
		dateChooserSearchStartDate.setDate(calS.getTime());
		
		Calendar calE = Calendar.getInstance();
		calE.setTime(new Date());
		calE.add(Calendar.DATE, 0);
		dateChooserSearchEndDate.setDate(calE.getTime());

	}
}
