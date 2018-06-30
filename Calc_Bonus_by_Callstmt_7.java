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
//�ۼ��� : 2018.06.28
//CallableStatement ��ü�� ����� ���ɰ���
//CallableStatement ��ü�� ����ϸ� Anonymous block�� Stored block�� 
//jdbc���α׷������� pl/sql block�� ����Ҽ� �ְԵʿ�����
//���� ���꼺�� ���� ȿ������ ���ϼ��ִ�.
//�ۼ��� : ���
	
	
	public static void main(String[] args) {
		
		CommonSaveLog csl = new CommonSaveLog();
		
		Connection conn = null;
        CallableStatement cstmt = null;
       
        String url = "jdbc:oracle:thin:@192.168.23.98:1521:orcl";
        String id = "kopo03";
        String passwd = "koposw";
        String yyyymm = "201806";
        
        //int bonus = 0;
        //int Batchsize = 1000; // ��ġ������ �����ϱ�
        //int LoopCnt = 0; //���� �ݺ� Ƚ�� ī��Ʈ       
        
        try {
        	long starttime = System.currentTimeMillis(); //���۽ð��� �����Ѵ�
        	//1)JDBC ����̹��� �ε��Ѵ�
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	//2)DBMS�� �����Ѵ�
        	conn = DriverManager.getConnection(url, id, passwd);
        	conn.setAutoCommit(false); //�ڵ� commit�� ���д�.(off)
        	
        	//3)SQL�� �����ϱ�
        	
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
        	      "  V_EMPNO,V_JOB,V_DEPTNO,V_SAL;                   " + //FETCH,��������
        	      "  EXIT  WHEN BONUS_CUR%NOTFOUND;                  " +
        	      
        	      "  IF V_DEPTNO=10 THEN                             " + //PL/SQL���� ���ʽ� ����ϱ�
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
          //JAVA���� ���ʽ� �����(yyyymm)�� parameter��
          //�Է� �޾� pl/sql��Ͽ� �����Ͽ� 
          //pl/sql��Ͽ��� ���� ���ʽ� Batchó�����ֱ� 
          
          //4) SQL�����ϱ�(PL/SQL��� �����ϱ�)
          cstmt.execute(); //����� dbms�� ���۵ǰ� dbms���ο��� ó������ jdbc�� �Ѿ��
     
            //��� �ð��� ������
            long endtime = System.currentTimeMillis(); //����ð� �����ϱ�
            System.out.println("����ð�: " + (endtime - starttime) +"ms");
        
        }catch(Exception e) {
			
			//����� pc�� ���� �޼���
			//csl.saveInUserComputer(e, "kopo03");  //�޼��带 �����ε��ؼ� DES�� �����ϰ� �Ƚ������� ����ǵ��� ����� ������
			csl.saveInUserComputer(e, "kopo03", "DES");
			e.printStackTrace();
			//������ ���� �޼���
            //csl.saveInCommonServer(e, "kopo03"); //���� ���뼭�� DB���ٰ� �����ϴ� CODE
			csl.saveInCommonServer(e, "kopo03", "DES"); //DES�� ������ �߰��ϰ� ���� ������ ���� ���� ���� ������ �ȴ�.
			                                              //DES�� �ȳ����� �������� �����̵ȴ�.
		}finally {
			//5) Resource �ݳ��ϱ�       	
        	if(cstmt != null)  try { cstmt.close();  } catch(SQLException sqle) {}  //statement�ݳ�
        	if(conn != null)   try { conn.close();   } catch(SQLException sqle) {} //connection�ݳ�
        	
        	
        }//end try
	}//end main
}//end class
