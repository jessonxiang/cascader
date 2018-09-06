package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;

/**
 * 双箭头图
 * @author cheny
 *
 */
public class DoubleArrowGraph extends AbstractBaseUnitChart {
	
	//是横向还是纵向
	public static final int LEVEL_X=1;
	public static final int LEVEL_Y=2;
	
	private float x;
	private float y;
	private float x0;
	private float y0;
	private float height;//箭头的高度
	private float arrowWidth=5;//箭头的宽度
	private int level=LEVEL_X;//箭头所在的水平线：LEVEL_X or LEVEL_Y
	private int color;//图形的颜色
	private float arrowLineWidth=1.5f;//箭头线的宽度
	

	public DoubleArrowGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
							Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public DoubleArrowGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		TPoint onePoint=new TPoint();
		TPoint onePoint_1=new TPoint();
		
		TPoint anotherPoint=new TPoint();
		TPoint anotherPoint_1=new TPoint();
		switch (level) {
		case LEVEL_X:{
			onePoint.setX(this.x+this.arrowWidth);
			onePoint.setY(this.y+this.height/2);
			
			onePoint_1.setX(this.x+this.arrowWidth);
			onePoint_1.setY(this.y-this.height/2);
			
			anotherPoint.setX(this.x0-this.arrowWidth);
			anotherPoint.setY(this.y0-this.height/2);
			
			anotherPoint_1.setX(this.x0-this.arrowWidth);
			anotherPoint_1.setY(this.y0+this.height/2);
			break;
		}
		case LEVEL_Y:{
			onePoint.setX(this.x+this.height/2);
			onePoint.setY(this.y-this.arrowWidth);
			
			onePoint_1.setX(this.x-this.height/2);
			onePoint_1.setY(this.y-this.arrowWidth);
			
			anotherPoint.setX(this.x0-this.height/2);
			anotherPoint.setY(this.y0+this.arrowWidth);
			
			anotherPoint_1.setX(this.x0+this.height/2);
			anotherPoint_1.setY(this.y0+this.arrowWidth);
			break;
		}
		default:
			break;
		}
		
		this.getBaseChart().moveRect(this.contentByte, onePoint.getX(), onePoint.getY()
													, anotherPoint.getX(), anotherPoint.getY(), this.color);
		
		this.contentByte.setLineWidth(this.arrowLineWidth);
		this.contentByte.setColorStroke(new BaseColor(this.color));
		this.getBaseChart().moveLine(this.contentByte, onePoint.getX(), onePoint.getY(), this.x, this.y);
		this.getBaseChart().moveLine(this.contentByte, onePoint_1.getX(), onePoint_1.getY(), this.x, this.y);
		 
		this.getBaseChart().moveLine(this.contentByte, anotherPoint.getX(), anotherPoint.getY(), this.x0, this.y0);
		this.getBaseChart().moveLine(this.contentByte, anotherPoint_1.getX(), anotherPoint_1.getY(), this.x0, this.y0);
	}

	/**
	 *  矩形的一个点的X坐标
	 * @param x
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * 矩形的一个点的Y坐标
	 * @param y
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 *  矩形的下一个点的X坐标
	 * @param x0
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setX0(float x0) {
		this.x0 = x0;
		return this;
	}

	/**
	 * 矩形的下一个点的Y坐标
	 * @param y0
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setY0(float y0) {
		this.y0 = y0;
		return this;
	}

	/**
	 * 箭头的高度
	 * @param height
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * 箭头的宽度
	 * @param arrowWidth
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setArrowWidth(float arrowWidth) {
		this.arrowWidth = arrowWidth;
		return this;
	}

	/**
	 * 箭头所在的水平线：LEVEL_X or LEVEL_Y
	 * @param level
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setLevel(int level) {
		this.level = level;
		return this;
	}

	/**
	 * 图形的颜色
	 * @param color
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * 箭头线的宽度
	 * @param arrowLineWidth
	 * @return DoubleArrowGraph
	 */
	public DoubleArrowGraph setArrowLineWidth(float arrowLineWidth) {
		this.arrowLineWidth = arrowLineWidth;
		return this;
	}
}
