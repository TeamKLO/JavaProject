package projectpackage;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;

// 결재 화면
public class ApprovalFrame extends JDialog {
	// static JFrame 필드는 단지 main의 EventQueue안의 생성자를 만족시키기 위한 것일 뿐, 프로그램 실행과 상관없음
	static JFrame tmpFrame;

	private JDateChooser dtcSDate;
	private JDateChooser dtcEDate;
	private JComboBox cbxCategory;
	private JComboBox cbxState;
	private JTextArea txtContent;
	private JButton btnDraft;
	private JButton btnClose;
	private JButton btnConfirm;
	private JButton btnDelete;

	private Statement stmt;

	// JTable을 위한 DefaultTableModel을 생성하는데, 타이틀이 없어도 쿼리문에서 정해질 듯 하지만 일단 넣어줌
	String[] colTitle = { "작성날짜", "결재유형", "제목", "내용", "결재여부", "승인날짜", "결재자", "결재코드" };
	DefaultTableModel defaultTableModel = new DefaultTableModel(colTitle, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel lblBackImg;
	private JLabel lblCloseX;
	int xx, xy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApprovalFrame dialog = new ApprovalFrame(tmpFrame);
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
	// constructor에서 modal에 관한 설정을 해줌 owner가 되는 frame을 parameter로 받아서 사용
	public ApprovalFrame(JFrame frame) {
		// 부모가 되는 JDialog의 owner는 frame, modal은 true이므로 결재 화면은 모달로 동작
		super(frame, true);
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
		getContentPane().setBackground(new Color(240, 240, 240));

		setTitle("나의 기안 및 결재");

		setBounds(100, 100, 661, 700);
		// frame이 생성될 때 위치는 frame의 중앙
		setLocationRelativeTo(frame);

		this.setUndecorated(true);

		// frame이 close 될 때의 설정
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		dtcSDate = new JDateChooser();
		dtcSDate.setBackground(new Color(255, 255, 224));
		// JDateChooser의 값이 변할 때 일어나는 이벤트
		setDateChooserChangeEvent(dtcSDate);
		dtcSDate.setDateFormatString("yyyy-MM-dd");
		dtcSDate.setBounds(48, 58, 172, 36);
		getContentPane().add(dtcSDate);

		dtcEDate = new JDateChooser();
		dtcEDate.setBackground(new Color(255, 255, 224));
		// JDateChooser의 값이 변할 때 일어나는 이벤트
		setDateChooserChangeEvent(dtcEDate);
		dtcEDate.setDateFormatString("yyyy-MM-dd");
		dtcEDate.setBounds(274, 58, 172, 36);
		getContentPane().add(dtcEDate);

		cbxCategory = new JComboBox();
		cbxCategory.setBackground(new Color(255, 255, 224));
		cbxCategory.setFont(new Font("굴림체", Font.BOLD, 12));
		// JComboBox 상태가 변할 때 일어나는 이벤트
		cbxCategory.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					// 항목이 선택되었다면 테이블을 다시 세팅
					setTable();
				}
			}
		});
		cbxCategory.setBounds(48, 117, 172, 36);
		cbxCategory.setModel(new DefaultComboBoxModel(new String[] { "전체", "일정", "기획", "구매" }));
		getContentPane().add(cbxCategory);

		cbxState = new JComboBox();
		cbxState.setBackground(new Color(255, 255, 224));
		cbxState.setFont(new Font("굴림체", Font.BOLD, 12));
		// JComboBox 상태가 변할 때 일어나는 이벤트
		cbxState.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 항목이 선택되었다면 테이블을 다시 세팅
					setTable();
				}
			}
		});
		cbxState.setBounds(274, 117, 172, 36);
		cbxState.setModel(new DefaultComboBoxModel(new String[] { "전체", "대기", "승인", "반려" }));
		getContentPane().add(cbxState);

		btnDraft = new JButton("기안 올리기");
		btnDraft.setBackground(new Color(255, 255, 224));
		btnDraft.setForeground(Color.BLACK);
		btnDraft.setFont(new Font("굴림체", Font.BOLD, 12));
		btnDraft.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnDraft.doClick();
				}
			}
		});
		btnDraft.setBounds(48, 629, 110, 30);
		btnDraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 기안 화면을 생성
				// modal의 owner로서 Window를 argument로 넘김
				DraftFrame window = new DraftFrame(ApprovalFrame.this);
				window.setVisible(true);
			}
		});
		getContentPane().add(btnDraft);

//		lblCloseX = new JLabel("X");
//		lblCloseX.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				ApprovalFrame.this.dispose();
//			}
//		});
//
//		lblCloseX.setFont(new Font("Euphemia", Font.BOLD, 18));
//		lblCloseX.setForeground(Color.WHITE);
//		lblCloseX.setHorizontalAlignment(SwingConstants.CENTER);
//		lblCloseX.setBounds(622, 8, 42, 20);
//		this.getContentPane().add(lblCloseX);

		btnClose = new JButton("닫 기");
		btnClose.setBackground(new Color(255, 255, 224));
		btnClose.setForeground(Color.BLACK);
		btnClose.setFont(new Font("굴림체", Font.BOLD, 12));
		btnClose.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnClose.doClick();
				}
			}
		});
		btnClose.setBounds(507, 629, 110, 30);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApprovalFrame.this.dispose();
			}
		});
		getContentPane().add(btnClose);

		btnConfirm = new JButton("결재 하기");
		btnConfirm.setBackground(new Color(255, 255, 224));
		btnConfirm.setForeground(Color.BLACK);
		btnConfirm.setFont(new Font("굴림체", Font.BOLD, 12));
		btnConfirm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					btnConfirm.doClick();
				}
			}
		});
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 결재 화면을 생성
				// modal의 owner로서 Window를 argument로 넘김
				ConfirmFrame window = new ConfirmFrame(ApprovalFrame.this);
				window.setVisible(true);
			}
		});
		btnConfirm.setBounds(199, 629, 110, 30);
		getContentPane().add(btnConfirm);

		scrollPane = new JScrollPane();
		// JScrollPane의 스크롤바 설정
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(48, 190, 564, 225);
		getContentPane().add(scrollPane);

		// JTable을 DefaultTableModel로 제어
		table = new JTable(defaultTableModel);
		// JTable의 컬럼사이즈 자동변경을 중지
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					// 마우스 클릭시 테이블에 값이 있다면 txtContent에 입력
					txtContent.setText(defaultTableModel.getValueAt(table.getSelectedRow(), 3).toString());
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnTableRefresh = new JButton("나의 기안 보기");
		btnTableRefresh.setBackground(new Color(255, 255, 224));
		btnTableRefresh.setFont(new Font("굴림체", Font.BOLD, 12));
		btnTableRefresh.setVisible(false);
		btnTableRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTable();
			}
		});
		btnTableRefresh.setBounds(477, 117, 172, 36);
		getContentPane().add(btnTableRefresh);

		JLabel label = new JLabel("~");
		label.setFont(new Font("굴림체", Font.BOLD, 40));
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(216, 68, 61, 21);
		getContentPane().add(label);

		btnDelete = new JButton("기안 삭제");
		btnDelete.setBackground(new Color(255, 255, 224));
		btnDelete.setForeground(Color.BLACK);
		btnDelete.setFont(new Font("굴림체", Font.BOLD, 12));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (defaultTableModel.getRowCount() > 0) {
					// 기안 삭제
					deleteDraft();
				} else {
					JOptionPane.showMessageDialog(ApprovalFrame.this, "삭제할 기안이 없습니다");
				}
			}
		});
		btnDelete.setBounds(351, 629, 110, 30);
		getContentPane().add(btnDelete);
		
		txtContent = new JTextArea();
		txtContent.setEditable(false);
		txtContent.append("\r\n");
		txtContent.setCaretPosition(txtContent.getText().length());
		
		
		JScrollPane scrollPane_1 = new JScrollPane(txtContent);
		scrollPane_1.setBounds(48, 438, 564, 100);
		getContentPane().add(scrollPane_1);
		scrollPane_1.setViewportView(txtContent);
				

		lblBackImg = new JLabel("lblBackImg");
		lblBackImg.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\BackImg.jpg"));
		lblBackImg.setBounds(0, 0, 661, 700);
		getContentPane().add(lblBackImg);



		// 화면에 나타나는 값 초기화
		initDisplay();

	}

	// 화면에 나타나는 값 초기화
	void initDisplay() {
		// JDateChooser 값 초기화
		initDateChooser();
		// JComboBox 값 초기화
		initComboBox();
		// 데이터베이스 연결은 필요에 따라 어디에든 붙일 수 있다.
		stmt = MainStart.connectDataBase();
		// DB자료를 테이블에 가져와서 보여줌
		setTable();
		// 테이블의 column 정렬
		setTableAlignment(table);
	}

	// JDateChooser 값 초기화
	void initDateChooser() {
		// 기간은 31전 부터 오늘까지
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -31);
		dtcSDate.setDate(cal.getTime());
		dtcEDate.setDate(new Date());

		// 텍스트 입력은 막음
		JTextFieldDateEditor sEditor = (JTextFieldDateEditor) dtcSDate.getDateEditor();
		JTextFieldDateEditor eEditor = (JTextFieldDateEditor) dtcEDate.getDateEditor();
		sEditor.setEditable(false);
		eEditor.setEditable(false);

	}

	// JComboBox 값 초기화
	void initComboBox() {
		cbxCategory.setSelectedIndex(0);
		cbxState.setSelectedIndex(0);
	}

	// DB자료를 테이블에 가져와서 보여줌
	void setTable() {
		// 날짜별, 항목별, 결재상태별로 현재의 사원이 기안한 자료를 가져옴
		String query = "select app_draftdate as 작성날짜, c.cat_name as 결재유형, app_title as 제목, app_content as 내용, "
				+ "app_isconfirm as 결재여부, app_confirmdate as 승인날짜, app_confirmer as 결재자, app_code as 결재코드 "
				+ "from EMPLOYEE a, APPROVAL b, APPROVAL_CATEGORY c "
				+ "where a.emp_no = b.emp_no and b.cat_code = c.cat_code " + "and a.emp_no = " + MainStart.emp_no
				+ " and app_draftdate >= to_date('" + ((JTextField) dtcSDate.getDateEditor().getUiComponent()).getText()
				+ " 00:00', 'yyyy-mm-dd hh24:mi') and " + "app_draftdate <= to_date('"
				+ ((JTextField) dtcEDate.getDateEditor().getUiComponent()).getText()
				+ " 23:59', 'yyyy-mm-dd hh24:mi') ";

		String[] addString = new String[2];

		// 쿼리문은 위에서 끝나지 않기에 조건에 따라 이어붙임
		if (cbxCategory.getSelectedIndex() > 0) {
			addString[0] = " and c.cat_code = " + Integer.toString(cbxCategory.getSelectedIndex());
		} else {
			addString[0] = "";
		}
		if (cbxState.getSelectedIndex() > 0) {
			addString[1] = " and app_isconfirm = '" + cbxState.getSelectedItem().toString() + "'";
		} else {
			addString[1] = "";
		}

		query = query + addString[0] + addString[1] + " order by app_draftdate";

		try {
			txtContent.setText("");
			defaultTableModel.setRowCount(0);

			ResultSet result = this.stmt.executeQuery(query);
			ResultSetMetaData resultSetMetaData = result.getMetaData();
			Object[] tmpObject = new Object[resultSetMetaData.getColumnCount()];

			while (result.next()) {
				for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
					tmpObject[i] = result.getString(i + 1);
					// 쿼리에서 가져온 필드 중에 첫 번째와 여섯 번째는 yyyy-dd-mm 10자리로 제한
					if (i == 0 || i == 5) {
						if (result.getString(i + 1) != null) {
							tmpObject[i] = result.getString(i + 1).substring(0, 10);
						}
					}

				}

				defaultTableModel.addRow(tmpObject);
			}

			if (defaultTableModel.getRowCount() > 0) {
				// 테이블에 값이 있다면 첫 번째 Row를 선택
				table.setRowSelectionInterval(0, 0);
				// 선택된 값을 txtContent에 입력
				txtContent.setText(defaultTableModel.getValueAt(table.getSelectedRow(), 3).toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 테이블의 column 정렬
	void setTableAlignment(JTable pTable) {
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer pTableCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		pTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmTable = pTable.getColumnModel();

		// 지정한 column을 가운데 정렬
		tcmTable.getColumn(0).setCellRenderer(pTableCellRenderer);
		tcmTable.getColumn(1).setCellRenderer(pTableCellRenderer);
		tcmTable.getColumn(4).setCellRenderer(pTableCellRenderer);
		tcmTable.getColumn(5).setCellRenderer(pTableCellRenderer);
		tcmTable.getColumn(6).setCellRenderer(pTableCellRenderer);
		tcmTable.getColumn(7).setCellRenderer(pTableCellRenderer);

	}

	// JDateChooser의 값이 변할 때 일어나는 이벤트
	void setDateChooserChangeEvent(JDateChooser tDateChooser) {
		// parameter인 JDateChooser에 PropertyChangeListener를 추가하는 것으로 보이는 메소드를 호출
		// 결론적으로 JDateChooser의 Property가 변할 때 어떤 일을 해줄 것인가를 추가하는 것
		tDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			// annotation은 없지만 override로 보여짐
			public void propertyChange(PropertyChangeEvent evt) {
				// JDateChooser의 Property중 date라는 놈이 변할 때
				if ("date".equals(evt.getPropertyName())) {
					JDateChooser aDateChooser = (JDateChooser) evt.getSource();
					boolean isDateSelectedByUser = false;
					// Get the otherwise unaccessible JDateChooser's 'dateSelected' field.
					try {
						// Get the desired field using reflection
						Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
						// This line makes the value accesible (can be read and/or modified)
						dateSelectedField.setAccessible(true);
						isDateSelectedByUser = dateSelectedField.getBoolean(aDateChooser);
					} catch (Exception ignoreOrNot) {
					}

					// Do some important stuff depending on wether value was changed by user
					if (isDateSelectedByUser) {
						setTable();
					}

					// Reset the value to false
					try {
						Field dateSelectedField = JDateChooser.class.getDeclaredField("dateSelected");
						dateSelectedField.setAccessible(true);
						dateSelectedField.setBoolean(aDateChooser, false);
					} catch (Exception ignoreOrNot) {
					}
				}
			}
		});
	}

	// 기안 삭제
	void deleteDraft() {
		// 값이 "대기"인 경우에만 삭제가능
		if ((defaultTableModel.getValueAt(table.getSelectedRow(), 4).toString().equals("대기"))) {
			try {
				String aCode = defaultTableModel.getValueAt(table.getSelectedRow(), 7).toString();
				// 대기인 기안을 DB에서 삭제
				String query = "delete from APPROVAL " + "where app_code = " + aCode;

				this.stmt.executeQuery(query);
				JOptionPane.showMessageDialog(this, "삭제 했습니다");
				// DB자료를 테이블에 가져와서 보여줌
				setTable();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(this, "삭제할 수 없습니다");
		}
	}
}
