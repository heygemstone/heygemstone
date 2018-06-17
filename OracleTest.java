package Database01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTest {

	public static void main(String args[])
    {
        Connection conn = null; // DB����� ����(����)�� ���� ��ü
        PreparedStatement pstm = null;  // SQL ���� ��Ÿ���� ��ü
        ResultSet rs = null;  // �������� �����Ϳ� ���� ��ȯ���� ���� ��ü
        
        try {
            // SQL ������ ����� ���� ������ ���Ǿ�(SELECT��)���
            // �� ����� ���� ResulSet ��ü�� �غ��� �� �����Ų��.
            String quary = "SELECT �̽ʴ�, CREDIT, ����,  count(*) FROM (SELECT CASE  \r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 5000 THEN '1���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 4000 THEN '2���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 3000 THEN '3���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 2000 THEN '4���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 1000 THEN '5���'\r\n" + 
            		"                      ELSE '6���'\r\n" + 
            		"                      END AS CREDIT,\r\n" + 
            		"                      CASE \r\n" + 
            		"                      WHEN FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
            		"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 \r\n" + 
            		"                      THEN '�̽ʴ�' END AS �̽ʴ�,\r\n" + 
            		"                      CASE WHEN ADDRESS1 LIKE '��� ������%' \r\n" + 
            		"                      THEN '������ ����' END AS ���� \r\n" + 
            		"                      FROM CUSTOMER)\r\n" + 
            		"                      WHERE �̽ʴ� IS NOT NULL AND ���� IS NOT NULL\r\n" + 
            		"                      GROUP BY �̽ʴ�, CREDIT, ����\r\n" + 
            		"                      ORDER BY CREDIT";
            
            conn = DBConnection.getConnection();
            pstm = conn.prepareStatement(quary);
            rs = pstm.executeQuery();
            
            /*  CUSTOMER ���̺� ������ Ÿ��
             * 
                ID           NOT NULL VARCHAR2(20)  
				PWD          NOT NULL VARCHAR2(20)  
				NAME         NOT NULL VARCHAR2(20)  
				ZIPCODE               VARCHAR2(7)   
				ADDRESS1              VARCHAR2(100) 
				ADDRESS2              VARCHAR2(100) 
				MOBILE_NO             VARCHAR2(14)  
				PHONE_NO              VARCHAR2(14)  
				CREDIT_LIMIT          NUMBER(9,2)   
				EMAIL                 VARCHAR2(20)  
				ACCOUNT_MGR           NUMBER(4)     
				BIRTH_DT              DATE          
				ENROLL_DT             DATE          
				GENDER                VARCHAR2(1)   
            */
            
            System.out.println("AGE    CREDIT ADDRESS    COUNT");
            System.out.println("============================================");
            
            while(rs.next()){
                //int empno = rs.getInt("empno"); ���� ��� �÷� �̸��� ��� �ȴ�.
                String age = rs.getString(1);
                String credit  = rs.getString(2);
                //java.sql.Date BIRTH_DT   = rs.getDate(3); // Date Ÿ�� ó��
                String address  = rs.getString(3);             
                int count   = rs.getInt(4);
          
                String result = age+" "+credit+" "+address+" "+count;
                System.out.println(result);
            }
            
        } catch (SQLException sqle) {
            System.out.println("SELECT������ ���� �߻�");
            sqle.printStackTrace();
            
        }finally{
            // DB ������ �����Ѵ�.
            try{
                if ( rs != null ){rs.close();}   
                if ( pstm != null ){pstm.close();}   
                if ( conn != null ){conn.close(); }
            }catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
            
        }
    }
}
 


