package Project;
import kopo.ctc.java.SaveLog.CommonSaveLog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Calc_Bonus_by_stmt_1 {
//�ۼ��� : 2018.06.28
//statement��ü�� ����� ���ʽ� ����ϱ�
//�ۼ��� : ���
	
	
	public static void main(String[] args) {
		CommonSaveLog csl = new CommonSaveLog();
		
		//System.out.println("hello");
		Connection conn = null;
        Statement stmt = null;
        Statement stmt_insert = null;
        ResultSet rs = null;
        String sqlstr = null;
        String url = "jdbc:oracle:thin:@192.168.23.98:1521:orcl";
        String id = "kopo03";
        String passwd = "koposw";
        String yyyymm = "201806";
        int bonus = 0;
        
        try {
        	long starttime = System.currentTimeMillis(); //���۽ð��� �����Ѵ�
        	//1)JDBC ����̹��� �ε��Ѵ�
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	//2)DBMS�� �����Ѵ�
        	conn = DriverManager.getConnection(url, id, passwd);
        	conn.setAutoCommit(false); //�ڵ� commit�� ���д�.(off)
        	
        	//3)SQL�� �����
        	//sqlstr = "SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE " +
        	         //"WHERE ROWNUM <= 10";//����� sql
        	sqlstr = "SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE ";
        	
        	stmt = conn.createStatement();
        	
        	//4)sql�� ����(�����ϱ�)
        	stmt.executeQuery(sqlstr);
        	
        	//5)������ fetch (��������)
            rs = stmt.getResultSet();
            while(rs.next()) {
           	//6)�μ��� ���ʽ� ����ϱ�
            	if(rs.getString(2).equals("PRESIDENT")) //�濵�ڴ� ���ʽ��� �������� �ʴ´�
            		continue;
            	
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
          
      
            //7)���� ���ʽ��� insert���ش�. 
            sqlstr = "INSERT INTO BONUS_LARGE(YYYYMM, EMPNO, JOB, DEPTNO, SAL, BONUS)"
            		+ "VALUES('"+ yyyymm + "',"+rs.getInt(1) + ",'" + rs.getString(2) +
            		"'," + rs.getInt(3) + "," + rs.getInt(4) + "," + bonus + ")";
            
            //System.out.println(sqlstr);
            
            stmt_insert = conn.createStatement();
            stmt_insert.executeUpdate(sqlstr);
            
            conn.commit();
            stmt_insert.close();//�긦���ݾ������
            
            } //while�� �� 
            
            //8)��� �ð��� ������
            long endtime = System.currentTimeMillis(); //����ð� �����ϱ�
            System.out.println("����ð�: " + (endtime - starttime) +"ms");
        
        }catch(Exception e) {
			
			//����� pc�� ���� �޼���
			//csl.saveInUserComputer(e, "kopo03");  //�޼��带 �����ε��ؼ� DES�� �����ϰ� �Ƚ������� ����ǵ��� ����� ������
			csl.saveInUserComputer(e, "kopo03", "DES");
			
			//������ ���� �޼���
//			csl.saveInCommonServer(e, "kopo03"); //���� ���뼭�� DB���ٰ� �����ϴ� CODE
			//csl.saveInCommonServer(e, "kopo03", "DES"); //DES�� ������ �߰��ϰ� ���� ������ ���� ���� ���� ������ �ȴ�.
			                                              //DES�� �ȳ����� �������� �����̵ȴ�.
		}finally {
        	try {
        		rs.close();
        		stmt.close();
        		conn.close();
        	
        	}catch(Exception Ex) {
        		Ex.printStackTrace();
        	}
        	
        }//end try
	}//end main
}//end class
