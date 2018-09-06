package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;

/**
 * 画多边形
 * @param x
 * @param y
 * @param r
 * @param sideNum
 * @param rotation
 * @param fillColor 填充的颜色
 */
public class Polygon extends AbstractBaseUnitChart {
	
	private float x;
	private float y;
	private float r;
	private int sideNum;
	private float rotation;
	private BaseColor fillColor;
	
	public Polygon(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte cb, Document doc) {
		super(baseChart, writer, cb, doc);
	}

	public Polygon(PdfWriter writer, PdfContentByte cb, Document doc) {
		super(writer, cb, doc);
	}

	@Override
	public void chart() {
		float spaceSize=360f/this.sideNum;
        float space=spaceSize;
        float x0=0,y0=0,nextX=0,nextY=0;
        final float max=180f;
        for(int i=0;i<=this.sideNum;i++){
            x0=this.x + (float) (this.r * Math.sin(Math.PI * (space-spaceSize+this.rotation) / max));
            y0=this.y+ (float) (this.r * Math.cos(Math.PI * (space-spaceSize+this.rotation) / max));
            nextX=this.x + (float) (this.r * Math.sin(Math.PI * (space+this.rotation) / max));
            nextY=this.y+ (float) (this.r * Math.cos(Math.PI * (space+this.rotation) / max));

            if(i!=0){
            	this.getBaseChart().moveLine(this.contentByte,x0,y0,nextX,nextY);
            }

            //填充颜色
            if(this.fillColor!=null&&i>0){
            	this.contentByte.setColorStroke(this.fillColor);
            	this.contentByte.setColorFill(this.fillColor);
                if(i==this.sideNum)
                    space=spaceSize;
                for(float j=0;j<this.r;j+=0.1) {
                    nextX = this.x + (float) (j * Math.sin(Math.PI * (space+this.rotation) / max));
                    nextY = this.y + (float) (j * Math.cos(Math.PI * (space+this.rotation) / max));
                    this.getBaseChart().moveLine(this.contentByte, x0, y0, nextX, nextY);
                }
            }else{
            	this.getBaseChart().moveLine(this.contentByte, this.x, this.y, nextX, nextY);
            }
            space+=spaceSize;
        }
	}

	public Polygon setX(float x) {
		this.x = x;
		return this;
	}

	public Polygon setY(float y) {
		this.y = y;
		return this;
	}

	public Polygon setR(float r) {
		this.r = r;
		return this;
	}

	public Polygon setSideNum(int sideNum) {
		this.sideNum = sideNum;
		return this;
	}

	public Polygon setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}

	public Polygon setFillColor(BaseColor fillColor) {
		this.fillColor = fillColor;
		return this;
	}
}
