package Database01;

import java.awt.BasicStroke;

import java.awt.Color;

import java.awt.Font;

import java.awt.GradientPaint;

import java.awt.Paint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.chart.ChartFrame;

import org.jfree.chart.JFreeChart;

import org.jfree.chart.axis.CategoryAxis;

import org.jfree.chart.axis.CategoryLabelPositions;

import org.jfree.chart.axis.NumberAxis;

import org.jfree.chart.labels.CategoryItemLabelGenerator;

import org.jfree.chart.labels.ItemLabelAnchor;

import org.jfree.chart.labels.ItemLabelPosition;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;

import org.jfree.chart.plot.CategoryPlot;

import org.jfree.chart.plot.DatasetRenderingOrder;

import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.renderer.category.BarRenderer;

import org.jfree.chart.renderer.category.CategoryItemRenderer;

import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import org.jfree.chart.renderer.category.StandardBarPainter;

import org.jfree.chart.title.TextTitle;

import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.ui.GradientPaintTransformType;

import org.jfree.ui.HorizontalAlignment;

import org.jfree.ui.StandardGradientPaintTransformer;

import org.jfree.ui.TextAnchor;

public class FreeChart {

	//Run As > Java Application ���� �����ϸ� �ٷ� Ȯ���� �� ����.

	public static void main(final String[] args) {

		FreeChart demo = new FreeChart();
		JFreeChart chart = demo.getChart();
		ChartFrame frame1 = new ChartFrame("Bar Chart", chart);
		frame1.setSize(800, 400);
		frame1.setVisible(true);
	}

	public JFreeChart getChart() {

		// ������ ����

		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset(); // bar chart 1
		DefaultCategoryDataset dataset12 = new DefaultCategoryDataset(); // bar chart 2
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1
//
        Connection conn = null; // DB����� ����(����)�� ���� ��ü
        PreparedStatement pstm = null;  // SQL ���� ��Ÿ���� ��ü
        ResultSet rs = null;  // �������� ������ ���� ��ȯ���� ���� ��ü
        
        try {
            // SQL ������ ����� ���� ������ ���Ǿ�(SELECT��)���
            // �� ����� ���� ResulSet ��ü�� �غ��� �� �����Ų��.
            String quary = "SELECT �̽ʴ�, CREDIT, ����,  count(*) FROM (SELECT CASE  \r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 5000 THEN '1���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 4000 THEN '2���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 3000 THEN '3���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 2000 THEN '4���'\r\n" + 
            		"                      WHEN CREDIT_LIMIT >= 1000 THEN '5���'\r\n" + 
            		"                      ELSE '6���'\r\n" + 
            		"                      END AS CREDIT,\r\n" + 
            		"                      CASE \r\n" + 
            		"                      WHEN FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
            		"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 \r\n" + 
            		"                      THEN '�̽ʴ�' END AS �̽ʴ�,\r\n" + 
            		"                      CASE WHEN ADDRESS1 LIKE '��� ������%' \r\n" + 
            		"                      THEN '������ ����' END AS ���� \r\n" + 
            		"                      FROM CUSTOMER)\r\n" + 
            		"                      WHERE �̽ʴ� IS NOT NULL AND ���� IS NOT NULL\r\n" + 
            		"                      GROUP BY �̽ʴ�, CREDIT, ����\r\n" + 
            		"                      ORDER BY CREDIT";
            
            conn = DBConnection.getConnection();
            pstm = conn.prepareStatement(quary);
            rs = pstm.executeQuery();
            
            /*  CUSTOMER ���̺� ������ Ÿ��
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
            
            System.out.println("ID  NAME  ZIPCODE  ADDRESS1  ADDRESS2  MOBILE_NO PHONE_NO  CREDIT_LIMIT  EMAIL  ACCOUNT_MGR  BIRTH_DT  ENROLL_DT  GENDER");
            System.out.println("============================================");
            
            while(rs.next()){
                //int credit = rs.getInt("credit"); ���� ��� �÷� �̸��� ��� �ȴ�.
                String CREDIT = rs.getString(2);
                int COUNT = rs.getInt(4);
               
          
                String result = CREDIT+" "+COUNT;
                System.out.println(result);
                
                dataset1.addValue(COUNT, "������ �ſ��޺� �̽ʴ� �α�����", CREDIT); //��ġ �̸�
            }
            
        } catch (SQLException sqle) {
            System.out.println("SELECT������ ���� �߻�");
            sqle.printStackTrace();
            
        }finally{
            // DB ������ �����Ѵ�.
            try{
                if ( rs != null ){rs.close();}   
                if ( pstm != null ){pstm.close();}   
                if ( conn != null ){conn.close(); }
            }catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }
            
        }
		
		// ������ �Է� ( ��, ����, ī�װ� )
		// �׷��� 1
        // dataset1.addValue(1.0, "S1", "1��");
		// ������ ���� �� ����
		// ������ ����

		final BarRenderer renderer = new BarRenderer();
		final BarRenderer renderer12 = new BarRenderer();
		final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

		// ���� �ɼ� ����

		final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		final ItemLabelPosition p_center = new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER
		);

		final ItemLabelPosition p_below = new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT
		);

		Font f = new Font("Gulim", Font.BOLD, 14);
		Font axisF = new Font("Gulim", Font.PLAIN, 14);

		// ������ ����
		// �׷��� 1

		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(p_center);
		renderer.setBaseItemLabelFont(f);

		// renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(
		// GradientPaintTransformType.VERTICAL));
		// renderer.setSeriesPaint(0, new GradientPaint(1.0f, 1.0f, Color.white, 0.0f,
		// 0.0f, Color.blue));

		renderer.setSeriesPaint(0, new Color(255, 204, 204));

		// �׷��� 2

		renderer12.setSeriesPaint(0, new Color(232, 168, 255));

		renderer12.setBaseItemLabelFont(f);

		renderer12.setBasePositiveItemLabelPosition(p_center);

		renderer12.setBaseItemLabelGenerator(generator);

		renderer12.setBaseItemLabelsVisible(true);

		// �׷��� 3

		renderer2.setBaseItemLabelGenerator(generator);

		renderer2.setBaseItemLabelsVisible(true);

		renderer2.setBaseShapesVisible(true);

		renderer2.setDrawOutlines(true);

		renderer2.setUseFillPaint(true);

		renderer2.setBaseFillPaint(Color.WHITE);

		renderer2.setBaseItemLabelFont(f);

		renderer2.setBasePositiveItemLabelPosition(p_below);

		renderer2.setSeriesPaint(0, new Color(19, 21, 22));

		renderer2.setSeriesStroke(0, new BasicStroke(
				2.0f,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND,

				3.0f)

		);

		// plot ����

		final CategoryPlot plot = new CategoryPlot();

		// plot �� ������ ����

		plot.setDataset(dataset1);
		plot.setRenderer(renderer);
		plot.setDataset(1, dataset12);
		plot.setRenderer(1, renderer12);
		plot.setDataset(2, dataset2);
		plot.setRenderer(2, renderer2);

		// plot �⺻ ����

		plot.setOrientation(PlotOrientation.VERTICAL); // �׷��� ǥ�� ����
		plot.setRangeGridlinesVisible(true); // X�� ���̵� ���� ǥ�ÿ���
		plot.setDomainGridlinesVisible(true); // Y�� ���̵� ���� ǥ�ÿ���

		// ������ ���� ���� : dataset ��� ������� ������ ( ��, ���� ����Ѱ� �Ʒ��� �� )

		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		// X�� ����
		plot.setDomainAxis(new CategoryAxis()); // X�� ���� ����
		plot.getDomainAxis().setTickLabelFont(axisF); // X�� ���ݶ� ��Ʈ ����
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD); // ī�װ� �� ��ġ ����

		// Y�� ����
		plot.setRangeAxis(new NumberAxis()); // Y�� ���� ����
		plot.getRangeAxis().setTickLabelFont(axisF); // Y�� ���ݶ� ��Ʈ ����

		// ���õ� plot�� �������� chart ����

		final JFreeChart chart = new JFreeChart(plot);

		// chart.setTitle("Overlaid Bar Chart"); // ��Ʈ Ÿ��Ʋ
		// TextTitle copyright = new TextTitle("JFreeChart WaferMapPlot", new
		// Font("SansSerif", Font.PLAIN, 9));
		// copyright.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		// chart.addSubtitle(copyright); // ��Ʈ ���� Ÿ��Ʋ

		return chart;
	}
}
