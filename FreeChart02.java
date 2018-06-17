package Database01;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

public class FreeChart02 {

	public static void main(final String[] args) {

		FreeChart02 demo = new FreeChart02();
		JFreeChart chart = demo.getChart();
		ChartFrame frame1 = new ChartFrame("Bar Chart", chart);
		frame1.setSize(800, 400);
		frame1.setVisible(true);
	}

	public JFreeChart getChart() {

		// 데이터 생성
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset(); // bar chart 1
		DefaultCategoryDataset dataset12 = new DefaultCategoryDataset(); // bar chart 2
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1
//
        Connection conn = null; // DB연결된 상태(세션)을 담은 객체
        PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
        ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
        
        try {
            // SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
            // 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
            String quary = "SELECT ACCOUNT_MGR, COUNT(*) AS 신용관리대상자수 FROM CUSTOMER    \r\n" + 
            		"WHERE CREDIT_LIMIT < 1000 AND\r\n" + 
            		"      FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)<30 \r\n" + 
            		"                      AND FLOOR(MONTHS_BETWEEN(sysdate, to_date(BIRTH_DT))/12)>=20 AND\r\n" + 
            		"      ADDRESS1 LIKE '경기 성남시%'  \r\n" + 
            		"GROUP BY ACCOUNT_MGR";
            
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
            
            System.out.println(" ACCOUNT_MGR  CREDIT");
            System.out.println("============================================");
            
            while(rs.next()){
                //int empno = rs.getInt("empno"); 숫자 대신 컬럼 이름을 적어도 된다.
                String ACCOUNT_MGR = rs.getString(1);
               int CREDIT_COUNT = rs.getInt(2);
               
          
                String result = ACCOUNT_MGR +" "+ CREDIT_COUNT;
                System.out.println(result);
                
                dataset1.addValue(CREDIT_COUNT, "매니저별담당신용관리대상자수", ACCOUNT_MGR); //수치 이름,뒤에거 이름, 문자열형태로와야함
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
		// 렌더링 생성 및 세팅

		// 렌더링 생성

		final BarRenderer renderer = new BarRenderer();
		final BarRenderer renderer12 = new BarRenderer();
		final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

		// 공통 옵션 정의
		final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		final ItemLabelPosition p_center = new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER
		);

		final ItemLabelPosition p_below = new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT
		);

		Font f = new Font("Gulim", Font.BOLD, 14);
		Font axisF = new Font("Gulim", Font.PLAIN, 14);

		// 렌더링 세팅

		// 그래프 1

		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBasePositiveItemLabelPosition(p_center);
		renderer.setBaseItemLabelFont(f);

		// renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(
		// GradientPaintTransformType.VERTICAL));
		// renderer.setSeriesPaint(0, new GradientPaint(1.0f, 1.0f, Color.white, 0.0f,
		// 0.0f, Color.blue));
        //renderer.setSeriesPaint(0, new Color(0, 162, 255));
		renderer.setSeriesPaint(0, new Color(150, 245, 245));

		// 그래프 2

		renderer12.setSeriesPaint(0, new Color(232, 168, 255));
		renderer12.setBaseItemLabelFont(f);
		renderer12.setBasePositiveItemLabelPosition(p_center);
		renderer12.setBaseItemLabelGenerator(generator);
		renderer12.setBaseItemLabelsVisible(true);

		// 그래프 3

		renderer2.setBaseItemLabelGenerator(generator);
		renderer2.setBaseItemLabelsVisible(true);
		renderer2.setBaseShapesVisible(true);
		renderer2.setDrawOutlines(true);
		renderer2.setUseFillPaint(true);
		renderer2.setBaseFillPaint(Color.WHITE);
		renderer2.setBaseItemLabelFont(f);
		renderer2.setBasePositiveItemLabelPosition(p_below);
		renderer2.setSeriesPaint(0, new Color(219, 121, 22));
		renderer2.setSeriesStroke(0, new BasicStroke(
				2.0f,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND,
				3.0f)
		);

		// plot 생성

		final CategoryPlot plot = new CategoryPlot();

		// plot 에 데이터 적재

		plot.setDataset(dataset1);
		plot.setRenderer(renderer);
		plot.setDataset(1, dataset12);
		plot.setRenderer(1, renderer12);
		plot.setDataset(2, dataset2);
		plot.setRenderer(2, renderer2);

		// plot 기본 설정

		plot.setOrientation(PlotOrientation.VERTICAL); // 그래프 표시 방향
		plot.setRangeGridlinesVisible(true); // X축 가이드 라인 표시여부
		plot.setDomainGridlinesVisible(true); // Y축 가이드 라인 표시여부

		// 렌더링 순서 정의 : dataset 등록 순서대로 렌더링 ( 즉, 먼저 등록한게 아래로 깔림 )

		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		// X축 세팅

		plot.setDomainAxis(new CategoryAxis()); // X축 종류 설정
		plot.getDomainAxis().setTickLabelFont(axisF); // X축 눈금라벨 폰트 조정
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD); // 카테고리 라벨 위치 조정

		// Y축 세팅

		plot.setRangeAxis(new NumberAxis()); // Y축 종류 설정
		plot.getRangeAxis().setTickLabelFont(axisF); // Y축 눈금라벨 폰트 조정

		// 세팅된 plot을 바탕으로 chart 생성

		final JFreeChart chart = new JFreeChart(plot);

		// chart.setTitle("Overlaid Bar Chart"); // 차트 타이틀
		// TextTitle copyright = new TextTitle("JFreeChart WaferMapPlot", new
		// Font("SansSerif", Font.PLAIN, 9));
		// copyright.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		// chart.addSubtitle(copyright); // 차트 서브 타이틀

		return chart;

	}

}
