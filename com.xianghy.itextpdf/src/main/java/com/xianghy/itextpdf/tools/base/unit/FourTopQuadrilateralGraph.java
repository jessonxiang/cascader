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
 * 通过给定四个点，画出一个填充的四边形 或者 一些其它的图形
 * @author cheny
 */
public class FourTopQuadrilateralGraph extends AbstractBaseUnitChart {
	
	private float first_x;
	private float first_y;
	
	private float second_x;
	private float second_y;
	
	private float third_x;
	private float third_y;
	
	private float fourth_x;
	private float fourth_y;
	
	private int color;
	private boolean isFillColor=true;
	
	public FourTopQuadrilateralGraph(AbstractBaseChart baseChart, PdfWriter writer,
									 PdfContentByte contentByte, Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public FourTopQuadrilateralGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		BaseColor color_=new BaseColor(this.color);
		this.contentByte.setColorStroke(color_);
		if(!this.isFillColor){
			//画出四边
			this.getBaseChart().moveLine(this.contentByte, this.first_x, this.first_y, this.second_x, this.second_y);
			this.getBaseChart().moveLine(this.contentByte, this.third_x, this.third_y, this.fourth_x, this.fourth_y);
			
			this.getBaseChart().moveLine(this.contentByte, this.first_x, this.first_y, this.fourth_x, this.fourth_y);
			this.getBaseChart().moveLine(this.contentByte, this.third_x, this.third_y, this.second_x, this.second_y);
			return ;
		}
		
		double l1=Math.sqrt((this.second_x-this.first_x)*(this.second_x-this.first_x)
											+(this.second_y-this.first_y)*(this.second_y-this.first_y));
		double l2=Math.sqrt((this.third_x-this.fourth_x)*(this.third_x-this.fourth_x)
											+(this.third_y-this.fourth_y)*(this.third_y-this.fourth_y));
		
		//这里没有用递归，是为了防止三角形太大的时候，栈溢出
		Deque<TPoint> oneStack=new LinkedList<TPoint>();
		Deque<TPoint> anotherStack=new LinkedList<TPoint>();
		//找出边长大的一边
		TPoint onePoint=new TPoint();
		if(l1>l2){
			onePoint.setX(this.first_x);
			onePoint.setY(this.first_y);
			oneStack.push(onePoint);
			 
			onePoint=new TPoint();
			onePoint.setX(this.second_x);
			onePoint.setY(this.second_y);
			oneStack.push(onePoint);
			
			onePoint=new TPoint();
			onePoint.setX(this.fourth_x);
			onePoint.setY(this.fourth_y);
			anotherStack.push(onePoint);
			onePoint=new TPoint();
			onePoint.setX(this.third_x);
			onePoint.setY(this.third_y);
			anotherStack.push(onePoint);
		}else{
			onePoint.setX(this.first_x);
			onePoint.setY(this.first_y);
			anotherStack.push(onePoint);
			 
			onePoint=new TPoint();
			onePoint.setX(this.second_x);
			onePoint.setY(this.second_y);
			anotherStack.push(onePoint);
			
			onePoint=new TPoint();
			onePoint.setX(this.fourth_x);
			onePoint.setY(this.fourth_y);
			oneStack.push(onePoint);
			onePoint=new TPoint();
			onePoint.setX(this.third_x);
			onePoint.setY(this.third_y);
			oneStack.push(onePoint);
		}
		
		TPoint centerPoint=null,anotherPoint=null,oneCenterPoint=null;
		//找出一边上所有的点
		while(!oneStack.isEmpty()){
			centerPoint=new TPoint();
			anotherPoint=oneStack.pop();
			onePoint=oneStack.peek();
			//找出一个边的中心点
			centerPoint.setX((anotherPoint.getX()+onePoint.getX())/2);
			centerPoint.setY((anotherPoint.getY()+onePoint.getY())/2);
			//如果找出的点事两端点，不在处理并且把另一个点释放
			if((Math.round(centerPoint.getX()*10)==Math.round(onePoint.getX()*10)
					&&Math.round(centerPoint.getY()*10)==Math.round(onePoint.getY()*10))
				||(Math.round(centerPoint.getX()*10)==Math.round(anotherPoint.getX()*10)
					&&Math.round(centerPoint.getY()*10)==Math.round(anotherPoint.getY()*10))){
				oneStack.pop();
				anotherStack.pop();
				anotherStack.pop();
				continue;
			}
			//把将要处理的点放入栈中
			oneStack.push(centerPoint);
			oneStack.push(anotherPoint);
			oneStack.push(centerPoint);
			
			oneCenterPoint=new TPoint();
			anotherPoint=anotherStack.pop();
			onePoint=anotherStack.peek();
			//找出另一个边的中心点
			oneCenterPoint.setX((anotherPoint.getX()+onePoint.getX())/2);
			oneCenterPoint.setY((anotherPoint.getY()+onePoint.getY())/2);
			
			//把将要处理的点放入栈中
			anotherStack.push(oneCenterPoint);
			anotherStack.push(anotherPoint);
			anotherStack.push(oneCenterPoint);
			this.getBaseChart().moveLine(this.contentByte, 
					oneCenterPoint.getX(),oneCenterPoint.getY(), 
					centerPoint.getX(), centerPoint.getY());
		}
    }
	
	/**
	 * 第一个点X坐标
	 * @param first_x
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setFirst_x(float first_x) {
		this.first_x = first_x;
		return this;
	}

	/**
	 * 第一个点Y坐标
	 * @param first_y
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setFirst_y(float first_y) {
		this.first_y = first_y;
		return this;
	}

	/**
	 * 第二个点X坐标
	 * @param second_x
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setSecond_x(float second_x) {
		this.second_x = second_x;
		return this;
	}

	/**
	 * 第二个点Y坐标
	 * @param second_y
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setSecond_y(float second_y) {
		this.second_y = second_y;
		return this;
	}

	/**
	 * 第三个点X坐标
	 * @param third_x
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setThird_x(float third_x) {
		this.third_x = third_x;
		return this;
	}

	/**
	 * 第三个点Y坐标
	 * @param third_y
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setThird_y(float third_y) {
		this.third_y = third_y;
		return this;
	}

	/**
	 * 第四个点X坐标
	 * @param fourth_x
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setFourth_x(float fourth_x) {
		this.fourth_x = fourth_x;
		return this;
	}

	/**
	 * 第四个点Y坐标
	 * @param fourth_y
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setFourth_y(float fourth_y) {
		this.fourth_y = fourth_y;
		return this;
	}

	/**
	 * 图形颜色
	 * @param color
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * 是否填充
	 * @param isFillColor
	 * @return FourTopQuadrilateralGraph
	 */
	public FourTopQuadrilateralGraph setFillColor(boolean isFillColor) {
		this.isFillColor = isFillColor;
		return this;
	}
}
