package projectpackage;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

// 프로그램 시작 및 정적필드, 정적메소드 모음
public class MainStart {
	// 가장 먼저 로그인 화면을 출력
	public static LoginFrame classLogin;
	// 메인 화면
	public static MainFrame classMain;

	// 로그인 하는 사원의 정보..
	public static String emp_no; // 사원번호
	public static String emp_name; // 사원이름
	public static String emp_image; // 사원이미지경로
	public static String dep_code; // 부서코드
	public static String dep_name; // 부서이름
	public static String man_code; // 관리코드
	public static String pos_code; // 직책코드
	public static String pos_name; // 직책이름

	// 프로그램 시작. 로그인 화면을 출력
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					classLogin = new LoginFrame();
					classLogin.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// jdbc를 활용해서 oracle에 접속
	// return: DB 접속 정보를 가진 Statement class instance
	public static Statement connectDataBase() {
		try {

			// 접속 정보를 가진 ini파일을 얻음
			Properties prop = getIniFile();
			String url = prop.getProperty("url");
			String id = prop.getProperty("id");
			String pw = prop.getProperty("pw");
			
			Statement stmt;

			Connection conn = DriverManager.getConnection(url, id, pw);
			stmt = conn.createStatement();

			return stmt;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 테이블의 필드값이 1씩 증가한다면 사용
	// parameter: stmt(DB 접속 정보를 가진 Statement) tableNm(해당 테이블 이름), fieldNm(해당 필드 이름)
	// return: 테이블에서 필드가 가진 값의 최대값 + 1
	public static String getNewMaxCode(Statement stmt, String tableNm, String fieldNm) {
		String retValue = "1";
		String query = "select max(" + fieldNm + ") + 1 " + "from " + tableNm;

		try {
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

	private static Properties getIniFile() {
		Properties prop = new Properties();
		// 이클립스에서 user.dir은 project 경로
		String filePath = System.getProperty("user.dir");

		// 파일이 존재하는가
		File f = new File(filePath + "\\IniFile.ini");
		if (!f.exists()) {
			return null;
		}
		
		try {
			prop.load(new FileInputStream(filePath + "\\IniFile.ini"));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			 
			e.printStackTrace();
		}
		
		return prop;
	}

}
