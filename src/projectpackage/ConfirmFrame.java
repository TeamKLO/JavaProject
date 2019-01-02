package projectpackage;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ConfirmFrame extends JDialog {
	static JFrame tmpFrame;
	private JTable table;
	private JDateChooser dtcSDate;
	private JDateChooser dtcEDate;
	private JComboBox cbxCategory;
	private JComboBox cbxState;
	private JTextArea txtContent;
	
	private Statement stmt;
	
	String[] colTitle = {"작성날짜", "결재유형", "제목", "내용", "결재여부", "승인날짜", "결재코드"};
	
	DefaultTableModel defaultTableModel = new DefaultTableModel(colTitle, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JButton btnRefresh;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfirmFrame dialog = new ConfirmFrame(tmpFrame);
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
	public ConfirmFrame(Window frame) {
		super(frame, Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("내가 결재할 내용");
		setBounds(100, 100, 550, 450);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		dtcSDate = new JDateChooser();
		dtcSDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {
					JDateChooser aDateChooser = (JDateChooser) arg0.getSource();
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
		dtcSDate.setDateFormatString("yyyy-MM-dd");
		dtcSDate.setBounds(12, 10, 130, 21);
		getContentPane().add(dtcSDate);
		
		dtcEDate = new JDateChooser();
		dtcEDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
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
		dtcEDate.setDateFormatString("yyyy-MM-dd");
		dtcEDate.setBounds(169, 10, 130, 21);
		getContentPane().add(dtcEDate);
		
		cbxCategory = new JComboBox();
		cbxCategory.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					setTable();
				}
			}
		});
		cbxCategory.setModel(new DefaultComboBoxModel(new String[] {"전체", "일정", "기획", "구매"}));
		cbxCategory.setSelectedIndex(0);
		cbxCategory.setBounds(12, 50, 130, 21);
		getContentPane().add(cbxCategory);
		
		cbxState = new JComboBox();
		cbxState.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTable();
				}
			}
		});
		cbxState.setModel(new DefaultComboBoxModel(new String[] {"전체", "대기", "승인", "반려"}));
		cbxState.setSelectedIndex(0);
		cbxState.setBounds(169, 50, 130, 21);
		getContentPane().add(cbxState);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 81, 510, 158);
		getContentPane().add(scrollPane);
		
		table = new JTable(defaultTableModel);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					txtContent.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				}
			}
		});
		scrollPane.setViewportView(table);
		
		txtContent = new JTextArea();
		txtContent.setText("");
		txtContent.setBounds(12, 248, 510, 93);
		getContentPane().add(txtContent);
		
		JButton btnConfirm = new JButton("승인");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setConfirm();
			}
		});
		btnConfirm.setBounds(12, 364, 130, 23);
		getContentPane().add(btnConfirm);
		
		JButton btnReturn = new JButton("반려");
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setReturn();
			}
		});
		btnReturn.setBounds(169, 364, 130, 23);
		getContentPane().add(btnReturn);
		
		JButton btnClose = new JButton("닫기");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmFrame.this.dispose();
			}
		});
		btnClose.setBounds(392, 364, 130, 23);
		getContentPane().add(btnClose);
		
		btnRefresh = new JButton("결재 내용 보기");
		btnRefresh.setVisible(false);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTable();
			}
		});
		btnRefresh.setBounds(392, 49, 130, 23);
		getContentPane().add(btnRefresh);
		
		setDisplay();
	}
	
	void setDisplay() {
		setDateChooser();
		setComboBox();
		// 데이터베이스 연결은 필요에 따라 어디에든 붙일 수 있다.
		stmt = MainStart.connectDataBase();
		setTable();
	}
	
	void setDateChooser() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -7);
		dtcSDate.setDate(cal.getTime());
		dtcEDate.setDate(new Date());
	}
	
	void setComboBox() {
		cbxCategory.setSelectedIndex(0);
		cbxState.setSelectedIndex(0);
	}
	
	void setTable() {
		String query = "select app_draftdate as 작성날짜, c.cat_name as 결재유형, app_title as 제목, app_content as 내용, " +
					   "app_isconfirm as 결재여부, app_confirmdate as 승인날짜, app_code as 결재코드 " +
					   "from EMPLOYEE a, APPROVAL b, APPROVAL_CATEGORY c " + 
					   "where a.emp_no = b.emp_no and b.cat_code = c.cat_code " +
					   "and b.app_confirmer = " + MainStart.emp_no +
					   " and app_draftdate >= to_date('" + ((JTextField)dtcSDate.getDateEditor().getUiComponent()).getText() + " 00:00', 'yyyy-mm-dd hh24:mi') and " +
					   "app_draftdate <= to_date('" + ((JTextField)dtcEDate.getDateEditor().getUiComponent()).getText() + " 23:59', 'yyyy-mm-dd hh24:mi') ";					   		
				
		String[] addString = new String[2];
		
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
			
			
//			DefaultTableModel model = (DefaultTableModel)table.getModel();
//			model.setNumRows(0);
//			model.setRowCount(0);
			
			txtContent.setText("");
			defaultTableModel.setRowCount(0);
			
			ResultSet result = this.stmt.executeQuery(query);
			ResultSetMetaData resultSetMetaData = result.getMetaData();
			Object[] tmpObject = new Object[resultSetMetaData.getColumnCount()];			

			while (result.next()) {
				for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
					tmpObject[i] = result.getString(i + 1);
				}

				defaultTableModel.addRow(tmpObject);
//				model.addRow(tmpObject);
			}
			
			if (defaultTableModel.getRowCount() > 0) {
				table.setRowSelectionInterval(0, 0);
			}
			
			if (table.getSelectedRow() >= 0) {
				txtContent.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void setConfirm() {
		if (defaultTableModel.getRowCount() > 0 && (!table.getValueAt(table.getSelectedRow(), 4).toString().equals("승인"))) {
			try {
				String query = "update APPROVAL set " +
							   "app_isconfirm = '승인', " +
							   "app_confirmdate = sysdate " +
							   "where app_code = " + table.getValueAt(table.getSelectedRow(), 6).toString();
				
				this.stmt.executeQuery(query);
				JOptionPane.showMessageDialog(this, "승인 했습니다.");
				setTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			JOptionPane.showMessageDialog(this, "승인할 수 없습니다.");
		}
	}
	
	void setReturn() {
		if (defaultTableModel.getRowCount() > 0 && (!table.getValueAt(table.getSelectedRow(), 4).toString().equals("반려"))) {
			try {
				String query = "update APPROVAL set " +
							   "app_isconfirm = '반려', " +
							   "app_confirmdate = sysdate " +
							   "where app_code = " + table.getValueAt(table.getSelectedRow(), 6).toString();
				
				this.stmt.executeQuery(query);
				JOptionPane.showMessageDialog(this, "반려 했습니다.");
				setTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			JOptionPane.showMessageDialog(this, "반려할 수 없습니다.");
		}
	}
}
