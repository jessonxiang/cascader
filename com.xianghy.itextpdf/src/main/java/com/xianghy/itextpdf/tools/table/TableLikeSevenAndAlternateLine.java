package com.xianghy.itextpdf.tools.table;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.xianghy.itextpdf.tools.chart.AbstractChart;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * 行颜色交替+看起来像一个反向的7
 *
 * @author cheny
 */
public class TableLikeSevenAndAlternateLine extends AbstractChart {

    private float width = 440;
    // 表头数据
    private String[] headDatas;
    //每一行的数据
    private List<Object[]> rowDatas;

    private int firstColumnColor = 0xDBEEF4;// 第一列背景颜色
    private int[] rowColors;// 行的交替背景颜色
    private int headFontColor = 0xFFFFFF;// 表头文字颜色
    private int headBackgroundColor = 0x016CA4;// 表头背景颜色

    private float fontSize = 10;// 字体大小

    public TableLikeSevenAndAlternateLine() {
        super();
    }

    public TableLikeSevenAndAlternateLine(PdfWriter writer, PdfContentByte contentByte, Document document,
                                          BaseFont baseFont) {
        super(writer, contentByte, document, baseFont);
    }

    @Override
    public void chart() {
        if (ObjectUtils.equals(null, this.rowDatas) || this.rowDatas.isEmpty()
                || ObjectUtils.equals(null, this.headDatas) || this.headDatas.length < 1)
            throw new RuntimeException("请检测rowDatas、headDatas数据是否存在！");

        if (ObjectUtils.equals(null, rowColors)) {
            this.rowColors = new int[]{0xFFFFFF, 0xF2F2F2};
        }

        try {
            BaseColor headFontColor_ = new BaseColor(this.headFontColor);
            BaseColor headBackgroundColor_ = new BaseColor(this.headBackgroundColor);
            BaseColor firstColumnColor_ = new BaseColor(this.firstColumnColor);
            BaseColor[] rowColors_ = new BaseColor[]{new BaseColor(this.rowColors[0]),
                    new BaseColor(this.rowColors[1])};

            float[] ws = new float[this.headDatas.length];
            for (int i = 0 ; i < ws.length ; i++) {
                ws[i] = 100 / this.headDatas.length;
            }

            // 设置 Table
            PdfPTable pTable = new PdfPTable(this.headDatas.length);
            pTable.setTotalWidth(this.width);
            pTable.setLockedWidth(true);
            pTable.setWidths(ws);

            PdfPCell cell = null;
            // 添加TH
            for (int i = 0 ; i < this.headDatas.length ; i++) {
                cell = this.addOneCell(this.headDatas[i], "th", headFontColor_, headBackgroundColor_,
                        headBackgroundColor_, Font.BOLD, 0, 10);
                if (i > 0) {
                    cell.setBorderWidthLeft(1.5f);
                    cell.setBorderColorLeft(BaseColor.WHITE);
                }
                pTable.addCell(cell);
            }

            //加TD
            for (int i = 0 ; i < this.rowDatas.size() ; i++) {
                Object[] aStr = (Object[]) this.rowDatas.get(i);
                for (int j = 0 ; j < aStr.length ; j++) {
                    cell = this.addOneCell(aStr[j].toString(), "td", BaseColor.BLACK, j == 0 ? firstColumnColor_ : rowColors_[i % 2],
                            headBackgroundColor_, Font.NORMAL, 0, 10);

                    if (j == this.headDatas.length - 1) {
                        cell.setBorderWidthRight(1f);
                        cell.setBorderColorRight(headBackgroundColor_);
                    }
                    if (i == this.rowDatas.size() - 1) {
                        cell.setBorderWidthBottom(1f);
                        cell.setBorderColorBottom(headBackgroundColor_);
                    }
                    pTable.addCell(cell);
                }
            }

            this.document.add(pTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为了table的各个边框颜色不同
     *
     * @param str
     * @param type
     * @param style
     * @param fixedHeight
     * @param padding
     * @return
     */
    protected PdfPCell addOneCell(String str, String type, BaseColor fontColor, BaseColor backColor,
                                  BaseColor borderColor, int style, float fixedHeight, float padding) {

        PdfPCell cell = new PdfPCell(new Paragraph(str, new Font(this.baseFont, this.fontSize, style, fontColor)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中Element.ALIGN_CENTER
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
        cell.setBackgroundColor(backColor);// 背景颜色

        // 设置固定高度
        if (fixedHeight != 0) {
            cell.setFixedHeight(fixedHeight);// 设置固定高度
        }

        // 设置边距
        if (0 != padding) {
            cell.setPaddingTop(padding);// 设置上边距
            cell.setPaddingBottom(padding);// 设置下边距
        } else {
            cell.setPaddingBottom(5);
        }

        cell.setLeading(1, 1.2f);
        cell.setBorder(Rectangle.NO_BORDER);

        if ("td".equals(type)) {
            cell.setBorderWidthTop(1f);
            cell.setBorderColorTop(BaseColor.WHITE);
            cell.setBorderWidthLeft(1f);
            cell.setBorderColorLeft(borderColor);
        }

        return cell;
    }

    /**
     * 表格宽度
     *
     * @param width
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * 表头数据
     *
     * @param headDatas
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setHeadDatas(String[] headDatas) {
        this.headDatas = headDatas;
        return this;
    }

    /**
     * 每一行的数据
     *
     * @param rowDatas
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setRowDatas(List<Object[]> rowDatas) {
        this.rowDatas = rowDatas;
        return this;
    }

    /**
     * 第一列背景颜色
     *
     * @param firstColumnColor
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setFirstColumnColor(int firstColumnColor) {
        this.firstColumnColor = firstColumnColor;
        return this;
    }

    /**
     * 行的交替背景颜色
     *
     * @param rowColors
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setRowColors(int[] rowColors) {
        this.rowColors = rowColors;
        return this;
    }

    /**
     * 表头文字颜色
     *
     * @param headFontColor
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setHeadFontColor(int headFontColor) {
        this.headFontColor = headFontColor;
        return this;
    }

    /**
     * 表头背景颜色
     *
     * @param headBackgroundColor
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setHeadBackgroundColor(int headBackgroundColor) {
        this.headBackgroundColor = headBackgroundColor;
        return this;
    }

    /**
     * 字体大小
     *
     * @param fontSize
     * @return TableLikeSevenAndAlternateLine
     */
    public TableLikeSevenAndAlternateLine setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }
}
