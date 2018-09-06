package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;

/**
 * 备注提示框
 * @author cheny
 *
 */
public class NoteTipGraph extends AbstractBaseUnitChart {

	private float x;
	private float y;
	private float height=15;
	private float width=20;
	private int color;
	
	public NoteTipGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public NoteTipGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		this.contentByte.setColorStroke(new BaseColor(this.color));
		 this.contentByte.setLineWidth(0.5f);
		 this.contentByte.roundRectangle(this.x, this.y, this.width	, this.height, this.height/5);
		 this.contentByte.stroke();
		 
		 float y0=this.y-this.height*1/5;
		 float x0=this.x+this.width/2;
		 float x1=this.x+this.width*3/4;
		 this.getBaseChart().moveLine(this.contentByte, x0,this.y , x0,y0);
		 this.getBaseChart().moveLine(this.contentByte, x1,this.y , x0,y0);
		 this.contentByte.setLineWidth(1f);
		 this.contentByte.setColorStroke(BaseColor.WHITE);
		 this.getBaseChart().moveLine(this.contentByte, x0+0.4f,this.y , x1-0.8f,this.y);
	}

	/**
	 * X坐标
	 * @param x
	 * @return NoteTipGraph
	 */ 
	public NoteTipGraph setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return NoteTipGraph
	 */
	public NoteTipGraph setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 高度
	 * @param height
	 * @return NoteTipGraph
	 */
	public NoteTipGraph setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * 宽度
	 * @param width
	 * @return NoteTipGraph
	 */
	public NoteTipGraph setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 图形颜色
	 * @param color
	 * @return NoteTipGraph
	 */
	public NoteTipGraph setColor(int color) {
		this.color = color;
		return this;
	}
}
