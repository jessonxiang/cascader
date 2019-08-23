package nbmf;

import base.TestBaseChart;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.nbmf.*;
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
    public void testTableGradeDistributionOnlyTextChart() throws Exception {
        doc.open();

        TableGradeDistributionOnlyTextChart tableGradeDistributionChart = new TableGradeDistributionOnlyTextChart(writer,
                writer.getDirectContent(), doc, bfChinese);
        tableGradeDistributionChart.setX(40).setY(700)
                .setGradeDescs(new String[]{"前前前前前w15% ", "前15%-前45% ", "后55%-后15% ", "后15% "})
                .setMaxNumber(100).setCurNumber("15").setCurPercentage(15)
                .setMarkCurPositon(false).setWidth(500)
                .setShowSepLine(true)
        ;

        tableGradeDistributionChart.chart();

        tableGradeDistributionChart.setX(40).setY(600)
                .setGradeDescs(null)
                .setMaxNumber(100).setCurNumber("60").setCurPercentage(95)
                .setMarkCurPositon(true).setWidth(500)
                .setShowSepLine(false)
        ;

        tableGradeDistributionChart.chart();

        TestBaseChart.addDescText(tableGradeDistributionChart,
                doc, bfChinese, "pdf.nbmf.TableGradeDistributionOnlyTextChart",
                "nbmf.TestChart.testTableGradeDistributionOnlyTextChart()");
        tableGradeDistributionChart.setLine(1, doc);

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = tableGradeDistributionChart.getPositionY();
        tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testCircleSeparateManySectorsChart() throws Exception {
        doc.open();

        CircleSeparateManySectorsChart tableGradeDistributionChart = new CircleSeparateManySectorsChart(writer,
                writer.getDirectContent(), doc, bfChinese);
        tableGradeDistributionChart.setX(doc.getPageSize().getWidth() / 2).setY(570)
                .setScores(new float[]{10,
                        20, 50,
                        60})
                .setR(120)
                .setItemNames(new String[]{"我认识我认识去认识我认识",
                        "爱我发给爱我去给爱我发给", "爱我发给爱我去给爱我发给",
                        "还让他杀人还让他"}).setTotalScore(50.6f).setTotalScoreNameColor(0x16CA4)
                .setFillColors(new int[]{0x1897BD, 0x2DBFD5, 0xBFBFBF, 0x16CA4})
        ;

        TestBaseChart.addDescText(tableGradeDistributionChart,
                doc, bfChinese, "pdf.nbmf.CircleSeparateManySectorsChart",
                "nbmf.TestChart.testCircleSeparateManySectorsChart()");
        tableGradeDistributionChart.setLine(1, doc);

        tableGradeDistributionChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = tableGradeDistributionChart.getPositionY();
        tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableChartedPositionAreaCurveChart() throws Exception {
        doc.open();

        for (int i = 0 ; i < 25 ; i++) {
            doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
        }

        TableChartedPositionAreaCurveChart chart = new TableChartedPositionAreaCurveChart(writer,
                writer.getDirectContent(), doc, bfChinese);

        chart.setWidth(450)
                .setFontSize(8)
                .setLineHeight(20)
                .setWidths(new float[]{10, 35, 5, 50})
                .setScoreLevels(new int[]{0, 1, 2, 3, 4, 5})
                .setRowColors(new int[]{0xFFFFFF, 0xF2F2F2})
                .setLevelFontSize(12)
                .setColNumber(2)
                .setScores(new float[]{1.5f, 2.25f, 3, 4,
                        4.2f, 1.5f, 0f, 5, 0.5f,
                        1, 3.5f, 3, 3.5f})
                .setGradeUpperLimitScore(new float[]{3f, 2.25f, 1.5f})
                .setTableHeads(new String[]{"指  标", "维度", "得得分", "  "})
                .setChildrenTypes(new ArrayList<String[]>() {
                    {
                        add(new String[]{"沟通能力沟通能力沟通能力沟通能力沟通能力", "合作能力"});
                        add(new String[]{"语言能力沟通能力沟通能力沟通能力沟通能力", "数字能力"});
                        add(new String[]{"组织归", "物质回报", "理想抱负", "自我实现", "获得尊重"
                                , "创新能力属组织归属组织归属组织归属组织归属组织归属组织归属", "学习能力", "逻辑能力", "机械推理"});
                    }
                })
                .setParentTypes(new String[]{"基础机械推械推", "基本", "领导"});

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.nbmf.TableChartedPositionAreaCurveChart",
                "nbmf.TestChart.testTableChartedPositionAreaCurveChart()");
        chart.setLine(1, doc);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableChartedPositionAreaCurveScoresChart() throws Exception {
        doc.open();

        for (int i = 0 ; i < 25 ; i++) {
            doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
        }

        TableChartedPositionAreaCurveScoresChart chart = new TableChartedPositionAreaCurveScoresChart(writer,
                writer.getDirectContent(), doc, bfChinese);

        chart.setWidth(450)
                .setFontSize(8)
                .setLineHeight(15)
                .setWidths(new float[]{10, 30, 5, 5, 5, 5, 50})
                .setScoreLevels(new int[]{0, 1, 2, 3, 4, 5})
                .setRowColors(new int[]{0xFFFFFF, 0xF2F2F2})
                .setLevelFontSize(7)
                .setMaxScoreColNum(3)
                .setMinScoreColNum(2)
                .setCurScoreColNum(0)
                .setScores(new float[][]{{3f, 2f, 1.5f, 4f}, {2, 2.5f, 2.0f, 3.5f}, {3f, 2f, 1.5f, 4f}, {2, 1, 0.8f, 2.2f},
                        {3.2f, 2, 1.85f, 3.2f}, {2.3f, 3.1f, 1.05f, 3.2f}, {5, 4, 3.02f, 5f}, {2, 2.5f, 2.0f, 3.5f}, {2, 1, 0.8f, 2.2f},
                        {3.2f, 2, 1.85f, 3.2f}, {2.3f, 3.1f, 1.05f, 3.2f}, {3f, 2f, 1.5f, 4f}, {5, 4, 3.02f, 5f}})
                .setTableHeads(new String[]{"指  标", "维度", "得分", "平均", "最低", "最高", "  "})
                .setChildrenTypes(new ArrayList<String[]>() {
                    {
                        add(new String[]{"沟通能力沟通能力沟通能力沟通能力沟通能力", "合作能力"});
                        add(new String[]{"语言能力沟通能力沟通能力沟通能力沟通能力", "数字能力"});
                        add(new String[]{"组织归", "物质回报", "理想抱负", "自我实现", "获得尊重"
                                , "创新能力属组织归属组织归属组织归属组织归属组织归属组织归属", "学习能力", "逻辑能力", "机械推理"});
                    }
                })
                .setParentTypes(new String[]{"基础机基础机基础机械推械", "基本", "领导"});

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.nbmf.TableChartedPositionAreaCurveScoresChart",
                "nbmf.TestChart.testTableChartedPositionAreaCurveScoresChart()");
        chart.setLine(1, doc);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }


    @Test
    public void testTableGradeSimpleAreaCurveChart() throws Exception {
        doc.open();

        TableGradeSimpleAreaCurveChart chart = new TableGradeSimpleAreaCurveChart(writer,
                writer.getDirectContent(), doc, bfChinese);

        chart.setWidth(450)
                .setX(80)
                .setY(500)
                .setFontSize(10)
                .setLineHeight(15)
                .setLevelFontSize(9)
                .setGradeColors(new int[]{0x5DD3B0, 0x92D050, 0xFFC000})
                .setGradeNames(new String[]{"优秀", "良好", "合格"})
                .setLevels(new float[]{2f, 4f, 6f, 8f, 10f})
                .setScores(new float[]{5.55f, 8.25f, 2.3f, 9f, 4.9f, 5.5f, 8.25f, 2.3f, 9f, 4.9f, 5.5f, 8.25f, 2.3f, 9f, 4.9f, 9f, 4.9f})
                .setItemNames(new String[]{"德德德德德德德德德德", "智", "体", "美", "劳"
                        , "德德德德德", "智", "体", "美", "劳", "德德德德", "智", "体", "美", "劳", "美", "劳"})
                .setGradeUpperLimitScore(new float[]{9f, 7f, 5f});

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.nbmf.TableGradeSimpleAreaCurveChart",
                "nbmf.TestChart.testTableGradeSimpleAreaCurveChart()");
        chart.setLine(1, doc);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testTableChartedManyCurveScoresChart() throws Exception {
        doc.open();

        for (int i = 0 ; i < 25 ; i++) {
            doc.add(new Paragraph("极乐空间阿尔法", new Font(bfChinese)));
        }

        TableChartedManyCurveScoresChart chart = new TableChartedManyCurveScoresChart(writer,
                writer.getDirectContent(), doc, bfChinese);

        chart.setWidth(450)
                .setFontSize(8)
                .setLineHeight(15)
                .setWidths(new float[]{10, 30, 5, 5, 5, 5, 5, 50})
                .setScoreLevels(new int[]{0, 1, 2, 3, 4, 5})
                .setRowColors(new int[]{0xFFFFFF, 0xF2F2F2})
                .setLevelFontSize(7)
                .setCurveColor(new int[]{0xBFBFBF, 0x4BACC6, 0x264F81, 0x4F81BD, 0x4BACC6})
                .setItemMarkFontSize(7)
                .setStringOnCurve(new String[]{"■", "★", "●", "▲", "★"})
                .setScores(new float[][]{{3f, 2f, 1.5f, 4f, 4f}, {2, 2.5f, 2.0f, 3.5f, 4f}, {3f, 2f, 1.5f, 4f, 4f}, {2, 1, 0.8f, 2.2f, 4f},
                        {3.2f, 2, 1.85f, 3.2f, 4f}, {2.3f, 3.1f, 1.05f, 3.2f, 4f}, {5, 4, 3.02f, 5f, 4f}, {2, 2.5f, 2.0f, 3.5f, 4f}, {2, 1, 0.8f, 2.2f, 4f},
                        {3.2f, 2, 1.85f, 3.2f, 4f}, {2.3f, 3.1f, 1.05f, 3.2f, 4f}, {3f, 2f, 1.5f, 4f, 4f}, {5, 4, 3.02f, 5f, 4f}})
                .setTableHeads(new String[]{"指  标", "维度", "得分得分得分得分", "得分得分得分得分", "平均平均平均平均", "最低最低最低最低", "最高最高最高最高", "  "})
                .setChildrenTypes(new ArrayList<String[]>() {
                    {
                        add(new String[]{"沟通能力沟通能力沟通能力沟通能力沟通能力", "合作能力"});
                        add(new String[]{"语言能力沟通能力沟通能力沟通能力沟通能力", "数字能力"});
                        add(new String[]{"组织归", "物质回报", "理想抱负", "自我实现", "获得尊重"
                                , "创新能力属组织归属组织归属组织归属组织归属组织归属组织归属", "学习能力", "逻辑能力", "机械推理"});
                    }
                })
                .setParentTypes(new String[]{"基础机基础机基础机械推械", "基本", "领导"});

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.nbmf.TableChartedManyCurveScoresChart",
                "nbmf.TestChart.testTableChartedManyCurveScoresChart()");
        chart.setLine(3, doc);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }


    @Test
    public void testTableSmallRectCurveLineChart() throws Exception {
        doc.open();

        TableSmallRectCurveLineChart chart = new TableSmallRectCurveLineChart(writer, writer.getDirectContent(),
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

        chart.setY(600).setX(90).setHeight(80).setShowDataColInTables(new int[]{0, 1, 2, 3})
                .setTagNameRowRectWidth(80)
                .setTagNames(new String[]{"分数", "平均分", "最大", "最小"})
                .setItemNames(new String[]{"勇担责任", "履行责任", "负责守信", "勤学善思", "创新实践", "程序执行", "善于经营", "团队管理", "团队管理理"})
                .setScores(aScores).setWidth(450);

        TestBaseChart.addDescText(chart,
                doc, bfChinese, "pdf.nbmf.TableSmallRectCurveLineChart",
                "nbmf.TestChart.testTableSmallRectCurveLineChart()");
        chart.setLine(3, doc);

        chart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = chart.getPositionY();
        chart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }

    @Test
    public void testCircleSeparateFixedSectorsChart() throws Exception {
        doc.open();

        CircleSeparateFixedSectorsChart tableGradeDistributionChart = new CircleSeparateFixedSectorsChart(writer,
                writer.getDirectContent(), doc, bfChinese);
        tableGradeDistributionChart.setX(doc.getPageSize().getWidth() / 2).setY(570)
                .setScores(new float[]{30, 70})
                .setR(120)
                /*.setItemNames(new String[]{"沟通能力沟通能力",
                        "负责守信"})*/.setTotalScore(30).setTotalScoreNameColor(0x16CA4)
                .setFillColors(new int[]{0xD9D9D9, 0x16CA4})
                .setTotalScoreColorFontSize(20)
        ;

        TestBaseChart.addDescText(tableGradeDistributionChart,
                doc, bfChinese, "pdf.nbmf.CircleSeparateFixedSectorsChart",
                "nbmf.TestChart1.testCircleSeparateFixedSectorsChart()");
        tableGradeDistributionChart.setLine(1, doc);

        tableGradeDistributionChart.chart();

        writer.getDirectContent().setColorStroke(BaseColor.BLACK);

        float y = tableGradeDistributionChart.getPositionY();
        tableGradeDistributionChart.moveLine(writer.getDirectContent(), 0, y, doc.getPageSize().getWidth(), y);

        doc.close();
    }
}
