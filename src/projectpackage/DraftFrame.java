package projectpackage;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DraftFrame extends JDialog {
	private static JFrame tmpFrame;
	private Window parentWindow;
	private ApprovalFrame parentFrame;
	private JTextField txtTitle;
	private JComboBox cbxCategory;
	private JTextArea txtContent;
	private JComboBox cbxConfirmer;

	private Statement stmt;	
	
	private String[] confirmerNo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DraftFrame dialog = new DraftFrame(tmpFrame);
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
	public DraftFrame(Window frame) {
		super(frame, Dialog.ModalityType.APPLICATION_MODAL);
		parentWindow = parentFrame;
		parentWindow = frame;
		parentFrame = (ApprovalFrame)parentWindow;
		setTitle("기안 작성");
		setBounds(100, 100, 450, 400);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		cbxCategory = new JComboBox();
		cbxCategory.setModel(new DefaultComboBoxModel(new String[] { "일정", "기획", "구매" }));
		cbxCategory.setSelectedIndex(0);
		cbxCategory.setBounds(79, 28, 130, 21);
		getContentPane().add(cbxCategory);

		txtTitle = new JTextField();
		txtTitle.setBounds(79, 59, 343, 21);
		getContentPane().add(txtTitle);
		txtTitle.setColumns(10);

		JLabel label = new JLabel("기안제목");
		label.setBounds(12, 62, 57, 15);
		getContentPane().add(label);

		JLabel label_1 = new JLabel("기안유형");
		label_1.setBounds(12, 31, 57, 15);
		getContentPane().add(label_1);

		JLabel label_2 = new JLabel("기안내용");
		label_2.setBounds(12, 90, 57, 15);
		getContentPane().add(label_2);

		txtContent = new JTextArea();
		txtContent.setBounds(79, 90, 343, 146);
		getContentPane().add(txtContent);

		JLabel label_3 = new JLabel("결제상사");
		label_3.setBounds(12, 252, 57, 15);
		getContentPane().add(label_3);

		cbxConfirmer = new JComboBox();
		cbxConfirmer.setBounds(79, 249, 130, 21);
		getContentPane().add(cbxConfirmer);

		JButton btnDraft = new JButton("기안 올리기");
		btnDraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendDraft();				
			}
		});
		btnDraft.setBounds(79, 307, 130, 23);
		getContentPane().add(btnDraft);

		JButton btnClose = new JButton("닫기");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				parentWindow.
				DraftFrame.this.dispose();
				parentFrame.setTable();
			}
		});
		btnClose.setBounds(292, 307, 130, 23);
		getContentPane().add(btnClose);

		setDisplay();
	}

	void setDisplay() {
		// 데이터베이스 연결은 필요에 따라 어디에든 붙일 수 있다.
		stmt = MainStart.connectDataBase();		
		setComboBox();
	}

	void setComboBox() {
		cbxCategory.setSelectedIndex(0);
		setCbxConfirmer();
	}

	void setCbxConfirmer() {
		// 쿼리의 레코드 수를 가져오는 함수가 아무래도 없다. 할 수 없이 쿼리를 두 개 던지자.
		String cQuery = "select count(*) " + 
				"from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e " + 
				"where a.emp_no = b.emp_no and b.pos_code = c.pos_code and " +
				"a.emp_no = d.emp_no and d.dep_code = e.dep_code " + 
				"and c.pos_code > " + MainStart.pos_code + 
				" and e.dep_code = " + MainStart.dep_code;
		
		String query = "select a.emp_no, a.emp_name " + 
				"from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e " + 
				"where a.emp_no = b.emp_no and b.pos_code = c.pos_code and " +
				"a.emp_no = d.emp_no and d.dep_code = e.dep_code " + 
				"and c.pos_code > " + MainStart.pos_code + 
				" and e.dep_code = " + MainStart.dep_code;
		
//		String cQuery = "select count(*) " + 
//				"from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e " + 
//				"where a.emp_no = b.emp_no and b.pos_code = c.pos_code and " +
//				"a.emp_no = d.emp_no and d.dep_code = e.dep_code " + 
//				"and c.pos_code > 2 " + 
//				" and e.dep_code = 1";
//		
//		String query = "select a.emp_no, a.emp_name " + 
//				"from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e " + 
//				"where a.emp_no = b.emp_no and b.pos_code = c.pos_code and " +
//				"a.emp_no = d.emp_no and d.dep_code = e.dep_code " + 
//				"and c.pos_code > 2 " + 
//				" and e.dep_code = 1";
		
		int rowCount = 0;		

		try {
			 
			ResultSet result = this.stmt.executeQuery(cQuery);
			result.next();
			rowCount = result.getInt(1);
			if (rowCount <= 0) return;
			
//			result.last();
//			rowCount = result.getRow();
//			result.first();
			
			confirmerNo = new String[rowCount];
			result = this.stmt.executeQuery(query);			
			int index = 0;
			while (result.next()) {
				confirmerNo[index] = result.getString(1);
				cbxConfirmer.insertItemAt(result.getString(2), index);
				index++;
			}
			cbxConfirmer.setSelectedIndex(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void sendDraft() {
					   
		if (cbxConfirmer.getSelectedIndex() >= 0 && txtTitle.getText().trim().length() > 0 && txtContent.getText().trim().length() > 0) {
			try {
				Integer tmpInt = new Integer(cbxCategory.getSelectedIndex() + 1);

				String query = "insert into APPROVAL (app_code, emp_no, cat_code, app_title, "
						+ "app_content, app_draftdate, app_confirmdate, app_confirmer, app_isconfirm) " + "values("
						+ MainStart.getNewMaxCode(this.stmt, "APPROVAL", "app_code") + ", " + MainStart.emp_no
						+ ", " + tmpInt.toString() + ", '" + txtTitle.getText() + "'" + ", '" + txtContent.getText()
						+ "', sysdate, null" + ", " + confirmerNo[cbxConfirmer.getSelectedIndex()] + ", '대기')";

//			String query = "insert into APPROVAL (app_code, emp_no, cat_code, app_title, " +
//				   	   "app_content, app_draftdate, app_confirmdate, app_confirmer, app_isconfirm) " +
//				       "values(" + ProjectControl.getNewMaxCode(this.stmt, "APPROVAL", "app_code") + 
//				       ", 10002" + 
//				       ", " + tmpInt.toString() + 
//				       ", '" + txtTitle.getText() + "'" + 
//				       ", '" + txtContent.getText() + "', sysdate, null" +
//				       ", " + confirmerNo[cbxConfirmer.getSelectedIndex()]  + ", '대기')";

				this.stmt.executeQuery(query);
				JOptionPane.showMessageDialog(this, "기안을 올렸습니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			JOptionPane.showMessageDialog(this, "기안을 올릴 수 없습니다.");
		}
	}
}
