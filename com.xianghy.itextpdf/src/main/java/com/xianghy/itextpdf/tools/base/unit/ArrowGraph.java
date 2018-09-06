package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;

/**
 * 单箭头图形
 * @author cheny
 */
public class ArrowGraph extends AbstractBaseUnitChart {
	
	//箭头的方向
	/**
	 * 向上
	 */
	public static final int ARROW_UP=1;
	/**
	 * 向下
	 */
	public static final int ARROW_DOWN=2;
	/**
	 * 向右
	 */
	public static final int ARROW_RIGHT=3;
	/**
	 * 向左
	 */
	public static final int ARROW_LEFT=4;
	
	private float height=15;//矩形区域的高度
	private float width=5;//矩形区域的宽度
	private float arrowWidth=5;//箭头的宽度
	private int color;// 图形的颜色
	private int arrowDirection=ARROW_RIGHT;//箭头的方向
	//箭头右：左上角 ，左：右上角，上：左下角，下：左上角
	private float x;//横坐标
	private float y;//纵坐标

	public ArrowGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public ArrowGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		TPoint onePoint=new TPoint();
		TPoint anotherPoint=new TPoint();
		TPoint topPoint=new TPoint();
		
		switch (this.arrowDirection) {//根据方向确定箭头的坐标
		case ARROW_UP : {
			onePoint.setX(this.x);
			onePoint.setY(this.y+this.width);
			
			anotherPoint.setX(this.x+this.height);
			anotherPoint.setY(this.y+this.width);
			
			topPoint.setX(this.x+this.height/2);
			topPoint.setY(this.y+this.width+this.arrowWidth);
			break;
		}
		case ARROW_DOWN:{
			onePoint.setX(this.x);
			onePoint.setY(this.y-this.width);
			
			anotherPoint.setX(this.x+this.height);
			anotherPoint.setY(this.y-this.width);
			
			topPoint.setX(this.x+this.height/2);
			topPoint.setY(this.y-this.width-this.arrowWidth);
			break;
		}
		case ARROW_RIGHT:{
			onePoint.setX(this.x+this.width);
			onePoint.setY(this.y);
			
			anotherPoint.setX(this.x+this.width);
			anotherPoint.setY(this.y-this.height);
			
			topPoint.setX(this.x+this.width+this.arrowWidth);
			topPoint.setY(this.y-this.height/2);
			break;
		}
		case ARROW_LEFT:{
			onePoint.setX(this.x-this.width);
			onePoint.setY(this.y);
			
			anotherPoint.setX(this.x-this.width);
			anotherPoint.setY(this.y-this.height);
			
			topPoint.setX(this.x-this.width-this.arrowWidth);
			topPoint.setY(this.y-this.height/2);
			break;
		}		
		default:
			break;
		}
		
		this.getBaseChart().moveRect(this.contentByte, this.x, this.y, anotherPoint.getX(), anotherPoint.getY(), this.color);
		this.contentByte.setColorStroke(new BaseColor(this.color));
		
		float x0=onePoint.getX(),y0=onePoint.getY();
		while(true){
			switch (this.arrowDirection) {//根据方向，确定哪个坐标在变，来填充三角形
			case ARROW_DOWN:
			case ARROW_UP:	
				x0+=0.1f;
				break;
			case ARROW_RIGHT:
			case ARROW_LEFT:	
				y0-=0.1f;
				break;
			default:
				break;
			} 
			
			if(x0>anotherPoint.getX()||y0<anotherPoint.getY()){
				break;
			}
			
			this.getBaseChart().moveLine(this.contentByte, x0,y0, topPoint.getX(), topPoint.getY());
		}
	}

	/**
	 * 矩形区域的高度
	 * @param height
	 * @return ArrowGraph
	 */
	public ArrowGraph setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * 矩形区域的宽度
	 * @param width
	 * @return ArrowGraph
	 */
	public ArrowGraph setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 箭头的宽度
	 * @param arrowWidth
	 * @return ArrowGraph
	 */
	public ArrowGraph setArrowWidth(float arrowWidth) {
		this.arrowWidth = arrowWidth;
		return this;
	}

	/**
	 * 横坐标
	 * @param x
	 * @return ArrowGraph
	 */
	public ArrowGraph setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * 纵坐标
	 * @param y
	 * @return ArrowGraph
	 */
	public ArrowGraph setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 图形的颜色
	 * @param color
	 * @return ArrowGraph
	 */
	public ArrowGraph setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * 箭头的方向
	 * @param arrowDirection
	 * @return ArrowGraph
	 */
	public ArrowGraph setArrowDirection(int arrowDirection) {
		this.arrowDirection = arrowDirection;
		return this;
	}
}
