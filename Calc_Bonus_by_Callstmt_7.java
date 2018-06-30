package Project;
import kopo.ctc.java.SaveLog.CommonSaveLog;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Calc_Bonus_by_Callstmt_7 {
//작성일 : 2018.06.28
//CallableStatement 객체를 사용한 성능개선
//CallableStatement 객체를 사용하면 Anonymous block과 Stored block을 
//jdbc프로그램내에서 pl/sql block을 사용할수 있게됨에따라
//개발 생산성과 실행 효율성을 높일수있다.
//작성자 : 김민
	
	
	public static void main(String[] args) {
		
		CommonSaveLog csl = new CommonSaveLog();
		
		Connection conn = null;
        CallableStatement cstmt = null;
       
        String url = "jdbc:oracle:thin:@192.168.23.98:1521:orcl";
        String id = "kopo03";
        String passwd = "koposw";
        String yyyymm = "201806";
        
        //int bonus = 0;
        //int Batchsize = 1000; // 배치사이즈 지정하기
        //int LoopCnt = 0; //루프 반복 횟수 카운트       
        
        try {
        	long starttime = System.currentTimeMillis(); //시작시간을 측정한다
        	//1)JDBC 드라이버를 로딩한다
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	//2)DBMS에 접속한다
        	conn = DriverManager.getConnection(url, id, passwd);
        	conn.setAutoCommit(false); //자동 commit을 꺼둔다.(off)
        	
        	//3)SQL을 생성하기
        	
        	String PLsql = ""+
        		  " DECLARE                                          " +
        	      " CURSOR  BONUS_CUR IS                             " +
        		  "    SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE " +
        	      "             WHERE JOB <> 'PRESIDENT' ;           " +
        		  "  V_EMPNO          EMP_LARGE.EMPNO%TYPE;          " +
        	      "  V_JOB            EMP_LARGE.JOB%TYPE;            " +
        	      "  V_DEPTNO         EMP_LARGE.DEPTNO%TYPE;         " +
        	      "  V_SAL            EMP_LARGE.SAL%TYPE;            " +
        	      "  V_BONUS          BONUS_LARGE.BONUS%TYPE;        " +
        	      "  BEGIN                                           " +
        	      "      OPEN         BONUS_CUR;                     " +
        	      "      LOOP                                        " +
        	      
        	      "  FETCH BONUS_CUR  INTO                           " +
        	      "  V_EMPNO,V_JOB,V_DEPTNO,V_SAL;                   " + //FETCH,가져오기
        	      "  EXIT  WHEN BONUS_CUR%NOTFOUND;                  " +
        	      
        	      "  IF V_DEPTNO=10 THEN                             " + //PL/SQL에서 보너스 계산하기
        	      "  V_BONUS := V_SAL*0.3;                           " +
        	      "  ELSIF V_DEPTNO=20 THEN                          " +
        	      "  V_BONUS := V_SAL*0.1;                           " +
        	      "  ELSIF V_DEPTNO=30 THEN                          " +
        	      "  V_BONUS := V_SAL*0.05;                          " +
        	      "  ELSIF V_DEPTNO=40 THEN                          " +
        	      "  V_BONUS := V_SAL*0.2;                           " +
        	      "  ELSE                                            " +
        	      "  V_BONUS := 0;                                   " +
        	      "  END IF;                                         " +
        	      
        	      "  INSERT INTO BONUS_LARGE(YYYYMM, EMPNO, JOB, DEPTNO, SAL, BONUS) " +
        	      "  VALUES(?, V_EMPNO, V_JOB, V_DEPTNO, V_SAL, ROUND(V_BONUS)); " +
        	      "         END LOOP;                                " +
        	      "         CLOSE BONUS_CUR;                         " +
        	      "         COMMIT;                                  " +
        	      "  END;   ";
        	
          cstmt = conn.prepareCall(PLsql);
          cstmt.setString(1, yyyymm);
          //JAVA에서 보너스 계산년월(yyyymm)을 parameter로
          //입력 받아 pl/sql블록에 전달하여 
          //pl/sql블록에서 월별 보너스 Batch처리해주기 
          
          //4) SQL실행하기(PL/SQL블록 전송하기)
          cstmt.execute(); //블록이 dbms로 전송되고 dbms내부에서 처리한후 jdbc로 넘어옴
     
            //경과 시간을 측정함
            long endtime = System.currentTimeMillis(); //종료시간 측정하기
            System.out.println("경과시간: " + (endtime - starttime) +"ms");
        
        }catch(Exception e) {
			
			//사용자 pc에 저장 메서드
			//csl.saveInUserComputer(e, "kopo03");  //메서드를 오버로딩해서 DES를 깜빡하고 안썼을때도 실행되도록 만들어 놓았음
			csl.saveInUserComputer(e, "kopo03", "DES");
			e.printStackTrace();
			//서버에 저장 메서드
            //csl.saveInCommonServer(e, "kopo03"); //실제 공용서버 DB에다가 저장하는 CODE
			csl.saveInCommonServer(e, "kopo03", "DES"); //DES는 본인이 추가하고 싶은 에러에 대한 설명 각자 넣으면 된다.
			                                              //DES에 안넣으면 공백으로 저장이된다.
		}finally {
			//5) Resource 반납하기       	
        	if(cstmt != null)  try { cstmt.close();  } catch(SQLException sqle) {}  //statement반납
        	if(conn != null)   try { conn.close();   } catch(SQLException sqle) {} //connection반납
        	
        	
        }//end try
	}//end main
}//end class
