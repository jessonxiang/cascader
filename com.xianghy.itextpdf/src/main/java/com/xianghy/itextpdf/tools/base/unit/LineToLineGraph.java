package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;

public class LineToLineGraph extends AbstractBaseUnitChart {
	
	private float x;
	private float y;
	private float x0;
	private float y0;
	/**
	 * 弧度大小，值越大，弧度越大
	 */
	private float radians;
	

	public LineToLineGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
						   Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public LineToLineGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		 
		
		

	}

	public LineToLineGraph setX(float x) {
		this.x = x;
		return this;
	}

	public LineToLineGraph setY(float y) {
		this.y = y;
		return this;
	}

	public LineToLineGraph setX0(float x0) {
		this.x0 = x0;
		return this;
	}

	public LineToLineGraph setY0(float y0) {
		this.y0 = y0;
		return this;
	}
}
