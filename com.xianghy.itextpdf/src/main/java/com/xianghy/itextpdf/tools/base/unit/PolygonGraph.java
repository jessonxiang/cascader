package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;

/**
 * 画多边形
 *
 * @param x
 * @param y
 * @param r
 * @param sideNum
 * @param rotation
 * @param fillColor 填充的颜色
 */
public class PolygonGraph extends AbstractBaseUnitChart {

    private float x;
    private float y;
    private float r = 7;
    private int sideNum = 3;
    private float rotation;
    private BaseColor fillColor;

    public PolygonGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte cb, Document doc) {
        super(baseChart, writer, cb, doc);
    }

    public PolygonGraph(PdfWriter writer, PdfContentByte cb, Document doc) {
        super(writer, cb, doc);
    }

    @Override
    public void chart() {
        float spaceSize = 360f / this.sideNum;
        float space = spaceSize;
        float x0 = 0, y0 = 0, nextX = 0, nextY = 0;
        final float max = 180f;
        for (int i = 0 ; i <= this.sideNum ; i++) {
            x0 = this.x + (float) (this.r * Math.sin(Math.PI * (space - spaceSize + this.rotation) / max));
            y0 = this.y + (float) (this.r * Math.cos(Math.PI * (space - spaceSize + this.rotation) / max));
            nextX = this.x + (float) (this.r * Math.sin(Math.PI * (space + this.rotation) / max));
            nextY = this.y + (float) (this.r * Math.cos(Math.PI * (space + this.rotation) / max));

            if (i != 0) {
                this.getBaseChart().moveLine(this.contentByte, x0, y0, nextX, nextY);
            }

            //填充颜色
            if (this.fillColor != null && i > 0) {
                this.contentByte.setColorStroke(this.fillColor);
                this.contentByte.setColorFill(this.fillColor);
                if (i == this.sideNum)
                    space = spaceSize;
                for (float j = 0 ; j < this.r ; j += 0.1) {
                    nextX = this.x + (float) (j * Math.sin(Math.PI * (space + this.rotation) / max));
                    nextY = this.y + (float) (j * Math.cos(Math.PI * (space + this.rotation) / max));
                    this.getBaseChart().moveLine(this.contentByte, x0, y0, nextX, nextY);
                }
            } else {
                this.getBaseChart().moveLine(this.contentByte, this.x, this.y, nextX, nextY);
            }
            space += spaceSize;
        }
    }

    /**
     * X坐标
     *
     * @param x
     * @return PolygonGraph
     */
    public PolygonGraph setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return PolygonGraph
     */
    public PolygonGraph setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 半径
     *
     * @param r
     * @return PolygonGraph
     */
    public PolygonGraph setR(float r) {
        this.r = r;
        return this;
    }

    /**
     * 图形的边数
     *
     * @param sideNum
     * @return PolygonGraph
     */
    public PolygonGraph setSideNum(int sideNum) {
        this.sideNum = sideNum;
        return this;
    }

    /**
     * 旋转角度
     *
     * @param rotation
     * @return PolygonGraph
     */
    public PolygonGraph setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    /**
     * 是否填充
     *
     * @param fillColor
     * @return PolygonGraph
     */
    public PolygonGraph setFillColor(BaseColor fillColor) {
        this.fillColor = fillColor;
        return this;
    }
}
