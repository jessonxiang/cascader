package base;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;

public class TestBaseChart extends AbstractBaseChart {

    static public void addDescText(AbstractBaseChart baseChart, Document doc, BaseFont bfChinese
            , String drawGraphInstance, String testDrawGraph) {
        /**
         * TestBaseChart.addDescText(baseChart,
         doc, bfChinese, "pdf.base.unit.TwoTriangleGraph",
         "base.TestBase.testTwoTriangleGraph()");
         */
        try {
            doc.add(new Paragraph("实现类：", new Font(bfChinese)));
            doc.add(new Paragraph(drawGraphInstance, new Font(bfChinese)));

            baseChart.setLine(1, doc);
            doc.add(new Paragraph("测试及使用方法：", new Font(bfChinese)));
            doc.add(new Paragraph(testDrawGraph, new Font(bfChinese)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
