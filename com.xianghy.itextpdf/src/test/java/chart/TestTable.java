package chart;

import base.TestBaseChart;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.table.TableCommonTable;
import com.xianghy.itextpdf.tools.table.TableLikeSevenAndAlternateLine;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestTable {

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
    public void testTableLikeSevenAndAlternateLine() throws Exception {
        doc.open();

        TableLikeSevenAndAlternateLine tableLikeSevenAndAlternateLine =
                new TableLikeSevenAndAlternateLine(writer, writer.getDirectContent(), doc, bfChinese);

        List<Object[]> orgScoreOrders = new ArrayList<Object[]>();
        orgScoreOrders.add(new String[]{"开拓创新", "创新意识", "4.75", "3.63"});
        orgScoreOrders.add(new String[]{"开拓创新", "创新思维", "3.13", "3.75"});
        orgScoreOrders.add(new String[]{"成就导向", "主动性", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"成就导向", "学习意识", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"成就导向", "成就性", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"成就导向", "进取性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"文化适应", "开放性", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"文化适应", "抗压性", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"文化适应", "情绪稳定", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"文化适应", "坚韧性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"团队领导", "自信心", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"团队领导", "自主性", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"团队领导", "支配性", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"团队领导", "说服力", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"关系建立", "主动沟通", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"关系建立", "人际洞察", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"关系建立", "关系建立", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"关系建立", "社交性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"关系建立", "灵活性", "6.50", "6.50"});

        tableLikeSevenAndAlternateLine.setHeadDatas(new String[]{"部门名称", "部门得分", "部门排名", "前15%个员工"})
                .setRowDatas(orgScoreOrders);

        tableLikeSevenAndAlternateLine.chart();

        doc.close();
    }

    @Test
    public void testTableCommonTable() throws Exception {
        doc.open();

        TableCommonTable tableCommonTable =
                new TableCommonTable(writer, writer.getDirectContent(), doc, bfChinese);

        List<Object[]> orgScoreOrders = new ArrayList<Object[]>();
        orgScoreOrders.add(new String[]{"开拓创新", "创新意识", "4.75", "3.63"});
        orgScoreOrders.add(new String[]{"开拓创新", "创新思维", "3.13", "3.75"});
        orgScoreOrders.add(new String[]{"成就导向", "主动性", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"成就导向", "学习意识", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"成就导向", "成就性", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"成就导向", "进取性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"文化适应", "开放性", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"文化适应", "抗压性", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"文化适应", "情绪稳定", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"文化适应", "坚韧性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"团队领导", "自信心", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"团队领导", "自主性", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"团队领导", "支配性", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"团队领导", "说服力", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"关系建立", "主动沟通", "5.00", "4.31"});
        orgScoreOrders.add(new String[]{"关系建立", "人际洞察", "2.75", "3.44"});
        orgScoreOrders.add(new String[]{"关系建立", "关系建立", "5.25", "4.81"});
        orgScoreOrders.add(new String[]{"关系建立", "社交性", "6.50", "5.44"});
        orgScoreOrders.add(new String[]{"关系建立", "灵活性", "6.50", "6.50"});

        tableCommonTable
                .setRowDatas(orgScoreOrders).setWidths(new float[]{15, 15, 35, 35})
                .setOtherColumnAlign(Element.ALIGN_LEFT).setFirstColumnColors(0x9090F0)
                .setRowColors(new int[]{0xFFFFFF, 0xEFEFEF}).setFirstColumnFontColor(0xFF00FF)
        ;

        TestBaseChart.addDescText(tableCommonTable,
                doc, bfChinese, "pdf.table.TableCommonTable",
                "chart.TestTable.testTableCommonTable()");
        tableCommonTable.setLine(1, doc);

        tableCommonTable.chart();

        doc.close();
    }

}
