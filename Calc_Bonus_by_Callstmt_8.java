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
//�ۼ��� : 2018.06.28
//CallableStatement + Bulkbinding�� ����� ���ɰ���
//Bulkbinding�� ����Ͽ� ����� fetch�� ����� insert�� �����ϱ�
//pl/sql bulk collect�� for all�� ����Ѵ�.
//bulk������ �����Ͽ� ���� ������ ����� ����Ѵ�. 
//�ۼ��� : ���
	
	
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
        	      "         COMMIT;                                    " + //1000���� INSERT�� Ŀ��
        	      "  END;   ";
        	
          cstmt = conn.prepareCall(PLsql);
          cstmt.setString(1, yyyymm);
          
          //4) SQL�����ϱ�(PL/SQL��� �����ϱ�)
          cstmt.execute(); //����� dbms�� ���۵ǰ� dbms���ο��� ó������ jdbc�� �Ѿ��
     
            //��� �ð��� ������
            long endtime = System.currentTimeMillis(); //����ð� �����ϱ�
            System.out.println("����ð�: " + (endtime - starttime)/1000 +"��");
        
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
