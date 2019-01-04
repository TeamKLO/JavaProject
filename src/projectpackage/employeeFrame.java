package projectpackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;

public class employeeFrame extends JDialog {
	

	//버튼
	private JButton insertButton;  //등록버튼
	private JButton updateButton;  //수정버튼
	private JButton deleteButton;  //삭제버튼
	private JButton closeButton;   //닫기버튼
	private JButton searchButton;  //이미지찾기 버튼
	
	//테이블
	private DefaultTableModel model;//
	private JTable table; 			// 사원 목록 테이블
	private JScrollPane jScollPane; //
	
	
	//입력 필드
	private JComboBox comboBoxDept; //  부서 
	private JComboBox comboBoxPosition; //직책 
	private JComboBox comboBoxManagementCode; // 관리코드
	private JComboBox comboBoxGender; //성별	
	private JTextField textFieldName; // 이름
	private JTextField textFieldEmpNo; // 사원번호	
	private JTextField textFieldPhone; // 전화번호
	private JTextField textFieldAddress; // 주소
	private JTextField textFieldPassword;//비밀번호	
	private JDateChooser dateChooserBirthday; //생년월일
	private JDateChooser dateChooserHiredate; //입사일	
	private JLabel labelName; //이름라벨
	private JLabel labelAddress; //주소라벨
	private JLabel labelPhone; //전화번호라벨
	private JLabel labelPassword; //비밀번호라벨
	private JLabel labelHiredate; //입사일라벨
	private JLabel labelBirthday; //생년월일라벨	
	private JPanel panelImage; //이미지 판넬
	private JTextField textFieldImage; //이미지 주소 필드
	private Image image;	   //이미지
	private JLabel labelImage; //이미지 라벨
	private String getimage;   //이미지 경로
	
	
	//이미지 추가
	
	//JFileChooser
	private JFileChooser jFileChooser;
	
	
	//DB접속
	private Statement stmt = MainStart.connectDataBase();
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			employeeFrame dialog = new employeeFrame();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	
	public employeeFrame() {
		setBounds(100, 100, 800, 750);
		getContentPane().setLayout(null);
		
		
		
		//////버튼
		insertButton = new JButton("등록");
		insertButton.setBounds(110, 650, 60, 30);
		getContentPane().add(insertButton);
		
		updateButton = new JButton("수정");
		updateButton.setBounds(270, 650, 60, 30);
		getContentPane().add(updateButton);
		
		deleteButton = new JButton("삭제");
		deleteButton.setBounds(440, 650, 60, 30);
		getContentPane().add(deleteButton);
		
		closeButton = new JButton("닫기");
		closeButton.setBounds(600, 650, 60, 30);
		getContentPane().add(closeButton);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dispose();
			}
		});
		
		
		////테이블
		String[] tableColumn = {"부서","직책","이름","사원번호","성별","입사일","생년월일","전화번호","주소","사원이미지","비밀번호","관리코드"};
		model = new DefaultTableModel(tableColumn,0){			
			@Override
			public boolean isCellEditable(int row, int column) {  //셀 편집 불가능 오버라이드
				return false;
			}
		};
		table = new JTable(model);		
		jScollPane = new JScrollPane (table);
		jScollPane.setBounds(45, 30, 700, 400);
		
		table.addMouseListener(new employeeTableMouseListener());
		table.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //가로스크롤
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) { ////테이블 가운데 정렬		
			tcm.getColumn(i).setCellRenderer(dtcr);
			}		
		getContentPane().add(jScollPane);
		
		
		
		////입력 필드/////////////////////////////////////////
		
		//부서
		String[] comboBoxDeptMenu = {"부서","영업부", "인사부", "기획부", "총무부", "개발부"};
		comboBoxDept = new JComboBox(comboBoxDeptMenu);
		comboBoxDept.setBounds(70, 480, 70, 30);
		getContentPane().add(comboBoxDept);
		
		//직책 
		String[] comboBoxPositionMenu = {"직책","사원","대리","과장","부장","이사","사장"};
		comboBoxPosition = new JComboBox(comboBoxPositionMenu);
		comboBoxPosition.setBounds(160, 480, 70,30);
		getContentPane().add(comboBoxPosition);
		
		// 관리코드
		String[] comboBoxManagementCodeMenu = {"권한","없음","일반관리","DB관리"};
		comboBoxManagementCode = new JComboBox(comboBoxManagementCodeMenu);
		comboBoxManagementCode.setBounds(430, 580, 80,30);
		getContentPane().add(comboBoxManagementCode);
		
		//성별					
		String[] comboBoxGenderMenu = {"성별","M","F"};		
		comboBoxGender = new JComboBox(comboBoxGenderMenu);
		comboBoxGender.setBounds(390, 480, 60,30);
		getContentPane().add(comboBoxGender);		
		
		//이름
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("돋움", Font.PLAIN, 15));
		textFieldName.setBounds(285, 480, 90,30);
		getContentPane().add(textFieldName);
		//이름라벨
		labelName = new JLabel("이름 :");
		labelName.setFont(new Font("돋움", Font.PLAIN, 15));
		labelName.setBounds(240, 480, 50,30);
		getContentPane().add(labelName);
		
		//사원번호
		textFieldEmpNo = new JTextField();
		
		// 전화번호
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(600, 530, 100,30);
		getContentPane().add(textFieldPhone);
		//전화번호라벨
		labelPhone = new JLabel("전화번호 :");
		labelPhone.setFont(new Font("돋움", Font.PLAIN, 15));
		labelPhone.setBounds(525, 530, 100,30);
		getContentPane().add(labelPhone);
		
		// 주소
		textFieldAddress = new JTextField();
		textFieldAddress.setBounds(110, 530, 400,30);
		getContentPane().add(textFieldAddress);
		//주소라벨
		labelAddress = new JLabel("주소 :");
		labelAddress.setFont(new Font("돋움", Font.PLAIN, 15));
		labelAddress.setBounds(65, 530, 50,30);
		getContentPane().add(labelAddress);
		
		//비밀번호
		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(600, 580, 100,30);
		getContentPane().add(textFieldPassword);
		//비밀번호라벨
		labelPassword = new JLabel("비밀번호 :");
		labelPassword.setFont(new Font("돋움", Font.PLAIN, 15));
		labelPassword.setBounds(525, 580, 100,30);
		getContentPane().add(labelPassword);
		
		//생년월일
		dateChooserBirthday = new JDateChooser();
		dateChooserBirthday.setDateFormatString("yyyy-MM-dd");
		dateChooserBirthday.setBounds(310, 580, 100, 30);
		getContentPane().add(dateChooserBirthday);
		//생년월일라벨
		labelBirthday = new JLabel("생년월일 :");
		labelBirthday.setFont(new Font("돋움", Font.PLAIN, 15));
		labelBirthday.setBounds(235, 580, 100,30);
		getContentPane().add(labelBirthday);
		 
		//입사일
		dateChooserHiredate = new JDateChooser();
		dateChooserHiredate.setDateFormatString("yyyy-MM-dd");
		dateChooserHiredate.setBounds(110, 580, 100, 30);
		getContentPane().add(dateChooserHiredate);
		//입사일라벨
		labelHiredate = new JLabel("입사일 :");
		labelHiredate.setFont(new Font("돋움", Font.PLAIN, 15));
		labelHiredate.setBounds(50, 580, 100,30);
		getContentPane().add(labelHiredate);
		
		//이미지 등록 버튼
		searchButton = new JButton("찾기");
		searchButton.setBounds(558, 440, 60, 30);
		getContentPane().add(searchButton);			
		//이미지경로 필드
		textFieldImage = new JTextField();
		textFieldImage.setFont(new Font("돋움", Font.PLAIN, 15));
		textFieldImage.setBounds(462, 480, 156, 30);
		getContentPane().add(textFieldImage);
		//이미지 라벨
		labelImage = new JLabel("사진등록 :");
		labelImage.setFont(new Font("돋움", Font.PLAIN, 15));
		labelImage.setBounds(483, 440, 70, 30);
		getContentPane().add(labelImage);
	
		
		//이미지 뷰
		panelImage = new JPanel(){
		// 이미지 파일을 가져오기 위해 paint 메소드를 오버라이드
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};		
		panelImage.setBounds(629, 440, 90, 70);
		getContentPane().add(panelImage);		
		
		
						
		////////액션리스너	
		///이미지 등록 버튼 엑션리스너
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChoice();
				model.setRowCount(0);
				employeeSelect();
			}
		});
		
		///등록버튼
		insertButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				employeeInsert();
				model.setRowCount(0);
				employeeSelect();
				
			}
		});
		
		
	
		
		
		
		employeeSelect();		
	}
	
	
	///테이블 뷰 메소드
	public void employeeSelect() {
		
		ResultSet result;
		String query = "SELECT D.DEP_NAME, P.POS_NAME, E.EMP_NAME, E.EMP_NO,E.EMP_GENDER, E.EMP_BIRTHDAY," 
				+" E.EMP_MOBILE, E.EMP_ADDRESS, E.EMP_JOINDAY, E.EMP_IMAGE, E.EMP_PASSWORD, M.MAN_CODE"
				+" FROM DEPARTMENT D, BELONG_DEPARTMENT BD, EMPLOYEE E, MANAGER M,"  
				+" EMPLOYEE_POSITION EP, POSITION P" 
				+" WHERE D.DEP_CODE = BD.DEP_CODE AND BD.EMP_NO = E.EMP_NO" 
				+" AND E.EMP_NO = M.EMP_NO AND E.EMP_NO = EP.EMP_NO" 
				+" AND EP.POS_CODE = P.POS_CODE";
		
		try {			
			result = stmt.executeQuery(query);		
			while (result.next()) {
				Object[] data = { result.getString(1),result.getString(2),result.getString(3),result.getString(4),
						result.getString(5), result.getString(6), result.getString(7),result.getString(8),
						result.getString(9),result.getString(10),result.getString(11),result.getString(12)};
				model.addRow(data);
			}
		}catch(Exception e) {
			System.out.println("아 제발 나타나지마");
		}		
		
	}
	
	
	
	///테이블 마우스리스너
	class employeeTableMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow(); /// 해당 선택 로우
			int column = table.getColumnCount(); // 컬럼 갯수 모두

			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");			


			for (int i = 0; i < column; i++) {
				if (i == 0) {
					comboBoxDept.setSelectedItem((String) model.getValueAt(row, 0));
				}
				if (i == 1) {
					comboBoxPosition.setSelectedItem((String) model.getValueAt(row, 1));
				}
				if (i == 2) {
					textFieldName.setText((String) model.getValueAt(row, 2));
				}
				if (i == 4) {
					comboBoxGender.setSelectedItem((String) model.getValueAt(row, 4));
				}
				if (i == 5) {
					if((String)model.getValueAt(row, 5)!=null) {
						((JTextField) dateChooserHiredate.getDateEditor().getUiComponent())
						.setText(transFormat.format(model.getValueAt(row, 5)));						
					}				
				}
				if (i == 6) {
					if((String)model.getValueAt(row, 6)!=null) {
					((JTextField) dateChooserBirthday.getDateEditor().getUiComponent())
					.setText(transFormat.format(model.getValueAt(row, 6)));
					}
				}
				if (i == 7) {
					textFieldPhone.setText((String) model.getValueAt(row, 7));
				}
				if (i == 8) {
					textFieldAddress.setText((String) model.getValueAt(row, 8));
				}
			
				if (i == 9) {											
					getimage = ((String)model.getValueAt(table.getSelectedRow(),9));				
				}
				if (i == 10) {
					textFieldPassword.setText((String) model.getValueAt(row, 10));
				}
				if (i == 11) {					
					if(((String) model.getValueAt(row, 11)).equals("0")) {
						comboBoxManagementCode.setSelectedIndex(1);				
					}else if(((String) model.getValueAt(row, 11)).equals("1")) {
						comboBoxManagementCode.setSelectedIndex(2);
					}else {
						comboBoxManagementCode.setSelectedIndex(3);
					}
				}
				
			}
			setEmployeeImage();
		}
	}
	
	
	///이미지 뷰 메소드	
	private boolean setEmployeeImage() {		
		File f = new File(getimage);
		if (!f.exists()) {
			return false;
		}		
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getimage);
		panelImage.repaint();		
		return true;
	}
	
	
	
	
	///등록 메소드
	public void employeeInsert() {
		ResultSet result;
		
		String password = textFieldPassword.getText();
		String name = textFieldName.getText();
		String gender = (String)comboBoxGender.getSelectedItem();
		String birthday = ((JTextField) dateChooserBirthday.getDateEditor().getUiComponent()).getText();
		String hiredate = ((JTextField) dateChooserHiredate.getDateEditor().getUiComponent()).getText();	
		String phone = textFieldPhone.getText();
		String address = textFieldAddress.getText();
		String image = "null";
		
		String deptname = (String)comboBoxDept.getSelectedItem();		
		String ManagementCode = (String)comboBoxManagementCode.getSelectedItem();
		String Position = (String)comboBoxPosition.getSelectedItem();
		
		
		
		String employeeInsertQuery = "INSERT INTO EMPLOYEE VALUES((select max(emp_no)+1 from employee),"
				+"'"+password+"',"
				+"'"+name+"',"
				+"'"+gender+"',"
				+"'"+birthday+"',"
				+"'"+phone+"',"
				+"'"+address+"',"
				+"'"+hiredate+"',"
				+image+")";
		

		
		String belong_DepartmentInsertQuery = "INSERT INTO BELONG_DEPARTMENT VALUES"
				+ "((select max(emp_no) from employee),"
				+ "(select dep_code from department where dep_name = '"+deptname+"'))";
		
		String managerInsertQuery = "INSERT INTO MANAGER VALUES"
				+ "((select max(emp_no) from employee),"
				+ "(select man_code from management_list where man_name = '"+ManagementCode+"'))";
		
		String employee_PositionInsertQuery = "INSERT INTO EMPLOYEE_POSITION"
				+ "((select max(emp_no) from employee),"
				+ "(select pos_code from position where pos_name = '"+Position+"'))";
		
		

		
		try {
			stmt.executeUpdate(employeeInsertQuery);
			stmt.executeUpdate(belong_DepartmentInsertQuery);
			stmt.executeUpdate(managerInsertQuery);
			stmt.executeUpdate(employee_PositionInsertQuery);
			
			
			
			//사진등록을 위한 사원번호 알아오는 코드/
			String empNo = "SELECT MAX(EMP_NO) FROM EMPLOYEE";		
			String getEmpNo = null;
			String location= "C:\\Users\\KITRI\\git\\JavaProject\\image\\";
			result = stmt.executeQuery(empNo);			
			while (result.next()){
				getEmpNo =result.getString(1);				
			}			
		      //파일을 DB 이미지 폴더에 복사	          
	           if(!getimage.isEmpty()) {
	        	   try {  		   			
	        		   	FileInputStream fis = new FileInputStream(getimage);  // 원본파일
	        		   	FileOutputStream fos = new FileOutputStream(location + getEmpNo+".JPG");// 복사위치 및 이름        		   	
	        		   	//파일복사
	        		   	byte[] buffer = new byte[1024];
	        		   	int readcount = 0;        		   	  
	        		   	while((readcount=fis.read(buffer)) != -1) {
	        		   	fos.write(buffer, 0, readcount);   
	        		   	}	        		   	
	        		   	fis.close();
	        		   	fos.close();
	        		   	
	        		      ///파일 복사 후 DB사원테이블 이미지 경로 업데이트
	     	           String updateQuery = "UPDATE EMPLOYEE SET EMP_IMAGE = '"+location+getEmpNo+"'"
	     	           					+ " WHERE EMP_NO ="+getEmpNo;
	        		   	
	     	          stmt.executeUpdate(updateQuery);
	        	   }catch(Exception e) {
	        		   System.out.println("오류투성이구만");
	        	   }         
	           }	      
	     
	           
		}catch(Exception e) {
			System.out.println("정말큰일이구만");
			
		}	
		
	}
	
	///삭제메소드
	public void employeeDelete() {
		ResultSet result;
		String empNo = textFieldEmpNo.getText();
		
		String employeeDeleteQuery = "DELETE FROM EMPLOYEE WHERE EMP_NO ="+empNo;
		String belong_DepartmentDeleteQuery = "DELTE FROM BELONG_DEPARTMENT WHERE EMP_NO ="+empNo;
		String managerDeleteQuery = "DELETE FROM MANAGER WHERE EMP_NO ="+empNo;
		String employee_PositionDeleteQuery = "DELTE FROM EMPLOYEE_POSITION WHERE EMP_NO ="+empNo;
		
		try {
			stmt.executeUpdate(employeeDeleteQuery);
			stmt.executeUpdate(belong_DepartmentDeleteQuery);
			stmt.executeUpdate(managerDeleteQuery);
			stmt.executeUpdate(employee_PositionDeleteQuery);			
		}catch(Exception e) {
			
		}
		
	}
	
	
	//수정메소드
	public void employeeUpdate() {
		ResultSet result;
		
		String empNo = textFieldEmpNo.getText();
		String password = textFieldPassword.getText();
		String name = textFieldName.getText();
		String gender = (String)comboBoxGender.getSelectedItem();
		String birthday = ((JTextField) dateChooserBirthday.getDateEditor().getUiComponent()).getText();
		String hiredate = ((JTextField) dateChooserHiredate.getDateEditor().getUiComponent()).getText();	
		String phone = textFieldPhone.getText();
		String address = textFieldAddress.getText();
		String image = textFieldImage.getText();
		
		
		//사진등록을 위한 사원번호 알아오는 코드/
		String updateImage = "null";
		String getEmpNo = null;
		String location= "C:\\Users\\KITRI\\git\\JavaProject\\image\\";
		try {		
		String a = "SELECT MAX(EMP_NO) FROM EMPLOYEE";	
		result = stmt.executeQuery(a);			
		while (result.next()){
			getEmpNo =result.getString(1);				
		}
		//불러온파일	
		File file = new File(image);	
		//저장할파일
		File updateFile = new File(location+getEmpNo+".JPG"); 
		BufferedImage bi = ImageIO.read(file);
		ImageIO.write(bi, "JPG", updateFile);
		
		updateImage = location+getEmpNo+".JPG";
		}catch(Exception e) {
			
		}		
		
		String employeeUpdateQuery = "UPDATE EMPLOYEE SET "
									+" EMP_PASSWORD = '"+password+"',"
									+" EMP_NAME = '"+name+"',"
									+" EMP_GENDER = '"+gender+"',"
									+" EMP_BIRTHDAY = '"+birthday+"',"
									+" EMP_MOBILE = "+phone+","
									+" EMP_ADDRESS = '"+address+"',"
									+" EMP_JOINDAY = '"+hiredate+"'"
									+" EMP_IMAGE ="+updateImage+"'"
									+" WHERE EMP_NO ="+ empNo;
		
		
		try {
			stmt.executeUpdate(employeeUpdateQuery);
		} catch (SQLException e) {
			
		}
		
	}
	
	
	//이미지 선택
	public void fileChoice() {
		JFileChooser fileChooser = new JFileChooser();		
           
		   //창을 강제로 닫거나 취소버튼을 누른경우 다이얼로그
           int ret=fileChooser.showOpenDialog(null);
           if(ret!=JFileChooser.APPROVE_OPTION){
               JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.","경고",JOptionPane.WARNING_MESSAGE);
               return;
           }
           //사용자가 열기 버튼을 누른경우
           //파일 경로명을 알아온다
           getimage=fileChooser.getSelectedFile().getPath();
           textFieldImage.setText(getimage);
           
           //관리창에서 이미지 보이게하는 메소드 이미지 뷰 메소드에서 뽑아옴
           Toolkit tk = Toolkit.getDefaultToolkit();
           image = tk.getImage(getimage);
           panelImage.repaint();
   		
         			   			
       }
	
	
	
}
