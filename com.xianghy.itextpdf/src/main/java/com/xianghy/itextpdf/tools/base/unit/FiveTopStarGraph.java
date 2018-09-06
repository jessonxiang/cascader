package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 五角星，是否填充可选
 * @author cheny
 *
 */
public class FiveTopStarGraph extends AbstractBaseUnitChart {
	
	private float x;
	private float y;
	private float r=6;//半径。越大，图形越大
	private BaseColor color=BaseColor.BLACK;//图形颜色
	private float rotation;//旋转的角度
	private boolean isFillColor;//是否填充

	public FiveTopStarGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
							Document document) {
		super(baseChart, writer, contentByte, document);
	}

	public FiveTopStarGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
		super(writer, contentByte, document);
	}

	@Override
	public void chart() {
		float spaceSize=360f/5;
        float space=spaceSize;
        float x0=0,y0=0,nextX=0,nextY=0;
        final float max=90f;
        this.contentByte.setLineWidth(0.5f);
        
        List<TPoint> points=null;
        TPoint point=null;
        final float  offset=0.376f;
        
        for(int i=0;i<=5;i++){
        	this.contentByte.setColorStroke( color);
            x0=this.x + (float) (this.r * Math.sin(Math.PI * (space-spaceSize+this.rotation) / max));
            y0=this.y+ (float) (this.r * Math.cos(Math.PI * (space-spaceSize+this.rotation) / max));
            nextX=this.x + (float) (this.r * Math.sin(Math.PI * (space+this.rotation) / max));
            nextY=this.y+ (float) (this.r * Math.cos(Math.PI * (space+this.rotation) / max));

            if(i!=0){
            	this.getBaseChart().moveLine(this.contentByte,x0,y0,nextX,nextY);
            }

            //填充颜色
            if(isFillColor&&i>0){
                if(i==5)
                    space=spaceSize;
                for(float j=0;j<this.r;j+=0.1) {
                    nextX = this.x + (float) (j * Math.sin(Math.PI * (space+this.rotation) / max));
                    nextY = this.y + (float) (j * Math.cos(Math.PI * (space+this.rotation) / max));
                    this.getBaseChart().moveLine(this.contentByte, x0, y0, nextX, nextY);
                }
				//不填充颜色
            }else if(!isFillColor){
            	if(ObjectUtils.equals(null, points)){
            		points=new ArrayList<TPoint>();
            	}
            	point=new TPoint();
                 point.setX(this.x + (float) (this.r*offset * Math.sin(Math.PI * (space-spaceSize+18f) / max)));
                 point.setY(this.y+ (float) (this.r *offset* Math.cos(Math.PI * (space-spaceSize+18f) / max)));
                 switch (i) {
                case 0:
				case 1:
				case 2:
					points.add(i,point);
					break;
				case 3:
					points.add(1,point);
					break;
				case 4:
					points.add(3,point);
					break;
				default:
					break;
				}
            }
            space+=spaceSize;
        }
        
        if(points!=null){//不填充颜色
        	this.contentByte.setLineWidth(2f);
        	this.contentByte.setColorStroke(BaseColor.WHITE);
        	for(int i=0;i<points.size();i++){
        		if(i<points.size()-1){
        			this.getBaseChart().moveLine(this.contentByte, points.get(i).getX(), points.get(i).getY(),
        					points.get(i+1).getX(), points.get(i+1).getY());
        		}else{
        			this.getBaseChart().moveLine(this.contentByte, points.get(i).getX(), points.get(i).getY(),
        					points.get(0).getX(), points.get(0).getY());
        		}
        	}
        }
        
	}

	/**
	 * 是否填充
	 * @param isFillColor
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setFillColor(boolean isFillColor) {
		this.isFillColor = isFillColor;
		return this;
	}

	/**
	 * X坐标
	 * @param x
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 半径。越大，图形越大
	 * @param r
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setR(float r) {
		this.r = r;
		return this;
	}

	/**
	 * 图形颜色
	 * @param color
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setColor(BaseColor color) {
		this.color = color;
		return this;
	}
	
	/**
	 * 旋转的角度
	 * @param rotation
	 * @return FiveTopStarGraph
	 */
	public FiveTopStarGraph setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}
}
