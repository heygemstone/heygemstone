package Database01;

import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Jdbc_Csv {

	public static String fieldMod(String[] field) {
		
		String newLine = ""; // readtxt를 초기화시켜줌
		
		for (int i=0; i < 4 ; i++ ) { // readtxtArr 배열의 길이만큼 돌려줄 for문
			if (i < 3) {	// 마지막 필드 전까진 콤마까지 넣어서 돌림
				newLine += field[i] + ",";
			} else {					// 마지막 필드는 콤마 넣지않고 돌림	
				newLine += field[i];
			}
		}
		return newLine;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver"); // 데이터베이스 매니지먼트 시스템에 접근하기 위해... 적절한 드라이버 클래스를 로드해 오는 것 
		System.out.println("드라이버 로딩 성공");
		} catch (Exception ex) {
		System.out.println("드라이버 로딩 실패");
		}
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.26.72:1521/DCSWCDB", "C##03", "CD03");
		// Connection 은 외부 데이터베이스와 연결하는 객체. JDBC를 통해 오라클과 연결...
		Statement stmt = conn.createStatement();  // Statement 는 연결된 데이터베이스의 자료와의 사이에 쿼리작업을 수행하는 객체
		
		String QueryTxt = "SELECT NAME, ADDRESS1, GENDER, CASE  \r\n" + 
				"                      WHEN CREDIT_LIMIT >= 5000 THEN '1등급'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 4000 THEN '2등급'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 3000 THEN '3등급'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 2000 THEN '4등급'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 1000 THEN '5등급'\r\n" + 
				"                      ELSE '6등급'\r\n" + 
				"                      END AS CREDIT   \r\n" + 
				"                      FROM CUSTOMER\r\n" + 
				"                      WHERE FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
				"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 AND\r\n" + 
				"                      ADDRESS1 LIKE '경기 성남시%'\r\n" + 
				"                      ORDER BY GENDER";    // "" 안에 절대로 ; 넣지 말 것!
	
		ResultSet rset = stmt.executeQuery(QueryTxt); // executeQuery 는 쿼리를 날려주는 메소드( Statement 클래스 내에 있음 ). 결과를 ResultSet 객체로 받음
		
		String[] fieldName = { "NAME", "ADDRESS", "GENDER", "CREDIT" };    // 여기에 컬럼헤딩이나 Alias 자기 꺼 넣으시오   !!!!!!!
		
		File f = new File("C:\\Users\\kopo\\Desktop\\ MinCsvREPORT.csv"); // 만들어줄 파일 객체 생성
		BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // 버퍼드라이터 객체 생성
		bw.write(fieldMod(fieldName)); bw.newLine();

		int lineCnt=0;    // rset.getString(1)
		
		while(rset.next()) {
			String[] field = {rset.getString(1), rset.getString(2), rset.getString(3), rset.getString(4)};
			bw.write(fieldMod(field)); bw.newLine();
			
			lineCnt++;
		}
		
		bw.close();
		rset.close(); // ResultSet 객체 닫아줌
		stmt.close(); // Statement 객체 닫아줌
		conn.close(); // Connection 객체 닫아줌
		// 명시적으로 닫아주지 않으면 메모리 부족해 질 수 있다.
		// 닫을 때는 또 역순으로 닫아줘야 한다.	
        
	}
}
