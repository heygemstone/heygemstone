package Database01;

import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.ResultSet;

public class FreChart03pie {
	
	static String agency[] = new String[4];	 // ��Ż翡 ���� �迭��
	static String nationwide[] = new String[4];	// ������ �� ��Ż� ����� ���� ����� �迭
	static String seongnam[] = new String[4];		// ������ �� ��Ż� ����� ���� ����� �迭
	
	
	static String seongAgency[] = new String[8];	 // ��Ż翡 ���� �迭��
	static String ageCount[] = new String[8];	// ������ �� ��Ż� ����� ���� ����� �迭
	

    /**
     * Default constructor.
     *
     * @param title  the frame title.
     * @return 
     */
//    public void TotalRate(String title) {
//        super(title);
//        setContentPanel(createDemoPanel());
//    }
//
//    private void setContentPanel(JPanel createDemoPanel) {
//		// TODO Auto-generated method stub
//		
//	}

	/**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset() {
    	
    	DefaultPieDataset dataset = new DefaultPieDataset();
  
    	
    	for(int i = 0; i < 4; i++) {
        dataset.setValue(agency[i] , Integer.parseInt(seongnam[i]));	// ������ �� ��Ż� �����
        // dataset.setValue(agency[i] , Integer.parseInt(nationwide[i])); // ���� �� ��Ż� �����
    	}
        return dataset;        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Mobile phone agency",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }
    
    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
    	try {
    		Class.forName("oracle.jdbc.driver.OracleDriver"); 
    		System.out.println("����̹� �ε� ����");
    		} catch (Exception ex) {
    		System.out.println("����̹� �ε� ����");
    		}
    		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.26.72:1521/DCSWCDB", "C##14", "CD14");
    		
    		Statement k14_stmt = conn.createStatement();  

    		String QueryTxt1 = "SELECT  ��������翬����, COUNT(��������翬����) AS ��������翬��������\r\n" + 
    				"    FROM (SELECT \r\n" + 
    				"    CASE WHEN MOBILE_NO LIKE '016%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '84/01/01' THEN '����KTF10������' \r\n" + 
    				"         WHEN MOBILE_NO LIKE '016%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '74/01/01' AND BIRTH_DT < '84/01/01' THEN '����KTF20��'\r\n" + 
    				"         WHEN MOBILE_NO LIKE '016%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '64/01/01' AND BIRTH_DT < '74/01/01' THEN '����KTF30��'\r\n" + 
    				"         WHEN MOBILE_NO LIKE '016%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '54/01/01' AND BIRTH_DT < '64/01/01' THEN '����KTF40���̻�'\r\n" + 
    				"         WHEN MOBILE_NO LIKE '019%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '84/01/01' THEN '����LGT10������' \r\n" + 
    				"         WHEN MOBILE_NO LIKE '019%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '74/01/01' AND BIRTH_DT < '84/01/01' THEN '����LGT20��'\r\n" + 
    				"         WHEN MOBILE_NO LIKE '019%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '64/01/01' AND BIRTH_DT < '74/01/01' THEN '����LGT30��'\r\n" + 
    				"         WHEN MOBILE_NO LIKE '019%' AND ADDRESS1 LIKE '%����%' AND BIRTH_DT >= '54/01/01' AND BIRTH_DT < '64/01/01' THEN '����LGT40���̻�'\r\n" + 
    				"         ELSE '�׿�' \r\n" + 
    				"        END AS ��������翬���� \r\n" + 
    				"    FROM CUSTOMER) GROUP BY ��������翬���� HAVING COUNT(��������翬����)<10000 ORDER BY ��������翬����";
    		
    		
    		
 
    		
    		ResultSet k14_rset1 = k14_stmt.executeQuery(QueryTxt1);
    		
    		int cnt =0;
    }
}


//    		while(k14_rset.next()) {	
//    			 agency[cnt] = k14_rset.getString(1);
//    			 nationwide[cnt] = k14_rset.getString(2);
//    			 seongnam[cnt] = k14_rset.getString(3);
//	 
//    			 cnt++;		
//    		}