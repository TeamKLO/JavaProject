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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class PositionFrame extends JDialog {
	private JTextField textFieldPositionname;
	private JTextField textFieldPositionApprove;
	
	private DefaultTableModel model;//
	private JTable table; 			// 테이블
	private JScrollPane jScollPane; //
	
	JButton insertButton; 
	JButton deleteButton;
	JButton closeButton;
	JLabel labelPositionname;
	JLabel labelPositionquota;
	private JLabel labelPositioncode;
	
	private Statement stmt = MainStart.connectDataBase();
	private JLabel labelCode;
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PositionFrame dialog = new PositionFrame();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PositionFrame() {
		setBounds(100, 100, 320, 300);
		getContentPane().setLayout(null);	
		setLocationRelativeTo(null);
		setTitle("부서등록");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		///테이블
		String[] tableColumn = {"직책코드","직책이름","상급자코드"};
		model = new DefaultTableModel(tableColumn,0){			
			@Override
			public boolean isCellEditable(int row, int column) {  //셀 편집 불가능 오버라이드
				return false;
			}
		};
		table = new JTable(model);		
		jScollPane = new JScrollPane (table);
		jScollPane.setBounds(25, 20, 250, 110);		
	
		table.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지
	
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) { ////테이블 가운데 정렬		
			tcm.getColumn(i).setCellRenderer(dtcr);
			}		
		getContentPane().add(jScollPane);

		
		
		///버튼
		insertButton = new JButton("등록");
		insertButton.setBounds(32, 213, 60, 30);
		getContentPane().add(insertButton);
		
		deleteButton = new JButton("삭제");
		deleteButton.setBounds(123, 213, 60, 30);
		getContentPane().add(deleteButton);
		
		closeButton = new JButton("닫기");
		closeButton.setBounds(219, 213, 60, 30);
		getContentPane().add(closeButton);
		
		//입력필드
		labelPositionname = new JLabel("직책명 :");
		labelPositionname.setBounds(20, 178, 50, 20);
		getContentPane().add(labelPositionname);
		
		labelPositionquota = new JLabel("상급자코드 :");
		labelPositionquota.setBounds(145, 178, 80, 20);
		getContentPane().add(labelPositionquota);		
		
		textFieldPositionApprove = new JTextField();
		textFieldPositionApprove.setColumns(10);
		textFieldPositionApprove.setBounds(220, 173, 40, 30);
		getContentPane().add(textFieldPositionApprove);
		
		textFieldPositionname = new JTextField();
		textFieldPositionname.setBounds(75, 173, 60, 30);
		getContentPane().add(textFieldPositionname);
		textFieldPositionname.setColumns(10);
		
		labelPositioncode = new JLabel("");
		labelPositioncode.setBounds(58, 150, 30, 20);
		getContentPane().add(labelPositioncode);
		labelPositioncode.setVisible(true);
		
		labelCode = new JLabel("코드 :");
		labelCode.setBounds(20, 150, 39, 20);
		getContentPane().add(labelCode);
	
		
		
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
				positionDelete();
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
					labelPositioncode.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					textFieldPositionname.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
					textFieldPositionApprove.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				}
			}
		});
		
		
		departmentSelect();
		
		
	}
	
	
	///테이블 뷰 메소드
		public void departmentSelect() {
			
			ResultSet result;
			String query = "SELECT POS_CODE,POS_NAME,POS_APPROVE FROM POSITION ORDER BY 1";
			
			try {			
				result = stmt.executeQuery(query);		
				while (result.next()) {
					Object[] data = { result.getString(1),result.getString(2),result.getString(3)};
					model.addRow(data);
				}
			}catch(Exception e) {
				
			}		
			
		}
		
		//직책 추가 메소드
		public void departmentInsert() {
			ResultSet result;	
			String posCode = "(select max(pos_code)+1 from position)";			
			String positionName =  textFieldPositionname.getText();
			String posApprove = textFieldPositionApprove.getText();
			String commit = "COMMIT";
			String query = "INSERT INTO POSITION VALUES("
					+posCode+","
					+"'"+positionName+"',";
				
			
			String[] addQuery = new String[2];
			if(!posApprove.isEmpty()) {
				addQuery[0] = posApprove;
			}else {addQuery[0]="0";}
			
			addQuery[1] = ")";			
			query = query + addQuery[0] + addQuery[1];
			
			
			//최초 아무부서도 없을때 무조건 없음부서 생성 코드
			String first = "INSERT INTO POSITION VALUES(0,'없음',0)"; //처음 아무부서도 없을때
			try {
				result = stmt.executeQuery(first);		
				while(!result.next()){
					stmt.executeQuery(first);
				}
			}catch(Exception e) {	
			}
			
			
			///등록된 직책코드만 상급자로 입력 가능한 코드
			boolean check = false;
			String allPosCode = "select pos_code from position";
			try {
			result = stmt.executeQuery(allPosCode); //직책코드 존재할때만 입력가능 코드
			while (result.next()) {
				if (posApprove.equals(result.getString(1))||posApprove.isEmpty()) {
					check = true;
					break;
				}
			}
			}catch(Exception e) {				
			}
			
			
			
			try {
				if(positionName.equals("없음")) {////없음을 또 생성불가
					departmentdialogCantInsert();
				}else if(check==false) { //상급자코드가 이상할때
					departmentdialogCantInsertCode();
				}
				else {
					stmt.executeQuery(query);
					stmt.executeQuery(commit);
					departmentdialogInsert();
				}
			}catch(Exception e) {	
				System.out.println(e);
				departmentdialog();
				
			}
			
			
			
		}
		
		//부서 삭제 메소드
		public void positionDelete() {
			String posname =  textFieldPositionname.getText();
			String poscode = labelPositioncode.getText();
			
			String departmentDeleteQuery = "DELETE FROM POSITION WHERE POS_NAME = '"+posname+"'";
			String belong_departmentUpdateQuery = "UPDATE EMPLOYEE_POSITION SET POS_CODE = 0 WHERE POS_CODE ="+poscode;
			String belong_departmentDeleteQuery = "DELETE FROM EMPLOYEE_POSITION WHERE POS_CODE = "+poscode;
			
			try {
				if (poscode.equals("0")) { //0번 없음 직책은 못지움
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
		
		//에러메세지 추가 할 수 없습니다.
		public void departmentdialogCantInsertCode() {	
            JOptionPane.showMessageDialog(null, "상급자 코드를 다시 확인하세요.","경고",JOptionPane.WARNING_MESSAGE);            
        
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
