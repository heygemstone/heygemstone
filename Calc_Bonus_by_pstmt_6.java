package Project;
import kopo.ctc.java.SaveLog.CommonSaveLog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Calc_Bonus_by_stmt_6 {
//작성일 : 2018.06.28
//빈번한 insert횟수 개선하기 배치처리
//addBatch() 메소드이용 1개 레코드당 1개의 insert를 매번 실행하지 않고
//1개의 insert에 여러 레코드를 묶어서 insert를 수행한다. 빈번한 sql실행을 개선함 
//out of memory를 방지, 최적화된 배치처리 사이즈를 찾아야한다.
//PreparedStatement 객체를 사용해 보너스 계산하기 
//작성자 : 김민
	
	
	public static void main(String[] args) {
		
		CommonSaveLog csl = new CommonSaveLog();
		
		Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt_insert = null;//PreparedStatement 사용하기
        ResultSet rs = null;
        
        String sqlstr = null;
        String url = "jdbc:oracle:thin:@192.168.23.98:1521:orcl";
        String id = "kopo03";
        String passwd = "koposw";
        String yyyymm = "201806";
        int bonus = 0;
        
        int Batchsize = 10; // 배치사이즈 지정하기
        int LoopCnt = 0; //루프 반복 횟수 카운트
        
        try {
        	long starttime = System.currentTimeMillis(); //시작시간을 측정한다
        	//1)JDBC 드라이버를 로딩한다
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	//2)DBMS에 접속한다
        	conn = DriverManager.getConnection(url, id, passwd);
        	conn.setAutoCommit(false); //자동 commit을 꺼둔다.(off)
        	
        	//3)SQL을 만든다
        	//sqlstr = "SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE " +
        	//         "WHERE JOB <> 'PRESIDENT' AND ROWNUM <= 100";//실험용 sql
        	sqlstr = "SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE "
        			 + "WHERE JOB <> 'PRESIDENT' "; //본래 SQL
        	
        	stmt = conn.createStatement();
        	
        	String insstr="INSERT INTO BONUS_LARGE(YYYYMM, EMPNO, JOB, DEPTNO, SAL, BONUS)"+
        	               " VALUES(?,?,?,?,?,?)";
        	pstmt_insert = conn.prepareStatement(insstr);//insert를 위한 PreparedStatement객체생성
           
        	//
        	//4)sql문 실행(전송하기)
        	stmt.executeQuery(sqlstr);
        	
        	//5)데이터 fetch (데이터 가져오기)
            rs = stmt.getResultSet();
            
            while(rs.next()) {    //1000만번 루프반복
           	//6)부서별 보너스 계산하기
            //if(rs.getString(2).equals("PRESIDENT")) //경영자는 보너스를 지급하지 않는다는 조건
            		//continue; //이 조건문 천만번 실행해야하므로 삭제하기 
            	    //다음조건을 실행하지않고 반복문끝으로 이동함
            	
            	if(rs.getInt(3) == 10)
            		bonus = (int)(rs.getInt(4) * 0.3);
            	      else if(rs.getInt(3) == 20)
            	    	  bonus = (int)(rs.getInt(4) * 0.1);
            	      else if(rs.getInt(3) == 30)
            	    	  bonus = (int)(rs.getInt(4) * 0.05);
            	      else if(rs.getInt(3) == 40)
            	    	  bonus = (int)(rs.getInt(4) * 0.2);
            	      else
            	    	  bonus = 0;
          
      
            //7)계산된 보너스를 insert해준다. 
           
            	pstmt_insert.setString(1,yyyymm); //보너스 지급월
            	pstmt_insert.setInt(2,rs.getInt(1)); //EMPNO
            	pstmt_insert.setString(3, rs.getString(2));//job
                pstmt_insert.setInt(4,rs.getInt(3));// deptno
                pstmt_insert.setInt(5, rs.getInt(4));// sal
                pstmt_insert.setInt(6, bonus);// bonus
                
                pstmt_insert.addBatch(); //addBatch 실행하기
         
                if(++LoopCnt % Batchsize == 0 ) //Batchsize단위로 execute(실행)하기
                	pstmt_insert.executeBatch();
                
            } //while문 끝 
         
            
            pstmt_insert.executeBatch(); //나머지 Execute Batch 배치 실행하기
            //1005개인데 10개단위 배치일경우 나머지 5개를 실행하기위함.
            
            conn.commit();
            //1000만건 데이터 입력후 1번 commit하기 
            
            //8)경과 시간을 측정함
            long endtime = System.currentTimeMillis(); //종료시간 측정하기
            System.out.println("경과시간: " + (endtime - starttime) +"ms");
        
        }catch(Exception e) {
			
			//사용자 pc에 저장 메서드
			//csl.saveInUserComputer(e, "kopo03");  //메서드를 오버로딩해서 DES를 깜빡하고 안썼을때도 실행되도록 만들어 놓았음
			csl.saveInUserComputer(e, "kopo03", "DES");
			
			//서버에 저장 메서드
            //csl.saveInCommonServer(e, "kopo03"); //실제 공용서버 DB에다가 저장하는 CODE
			csl.saveInCommonServer(e, "kopo03", "DES"); //DES는 본인이 추가하고 싶은 에러에 대한 설명 각자 넣으면 된다.
			                                              //DES에 안넣으면 공백으로 저장이된다.
		}finally {
			//9) Resource 반납하기
        	if(rs   != null)   try { rs.close();     } catch(SQLException sqle) {}  //resultset반납
        	if(stmt != null)   try { stmt.close();   } catch(SQLException sqle) {}  //statement반납
        	if(pstmt_insert != null)  try { pstmt_insert.close(); } catch(SQLException sqle) {} //preparedstatement반납
        	if(conn != null)   try { conn.close();   } catch(SQLException sqle) {} //connection반납
        	
        	
        }//end try
	}//end main
}//end class
