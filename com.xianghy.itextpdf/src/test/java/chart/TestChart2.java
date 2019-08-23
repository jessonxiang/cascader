package chart;

import base.TestBaseChart;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.chart.HistogramCurveExtremumChart;
import com.xianghy.itextpdf.tools.chart.HistogramGradeDistributionChart;
import com.xianghy.itextpdf.tools.chart.LineMulCurveLineAreaTableChart;
import com.xianghy.itextpdf.tools.chart.TableDashTableRectChart;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestChart2 {
    // 写入器
    private static PdfWriter writer;
    // 定义的文档对象
    private static Document doc;
    // 打开文档对象
    private static BaseFont bfChinese;

    static {
        try {
            // 定义输出位置并将新创建的文件放入输出对象中
            OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\cheny\\Desktop\\temp\\MyJSBPDF.pdf"));

            doc = new Document(PageSize.A4);

            writer = PdfWriter.getInstance(doc, outputStream);

            bfChinese = BaseFont.createFont("D:\\PDF\\font\\msyh.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHistogramCurveExtremumChart() throws Exception {
        doc.open();

        HistogramCurveExtremumChart histogramCurveExtremumChart = new HistogramCurveExtremumChart(writer, writer.getDirectContent(), doc, bfChinese);

        histogramCurveExtremumChart.setX(100).setY(550).setWidth(450)
                .setItemNames(new String[]{"我我我我我我我", "你", "他", "他"})
                .setMaxScores(new float[]{2.5f, 8, 6, 9}).setMinScores(new float[]{1.5f, 2.5f, 5f, 5.4f})
                .setAvgScores(new float[]{1.5f, 8f, 5f, 8.5f});

        histogramCurveExtremumChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        doc.close();
    }

    @Test
    public void testHistogramGradeDistributionChart() throws Exception {
        doc.open();

        HistogramGradeDistributionChart histogramGradeDistributionChart = new HistogramGradeDistributionChart(writer, writer.getDirectContent(), doc, bfChinese);

        histogramGradeDistributionChart.setX(30).setY(500).setWidth(400).setCellHeight(15)
                .setHeight(150).setFrameLeftOffsetX(100)
                .setItemNames(new String[]{"我我我我我我我我我我我我我我我我我", "你", "他", "他", "就拉尔夫"})
                .setScores(new float[]{25.229f, 30, 60, 90, 50});

        histogramGradeDistributionChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y1 = histogramGradeDistributionChart.getPositionY();
        histogramGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);
        doc.close();
    }

    @Test
    public void testTableDashTableRectChart() throws Exception {
        doc.open();

        TableDashTableRectChart tableDashTableRectChart = new TableDashTableRectChart(writer, writer.getDirectContent(), doc, bfChinese);

        tableDashTableRectChart.setX(40).setY(700).setWidth(450)
                .setHeadNames(new String[]{"我我我我我我我", "你", "他"})
                .setItemNames(new String[]{"我我我我我我我", "你", "他", "他", "就拉尔夫"})
                .setScores(new float[]{25.229f, 0, 60, 90, 50});

        tableDashTableRectChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y1 = tableDashTableRectChart.getPositionY();
        tableDashTableRectChart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);
        doc.close();
    }

//	@Test
//	public void testPolyLineChart() throws Exception {
//		doc.open();
//		// 批次数据
//		final List<Float[]> dataList = new ArrayList<Float[]>();
//		// 添加测试数据
//		Float[] batchData = new Float[] { 3.5f, 5.6f, 2.7f };
//		dataList.add(batchData);
//		batchData = new Float[] { 5.6f, 4.5f, 3.2f };
//		dataList.add(batchData);
//		batchData = new Float[] { 6.6f, 3.2f, 6.4f };
//		dataList.add(batchData);
//		batchData = new Float[] { 4.6f, 3.2f, 4.3f };
//		dataList.add(batchData);
////		batchData = new Float[] { 5.3f, 1.7f, 6.6f };
////		dataList.add(batchData);
//		// 指标颜色
//		final Integer[] normColor = new Integer[] { 0x59cfff, 0xa9d961, 0xffce54 };
//		// 指标背景色
//		final Integer[] normBGColor = new Integer[] { 0xdef5ff, 0xeef7df, 0xfff5dd };
//		// 批次颜色
//		final Integer[] batchColor = new Integer[] { 0x9792dd, 0xffce54, 0x72ccad, 0xe57d54, 0x89cfff };
//		// y轴最大值
//		final Float maxValue = 9f;
//		// y轴分多少档
//		final Integer rownum = 9;
//		int templateWidth =500;
//		// 画板高
//		int templateHeight =200;
//		// 距页面顶部距离
//		final int yOffset =150;
//		// 距页面左侧距离
//		final int xOffset =50;
//		// 表格表头宽
//		final int tableTitleWidth =30;
//		// 表格行高
//		final int tableRowHeight =25;
//
//		// 图形上留白
//		final int before =5;
//		// 图形格宽
//		final int chartColWidth = (templateWidth - tableTitleWidth) / normBGColor.length;
//		// 模板宽
//		templateWidth =470;
//		// 图形行高
//		final int chartRowHeight = (templateHeight - before) / (rownum);
//		// 图形高
//		final int chartHeight = chartRowHeight * rownum;
//		// 模板高
//		templateHeight = chartHeight + before;
//
//		/**
//		 * ,
//				dataList, maxValue, rownum, normBGColor,
//				batchColor, templateWidth, templateHeight, yOffset, xOffset, tableTitleWidth, before, 30
//		 */
//
//		pdf.chart.PolyLineChart chart=new pdf.chart.PolyLineChart(writer, writer.getDirectContent(), doc, bfChinese);
//		chart.setBatchColor(batchColor)
//		.setBefore(before)
//		.setDataList(dataList)
//		.setMaxValue(maxValue)
//		.setRownum(rownum)
//		.setNormBGColor(normBGColor)
//		.setTemplateWidth(templateWidth)
//		.setTemplateHeight(templateHeight)
//		.setyOffset(yOffset)
//		.setxOffset(xOffset)
//		.setDataColNames(new String[]{"安慰费安慰费安慰安慰费安慰费安慰费费安慰费","哇嘎","爱国和我"})
//		.setDataColColors(new int[] { 0x59CFFF, 0xA9D961, 0xFFCE54})
//		.setDataRowNames( new String[] { "第一批次", "第二批次", "第三批次","第四批次","第五批次" })
////		.setLeftBlank(29)
//		.setTableTitleWidth(tableTitleWidth)
//		.setScoreDescWidth(60)
//		;
//
//		chart.chart();
//
//		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
//
//		 float y1 =chart.getPositionY();
//		 chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);
//		doc.close();
//	}

    @Test
    public void testLineMulCurveLineAreaTableChart() throws Exception {
        doc.open();

        LineMulCurveLineAreaTableChart chart = new LineMulCurveLineAreaTableChart(writer, writer.getDirectContent(), doc, bfChinese);

        chart.setX(100).setY(580).setWidth(450)
                .setItemNames(new String[]{"我我我我我我我我我我我我我我", "你", "他", "他我"})
                .setScoreDescWidth(60)
                .setTableHeadFillColors(new int[]{0x59CFFF, 0xA9D961, 0xFFCE54, 0x59CFFF})
                .setScoreDescNames(new String[]{"第一批次", "第二批次", "第三批次", "第4批次"})
                .setScores(new float[][]{{8.8f, 3.9f, 3.2f, 3.2f}, {1.3f, 5.5f, 3.5f, 3.2f}, {7.5f, 8.3f, 4f, 3.2f}, {2.5f, 3.5f, 4.5f, 3.2f}})
                .setAreaBackgroundColors(new int[]{0xDEF5FF, 0xEEF7DF, 0xFFF5DD, 0xDEF5FF})
        ;

        chart.chart();

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.chart.LineMulCurveLineAreaTableChart",
                "chart.TestChart2.testLineMulCurveLineAreaTableChart()");
        chart.setLine(1, doc);

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y1 = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);

        doc.close();
    }

}
