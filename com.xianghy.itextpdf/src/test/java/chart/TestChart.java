package chart;

import base.TestBaseChart;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.text.DiamondBeforeParagraph;
import com.xianghy.itextpdf.tools.chart.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestChart {
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
    public void testRound2DCakeChart() throws Exception {
        doc.open();

        doc.add(new Paragraph("安慰法", new Font(bfChinese)));
//		
        Round2DCakeChart round2dCakeChart = new Round2DCakeChart(writer, writer.getDirectContent(), doc, bfChinese);
        round2dCakeChart.setX(250).setY(500).setR(80)
                .setFillColors(
                        new BaseColor[]{new BaseColor(0xFe89fe), BaseColor.RED, BaseColor.BLUE, BaseColor.GREEN})
                .setScores(new float[]{70f, 15f, 5f, 10f}).setDescs(new String[]{"的的S的", "的的防盗网", "时光如梭", "核对"});
        round2dCakeChart.chart();
//
//		writer.getDirectContent().setColorStroke(BaseColor.BLACK);

//		float y = round2dCakeChart.getPositionY();
//
//		round2dCakeChart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);
//
//		round2dCakeChart.setLine(29, doc);
//
//		doc.add(new Paragraph("安慰法", new Font(bfChinese)));

        doc.close();
    }

    @Test
    public void testTableDoubleArrowCurveChart() throws Exception {
        doc.open();

        TableDoubleArrowCurveChart chart = new TableDoubleArrowCurveChart(writer, writer.getDirectContent(), doc,
                bfChinese);

        for (int i = 0 ; i < 15 ; i++) {
            doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
        }

        chart.setWidth(450).setTitleColor(0x59CFFF).setTitle("即垃圾了看法可").setFontSize(9).setLineHeight(25)
                .setWidths(new float[]{10, 10, 10, 10, 10, 50})
                .setTableHeads(new String[]{"1拉就是十多岁", "2拉就拉", "3拉就拉", "1拉就拉", "2拉就拉", "3拉就拉"})
                .setTableHeadColor(0xACE7FF)
//				.setScoreLevels(new int[] { 0, 20, 40, 60, 80, 100 })
//				.setScores(new float[][] { { 15, 35, 25 }, { 11, 33, 22 }, { 10, 30, 20 }, { 30, 50, 40 },
//						{ 25, 45, 35 }, { 35, 55, 45 }, { 0, 100, 50 }, { 0, 95, 0 }, { 5, 100, 100 } })
                .setScoreLevels(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
                .setScores(new float[][]{{3, 1, 2}, {5, 3, 4},
                        {4, 2, 3}, {5, 3, 4}, {9, 0, 5}, {9, 5, 8}, {5, 3, 4}, {9, 0, 5}, {9, 5, 8}})
                .setChildrenTypes(new ArrayList<String[]>() {
                    {
                        add(new String[]{"是家具啊", "是家具啊"});
                        add(new String[]{"爱我家我家附近干", "爱我家我家附近干", "爱我家我家附近干", "是家具啊"});
                        add(new String[]{"让人发染发", "让人发染发", "让人发染发"});
                    }
                }).setParentTypeColor(0xDCF3FB).setParentTypes(new String[]{"家具家具家具是家具啊", "爱我家我家附近干", "让人发染发"})
                .setRowColors(new int[]{0xFFFFFF, 0xF2F2F2}).setLevelFontSize(7).setColNumber(3)
                .setColColor(0xFAE5DD).setMaxScoreColNum(1).setMinScoreColNum(2).setCurScoreColNum(3);

        chart.chart();

        doc.close();
    }

    @Test
    public void testTableChartedDoubleArrowCurveChart() throws Exception {
        doc.open();

        for (int i = 0 ; i < 35 ; i++) {
            doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
        }

        TableChartedDoubleArrowCurveChart chart = new TableChartedDoubleArrowCurveChart(writer,
                writer.getDirectContent(), doc, bfChinese);

        chart.setWidth(450)
                .setFontSize(9)
                .setLineHeight(25)
                .setWidths(new float[]{10, 10, 10, 10, 10, 50})
                .setScoreLevels(new int[]{0, 2, 4, 6, 8, 10})
                .setRowColors(new int[]{0xFFFFFF, 0xF2F2F2})
                .setLevelFontSize(7)
                .setColNumber(3)
                .setMaxScoreColNum(1)
                .setMinScoreColNum(2)
                .setCurScoreColNum(3)
                .setScores(new float[][]{{3f, 2f, 2.2f}, {6, 4, 5.11f}, {5, 2, 3.0f}, {7, 1, 2.12f},
                        {6, 2, 3.85f}, {7, 3, 4.05f}, {5, 2, 4.02f}/*, { 6, 2, 2.96f }, { 5, 2, 3.22f },
//			{ 6, 5, 5.11f }, { 5, 2, 3.0f }, { 7, 1, 2.12f }, { 6, 2, 3.85f } */})
                .setTitle("即垃圾了看法可")
                .setTableHeads(new String[]{"指标分类", "指标", "最高分", "最低分", "平均分", "  "})
                .setChildrenTypes(new ArrayList<String[]>() {
                    {
                        add(new String[]{"沟通能力", "合作能力"});
//				add(new String[] { "语言能力", "数字能力"});
                        add(new String[]{"组织归属", "物质回报", "理想抱负", "自我实现", "获得尊重"
                                /*, "创新能力", "学习能力", "逻辑能力", "机械推理"   */});
                    }
                })
                .setParentTypes(new String[]{"基础工作★能作工作能作", "基本基本潜能基本潜能潜能"/*, "领导者动力" */});

        chart.chart();

//		float y = 0, y0 = writer.getVerticalPosition(true);
//
//		for (int i = 0; i < 3; i++) {
//			doc.add(new Paragraph("●安慰费", new Font(this.bfChinese, 12, Font.NORMAL, BaseColor.BLUE)));
//
//			chart.setLine(3, 5, doc);
//			chart.setWidth(450).setTitleColor(0x59CFFF).setTitle("即垃圾了看法可").setFontSize(9).setLineHeight(25)
//					.setWidths(new float[] { 10, 10, 10, 10, 10, 50 })
//					.setTableHeads(new String[] { "指标分类", "指标", "最高分", "最低分", "平均分", "" }).setTableHeadColor(0xACE7FF)
//					.setScoreLevels(new int[] { 0, 20, 40,  60, 80,100 })
//					.setScores(new float[][] { { 3f, 2f,2.2f }, { 6, 4, 5.11f }, { 5, 2, 3.0f }, { 7, 1, 2.12f },
//							{ 6, 2, 3.85f }, { 7, 3, 4.05f }, { 5, 2, 4.02f }/*, { 6, 2, 2.96f }, { 5, 2, 3.22f },
//							{ 6, 5, 5.11f }, { 5, 2, 3.0f }, { 7, 1, 2.12f }, { 6, 2, 3.85f } */})
//					.setChildrenTypes(new ArrayList<String[]>() {
//						{
//							add(new String[] { "沟通能力", "合作能力"});
////							add(new String[] { "语言能力", "数字能力"});
//							add(new String[] { "组织归属", "物质回报", "理想抱负", "自我实现", "获得尊重"
//									/*, "创新能力", "学习能力", "逻辑能力", "机械推理"   */});
//						}
//					}).setParentTypeColor(0xDCF3FB)
//					.setParentTypes(new String[] { "基础工作能作工作能作", "基本基本潜能基本潜能潜能"/*, "领导者动力" */})
//					.setRowColors(new int[] { 0xFFFFFF, 0xF2F2F2 }).setLevelFontSize(7).setColNumber(3)
//					.setColColor(0xFAE5DD).setMaxScoreColNum(1).setMinScoreColNum(2).setCurScoreColNum(3);
//
//			chart.chart();
//
//			doc.newPage();
//
//		}

        doc.close();
    }

    @Test
    public void testTableRectLineCategoryChart() throws Exception {
        doc.open();

        TableRectLineCategoryChart chart = new TableRectLineCategoryChart(writer, writer.getDirectContent(), doc,
                bfChinese);

        List<float[]> aScores = new ArrayList<float[]>();
        float[] indicatorScore = new float[]{0.99f, 0.95f, 0.91f, 1.10f, 1.00f, 1.15f, 0.82f, 0.89f, 1.08f};
        float[] avgScore = new float[]{0.96f, 0.94f, 0.92f, 1.07f, 1.09f, 1.10f, 0.90f, 0.80f, 1.02f};
        float[] maxScore = new float[]{1.00f, 0.97f, 0.99f, 1.15f, 0.8f, 1.20f, 0.88f, 0.93f, 1.12f};
        float[] minScore = new float[]{0.92f, 0.90f, 0.87f, 1.02f, 1.13f, 1.08f, 0.81f, 0.80f, 1.00f};

        aScores.add(indicatorScore);
        aScores.add(avgScore);
        aScores.add(maxScore);
        aScores.add(minScore);

        chart.setY(600).setX(60).setHeight(120).setShowDataColInTables(new int[]{0, 1, 2, 3})
                .setTagNames(new String[]{"分数", "平均分", "最大", "最小"})
                .setItemNames(new String[]{"勇担责任", "履行责任", "负责守信", "勤学善思", "创新实践", "程序执行", "善于经营", "团队管理", "团队管理理"})
                .setScores(aScores).setWidth(450);

        chart.chart();

//		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
//		
//		 float y=chart.getPositionY();
//		 chart.moveLine(writer.getDirectContent(),0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableHistogramChart() throws Exception {
        doc.open();

        TableHistogramChart tableHistogramChart = new TableHistogramChart(writer, writer.getDirectContent(), doc,
                bfChinese);
        tableHistogramChart.setX(80).setY(500).setScores(new float[]{6f, 34.2f, 45.6f, 18.1f})
                .setPeopleNumbers(new String[]{"36", "102", "136", "24"});

        tableHistogramChart.chart();

        // writer.getDirectContent().setColorStroke(BaseColor.BLACK);
        //
        // float y=tableHistogramChart.getPositionY();
        // tableHistogramChart.moveLine(writer.getDirectContent(),0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableGradeDistributionChart() throws Exception {
        doc.open();

        List<String[]> lists = new ArrayList<String[]>();
        lists.add(new String[]{"12.1", "34.2", "45.6", "8.1"});
        lists.add(new String[]{"36", "102", "136", "24"});

        TableGradeDistributionChart tableGradeDistributionChart = new TableGradeDistributionChart(writer,
                writer.getDirectContent(), doc, bfChinese);
        tableGradeDistributionChart.setX(90).setY(550).setDataAreas(lists).setCurScore(60f);

        tableGradeDistributionChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = tableGradeDistributionChart.getPositionY();
        tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableRectLineRightCategoryChart() throws Exception {
        doc.open();

        TableRectLineRightCategoryChart chart = new TableRectLineRightCategoryChart(writer, writer.getDirectContent(),
                doc, bfChinese);

        List<float[]> aScores = new ArrayList<float[]>();
        float[] indicatorScore = new float[]{0.99f, 0.95f, 0.91f, 1.10f, 1.00f, 1.15f, 0.82f, 0.89f, 1.08f};
        float[] avgScore = new float[]{0.96f, 0.94f, 0.92f, 1.07f, 1.09f, 1.10f, 0.90f, 0.80f, 1.02f};
        float[] maxScore = new float[]{1.00f, 0.97f, 0.99f, 1.15f, 0.8f, 1.20f, 0.88f, 0.93f, 1.12f};
        float[] minScore = new float[]{0.92f, 0.90f, 0.87f, 1.02f, 1.13f, 1.08f, 0.81f, 0.80f, 1.00f};

        aScores.add(indicatorScore);
        aScores.add(avgScore);
        aScores.add(maxScore);
        aScores.add(minScore);

        chart.setY(450).setX(60).setHeight(120).setShowDataColInTables(new int[]{0, 1, 2, 3})
                .setTagNames(new String[]{"分数", "平均分", "最大", "最小"})
                .setItemNames(new String[]{"勇担责任", "履行责任", "负责守信", "勤学善思", "创新实践", "程序执行", "善于经营", "团队管理", "团队管理理"})
                .setScores(aScores).setWidth(450).setGradeRectWidth(50);

        chart.chart();

//		 writer.getDirectContent().setColorStroke(BaseColor.BLACK);
//		
//		 float y=chart.getPositionY();
//		 chart.moveLine(writer.getDirectContent(),0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testRadarSimpleRectChart() throws Exception {
        doc.open();

        float y = writer.getVerticalPosition(true);
        float[] s = new float[]{30.106f, 50, 100, 10, 20, 40, 10, 20, 40, 50, 100, 10, 20, 40, 10, 20, 40};
        String[] itemNames = new String[]{"能力能力能力", "我就垃圾费", "邮箱箱邮邮箱", "散步散步", "散步散步", "散步散步", "我就垃圾费", "邮箱箱邮邮箱",
                "散步散步", "我就垃圾费", "邮箱箱邮邮箱", "散步散步", "散步散步", "散步散步", "我就垃圾费", "邮箱箱邮邮箱", "散步散步"};

        RadarSimpleRectChart chart = new RadarSimpleRectChart(writer, writer.getDirectContent(), doc, bfChinese);

        chart.setX(270).setFontSize(9).setY(y - 200).setItemNames(itemNames).setScores(s);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        // ========================================================

        chart.setLine(28, doc);

        float y1 = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);

        // ========================================================

        DiamondBeforeParagraph chart1 = new DiamondBeforeParagraph(writer, writer.getDirectContent(), doc);

        chart1.setBaseChart(chart);
        chart1.setBaseFont(bfChinese);

        chart1.setItemNames(new String[]{"能力能力能力", "我就垃圾费", "邮箱箱邮邮箱", "散步散步", "散步散步", "散步散步", "我就垃圾费", "邮箱箱邮邮箱",
                "散步散步", "我就垃圾费", "邮箱箱邮邮箱", "散步散步", "散步散步", "散步散步", "我就垃圾费", "邮箱箱邮邮箱", "散步散步"})
                .setDescs(new String[]{"发违法", "案件范围", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟",
                        "案件范围", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟", "家居服挖坟"});

        chart1.chart();

        doc.close();
    }

    @Test
    public void testHistogramAreaExtremumChart() throws Exception {
        doc.open();

        float[] s = new float[]{1.2f, 4.1f, 8, 9};
        String[] itemNames = new String[]{"能力能力能力", "我就垃圾费", "邮箱箱邮邮箱", "散步散步"};

        HistogramAreaExtremumChart chart = new HistogramAreaExtremumChart(writer, writer.getDirectContent(), doc,
                bfChinese);

        chart.setX(80).setFontSize(10).setWidth(450).setY(500).setItemNames(itemNames).setScores(s);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y1 = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);

        doc.close();
    }

    @Test
    public void testHistogramManyHistogramChart() throws Exception {
        doc.open();

        List<float[]> scores = new ArrayList<float[]>();
        scores.add(new float[]{20.7f, 25f, 30, 35});
        scores.add(new float[]{52.6f, 15f, 50, 80});
        scores.add(new float[]{9.3f, 18f, 88, 100});

        String[] itemNames = new String[]{"能力能力能力", "我就垃圾费", "邮箱箱邮邮箱", "散步散步"};

        HistogramManyHistogramChart chart = new HistogramManyHistogramChart(writer, writer.getDirectContent(), doc,
                bfChinese);

        chart.setX(80).setFontSize(10).setWidth(450).setTypeFillColors(new int[]{0x72CBAD, 0xE47D54, 0xE1B04C})
                .setY(500).setItemNames(itemNames).setScores(scores).setTypeNames(new String[]{"男性", "女性", "人妖"});

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y1 = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);

        doc.close();
    }

    @Test
    public void testHistogramXOrYDirectionChart() throws Exception {
        doc.open();

        HistogramXOrYDirectionChart chart = new HistogramXOrYDirectionChart(writer, writer.getDirectContent(), doc,
                bfChinese);

        chart.setItemNames(new String[]{"男性", "女性", "人妖"}).setY(600).setX(50).setHeight(80).setWidth(200)
                .setScores(new float[]{2f, 6f, 9f}).setItemColors(new int[]{0x72CBAD, 0xE47D54, 0xFF0F00})
                .setHistogramDirection(HistogramXOrYDirectionChart.DIRECTION_Y);

        chart.chart();

        chart.setItemNames(new String[]{"男性", "女性", "人妖"}).setY(600).setX(300).setHeight(80).setWidth(200)
                .setScores(new float[]{2f, 6f, 9f}).setItemColors(new int[]{0x72CBAD, 0xE47D54, 0xFF0F00})
                .setHistogramDirection(HistogramXOrYDirectionChart.DIRECTION_X);

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.chart.HistogramXOrYDirectionChart",
                "chart.TestChart.testHistogramXOrYDirectionChart()");
        chart.setLine(1, doc);

        chart.chart();

        // writer.getDirectContent().setColorStroke(BaseColor.BLACK);
        //
        // float y1 =chart.getPositionY();
        // chart.moveLine(writer.getDirectContent(), 0, y1, doc.getPageSize().getWidth(), y1);

        doc.close();
    }

}
