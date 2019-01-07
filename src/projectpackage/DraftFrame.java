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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DraftFrame extends JDialog {
	// static JFrame 필드는 단지 main의 EventQueue안의 생성자를 만족시키기 위한 것일 뿐, 프로그램 실행과 상관없음
	private static JFrame tmpFrame;

	private Window ownerWindow;
	private ApprovalFrame ownerFrame;
	private JTextField txtTitle;
	private JComboBox cbxCategory;
	private JTextArea txtContent;
	private JComboBox cbxConfirmer;
	private JButton btnDraft;
	private JButton btnClose;

	private Statement stmt;

	// JComboBox cbxConfirmer와 보조를 맞춰 사원번호를 저장할 String Array
	// 콤보박스에는 사원의 이름으로 드러내 보여주고, 사원번호는 안 보이지만 DB에 쿼리를 던질 때는 사원번호가 필요
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
		// 부모가 되는 JDialog의 owner는 frame, modal은 Dialog.ModalityType.APPLICATION_MODAL 기안
		// 화면은 모달로 동작
		super(frame, Dialog.ModalityType.APPLICATION_MODAL);
		// DraftFrame은 ApprovalFrame으로 돌아갈 때 ApprovalFrame의 테이블을 수정해야함
		// ownerWindow field를 ApprovalFrame으로 형변환
		ownerWindow = ownerFrame;
		ownerWindow = frame;
		ownerFrame = (ApprovalFrame) ownerWindow;
		setTitle("기안 작성");
		setBounds(100, 100, 450, 400);
		// frame이 생성될 때 위치는 frame의 중앙
		setLocationRelativeTo(frame);
		// frame이 close 될 때의 설정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		cbxCategory = new JComboBox();
		cbxCategory.setModel(new DefaultComboBoxModel(new String[] { "일정", "기획", "구매" }));
		cbxCategory.setSelectedIndex(0);
		cbxCategory.setBounds(79, 28, 130, 21);
		getContentPane().add(cbxCategory);

		txtTitle = new JTextField();
		txtTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnDraft.doClick();
				}
			}
		});
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
		txtContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnDraft.doClick();
				}
			}
		});
		txtContent.setBounds(79, 90, 343, 146);
		getContentPane().add(txtContent);

		JLabel label_3 = new JLabel("결제상사");
		label_3.setBounds(12, 252, 57, 15);
		getContentPane().add(label_3);

		cbxConfirmer = new JComboBox();
		cbxConfirmer.setBounds(79, 249, 130, 21);
		getContentPane().add(cbxConfirmer);

		btnDraft = new JButton("기안 올리기");
		btnDraft.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnDraft.doClick();
				}
			}
		});
		btnDraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 기안을 함
				sendDraft();
			}
		});
		btnDraft.setBounds(79, 307, 130, 23);
		getContentPane().add(btnDraft);

		btnClose = new JButton("닫기");
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
				// 기안 화면을 닫고, 
				DraftFrame.this.dispose();
				ownerFrame.setTable();
			}
		});
		btnClose.setBounds(292, 307, 130, 23);
		getContentPane().add(btnClose);

		// 화면에 나타나는 값 초기화
		initDisplay();
	}

	void initDisplay() {
		// 데이터베이스 연결은 필요에 따라 어디에든 붙일 수 있다.
		stmt = MainStart.connectDataBase();
		// JComboBox 값 초기화
		initComboBox();
	}

	void initComboBox() {
		// 결재유형은 정해져 있으므로 0으로 초기화
		cbxCategory.setSelectedIndex(0);
		// 결재자를 DB에서 가져옴
		setCbxConfirmer();
	}

	// 결재자를 DB에서 가져옴
	void setCbxConfirmer() {
		// 쿼리의 레코드 수를 가져오는 함수가 아무래도 없으니 쿼리를 두 개 던짐
		// 결과적인 레코드 수를 가져옴
		String cQuery = "select count(*) "
				+ "from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e "
				+ "where a.emp_no = b.emp_no and b.pos_code = c.pos_code and "
				+ "a.emp_no = d.emp_no and d.dep_code = e.dep_code " + "and c.pos_code > " + MainStart.pos_code
				+ " and e.dep_code = " + MainStart.dep_code;

		// 자신이 올린 기안의 결재 권한을 가진 사원의 사원 번호와 사원 이름을 가져옴
		String query = "select a.emp_no, a.emp_name "
				+ "from EMPLOYEE a, EMPLOYEE_POSITION b, POSITION c, BELONG_DEPARTMENT d, DEPARTMENT e "
				+ "where a.emp_no = b.emp_no and b.pos_code = c.pos_code and "
				+ "a.emp_no = d.emp_no and d.dep_code = e.dep_code " + "and c.pos_code > " + MainStart.pos_code
				+ " and e.dep_code = " + MainStart.dep_code;

		int rowCount = 0;

		try {

			ResultSet result = this.stmt.executeQuery(cQuery);
			result.next();
			
			// 레코드 갯수를 가져와서 0이라면 함수 탈출
			rowCount = result.getInt(1);			
			if (rowCount <= 0) {
				return;			
			}

			// 사원번호를 넣는 필드의 배열 생성
			confirmerNo = new String[rowCount];
			result = this.stmt.executeQuery(query);
			int index = 0;
			// 가져온 레코드 수 만큼 사원번호와 이름을 각각 배열과 콤보박스에 넣음
			while (result.next()) {
				confirmerNo[index] = result.getString(1);
				cbxConfirmer.insertItemAt(result.getString(2), index);
				index++;
			}
			// 콤보박스의 인덱스 초기화
			cbxConfirmer.setSelectedIndex(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 기안을 함
	void sendDraft() {

		// 기안은 결재를 처리할 상사가 있고, 기안 제목이 있고, 기안 내용이 있는 경우에만 할 수 있음
		if (cbxConfirmer.getSelectedIndex() >= 0 && txtTitle.getText().trim().length() > 0
				&& txtContent.getText().trim().length() > 0) {
			try {
				// 통제된 자료이므로 사용할 수 있는 방법
				// DB의 cat_code와 콤보박스의 인덱스에 1을 더한 것이 같음
				Integer tmpInt = new Integer(cbxCategory.getSelectedIndex() + 1);

				// 결재 테이블에 기안을 insert
				// MainStart의 getNewMaxCode 메소드는 사용해서 결재코드를 생성
				// 결재권한을 가진 상급자의 사원번호는 콤보박스와 연동되는 confirmerNo 배열을 통해 알 수 있음
				String query = "insert into APPROVAL (app_code, emp_no, cat_code, app_title, "
						+ "app_content, app_draftdate, app_confirmdate, app_confirmer, app_isconfirm) " + "values("
						+ MainStart.getNewMaxCode(this.stmt, "APPROVAL", "app_code") + ", " + MainStart.emp_no + ", "
						+ tmpInt.toString() + ", '" + txtTitle.getText() + "'" + ", '" + txtContent.getText()
						+ "', sysdate, null" + ", " + confirmerNo[cbxConfirmer.getSelectedIndex()] + ", '대기')";			

				this.stmt.executeQuery(query);
				JOptionPane.showMessageDialog(this, "기안을 올렸습니다");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(this, "기안을 올릴 수 없습니다");
		}
	}
}
