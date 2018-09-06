package com.xianghy.itextpdf.tools.base;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 所有PDF基本单位图形的超类
 * @author cheny
 */
public abstract class AbstractBaseUnitChart {
	private AbstractBaseChart baseChart;
	
	protected PdfWriter writer;
	protected PdfContentByte contentByte;
	protected Document document;
	protected BaseFont baseFont;
	
	/**
	 * 具体画图，需要实现的方法
	 */
	public abstract void chart();

	public AbstractBaseUnitChart(AbstractBaseChart baseChart,
			PdfWriter writer,PdfContentByte contentByte,Document document) {
		this.baseChart = baseChart;
		this.writer=writer;
		this.contentByte=contentByte;
		this.document=document;
	}

	public AbstractBaseUnitChart(PdfWriter writer,PdfContentByte contentByte,Document document) {
		this.writer=writer;
		this.contentByte=contentByte;
		this.document=document;
	}
	
	/**
	 * 具体的pdf图形的实例
	 * @param baseChart
	 */
	public void setBaseChart(AbstractBaseChart baseChart) {
		this.baseChart = baseChart;
	}

	/**
	 * 基本的字体，仅仅需要显示文本的时候才必须
	 * @param baseFont
	 */
	public void setBaseFont(BaseFont baseFont) {
		this.baseFont = baseFont;
	}

	/**
	 * 具体的pdf图形的实例
	 * @return AbstractBaseChart
	 */
	public AbstractBaseChart getBaseChart() {
		return baseChart;
	}
}
