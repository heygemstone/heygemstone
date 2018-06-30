package Project;
import kopo.ctc.java.SaveLog.CommonSaveLog;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Calc_Bonus_by_Callstmt_8 {
//작성일 : 2018.06.28
//CallableStatement + Bulkbinding을 사용한 성능개선
//Bulkbinding을 사용하여 빈번한 fetch와 빈번한 insert를 개선하기
//pl/sql bulk collect와 for all을 사용한다.
//bulk단위를 구분하여 각각 실행후 결과를 기록한다. 
//작성자 : 김민
	
	
	public static void main(String[] args) {
		
		CommonSaveLog csl = new CommonSaveLog();
		
		Connection conn = null;
        CallableStatement cstmt = null;
        
        //ResultSet rs = null;
        
       
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
        	      " CURSOR  BONUS_CUR IS " +
        		  "    SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE " +
        	      "             WHERE JOB <> 'PRESIDENT' ;           " +
        		  
        	      "TYPE T_EMPNO  IS TABLE OF EMP_LARGE.EMPNO%TYPE    INDEX BY BINARY_INTEGER; " +
        	      "TYPE T_JOB    IS TABLE OF EMP_LARGE.JOB%TYPE      INDEX BY BINARY_INTEGER; " +
        	      "TYPE T_DEPTNO IS TABLE OF EMP_LARGE.DEPTNO%TYPE   INDEX BY BINARY_INTEGER; " +
        	      "TYPE T_SAL    IS TABLE OF EMP_LARGE.SAL%TYPE      INDEX BY BINARY_INTEGER; " + 
        	      "TYPE T_BONUS  IS TABLE OF BONUS_LARGE.BONUS%TYPE  INDEX BY BINARY_INTEGER; " + 
        	      "V_ARRAYSIZE NUMBER(10) := 5000;                                            " +
        	      //Arraysize
        	      
        	      "V_EMPNO       T_EMPNO;                            " +
        	      "V_JOB         T_JOB;                              " +
        	      "V_DEPTNO      T_DEPTNO;                           " +
        	      "V_SAL         T_SAL;                              " +
        	      "V_BONUS       T_BONUS;                            " +
        	      
        	    
                  "BEGIN                                           " +
                  "    OPEN         BONUS_CUR;                     " +
                  "    LOOP                                        " +
  
                  "    FETCH BONUS_CUR BULK COLLECT                    " +
                  "        INTO V_EMPNO, V_JOB, V_DEPTNO, V_SAL LIMIT V_ARRAYSIZE;" + 
                  "    FOR I IN 1..V_DEPTNO.COUNT                      " +
                  "    LOOP                                            " +
                  "    IF    V_DEPTNO(I)=10 THEN                       " +
                  "          V_BONUS(I) := V_SAL(I)*0.3;               " +
                  "      ELSIF V_DEPTNO(I)=20 THEN                    " +
                  "          V_BONUS(I) := V_SAL(I)*0.1;               " +
                  "      ELSIF V_DEPTNO(I)=30 THEN                    " +
                  "          V_BONUS(I) := V_SAL(I)*0.05;              " +
                  "      ELSIF V_DEPTNO(I)=40 THEN                    " +
                  "          V_BONUS(I) := V_SAL(I)*0.2;               " +
        	      "      ELSE                                      " +
                  "          V_BONUS(I) := V_SAL(I)*0;                 " +
        	      "      END IF;                                       " +
                  "      END LOOP;                                     " +
        	      
                  "      FORALL I IN 1..V_DEPTNO.COUNT                 " +
                  
        	      
        	      
        	      "  INSERT INTO BONUS_LARGE(YYYYMM, EMPNO, JOB, DEPTNO, SAL, BONUS) " +
        	      "  VALUES(?, V_EMPNO(I), V_JOB(I), V_DEPTNO(I), V_SAL(I), TRUNC(V_BONUS(I))); " +
        	      "         EXIT  WHEN BONUS_CUR%NOTFOUND;                 " +
        	      "         END LOOP;                                  " +
        	      "         CLOSE BONUS_CUR;                           " +
        	      "         COMMIT;                                    " + //1000만건 INSERT후 커밋
        	      "  END;   ";
        	
          cstmt = conn.prepareCall(PLsql);
          cstmt.setString(1, yyyymm);
          
          //4) SQL실행하기(PL/SQL블록 전송하기)
          cstmt.execute(); //블록이 dbms로 전송되고 dbms내부에서 처리한후 jdbc로 넘어옴
     
            //경과 시간을 측정함
            long endtime = System.currentTimeMillis(); //종료시간 측정하기
            System.out.println("경과시간: " + (endtime - starttime)/1000 +"초");
        
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
