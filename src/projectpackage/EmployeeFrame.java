package projectpackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

public class EmployeeFrame extends JDialog {
	// static JFrame 필드는 단지 main의 EventQueue안의 생성자를 만족시키기 위한 것일 뿐, 프로그램 실행과 상관없음
	static JFrame tmpFrame;
	
	private JLabel labelFrameTitle;//프레임 타이틀라벨
	//버튼
	private JButton insertButton;  //등록버튼
	private JButton updateButton;  //수정버튼
	private JButton deleteButton;  //삭제버튼
	private JButton closeButton;   //닫기버튼
	private JButton searchButton;  //이미지찾기 버튼
	private JButton insertDeptButton; //부서등록버튼
	private JButton insertPositionButton; //직책등록버튼
	//테이블
	private DefaultTableModel model;//
	private JTable table; 			// 사원 목록 테이블
	private JScrollPane jScollPane; //
	
	
	//입력 필드
	private String[] deptCombobox;//부서 콤보박스 내용
	private String[] positionCombobox;//직책 콤보박스 내용
	private JComboBox comboBoxDept; //  부서 
	private JComboBox comboBoxPosition; //직책 
	private JComboBox comboBoxManagementCode; // 관리코드
	private JComboBox comboBoxGender; //성별	
	private JTextField textFieldName; // 이름
	private JLabel labelEmpNoView; // 사원번호	
	private JTextField textFieldPhone; // 전화번호
	private JTextField textFieldAddress; // 주소
	private JTextField textFieldPassword;//비밀번호	
	private JDateChooser dateChooserBirthday; //생년월일
	private JDateChooser dateChooserHiredate; //입사일
	private JLabel labelEmpNo; //사원번호라벨
	private JLabel labelName; //이름라벨
	private JLabel labelAddress; //주소라벨
	private JLabel labelPhone; //전화번호라벨
	private JLabel labelPassword; //비밀번호라벨
	private JLabel labelHiredate; //입사일라벨
	private JLabel labelBirthday; //생년월일라벨	
	private JPanel panelImage; //이미지 판넬
	private JTextField textFieldImage; //이미지 주소 필드
	private Image image;	   //이미지
	private JLabel labelImage; //사진등록 라벨
	private String getimage = "";   //이미지 경로
	

	//이미지 추가	
	//JFileChooser
	private JFileChooser jFileChooser;
		
	///프레임이미지	
	private JLabel lblBackImg;
	
	//메뉴바	
//	JMenuBar menuBar;
//	JMenu menu;
//	JMenuItem menuItemDept;
//	JMenuItem menuItemPosition;
	
	 //프레인 마우스리스너용 좌표
	private int xx,xy;
	
	//DB접속
	private Statement stmt = MainStart.connectDataBase();
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeeFrame dialog = new EmployeeFrame(tmpFrame);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	
	public EmployeeFrame(JFrame ownerFrame) {
		super(ownerFrame, true);
	
//		deptnameSelect();//부서 콤보박스 아이템 가져오기
//		positionSelect();//직책 콤보박스 아이템 가져오기
		
		
		setBounds(100, 100, 661, 700);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		this.setUndecorated(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	
	
		//프레임 타이틀 라벨
		labelFrameTitle = new JLabel("사원 관리");
		labelFrameTitle.setForeground(Color.WHITE);
		labelFrameTitle.setBounds(12, 12, 57, 15);
		getContentPane().add(labelFrameTitle);
		
		//////버튼
		insertButton = new JButton("등 록");
		insertButton.setBackground(new Color(255, 255, 224));
		insertButton.setFont(new Font("굴림체", Font.BOLD, 12));
		insertButton.setBounds(40, 629, 110, 30);
		getContentPane().add(insertButton);
		
		updateButton = new JButton("수 정");
		updateButton.setBackground(new Color(255, 255, 224));
		updateButton.setFont(new Font("굴림체", Font.BOLD, 12));
		updateButton.setBounds(195, 629, 110, 30);
		getContentPane().add(updateButton);
		
		deleteButton = new JButton("삭 제");
		deleteButton.setBackground(new Color(255, 255, 224));
		deleteButton.setFont(new Font("굴림체", Font.BOLD, 12));
		deleteButton.setBounds(353, 629, 110, 30);
		getContentPane().add(deleteButton);
		
		closeButton = new JButton("닫 기");
		closeButton.setBackground(new Color(255, 255, 224));
		closeButton.setFont(new Font("굴림체", Font.BOLD, 12));
		closeButton.setBounds(513, 629, 110, 30);
		getContentPane().add(closeButton);
		
		insertDeptButton = new JButton("부서등록");
		insertDeptButton.setVisible(false);
		insertDeptButton.setBackground(new Color(255, 255, 224));
		insertDeptButton.setFont(new Font("굴림체", Font.BOLD, 12));
		insertDeptButton.setBounds(40, 650, 110, 30);
		getContentPane().add(insertDeptButton);
		
		insertPositionButton= new JButton("직책등록");
		insertPositionButton.setVisible(false);
		insertPositionButton.setBackground(new Color(255, 255, 224));
		insertPositionButton.setFont(new Font("굴림체", Font.BOLD, 12));
		insertPositionButton.setBounds(195, 650, 110, 30);
		getContentPane().add(insertPositionButton);
		
		
		
		
		
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
		jScollPane.setBounds(48, 43, 564, 276);
		
		table.addMouseListener(new employeeTableMouseListener());
		table.getTableHeader().setReorderingAllowed(false);// 칼럼순서변경금지
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //가로스크롤
		table.setRowSorter(new TableRowSorter(model)); //테이블 칼럼클릭 정렬
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
		dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
		TableColumnModel tcm = table.getColumnModel(); // 정렬할 테이블의 컬럼모델을 가져옴
		for (int i = 0; i < tcm.getColumnCount(); i++) { ////테이블 가운데 정렬		
			tcm.getColumn(i).setCellRenderer(dtcr);
			}		
		getContentPane().add(jScollPane);
		
		//사이즈조절
		table.getColumn("성별").setWidth(40); 
		table.getColumn("성별").setMinWidth(30);
		table.getColumn("성별").setMaxWidth(40);		
		table.getColumn("전화번호").setWidth(100);
		table.getColumn("전화번호").setMinWidth(100);
		
		
		
		////입력 필드/////////////////////////////////////////
		
		//부서
		String[] comboBoxDeptMenu = {"부서","영업부", "인사부", "기획부", "총무부", "개발부"};
		comboBoxDept = new JComboBox(comboBoxDeptMenu);
		comboBoxDept.setBackground(new Color(255, 255, 224));
		comboBoxDept.setBounds(31, 434, 70, 30);
		getContentPane().add(comboBoxDept);
		
		//직책 
		String[] comboBoxPositionMenu = {"직책","사원","대리","과장","부장","이사","사장"};
		
		comboBoxPosition = new JComboBox(comboBoxPositionMenu);
		comboBoxPosition.setBackground(new Color(255, 255, 224));
		comboBoxPosition.setBounds(124, 434, 70,30);
		getContentPane().add(comboBoxPosition);
		
		// 관리코드
		String[] comboBoxManagementCodeMenu = {"권한","없음","일반관리","DB관리"};
		comboBoxManagementCode = new JComboBox(comboBoxManagementCodeMenu);
		comboBoxManagementCode.setFont(new Font("굴림체", Font.BOLD, 12));
		comboBoxManagementCode.setBackground(new Color(255, 255, 224));
		comboBoxManagementCode.setBounds(386, 534, 80,30);
		getContentPane().add(comboBoxManagementCode);
		
		//성별					
		String[] comboBoxGenderMenu = {"성별","M","F"};		
		comboBoxGender = new JComboBox(comboBoxGenderMenu);
		comboBoxGender.setFont(new Font("굴림체", Font.BOLD, 12));
		comboBoxGender.setBackground(new Color(255, 255, 224));
		comboBoxGender.setBounds(360, 433, 60,30);
		getContentPane().add(comboBoxGender);		
		
		//이름
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("돋움", Font.PLAIN, 15));
		textFieldName.setBounds(255, 433, 90,30);
		getContentPane().add(textFieldName);
		//이름라벨
		labelName = new JLabel("이름 :");
		labelName.setForeground(Color.WHITE);
		labelName.setFont(new Font("굴림체", Font.BOLD, 12));
		labelName.setBounds(205, 433, 50,30);
		getContentPane().add(labelName);
		
		//사원번호
		labelEmpNoView = new JLabel();
		labelEmpNoView.setForeground(Color.WHITE);
		labelEmpNoView.setFont(new Font("돋움", Font.PLAIN, 15));
		labelEmpNoView.setBounds(102, 387, 60,30);
		getContentPane().add(labelEmpNoView);
		
		//사원번호 라벨
		labelEmpNo = new JLabel("사원번호 : ");
		labelEmpNo.setForeground(Color.WHITE);
		labelEmpNo.setFont(new Font("굴림체", Font.BOLD, 12));
		labelEmpNo.setBounds(31, 387, 100,30);
		getContentPane().add(labelEmpNo);
		
		// 전화번호
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(551, 487, 92,30);
		getContentPane().add(textFieldPhone);
		//전화번호라벨
		labelPhone = new JLabel("전화번호 :");
		labelPhone.setForeground(Color.WHITE);
		labelPhone.setFont(new Font("굴림체", Font.BOLD, 12));
		labelPhone.setBounds(477, 487, 100,30);
		getContentPane().add(labelPhone);
		
		// 주소
		textFieldAddress = new JTextField();
		textFieldAddress.setBounds(80, 487, 385,30);
		getContentPane().add(textFieldAddress);
		//주소라벨
		labelAddress = new JLabel("주소 :");
		labelAddress.setForeground(Color.WHITE);
		labelAddress.setFont(new Font("굴림체", Font.BOLD, 12));
		labelAddress.setBounds(29, 488, 50,30);
		getContentPane().add(labelAddress);
		
		//비밀번호
		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(551, 534, 92,30);
		getContentPane().add(textFieldPassword);
		//비밀번호라벨
		labelPassword = new JLabel("비밀번호 :");
		labelPassword.setForeground(Color.WHITE);
		labelPassword.setFont(new Font("굴림체", Font.BOLD, 12));
		labelPassword.setBounds(477, 534, 100,30);
		getContentPane().add(labelPassword);
		
		//생년월일
		dateChooserBirthday = new JDateChooser();
		dateChooserBirthday.setDateFormatString("yyyy-MM-dd");
		dateChooserBirthday.setBounds(268, 534, 100, 30);
		getContentPane().add(dateChooserBirthday);
		//생년월일라벨
		labelBirthday = new JLabel("생년월일 :");
		labelBirthday.setForeground(Color.WHITE);
		labelBirthday.setFont(new Font("굴림체", Font.BOLD, 12));
		labelBirthday.setBounds(193, 532, 100,30);
		getContentPane().add(labelBirthday);
		 
		//입사일
		dateChooserHiredate = new JDateChooser();
		dateChooserHiredate.setDateFormatString("yyyy-MM-dd");
		dateChooserHiredate.setBounds(80, 532, 100, 30);
		getContentPane().add(dateChooserHiredate);
		//입사일라벨
		labelHiredate = new JLabel("입사일 :");
		labelHiredate.setForeground(Color.WHITE);
		labelHiredate.setFont(new Font("굴림체", Font.BOLD, 12));
		labelHiredate.setBounds(17, 530, 100,30);
		getContentPane().add(labelHiredate);
		
		//이미지 등록 버튼
		searchButton = new JButton("찾기");
		searchButton.setFont(new Font("굴림체", Font.BOLD, 12));
		searchButton.setBackground(new Color(255, 255, 224));
		searchButton.setBounds(477, 387, 60, 30);
		getContentPane().add(searchButton);			
		//이미지경로 필드
		textFieldImage = new JTextField();
		textFieldImage.setFont(new Font("돋움", Font.PLAIN, 15));
		textFieldImage.setBounds(432, 433, 210, 30);
		getContentPane().add(textFieldImage);
		//이미지 라벨
		labelImage = new JLabel("사진등록 :");
		labelImage.setForeground(Color.WHITE);
		labelImage.setFont(new Font("굴림체", Font.BOLD, 12));
		labelImage.setBounds(399, 387, 70, 30);
		getContentPane().add(labelImage);
		

//		//메뉴바
//		menuBar = new JMenuBar();
//		setJMenuBar(menuBar);
//		
//		menu = new JMenu("등록");
//		menuBar.add(menu);
//		
//		menuItemDept = new JMenuItem("부서등록");
//		menu.add(menuItemDept);
//		
//		menuItemPosition = new JMenuItem("직책등록");
//		menu.add(menuItemPosition);
		
		
		
		//사원사진 뷰
		panelImage = new JPanel(){
		// 이미지 파일을 가져오기 위해 paint 메소드를 오버라이드
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};		
		panelImage.setBounds(553, 350, 90, 70);
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
		///수정버튼
		updateButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				employeeUpdate();
				model.setRowCount(0);
				employeeSelect();
				
			}
		});
		///삭제버튼
		deleteButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				employeeDelete();
				model.setRowCount(0);
				employeeSelect();
				
			}
		});
		//닫기버튼		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			dispose();
			}
		});
		
		//부서등록버튼
		insertDeptButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				DepartmentFrame departmentFrame = new DepartmentFrame();
				departmentFrame.setVisible(true);
				
			}
		});
		//직책등록버튼
		insertPositionButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				PositionFrame positionFrame = new PositionFrame();
				positionFrame.setVisible(true);
				
			}
		});
		
		
	
		///사원이미지 클릭시 파일열기
		panelImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// 더블클릭
					if(!getimage.isEmpty()) {				
					try {
						Process p = Runtime.getRuntime().exec(new String[] {"cmd.exe", "/c", getimage});
						System.out.println(getimage);
					} catch (Exception e1) {						
						System.out.println(e1);
					}
					
				 }
				}
			}
		});		
		
		
		///프레임 드래그 마우스리스너
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
		
		
		
		//////프레임이미지	      
		lblBackImg = new JLabel("lblBackImg");
		lblBackImg.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 661, 700);
		getContentPane().add(lblBackImg);
		
		
		//

		////메뉴바 액션리스너
		//부서변경메뉴
//		menuItemDept.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				DepartmentFrame departmentFrame = new DepartmentFrame();
//				departmentFrame.setVisible(true);
//				
//			}
//		});
//		menuItemPosition.addActionListener(new ActionListener() {			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				PositionFrame positionFrame = new PositionFrame();
//				positionFrame.setVisible(true);
//				
//			}
//		});
		
		
		
		
		employeeSelect();
		
	}
	
	
	//부서 콤보박스 뷰 메소드
	public void deptnameSelect() {
		ResultSet result;
		String query = "SELECT DEP_NAME FROM DEPARTMENT ORDER BY DEP_CODE";
		int count =0;
		int i =1;
		try {
			result = stmt.executeQuery(query);
			while(result.next()) {
				count++;
			}//쿼리를 날려서 행의 갯수를 알아온 뒤
			 //갯수 +1만큼 배열을 만들고
			deptCombobox = new String[count+1];
			deptCombobox[0] = "부서";
			//다시 쿼리를 날려서 배열안에 목록을 넣어준다
			result = stmt.executeQuery(query);
			while(result.next()) {							
				deptCombobox[i++] = result.getString(1);		
			}
		}catch(Exception e) {
			
		}
	}
	
	//직책 콤보박스 뷰 메소드
	
	public void positionSelect() {
		ResultSet result;
		String query = "SELECT POS_NAME FROM POSITION ORDER BY POS_CODE";
		int count =0;
		int i =1;
		try {
			result = stmt.executeQuery(query);
			while(result.next()) {
				count++;
			}//쿼리를 날려서 행의 갯수를 알아온 뒤
			 //갯수 +1만큼 배열을 만들고
			positionCombobox = new String[count+1];
			positionCombobox[0] = "직책";
			//다시 쿼리를 날려서 배열안에 목록을 넣어준다
			result = stmt.executeQuery(query);
			while(result.next()) {							
				positionCombobox[i++] = result.getString(1);		
			}
		}catch(Exception e) {
			
		}
	}
	
	
	
	///테이블 뷰 메소드
	public void employeeSelect() {
		
		ResultSet result;
		String query = "SELECT D.DEP_NAME, P.POS_NAME, E.EMP_NAME, E.EMP_NO,E.EMP_GENDER, E.EMP_JOINDAY," 
				+" E.EMP_BIRTHDAY, E.EMP_MOBILE, E.EMP_ADDRESS, E.EMP_IMAGE, E.EMP_PASSWORD, M.MAN_CODE"
				+" FROM DEPARTMENT D, BELONG_DEPARTMENT BD, EMPLOYEE E, MANAGER M,"  
				+" EMPLOYEE_POSITION EP, POSITION P" 
				+" WHERE D.DEP_CODE = BD.DEP_CODE AND BD.EMP_NO = E.EMP_NO" 
				+" AND E.EMP_NO = M.EMP_NO AND E.EMP_NO = EP.EMP_NO" 
				+" AND EP.POS_CODE = P.POS_CODE"
				+" ORDER BY 1,4";
		
		try {			
			result = stmt.executeQuery(query);		
			while (result.next()) {
				Object[] data = { result.getString(1),result.getString(2),result.getString(3),result.getString(4),
						result.getString(5), result.getDate(6), result.getDate(7),result.getString(8),
						result.getString(9),result.getString(10),result.getString(11),result.getString(12)};
				model.addRow(data);
			}
		}catch(Exception e) {
			
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
					comboBoxDept.setSelectedItem((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0));
				}
				if (i == 1) {
					comboBoxPosition.setSelectedItem((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 1));
				}
				if (i == 2) {
					textFieldName.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 2));
				}
				if (i ==3) {
					labelEmpNoView.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()),3));
				}
				if (i == 4) {
					if((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 4)==null) {
						comboBoxGender.setSelectedIndex(0);
					}else {
					comboBoxGender.setSelectedItem((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 4));
				}
				}
				if (i == 5) {
					if((Date)model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 5)!=null) {
						((JTextField) dateChooserHiredate.getDateEditor().getUiComponent())
						.setText(transFormat.format(model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 5)));						
					}else {
						((JTextField) dateChooserHiredate.getDateEditor().getUiComponent())
						.setText("");	
					}
				}
				if (i == 6) {
					if((Date)model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6)!=null) {
					((JTextField) dateChooserBirthday.getDateEditor().getUiComponent())
					.setText(transFormat.format(model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 6)));
					}else {
						((JTextField) dateChooserBirthday.getDateEditor().getUiComponent())
						.setText("");
					}
				}
				if (i == 7) {
					textFieldPhone.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 7));
				}
				if (i == 8) {
					textFieldAddress.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 8));
				}
			
				if (i == 9) {
					textFieldImage.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 9));
					File file = new File(textFieldImage.getText());			
					if(file.exists()) {
					getimage = ((String)model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()),9));				
					}else {
						getimage = "";
					}
					setEmployeeImage();
					
				}
				if (i == 10) {
					textFieldPassword.setText((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 10));
				}
				if (i == 11) {					
					if(((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 11)).equals("0")) {
						comboBoxManagementCode.setSelectedIndex(1);				
					}else if(((String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 11)).equals("1")) {
						comboBoxManagementCode.setSelectedIndex(3);
					}else {
						comboBoxManagementCode.setSelectedIndex(2);
					}
				}
				
			}
			
		}
	}
	
	
	///이미지 뷰 메소드	
	private boolean setEmployeeImage() {		
		File f = new File(getimage);
		if (!f.exists()) {		
			panelImage.setVisible(false);
			return true;
		}else{		
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getimage);
		panelImage.repaint();
		panelImage.setVisible(true);
		return true;
		}
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
		String image = textFieldImage.getText();
		
		String deptname = (String)comboBoxDept.getSelectedItem();		
		String ManagementCode = (String)comboBoxManagementCode.getSelectedItem();
		String Position = (String)comboBoxPosition.getSelectedItem();
		
		////최초로 등록할때 사원번호 입력
		String empno = "10001"; //메인테이블
		String empno2 = "10001"; //연결테이블
		String checkQuery = "select emp_no from employee";
		try {result = stmt.executeQuery(checkQuery);
			while(result.next()) {//사원번호가 있으면
				empno = "(select max(emp_no)+1 from employee)";
				empno2 = "(select max(emp_no) from employee)";
			}
		}catch(Exception e) {			
		}		
		
		///입력값이 하나라도 널이면 에러
		File f = new File(image);		
		if(password.isEmpty()||name.isEmpty()||gender.isEmpty()||birthday.isEmpty()||
				hiredate.isEmpty()||phone.isEmpty()||address.isEmpty()||image.isEmpty()||
				deptname.isEmpty()||ManagementCode.isEmpty()||Position.isEmpty()||!f.exists()) {
			employeedialog();
			return;
		}
		
		String employeeInsertQuery = "INSERT INTO EMPLOYEE VALUES("
				+empno+","
				+"'"+password+"',"
				+"'"+name+"',"
				+"'"+gender+"',"
				+"'"+birthday+"',"
				+"'"+phone+"',"
				+"'"+address+"',"
				+"'"+hiredate+"',"
				+"'"+image+"')";
		

		
		String belong_DepartmentInsertQuery = "INSERT INTO BELONG_DEPARTMENT VALUES ("
				+ empno2+","
				+ "(select dep_code from department where dep_name = '"+deptname+"'))";
		
		String managerInsertQuery = "INSERT INTO MANAGER VALUES ("
				+ empno2+","
				+ "(select man_code from management_list where man_name = '"+ManagementCode+"'))";
		
		String employee_PositionInsertQuery = "INSERT INTO EMPLOYEE_POSITION VALUES ("
				+ empno2+","
				+ "(select pos_code from position where pos_name = '"+Position+"'))";
		String commit = "COMMIT";

		
		try {
			
			stmt.executeUpdate(employeeInsertQuery);
			stmt.executeUpdate(belong_DepartmentInsertQuery);
			stmt.executeUpdate(managerInsertQuery);
			stmt.executeUpdate(employee_PositionInsertQuery);
			stmt.executeQuery(commit);
			
			
			
			//사진등록을 위한 사원번호 알아오는 코드/
			String empNo = "SELECT MAX(EMP_NO) FROM EMPLOYEE";		
			String getEmpNo = null;
			String location= System.getProperty("user.dir") + "\\image\\";
			result = stmt.executeQuery(empNo);			
			while (result.next()){
				getEmpNo =result.getString(1);				
			}			
		      //파일을 DB 이미지 폴더에 복사	          
	           if(f.exists()) {//사진이 있다면
	        	   try {  		   			
	        		   	FileInputStream fis = new FileInputStream(image);  // 원본파일
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
	     	           String updateQuery = "UPDATE EMPLOYEE SET EMP_IMAGE = '"+location+getEmpNo+".JPG'"
	     	           					+ " WHERE EMP_NO ="+getEmpNo;	        		   	
	     	          stmt.executeUpdate(updateQuery);
	     	          stmt.executeQuery(commit);
	        	   }catch(Exception e) {
	        		   employeedialog();
	        		   
	        	   }         
	           }	      
	           employeedialogInsert();
	         
		}catch(Exception e) {
			employeedialog();
			
		}	
		
	}
	
	///삭제메소드
	public void employeeDelete() {
		ResultSet result;
		String empNo = labelEmpNoView.getText();
		String Image = textFieldImage.getText();
		
		String employeeDeleteQuery = "DELETE FROM EMPLOYEE WHERE EMP_NO ="+empNo;
		String belong_DepartmentDeleteQuery = "DELETE FROM BELONG_DEPARTMENT WHERE EMP_NO ="+empNo;
		String managerDeleteQuery = "DELETE FROM MANAGER WHERE EMP_NO ="+empNo;
		String employee_PositionDeleteQuery = "DELETE FROM EMPLOYEE_POSITION WHERE EMP_NO ="+empNo;
		String commit = "COMMIT";
		
		try {
			stmt.executeUpdate(employeeDeleteQuery);
			stmt.executeUpdate(belong_DepartmentDeleteQuery);
			stmt.executeUpdate(managerDeleteQuery);
			stmt.executeUpdate(employee_PositionDeleteQuery);
			 stmt.executeQuery(commit);
			
			//사진 파일이 있다면 사진파일 삭제
			File file = new File(Image);
			if(file.exists()) {				
				file.delete();
			}
			employeedialogDelete();
		}catch(Exception e) {
			employeedialog();
		}
		
	}
	
	
	//수정메소드
	public void employeeUpdate() {
		ResultSet result;
		
		String empNo = labelEmpNoView.getText();
		String password = textFieldPassword.getText();
		String name = textFieldName.getText();
		String gender = (String)comboBoxGender.getSelectedItem();
		String birthday = ((JTextField) dateChooserBirthday.getDateEditor().getUiComponent()).getText();
		String hiredate = ((JTextField) dateChooserHiredate.getDateEditor().getUiComponent()).getText();	
		String phone = textFieldPhone.getText();
		String address = textFieldAddress.getText();
		String image = textFieldImage.getText();
		
		
	    //이미지 수정 경로 스트링
		String updateImage = image;		
		String location= System.getProperty("user.dir") + "\\image\\";		
		
		
		
		
		
		String employeeUpdateQuery = "UPDATE EMPLOYEE SET "
									+" EMP_PASSWORD = '"+password+"',"
									+" EMP_NAME = '"+name+"',"
									+" EMP_GENDER = '"+gender+"',"
									+" EMP_BIRTHDAY = '"+birthday+"',"
									+" EMP_MOBILE = '"+phone+"',"
									+" EMP_ADDRESS = '"+address+"',"
									+" EMP_JOINDAY = '"+hiredate+"',"
									+" EMP_IMAGE ='"+updateImage+"'"
									+" WHERE EMP_NO ="+ empNo;
		
		
		try {
			if(password.isEmpty()||name.isEmpty()||gender.isEmpty()||birthday.isEmpty()||phone.isEmpty()||
					address.isEmpty()||hiredate.isEmpty()||updateImage.isEmpty()){
				employeedialog();
			}else {
				///이미지파일 수정 코드
				try {
					File file = new File(image);
					if(file.exists()) {//이미지파일이 있다면			
					//저장할파일
					File updateFile = new File(location+empNo+".JPG"); 
					BufferedImage bi = ImageIO.read(file);
					ImageIO.write(bi, "JPG", updateFile);		
					updateImage = location+empNo+".JPG";
					getimage = updateImage;
					}
					}catch(Exception e) {			
						employeedialog();
					}				
			stmt.executeUpdate(employeeUpdateQuery);
			stmt.executeUpdate("COMMIT");
			employeedialogUpdate();}
		} catch (SQLException e) {			
			employeedialog();
		}
		
	}
	
	
	//이미지 선택
	public void fileChoice() {
		JFileChooser fileChooser = new JFileChooser();
		//JPG 파일만 불러오기
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Image", "jpg");
		fileChooser.addChoosableFileFilter(filter);
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
           panelImage.setVisible(true);   		
         			   			
       }
	//에러메세지
	public void employeedialog() {	
            JOptionPane.showMessageDialog(null, "다시확인하여 주세요.","경고",JOptionPane.WARNING_MESSAGE);            
        
	}
	//등록완료 메세지
	public void employeedialogInsert() {	
        JOptionPane.showMessageDialog(null, "등록되었습니다.","완료",JOptionPane.WARNING_MESSAGE);            
    
}
	//수정완료 메세지
	public void employeedialogUpdate() {	
        JOptionPane.showMessageDialog(null, "수정되었습니다.","완료",JOptionPane.WARNING_MESSAGE);            
    
}
	//삭제완료 메세지
	public void employeedialogDelete() {	
        JOptionPane.showMessageDialog(null, "삭제되었습니다.","완료",JOptionPane.WARNING_MESSAGE);            
    
}
}
