package Database01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTest02 {

	public static void main(String args[])
    {
        Connection conn = null; // DB����� ����(����)�� ���� ��ü
        PreparedStatement pstm = null;  // SQL ���� ��Ÿ���� ��ü
        ResultSet rs = null;  // �������� �����Ϳ� ���� ��ȯ���� ���� ��ü
        
        try {
            // SQL ������ ����� ���� ������ ���Ǿ�(SELECT��)���
            // �� ����� ���� ResulSet ��ü�� �غ��� �� �����Ų��.
            String quary = "SELECT ACCOUNT_MGR, COUNT(*) AS �ſ��������ڼ� FROM CUSTOMER    \r\n" + 
            		"WHERE CREDIT_LIMIT < 1000 AND\r\n" + 
            		"      (SUBSTR(BIRTH_DT,1,1) = '9' OR SUBSTR(BIRTH_DT,1,2) = '89') AND\r\n" + 
            		"      ADDRESS1 LIKE '��� ������%'  \r\n" + 
            		"GROUP BY ACCOUNT_MGR;";
            
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
            
            System.out.println("ACCOUNT_MGR  �ſ��������ڼ�");
            System.out.println("============================================");
            
            while(rs.next()){
                //int empno = rs.getInt("empno"); ���� ��� �÷� �̸��� ��� �ȴ�.
                int ACCOUNT_MGR = rs.getInt(1);
                int CREDIT  = rs.getInt(2);
                
                String result = ACCOUNT_MGR+" "+CREDIT;
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
 


