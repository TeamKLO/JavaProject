package projectpackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import projectpackage.employeeFrame.employeeTableMouseListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DepartmentFrame extends JDialog {
	private JTextField textFieldDeptname;
	private JTextField textFieldDeptquota;
	
	private DefaultTableModel model;//
	private JTable table; 			// 테이블
	private JScrollPane jScollPane; //
	
	JButton insertButton; 
	JButton deleteButton;
	JButton closeButton;
	JLabel labelDeptname;
	JLabel labelDeptquota;
	private JLabel labelDepcode;
	
	private Statement stmt = MainStart.connectDataBase();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DepartmentFrame dialog = new DepartmentFrame();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DepartmentFrame() {
		setBounds(100, 100, 320, 300);
		getContentPane().setLayout(null);	
		setLocationRelativeTo(null);
		setTitle("부서등록");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		///테이블
		String[] tableColumn = {"부서번호","부서이름","정원"};
		model = new DefaultTableModel(tableColumn,0){			
			@Override
			public boolean isCellEditable(int row, int column) {  //셀 편집 불가능 오버라이드
				return false;
			}
		};
		table = new JTable(model);		
		jScollPane = new JScrollPane (table);
		jScollPane.setBounds(25, 35, 250, 110);		
	
		table.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지
	
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) { ////테이블 가운데 정렬		
			tcm.getColumn(i).setCellRenderer(dtcr);
			}		
		getContentPane().add(jScollPane);
		table.getColumn("부서번호").setWidth(0); 
		table.getColumn("부서번호").setMinWidth(0);
		table.getColumn("부서번호").setMaxWidth(0);	
		
		
		///버튼
		insertButton = new JButton("등록");
		insertButton.setBounds(32, 213, 60, 30);
		getContentPane().add(insertButton);
		
		deleteButton = new JButton("삭제");
		deleteButton.setBounds(120, 213, 60, 30);
		getContentPane().add(deleteButton);
		
		closeButton = new JButton("닫기");
		closeButton.setBounds(219, 213, 60, 30);
		getContentPane().add(closeButton);
		
		//입력필드
		labelDeptname = new JLabel("부서명 :");
		labelDeptname.setBounds(20, 180, 50, 15);
		getContentPane().add(labelDeptname);
		
		labelDeptquota = new JLabel("부서정원 :");
		labelDeptquota.setBounds(155, 180, 60, 15);
		getContentPane().add(labelDeptquota);		
		
		textFieldDeptquota = new JTextField();
		textFieldDeptquota.setColumns(10);
		textFieldDeptquota.setBounds(220, 173, 50, 30);
		getContentPane().add(textFieldDeptquota);
		
		textFieldDeptname = new JTextField();
		textFieldDeptname.setBounds(75, 173, 70, 30);
		getContentPane().add(textFieldDeptname);
		textFieldDeptname.setColumns(10);
		
		labelDepcode = new JLabel("");
		labelDepcode.setBounds(25, 10, 57, 15);
		getContentPane().add(labelDepcode);
		labelDepcode.setVisible(false);
		
		
		insertButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				departmentInsert();
				model.setRowCount(0);
				departmentSelect();
				
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				departmentDelete();
				model.setRowCount(0);
				departmentSelect();
				
			}
		});
		
		closeButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					labelDepcode.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					textFieldDeptname.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					textFieldDeptquota.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				}
			}
		});
		
		
		departmentSelect();
		
		
	}
	
	
	///테이블 뷰 메소드
		public void departmentSelect() {
			
			ResultSet result;
			String query = "SELECT dep_code,DEP_NAME,DEP_QUOTA FROM DEPARTMENT ORDER BY DEP_CODE";
			
			try {			
				result = stmt.executeQuery(query);		
				while (result.next()) {
					Object[] data = { result.getString(1),result.getString(2),result.getString(3)};
					model.addRow(data);
				}
			}catch(Exception e) {
				
			}		
			
		}
		
		//부서 추가 메소드
		public void departmentInsert() {
			ResultSet result;	
			String depcode = "(select max(dep_code)+1 from department)";
			String first = "INSERT INTO DEPARTMENT VALUES(0,'없음',999)"; //처음 아무부서도 없을때
			String checkQuery = "select dep_code from department";		
			String depname =  textFieldDeptname.getText();
			String depquota = textFieldDeptquota.getText();
			String commit = "COMMIT";
			String query = "INSERT INTO DEPARTMENT VALUES("
					+depcode+","
					+"'"+depname+"',"
					+depquota+")";
			
			try {//최초 아무부서도 없을때 무조건 없음부서 생성 코드
				result = stmt.executeQuery(checkQuery);		
				while(!result.next()){
					stmt.executeQuery(first);
				}
			}catch(Exception e) {	
			}				
			try {
				if(depname.equals("없음")) {
					departmentdialogCantInsert();
				}else {
					stmt.executeQuery(query);
					stmt.executeQuery(commit);
					departmentdialogInsert();
				}
			}catch(Exception e) {				
				departmentdialog();
				
			}
			
			
			
		}
		
		//부서 삭제 메소드
		public void departmentDelete() {
			String depname =  textFieldDeptname.getText();
			String depcode = labelDepcode.getText();
			
			String departmentDeleteQuery = "DELETE FROM DEPARTMENT WHERE DEP_NAME = '"+depname+"'";
			String belong_departmentUpdateQuery = "UPDATE BELONG_DEPARTMENT SET DEP_CODE = 0 WHERE DEP_CODE ="+depcode;
			String belong_departmentDeleteQuery = "DELETE FROM BELONG_DEPARTMENT WHERE DEP_CODE = "+depcode;
			
			try {
				if (depcode.equals("0")) {
					departmentdialogCantDelete();
				}else {
				stmt.executeQuery(departmentDeleteQuery);
				stmt.executeQuery(belong_departmentUpdateQuery);
				stmt.executeQuery(belong_departmentDeleteQuery);
				stmt.executeQuery("COMMIT");
				departmentdialogDelete();
				}
			}catch(Exception e) {
				departmentdialog();
			}
			
		}
		
		
		
		
		
		
		//에러메세지
		public void departmentdialog() {	
	            JOptionPane.showMessageDialog(null, "다시확인하여 주세요.","경고",JOptionPane.WARNING_MESSAGE);            
	        
		}
		////에러메세지 없음부서는 지울 수 없습니다
		public void departmentdialogCantDelete() {	
            JOptionPane.showMessageDialog(null, "지울 수 없습니다.","경고",JOptionPane.WARNING_MESSAGE);            
        
	}
		//에러메세지 추가 할 수 없습니다.
		public void departmentdialogCantInsert() {	
            JOptionPane.showMessageDialog(null, "없음은 추가 할 수 없습니다.","경고",JOptionPane.WARNING_MESSAGE);            
        
	}
		
		
		//등록완료 메세지
		public void departmentdialogInsert() {	
	        JOptionPane.showMessageDialog(null, "등록되었습니다.","완료",JOptionPane.WARNING_MESSAGE);            
	    
	}
		//삭제완료 메세지
		public void departmentdialogDelete() {	
	        JOptionPane.showMessageDialog(null, "삭제되었습니다.","완료",JOptionPane.WARNING_MESSAGE);            
	    
	}
	
	
}
