package Project;
import kopo.ctc.java.SaveLog.CommonSaveLog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Calc_Bonus_by_stmt_5 {
//�ۼ��� : 2018.06.28
//PreparedStatement ��ü�� ����� ���� ���� 
//PreparedStatement ��ü�� ����� ���ʽ� ����ϱ� 
//�ۼ��� : ���
	
	
	public static void main(String[] args) {
		
		CommonSaveLog csl = new CommonSaveLog();
		
		Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt_insert = null;//PreparedStatement ����ϱ�
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
        	//         "WHERE JOB <> 'PRESIDENT' AND ROWNUM <= 100";//����� sql
        	sqlstr = "SELECT EMPNO, JOB, DEPTNO, SAL FROM EMP_LARGE "
        			 + "WHERE JOB <> 'PRESIDENT' "; //���� SQL
        	
        	stmt = conn.createStatement();
        	
        	String insstr="INSERT INTO BONUS_LARGE(YYYYMM, EMPNO, JOB, DEPTNO, SAL, BONUS)"+
        	               " VALUES(?,?,?,?,?,?)";
        	pstmt_insert = conn.prepareStatement(insstr);//insert�� ���� PreparedStatement��ü����
           
        	//
        	//4)sql�� ����(�����ϱ�)
        	stmt.executeQuery(sqlstr);
        	
        	//5)������ fetch (������ ��������)
            rs = stmt.getResultSet();
            
            while(rs.next()) {    //1000���� �����ݺ�
           	//6)�μ��� ���ʽ� ����ϱ�
            //if(rs.getString(2).equals("PRESIDENT")) //�濵�ڴ� ���ʽ��� �������� �ʴ´ٴ� ����
            		//continue; //�� ���ǹ� õ���� �����ؾ��ϹǷ� �����ϱ� 
            	    //���������� ���������ʰ� �ݺ��������� �̵���
            	
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
           
            	pstmt_insert.setString(1,yyyymm); //���ʽ� ���޿�
            	pstmt_insert.setInt(2,rs.getInt(1)); //EMPNO
            	pstmt_insert.setString(3, rs.getString(2));//job
                pstmt_insert.setInt(4,rs.getInt(3));// deptno
                pstmt_insert.setInt(5, rs.getInt(4));// sal
                pstmt_insert.setInt(6, bonus);// bonus
                
                pstmt_insert.executeUpdate(); //insert �����ϱ�
           
            //stmt_insert.executeUpdate(sqlstr);//�ѹ������ָ�ȴ�.
          
           
            } //while�� �� 
            
            conn.commit();
            //1000���� ������ �Է��� 1�� commit�ϱ� 
            
            //8)��� �ð��� ������
            long endtime = System.currentTimeMillis(); //����ð� �����ϱ�
            System.out.println("����ð�: " + (endtime - starttime) +"ms");
        
        }catch(Exception e) {
			
			//����� pc�� ���� �޼���
			//csl.saveInUserComputer(e, "kopo03");  //�޼��带 �����ε��ؼ� DES�� �����ϰ� �Ƚ������� ����ǵ��� ����� ������
			csl.saveInUserComputer(e, "kopo03", "DES");
			
			//������ ���� �޼���
            //csl.saveInCommonServer(e, "kopo03"); //���� ���뼭�� DB���ٰ� �����ϴ� CODE
			csl.saveInCommonServer(e, "kopo03", "DES"); //DES�� ������ �߰��ϰ� ���� ������ ���� ���� ���� ������ �ȴ�.
			                                              //DES�� �ȳ����� �������� �����̵ȴ�.
		}finally {
			//9) Resource �ݳ��ϱ�
        	if(rs   != null)   try { rs.close();     } catch(SQLException sqle) {}  //resultset�ݳ�
        	if(stmt != null)   try { stmt.close();   } catch(SQLException sqle) {}  //statement�ݳ�
        	if(pstmt_insert != null)  try { pstmt_insert.close(); } catch(SQLException sqle) {} //preparedstatement�ݳ�
        	if(conn != null)   try { conn.close();   } catch(SQLException sqle) {} //connection�ݳ�
        	
        	
        }//end try
	}//end main
}//end class
