package projectpackage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class AttendanceLog extends JDialog {

	private JDateChooser dtcSDate;
	private JDateChooser dtcEDate;
	private JComboBox cbxCategory;
	private Statement stmt;

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
					AttendanceLog dialog = new AttendanceLog();
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
	public AttendanceLog() {
		setBounds(100, 100, 602, 458);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);

		dtcSDate = new JDateChooser();
		dtcSDate.setDateFormatString("yyyy-MM-dd");
		dtcSDate.setBounds(12, 10, 130, 21);
		getContentPane().add(dtcSDate);

		dtcEDate = new JDateChooser();
		dtcEDate.setDateFormatString("yyyy-MM-dd");
		dtcEDate.setBounds(166, 10, 130, 21);
		getContentPane().add(dtcEDate);

		cbxCategory = new JComboBox();
		cbxCategory.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
//					setTable();
				}
			}
		});
		cbxCategory.setModel(new DefaultComboBoxModel(new String[] { "전체", "영업부", "인사부", "기획부", "총무부", "개발부" }));
		cbxCategory.setSelectedIndex(0);
		cbxCategory.setBounds(12, 55, 130, 21);
		getContentPane().add(cbxCategory);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 107, 562, 158);
		getContentPane().add(scrollPane);

		JButton buttonClose = new JButton("닫기");
		buttonClose.setBounds(444, 373, 130, 23);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttendanceLog.this.dispose();
			}
		});
		getContentPane().add(buttonClose);

		
		setTable();
		
	}
	void setDisplay() {
	
		stmt = MainStart.connectDataBase();
		setTable();
	}



	void setTable() {
		String query = "select * from COMMUTE"; 
					   
					   
}
}	
