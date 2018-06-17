package Database01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTest {

	public static void main(String args[])
    {
        Connection conn = null; // DB연결된 상태(세션)을 담은 객체
        PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
        ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
        
        try {
            // SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
            // 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
            String quary = "SELECT 이십대, CREDIT, 성남,  count(*) FROM (SELECT CASE  \r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 5000 THEN '1등급'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 4000 THEN '2등급'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 3000 THEN '3등급'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 2000 THEN '4등급'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 1000 THEN '5등급'\r\n" + 
            		"                      ELSE '6등급'\r\n" + 
            		"                      END AS CREDIT,\r\n" + 
            		"                      CASE \r\n" + 
            		"                      WHEN FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
            		"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 \r\n" + 
            		"                      THEN '이십대' END AS 이십대,\r\n" + 
            		"                      CASE WHEN ADDRESS1 LIKE '경기 성남시%' \r\n" + 
            		"                      THEN '성남시 거주' END AS 성남 \r\n" + 
            		"                      FROM CUSTOMER)\r\n" + 
            		"                      WHERE 이십대 IS NOT NULL AND 성남 IS NOT NULL\r\n" + 
            		"                      GROUP BY 이십대, CREDIT, 성남\r\n" + 
            		"                      ORDER BY CREDIT";
            
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
            
            System.out.println("AGE    CREDIT ADDRESS    COUNT");
            System.out.println("============================================");
            
            while(rs.next()){
                //int empno = rs.getInt("empno"); 숫자 대신 컬럼 이름을 적어도 된다.
                String age = rs.getString(1);
                String credit  = rs.getString(2);
                //java.sql.Date BIRTH_DT   = rs.getDate(3); // Date 타입 처리
                String address  = rs.getString(3);             
                int count   = rs.getInt(4);
          
                String result = age+" "+credit+" "+address+" "+count;
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
 


