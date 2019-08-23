package com.xianghy.itextpdf.tools.base.text;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 前面是菱形的，后面是文本，还有子项文本
 *
 * @author cheny
 */
public class DiamondBeforeParagraph extends AbstractBaseUnitChart {

    private int fontColor = 0xFCA822;// 文字顏色
    private float fontSize = 12f;// 文字字体大小
    private float textOffsetY = 35f;// 描述的文字左边的偏移量

    private String[] itemNames;// 名称
    private String[] descs;// 描述

    public DiamondBeforeParagraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
                                  Document document) {
        super(baseChart, writer, contentByte, document);
    }

    public DiamondBeforeParagraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
        super(writer, contentByte, document);
    }

    @Override
    public void chart() {

        if (ObjectUtils.equals(null, this.itemNames) ||
                ObjectUtils.equals(null, this.baseFont) ||
                ObjectUtils.equals(null, this.descs)
                || this.itemNames.length < 1
                || this.descs.length < 1)
            throw new RuntimeException("请确保baseFont、itemNames、descs被添加！");

        BaseColor fontColor_ = new BaseColor(this.fontColor);
        Font font = new Font(this.baseFont, this.fontSize, Font.NORMAL, fontColor_);

        try {
            Paragraph paragraph = null;
            Chunk ch = null;

            Font f = new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK);
            Font f2 = new Font(this.baseFont, this.fontSize + 8f, Font.NORMAL, fontColor_);

            for (int i = 0 ; i < this.itemNames.length ; i++) {
                paragraph = new Paragraph();
                ch = new Chunk("◆", f2);
                ch.setTextRise(-3f);
                paragraph.add(ch);
                paragraph.add(new Chunk(this.itemNames[i], font));
                paragraph.setIndentationLeft(this.textOffsetY);
                this.document.add(paragraph);
                paragraph = new Paragraph(this.descs[i], f);
                paragraph.setIndentationLeft(this.textOffsetY + 10);
                this.document.add(paragraph);

                if (this.fontSize > 12) {
                    this.getBaseChart().setLine(1, this.fontSize / 4, this.document);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 文字顏色
     *
     * @param fontColor
     * @return DiamondBeforeParagraph
     */
    public DiamondBeforeParagraph setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    /**
     * 文字字体大小
     *
     * @param fontSize
     * @return DiamondBeforeParagraph
     */
    public DiamondBeforeParagraph setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 描述的文字左边的偏移量
     *
     * @param textOffsetY
     * @return DiamondBeforeParagraph
     */
    public DiamondBeforeParagraph setTextOffsetY(float textOffsetY) {
        this.textOffsetY = textOffsetY;
        return this;
    }

    /**
     * 比如：指标名称
     *
     * @param itemNames
     * @return DiamondBeforeParagraph
     */
    public DiamondBeforeParagraph setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
        return this;
    }

    /**
     * 对itemNames的详细描述
     *
     * @param descs
     * @return DiamondBeforeParagraph
     */
    public DiamondBeforeParagraph setDescs(String[] descs) {
        this.descs = descs;
        return this;
    }
}
