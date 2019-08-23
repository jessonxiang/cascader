package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;

/**
 * 所有PDF图形的超类
 *
 * @author cheny
 */
public abstract class AbstractChart extends AbstractBaseChart {

    protected PdfWriter writer;
    protected PdfContentByte contentByte;
    protected Document document;
    protected BaseFont baseFont;

    /**
     * 具体画图，需要实现的方法
     */
    public abstract void chart();

    public AbstractChart(PdfWriter writer, PdfContentByte contentByte,
                         Document document, BaseFont baseFont) {
        super();
        this.writer = writer;
        this.contentByte = contentByte;
        this.document = document;
        this.baseFont = baseFont;
    }

    public AbstractChart() {
        super();
    }

    /**
     * com.itextpdf.text.pdf.PdfWriter
     *
     * @param writer
     */
    public void setWriter(PdfWriter writer) {
        this.writer = writer;
    }

    /**
     * com.itextpdf.text.pdf.PdfContentByte
     *
     * @param contentByte
     */
    public void setContentByte(PdfContentByte contentByte) {
        this.contentByte = contentByte;
    }

    /**
     * com.itextpdf.text.Document
     *
     * @param document
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * 基本的字体
     *
     * @param baseFont
     */
    public void setBaseFont(BaseFont baseFont) {
        this.baseFont = baseFont;
    }
}
