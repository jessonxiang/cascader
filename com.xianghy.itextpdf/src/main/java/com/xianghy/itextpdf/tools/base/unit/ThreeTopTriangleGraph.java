package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 通过给定三个点，画出一个填充的三角形
 * @author cheny
 */
public class ThreeTopTriangleGraph extends AbstractBaseUnitChart {
	
	private float top_x;
	private float top_y;
	
	private float left_x;
	private float left_y;
	
	private float right_x;
	private float right_y;
	
	private int color;
	private boolean isFillColor=true;
	
	public ThreeTopTriangleGraph(AbstractBaseChart baseChart, PdfWriter writer,
								 PdfContentByte contentByte, Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public ThreeTopTriangleGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		BaseColor color_=new BaseColor(this.color);
		this.contentByte.setColorStroke(color_);
		if(!this.isFillColor){
			//画出三边
			this.getBaseChart().moveLine(this.contentByte, this.top_x, this.top_y, this.right_x, this.right_y);
			this.getBaseChart().moveLine(this.contentByte, this.top_x, this.top_y, this.left_x, this.left_y);
			this.getBaseChart().moveLine(this.contentByte, this.right_x, this.right_y, this.left_x, this.left_y);
			return ;
		}
		
		//这里没有用递归，是为了防止三角形太大的时候，栈溢出
		Deque<TPoint> stack=new LinkedList<TPoint>();
		TPoint onePoint=new TPoint();
		onePoint.setX(this.left_x);
		onePoint.setY(this.left_y);
		stack.push(onePoint);
		onePoint=new TPoint();
		onePoint.setX(this.right_x);
		onePoint.setY(this.right_y);
		stack.push(onePoint);
		
		TPoint centerPoint=null,anotherPoint=null;
		while(!stack.isEmpty()){
			centerPoint=new TPoint();
			anotherPoint=stack.pop();
			onePoint=stack.peek();
			//找出中心点
			centerPoint.setX((anotherPoint.getX()+onePoint.getX())/2);
			centerPoint.setY((anotherPoint.getY()+onePoint.getY())/2);
			//如果找出的点事两端点，不在处理并且把另一个点释放
			if((Math.round(centerPoint.getX()*10)==Math.round(onePoint.getX()*10)
					&&Math.round(centerPoint.getY()*10)==Math.round(onePoint.getY()*10))
				||(Math.round(centerPoint.getX()*10)==Math.round(anotherPoint.getX()*10)
					&&Math.round(centerPoint.getY()*10)==Math.round(anotherPoint.getY()*10))){
				stack.pop();
				continue;
			}
			this.getBaseChart().moveLine(this.contentByte, this.top_x, this.top_y,
															centerPoint.getX(), centerPoint.getY());
			//把将要处理的点放入栈中
			stack.push(centerPoint);
			stack.push(anotherPoint);
			stack.push(centerPoint);
		}
    }
	
	/**
	 *  一个点的X坐标
	 * @param top_x
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setTop_x(float top_x) {
		this.top_x = top_x;
		return this;
	}

	/**
	 *  一个点的Y坐标
	 * @param top_y
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setTop_y(float top_y) {
		this.top_y = top_y;
		return this;
	}

	/**
	 *  一个点的X坐标
	 * @param left_x
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setLeft_x(float left_x) {
		this.left_x = left_x;
		return this;
	}

	/**
	 *  一个点的Y坐标
	 * @param left_y
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setLeft_y(float left_y) {
		this.left_y = left_y;
		return this;
	}

	/**
	 *  一个点的X坐标
	 * @param right_x
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setRight_x(float right_x) {
		this.right_x = right_x;
		return this;
	}

	/**
	 * 一个点的Y坐标
	 * @param right_y
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setRight_y(float right_y) {
		this.right_y = right_y;
		return this;
	}

	/**
	 * 图形颜色
	 * @param color
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * 是否填充
	 * @param isFillColor
	 * @return ThreeTopTriangleGraph
	 */
	public ThreeTopTriangleGraph setFillColor(boolean isFillColor) {
		this.isFillColor = isFillColor;
		return this;
	}
}
