package projectpackage;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.toedter.calendar.JDateChooser;

public class ScheduleFrame {
	////
	// *** 메인 UI 관련 변수 선언 ***
	public JFrame frame;
	private JPanel panel; // 일반 사원용 판넬

	private DefaultTableModel model;//
	private JTable dbTable; // 셋이 하나의 테이블
	private JScrollPane jScollPane; //

	private JButton buttonClose; // 닫기버튼
	private JDateChooser dateChooserSearchStartDate; // 날짜 검색 : 시작날짜
	private JDateChooser dateChooserSearchEndDate; // 날짜 검색 : 마지막날짜
	private JLabel labelSearchName; // 이름 검색 라벨

	private JPanel panelEdit; // 관리자용 판넬
	private JComboBox comboBoxDept; // 테이블 부서 변경
	private JTextField textFieldEmpNo; // 사원번호
	private JTextField textFieldSchCode; // 일정코드
	private JLabel labelName; // 이름
	private JLabel labelDept; // 부서
	private JDateChooser dateChooserStartDate; // 날짜 입력 : 시작날짜
	private JDateChooser dateChooserEndDate; // 날짜 입력 :마지막날짜
	private JComboBox comboBoxContent; // 일정 사유
	private JButton buttonInsert; // 추가버튼
	private JButton buttonUpdate; // 수정버튼
	private JButton buttonDelete; // 삭제버튼

	private Statement stmt = MainStart.connectDataBase();// db연결
	private JTextField textFieldSearchName;

	// *** 상수 선언 ***

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScheduleFrame window = new ScheduleFrame();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ScheduleFrame() {
		initialize();
		select();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("일정");
		frame.setBounds(100, 100, 554, 719);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null); // 프레임 가운데서 띄우기
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 540, 690);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		model = new DefaultTableModel(new String[] { "부서", "이름", "시작날짜", "끝날짜", "내용", "사원번호", "일정코드" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 셀 편집 불가능 오버라이드
				return false;
			}
		};

		dbTable = new JTable(model);
//		dbTable.removeColumn(dbTable.getColumnModel().getColumn(5));//칼럼 삭제

		dbTable.getColumn("사원번호").setWidth(0);
		dbTable.getColumn("사원번호").setMinWidth(0);
		dbTable.getColumn("사원번호").setMaxWidth(0); // 셋이 셋트로 해야 컬럼 하나 숨겨짐

		dbTable.getColumn("일정코드").setWidth(0);
		dbTable.getColumn("일정코드").setMinWidth(0);
		dbTable.getColumn("일정코드").setMaxWidth(0);

		jScollPane = new JScrollPane(dbTable);
		dbTable.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지

		/// 테이블 내용 가운데 정렬하기
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = dbTable.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴

		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
//	       컬럼모델에서 컬럼의 갯수만큼 컬럼을 가져와 for문을 이용하여
//	       각각의 셀렌더러를 아까 생성한 dtcr에 set해줌
		}

		/// 테이블 체크박스 추가

		jScollPane.setBounds(70, 130, 400, 350);
		panel.add(jScollPane);
		dbTable.addMouseListener(new MyMouseListener());

		buttonClose = new JButton("닫기");
		buttonClose.setBounds(420, 600, 60, 30);
		panel.add(buttonClose);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		JButton buttonSearch = new JButton("검색");
		buttonSearch.setBounds(420, 70, 60, 30);
		panel.add(buttonSearch);
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				model.setRowCount(0);
				search();
			}
		});

		dateChooserSearchStartDate = new JDateChooser();
		dateChooserSearchStartDate.setDateFormatString("yyyy-MM-dd");
		dateChooserSearchStartDate.setBounds(30, 70, 120, 30);
		panel.add(dateChooserSearchStartDate);

		dateChooserSearchEndDate = new JDateChooser();
		dateChooserSearchEndDate.setDateFormatString("yyyy-MM-dd");
		dateChooserSearchEndDate.setBounds(170, 70, 120, 30);
		panel.add(dateChooserSearchEndDate);

		textFieldSearchName = new JTextField(10);
		textFieldSearchName.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldSearchName.setBounds(340, 70, 70, 30);
		panel.add(textFieldSearchName);

		labelSearchName = new JLabel("이름 : ");
		labelSearchName.setBounds(300, 70, 51, 30);
		panel.add(labelSearchName);

		panelEdit = new JPanel();
		panelEdit.setBounds(0, 0, 540, 680);
		panel.add(panelEdit);
		panelEdit.setLayout(null);
		panelEdit.setVisible(false);
		if (MainStart.man_code.equals("1")) { // 1번 코드일 때만 관리자 메뉴 보임
			panelEdit.setVisible(true);
		}

		String[] CBmenu = { "휴가", "출장", "외근", "반차" };
		comboBoxContent = new JComboBox(CBmenu);
		comboBoxContent.setBounds(311, 540, 68, 30);
		panelEdit.add(comboBoxContent);

		/////////////// 추가버튼
		buttonInsert = new JButton("추가");
		buttonInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert();
				model.setRowCount(0);
				select();

			}
		});

		buttonInsert.setBounds(50, 600, 60, 30);
		panelEdit.add(buttonInsert);

		///////////// 수정버튼
		buttonUpdate = new JButton("수정");
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update();
				model.setRowCount(0); /// 전체 테이블 화면을 지웠다가
				select(); /// 다시 셀렉을하면 변경된 값이 보인다

			}
		});
		buttonUpdate.setBounds(174, 600, 60, 30);
		panelEdit.add(buttonUpdate);

		////////////// 삭제버튼
		buttonDelete = new JButton("삭제");
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
				model.setRowCount(0);
				select();
			}
		});
		buttonDelete.setBounds(298, 600, 60, 30);
		panelEdit.add(buttonDelete);

		textFieldEmpNo = new JTextField();
		textFieldEmpNo.setText("사원번호");
		textFieldEmpNo.setBounds(50, 500, 80, 30);
		panelEdit.add(textFieldEmpNo);
		textFieldEmpNo.setColumns(10);
		textFieldEmpNo.setHorizontalAlignment(JTextField.CENTER);

		labelName = new JLabel("이름");
		labelName.setFont(new Font("돋움", Font.PLAIN, 12));
		labelName.setBounds(288, 500, 80, 30);
		panelEdit.add(labelName);

		labelDept = new JLabel("부서");
		labelDept.setFont(new Font("돋움", Font.PLAIN, 12));
		labelDept.setBounds(174, 500, 80, 30);
		panelEdit.add(labelDept);

		textFieldSchCode = new JTextField();
		textFieldSchCode.setText("일정코드");
		textFieldSchCode.setColumns(10);
		textFieldSchCode.setBounds(391, 541, 60, 30);
		textFieldSchCode.setVisible(false);
		panelEdit.add(textFieldSchCode);

		String[] CBMenuDept = { "전체", "영업부", "인사부", "기획부", "총무부", "개발부" };
		comboBoxDept = new JComboBox(CBMenuDept);
		comboBoxDept.setBounds(50, 30, 70, 30);
		panelEdit.add(comboBoxDept);

		dateChooserStartDate = new JDateChooser();
		dateChooserStartDate.setDateFormatString("yyyy-MM-dd");
		dateChooserStartDate.setBounds(30, 540, 120, 30);
		panelEdit.add(dateChooserStartDate);

		dateChooserEndDate = new JDateChooser();
		dateChooserEndDate.setDateFormatString("yyyy-MM-dd");
		dateChooserEndDate.setBounds(170, 540, 120, 30);
		panelEdit.add(dateChooserEndDate);

		comboBoxDept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				model.setRowCount(0);
				select();

			}
		});

	}

///테이블 뷰
	public void select() {
		// DEPT COMBOBOX Menu
		String deptCBM = (String) comboBoxDept.getSelectedItem();
		ResultSet result;
		// 부서 선택 쿼리
		String query = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND DEP.DEP_NAME ='" + deptCBM + "' ORDER BY 1,2,3 DESC";

		// 전체 부서 쿼리
		String query2 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE"
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE ORDER BY 1,2,3 DESC";

		// 디폴트 부서 쿼리 (자기자신부서)
		String query3 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE"
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND DEP.DEP_NAME ='" + MainStart.dep_name + "' ORDER BY 1,2,3 DESC";

		try {
			// 관리자가 아니라면 자기부서
			if (!MainStart.man_code.equals("1")) {
				result = stmt.executeQuery(query3);
			}
			// 전체부서 볼 때
			else if (deptCBM.equals("전체")) {
				result = stmt.executeQuery(query2);
			}
			// 특정 부서 볼 때
			else {
				result = stmt.executeQuery(query);
			}
			while (result.next()) {
				Object[] data = { result.getString(1), result.getString(2), result.getDate(3), result.getDate(4),
						result.getString(5), result.getString(6), result.getString(7) };
				model.addRow(data);
			}
		} catch (Exception e) {
		}
	}

///날짜 선택 테이블 뷰
	public void search() {

		String a = ((JTextField) dateChooserSearchStartDate.getDateEditor().getUiComponent()).getText();// get value JDateChooser																									
		String b = ((JTextField) dateChooserSearchEndDate.getDateEditor().getUiComponent()).getText();
		String c = textFieldSearchName.getText();
		String deptCBM = (String) comboBoxDept.getSelectedItem();
		ResultSet result;

		/// 날짜만 검색
		/// 부서 선택+날짜 검색
		String query = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND SCH.SCH_STARTDATE >= '" + a + "' AND SCH.SCH_ENDDATE <= '" + b + "' AND DEP.DEP_NAME ='"
				+ deptCBM + "' ORDER BY 1,2,3 DESC";

		/// 전체부서 + 날짜 검색
		String query2 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND SCH.SCH_STARTDATE >= '" + a + "' AND SCH.SCH_ENDDATE <= '" + b + "' ORDER BY 1,2,3 DESC";

		/// 일반사원용 날짜검색
		String query3 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND SCH.SCH_STARTDATE >= '" + a + "' AND SCH.SCH_ENDDATE <= '" + b + "' AND DEP.DEP_NAME ='"
				+ MainStart.dep_name + "' ORDER BY 1,2,3 DESC";

		// 이름만 검색
		// 이름입력 + 전체부서
		String query4 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND EMP.EMP_NAME = '" + c + "'" + " ORDER BY 1,2,3 DESC";

		// 이름입력 + 기본 자기부서
		String query6 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND EMP.EMP_NAME = '" + c + "'" + " AND DEP.DEP_NAME = '" + MainStart.dep_name + "'"
				+ " ORDER BY 1,2,3 DESC";

		// 이름 + 날짜 검색
		// 이름입력 + 날짜지정
		String query5 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND SCH.SCH_STARTDATE >= '" + a + "' AND SCH.SCH_ENDDATE <= '" + b + "'" + " AND EMP.EMP_NAME = '"
				+ c + "'" + " ORDER BY 1,2,3 DESC";

		// 기본 자기부서 + 이름입력 + 날짜지정
		String query7 = " SELECT DEP.DEP_NAME,EMP.EMP_NAME, SCH.SCH_STARTDATE,SCH.SCH_ENDDATE, SCH.SCH_CONTENT,ES.EMP_NO,ES.SCH_CODE "
				+ " FROM EMPLOYEE EMP ,DEPARTMENT DEP , BELONG_DEPARTMENT BD,"
				+ " EMPLOYEE_SCHEDULE ES, SCHEDULE SCH WHERE EMP.EMP_NO = BD.EMP_No"
				+ " AND BD.DEP_CODE = DEP.DEP_CODE AND EMP.EMP_NO = ES.EMP_NO AND ES.SCH_CODE = SCH.SCH_CODE"
				+ " AND SCH.SCH_STARTDATE >= '" + a + "' AND SCH.SCH_ENDDATE <= '" + b + "' AND DEP.DEP_NAME ='"
				+ MainStart.dep_name + "' " + " AND EMP.EMP_NAME = '" + c + "'" + " ORDER BY 1,2,3 DESC";

		try {
			//// 파싱해야함 중간에 - 혹은 / 넣으면 에러
			// 특정문자를 ""로 변환

			if (!a.isEmpty() && !b.isEmpty()) {// 날짜가 입력됬을때
				a = a.replaceAll("-", "");
				a = a.replaceAll("/", "");
				a = a.replaceAll("_", "");
				a = a.replaceAll("\\.", "");

				b = b.replaceAll("-", "");
				b = b.replaceAll("/", "");
				b = b.replaceAll("_", "");
				b = b.replaceAll("\\.", "");

				int x = Integer.parseInt(a); // 문자를 int로 바꿔서 비교가능하게만듬
				int y = Integer.parseInt(b); // 그리고 if문으로 enddate가 startdate보다 빠르면 에러발생
				if (x > y) {
					result = null;
					dialog();
				} else {
					if (c.isEmpty()) {// 날짜만 검색용
						if (!MainStart.man_code.equals("1")) {// 일반사원용
							result = stmt.executeQuery(query3);
						} else {
							if (deptCBM.equals("전체")) {
								result = stmt.executeQuery(query2); // 전체부서
							} else {
								result = stmt.executeQuery(query);
							} // 부서선택
						}
					} else {// 이름 + 날짜 검색용
						if (!MainStart.man_code.equals("1")) {// 일반사원용
							result = stmt.executeQuery(query7);
						} else {
							result = stmt.executeQuery(query5);
						}
					}
				}
			} else {// 이름만 검색할 때
				if (!MainStart.man_code.equals("1")) {// 일반사원용
					result = stmt.executeQuery(query6);
				} else {
					result = stmt.executeQuery(query4); // 전체부서
				}
			}
			
			while (result.next()) {					
				Object[] data = { result.getString(1), result.getString(2), result.getDate(3), result.getDate(4),
						result.getString(5), result.getString(6), result.getString(7) };
		model.addRow(data);
			}
			
			
		} catch (Exception e) {
			dialog();
			select();
		}

	}

//추가 버튼 메소드

	public void insert() {
		ResultSet result;
		String a = ((JTextField) dateChooserStartDate.getDateEditor().getUiComponent()).getText();
		String b = ((JTextField) dateChooserEndDate.getDateEditor().getUiComponent()).getText();
		String c = (String) comboBoxContent.getSelectedItem();
		String d = textFieldEmpNo.getText();
		String query = "INSERT INTO SCHEDULE VALUES( (select max(sch_code)+1 from SCHEDULE),sysdate,'" + a + "','" + b
				+ "','" + c + "')";
		String query2 = "INSERT INTO EMPLOYEE_SCHEDULE VALUES(" + d + ",(select max(sch_code) from SCHEDULE))";
		String queryCommit = "COMMIT";
		String allEmp_no = "SELECT EMP_NO FROM EMPLOYEE";
		boolean check = false;

		try {
			//// 파싱해야함 중간에 - 혹은 / 넣으면 에러
			// 특정문자를 ""로 변환
			a = a.replaceAll("-", "");
			a = a.replaceAll("/", "");
			a = a.replaceAll("_", "");
			a = a.replaceAll("\\.", "");

			b = b.replaceAll("-", "");
			b = b.replaceAll("/", "");
			b = b.replaceAll("_", "");
			b = b.replaceAll("\\.", "");

			int x = Integer.parseInt(a); // 문자를 int로 바꿔서 비교가능하게만듬
			int y = Integer.parseInt(b); // 그리고 if문으로 enddate가 startdate보다 빠르면 에러발생

			result = stmt.executeQuery(allEmp_no);
			while (result.next()) {
				if (d.equals(result.getString(1))) {
					check = true;
					break;
				}
			}
			if (a.isEmpty() || b.isEmpty() || check == false || x > y) {
				dialog();
			} else {
				stmt.executeUpdate(query);
				stmt.executeUpdate(query2);
				stmt.executeUpdate(queryCommit);
			}
		} catch (Exception e) {
			dialog();

		}
	}

// 삭제 버튼 메소드

	public void delete() {

		String a = textFieldSchCode.getText();
		String query = "DELETE FROM SCHEDULE WHERE SCH_CODE =" + a;
		String query2 = "DELETE FROM EMPLOYEE_SCHEDULE WHERE SCH_CODE =" + a;
		String queryCommit = "COMMIT";
		try {
			stmt.executeUpdate(query);
			stmt.executeUpdate(query2);
			stmt.executeUpdate(queryCommit);

		} catch (Exception e) {
			dialog();
		}
	}

//수정 버튼 메소드
	public void update() {
		String code = textFieldSchCode.getText();
		String start = ((JTextField) dateChooserStartDate.getDateEditor().getUiComponent()).getText();
		String end = ((JTextField) dateChooserEndDate.getDateEditor().getUiComponent()).getText();
		String content = (String) comboBoxContent.getSelectedItem();
		String queryCommit = "COMMIT";

		String query = "UPDATE SCHEDULE SET SCH_STARTDATE = '" + start + "',SCH_ENDDATE ='" + end + "',"
				+ "SCH_CONTENT = '" + content + "' " + " WHERE SCH_CODE =" + code;
		try {
			if (start.isEmpty() || end.isEmpty()) {
				dialog();
			}
			stmt.executeQuery(query);
			stmt.executeQuery(queryCommit);
		} catch (Exception e) {
			dialog();
		}
	}

//Table 마우스 리스너 메소드
	class MyMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			/// 디폴트로 클릭 했을때 마우스 이벤트가 발동하고
			// 밑에 if문에서 클릭당 이벤트를 넣어줄수도 있음
			// 왜인지 모르겟지만 메소드를 호출해서 각 클릭버튼에 넣을려고 하면 에러
			// mouseClicked 메소드 안에 직접 밑의 코드를 짜야 돌아감 왜지?

			int row = dbTable.getSelectedRow(); /// 해당 선택 로우
			int column = dbTable.getColumnCount(); // 컬럼 갯수 모두

//		String to Date
//		String from = "2013-04-08 10:10:10";
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date to = transFormat.parse(from);
//
//		Date to String
//		Date from = new Date();
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String to = transFormat.format(from);
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < column; i++) {
				if (i == 0) {
					labelDept.setText((String) model.getValueAt(row, 0));
				}
				if (i == 1) {
					labelName.setText((String) model.getValueAt(row, 1));
				}
				if (i == 2) {
					((JTextField) dateChooserStartDate.getDateEditor().getUiComponent())
							.setText(transFormat.format(model.getValueAt(row, 2)));
				}
				if (i == 3) {
					((JTextField) dateChooserEndDate.getDateEditor().getUiComponent())
							.setText(transFormat.format(model.getValueAt(row, 3)));
				}
				if (i == 4) {
					comboBoxContent.setSelectedItem((String) model.getValueAt(row, 4));
				} // 콤보박스라인
				if (i == 5) {
					textFieldEmpNo.setText((String) model.getValueAt(row, 5));
				}
				if (i == 6) {
					textFieldSchCode.setText((String) model.getValueAt(row, 6));
				}
			}

//			if (e.getButton() == 1) {
//			} // 클릭
//			if (e.getClickCount() == 2) {
//			} // 더블클릭
//			if (e.getButton() == 3) {
//			} // 오른쪽 클릭

		}

	}

//Dialog 메소드

	public void dialog() {

		// 부모 Frame을 frame로 하고, 이름을 "다시 입력하세요"
		Dialog dialog = new Dialog(frame, "다시 입력하세요");
		dialog.setSize(210, 120);
		dialog.setLocation(50, 50);
		dialog.setLayout(new FlowLayout());
		dialog.setLocationRelativeTo(null);
		JButton ok = new JButton("확인");
		JLabel jlabel = new JLabel("다시 확인하여 입력해주세요", JLabel.CENTER);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		dialog.add(jlabel);
		dialog.add(ok);

		dialog.setVisible(true);

	}
}
