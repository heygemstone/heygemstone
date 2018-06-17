package Database01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTest02 {

	public static void main(String args[])
    {
        Connection conn = null; // DB연결된 상태(세션)을 담은 객체
        PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
        ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
        
        try {
            // SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
            // 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
            String quary = "SELECT ACCOUNT_MGR, COUNT(*) AS 신용관리대상자수 FROM CUSTOMER    \r\n" + 
            		"WHERE CREDIT_LIMIT < 1000 AND\r\n" + 
            		"      (SUBSTR(BIRTH_DT,1,1) = '9' OR SUBSTR(BIRTH_DT,1,2) = '89') AND\r\n" + 
            		"      ADDRESS1 LIKE '경기 성남시%'  \r\n" + 
            		"GROUP BY ACCOUNT_MGR;";
            
            conn = DBConnection.getConnection();
            pstm = conn.prepareStatement(quary);
            rs = pstm.executeQuery();
            
            /*  CUSTOMER 테이블 데이터 타입
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
            
            System.out.println("ACCOUNT_MGR  신용관리대상자수");
            System.out.println("============================================");
            
            while(rs.next()){
                //int empno = rs.getInt("empno"); 숫자 대신 컬럼 이름을 적어도 된다.
                int ACCOUNT_MGR = rs.getInt(1);
                int CREDIT  = rs.getInt(2);
                
                String result = ACCOUNT_MGR+" "+CREDIT;
                System.out.println(result);
            }
            
        } catch (SQLException sqle) {
            System.out.println("SELECT문에서 예외 발생");
            sqle.printStackTrace();
            
        }finally{
            // DB 연결을 종료한다.
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
 


