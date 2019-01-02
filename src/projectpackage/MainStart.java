package projectpackage;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainStart {
	// 가장 먼저 로그인 창을 띄운다
	private static LoginFrame frmLogin;

	// 로그인 하는 사원의 정보..
	public static String emp_no; // 사원번호
	public static String emp_name; // 사원이름
	public static String emp_image; // 사원이미지경로
	public static String dep_code; // 부서코드
	public static String dep_name; // 부서이름
	public static String man_code; // 관리코드
	public static String pos_code; // 직책코드
	public static String pos_name; // 직책이름

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmLogin = new LoginFrame();
					frmLogin.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static Statement connectDataBase() {
		try {
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
			String id = "javaproject";
			String pw = "java";
			Statement stmt;

			Connection conn = DriverManager.getConnection(url, id, pw);
			stmt = conn.createStatement();

			return stmt;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getNewMaxCode(Statement stmt, String tableNm, String fieldNm) {
		String retValue = "1";
		String query = "select max(" + fieldNm + ") + 1 " + "from " + tableNm;
		
		try	{
		ResultSet result = stmt.executeQuery(query);
		result.next();
		if (result.getString(1) != null) {
			retValue = result.getString(1);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retValue;
	}

}
