package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;

/**
 * 圆滑的曲线图，节点是否有圆圈可选
 * @author cheny
 *
 */
public class CurveHasRadianGraph extends AbstractBaseUnitChart {
	
	private float x;
	private float y;
	private float x0;
	private float y0;
	private int color;
	private boolean hasCircle=true;
	private float lineWidth=1;

	public CurveHasRadianGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
							   Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public CurveHasRadianGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		//当两点的高度是30，决定弧度是59的时候，水平距离是170，曲线很圆滑，要问为什么，自己穷数据得出结论
		float defaultY=30,defaultX=59,subX=170;
		float nowX=defaultY/Math.abs(this.y-this.y0)*defaultX;
		float nowSubX=Math.abs(this.x-this.x0)*defaultX/subX;
		
		nowX=Math.abs(this.x-this.x0)>subX?nowX:nowSubX;
		
		this.contentByte.setColorStroke(new BaseColor(this.color));
		this.contentByte.setLineDash(1,this.lineWidth);
		
		this.contentByte.moveTo(this.x, this.y);
		this.contentByte.curveTo(this.x+nowX, this.y,this.x0-nowX,this.y0,this.x0,this.y0);
		
		this.contentByte.stroke();
		
		if(this.hasCircle){
			this.contentByte.setColorFill(BaseColor.WHITE);
			this.getBaseChart().moveCircle(this.contentByte, this.x, this.y, 2, true);
			this.getBaseChart().moveCircle(this.contentByte, this.x0, this.y0, 2, true);
			this.contentByte.setLineDash(this.lineWidth);
			this.contentByte.circle(this.x, this.y, 2);
			this.contentByte.circle(this.x0, this.y0, 2);
			this.contentByte.stroke();
		}
	}

	/**
	 * 一个点的X坐标
	 * @param x
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * 一个点的Y坐标
	 * @param y
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 另一个点的X坐标
	 * @param x0
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setX0(float x0) {
		this.x0 = x0;
		return this;
	}

	/**
	 * 另一个点的Y坐标
	 * @param y0
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setY0(float y0) {
		this.y0 = y0;
		return this;
	}

	/**
	 * 颜色
	 * @param color
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * 是否有圆圈
	 * @param hasCircle
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setHasCircle(boolean hasCircle) {
		this.hasCircle = hasCircle;
		return this;
	}

	/**
	 * 线的宽度
	 * @param lineWidth
	 * @return CurveHasRadianGraph
	 */
	public CurveHasRadianGraph setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}
}
