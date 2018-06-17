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
		
		String newLine = ""; // readtxt�� �ʱ�ȭ������
		
		for (int i=0; i < 4 ; i++ ) { // readtxtArr �迭�� ���̸�ŭ ������ for��
			if (i < 3) {	// ������ �ʵ� ������ �޸����� �־ ����
				newLine += field[i] + ",";
			} else {					// ������ �ʵ�� �޸� �����ʰ� ����	
				newLine += field[i];
			}
		}
		return newLine;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver"); // �����ͺ��̽� �Ŵ�����Ʈ �ý��ۿ� �����ϱ� ����... ������ ����̹� Ŭ������ �ε��� ���� �� 
		System.out.println("����̹� �ε� ����");
		} catch (Exception ex) {
		System.out.println("����̹� �ε� ����");
		}
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.26.72:1521/DCSWCDB", "C##03", "CD03");
		// Connection �� �ܺ� �����ͺ��̽��� �����ϴ� ��ü. JDBC�� ���� ����Ŭ�� ����...
		Statement stmt = conn.createStatement();  // Statement �� ����� �����ͺ��̽��� �ڷ���� ���̿� �����۾��� �����ϴ� ��ü
		
		String QueryTxt = "SELECT NAME, ADDRESS1, GENDER, CASE  \r\n" + 
				"                      WHEN CREDIT_LIMIT >= 5000 THEN '1���'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 4000 THEN '2���'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 3000 THEN '3���'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 2000 THEN '4���'\r\n" + 
				"                      WHEN CREDIT_LIMIT >= 1000 THEN '5���'\r\n" + 
				"                      ELSE '6���'\r\n" + 
				"                      END AS CREDIT   \r\n" + 
				"                      FROM CUSTOMER\r\n" + 
				"                      WHERE FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
				"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 AND\r\n" + 
				"                      ADDRESS1 LIKE '��� ������%'\r\n" + 
				"                      ORDER BY GENDER";    // "" �ȿ� ����� ; ���� �� ��!
	
		ResultSet rset = stmt.executeQuery(QueryTxt); // executeQuery �� ������ �����ִ� �޼ҵ�( Statement Ŭ���� ���� ���� ). ����� ResultSet ��ü�� ����
		
		String[] fieldName = { "NAME", "ADDRESS", "GENDER", "CREDIT" };    // ���⿡ �÷�����̳� Alias �ڱ� �� �����ÿ�   !!!!!!!
		
		File f = new File("C:\\Users\\kopo\\Desktop\\ MinCsvREPORT.csv"); // ������� ���� ��ü ����
		BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // ���۵������ ��ü ����
		bw.write(fieldMod(fieldName)); bw.newLine();

		int lineCnt=0;    // rset.getString(1)
		
		while(rset.next()) {
			String[] field = {rset.getString(1), rset.getString(2), rset.getString(3), rset.getString(4)};
			bw.write(fieldMod(field)); bw.newLine();
			
			lineCnt++;
		}
		
		bw.close();
		rset.close(); // ResultSet ��ü �ݾ���
		stmt.close(); // Statement ��ü �ݾ���
		conn.close(); // Connection ��ü �ݾ���
		// ��������� �ݾ����� ������ �޸� ������ �� �� �ִ�.
		// ���� ���� �� �������� �ݾ���� �Ѵ�.	
        
	}
}
