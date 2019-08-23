package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;

/**
 * 两个斜三角组成的图形，下半个被填充
 *
 * @author cheny
 */
public class TwoTriangleGraph extends AbstractBaseUnitChart {

    private float height = 10;
    private float width = 10;
    private float x;
    private float y;
    private BaseColor color = BaseColor.BLACK;

    public TwoTriangleGraph(AbstractBaseChart baseChart, PdfWriter writer,
                            PdfContentByte contentByte, Document document) {
        super(baseChart, writer, contentByte, document);
    }

    public TwoTriangleGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
        super(writer, contentByte, document);
    }

    @Override
    public void chart() {
        //水平线左边的点
        TPoint leftLevel = new TPoint();
        leftLevel.setY(this.y - this.height / 2);
        leftLevel.setX(this.x - this.width);
        //水平线右边的点
        TPoint rightLevel = new TPoint();
        rightLevel.setY(leftLevel.getY());
        rightLevel.setX(this.x);
        //左边左上角的点
        TPoint leftUp = new TPoint();
        leftUp.setY(this.y);
        leftUp.setX(this.x - 4 * this.width / 3);
        //左边左下角的点
        TPoint leftDown = new TPoint();
        leftDown.setX(leftUp.getX());
        leftDown.setY(this.y - this.height);
        //画出上半边三角形
        this.contentByte.setLineWidth(0.5f);
        this.contentByte.setColorStroke(color);
        this.getBaseChart().moveLine(this.contentByte, leftLevel.getX(), leftLevel.getY(),
                rightLevel.getX(), rightLevel.getY());
        this.getBaseChart().moveLine(this.contentByte, rightLevel.getX(), rightLevel.getY(),
                leftUp.getX(), leftUp.getY());
        this.getBaseChart().moveLine(this.contentByte, leftLevel.getX(), leftLevel.getY(),
                leftUp.getX(), leftUp.getY());
        //画出下半边的三角形
        this.getBaseChart().moveLine(this.contentByte, rightLevel.getX(), rightLevel.getY(),
                leftDown.getX(), leftDown.getY());
        this.getBaseChart().moveLine(this.contentByte, leftDown.getX(), leftDown.getY(),
                leftLevel.getX(), leftLevel.getY());

        for (float i = leftLevel.getX() + 0.5f ; i < rightLevel.getX() ; i += 0.5f) {
            this.getBaseChart().moveLine(this.contentByte, leftDown.getX(),
                    leftDown.getY(), i, leftLevel.getY());
        }
    }

    /**
     * 高度
     *
     * @param height
     * @return TwoTriangleGraph
     */
    public TwoTriangleGraph setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * 宽度
     *
     * @param width
     * @return TwoTriangleGraph
     */
    public TwoTriangleGraph setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * X坐标
     *
     * @param x
     * @return TwoTriangleGraph
     */
    public TwoTriangleGraph setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return TwoTriangleGraph
     */
    public TwoTriangleGraph setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 图形颜色
     *
     * @param color
     * @return TwoTriangleGraph
     */
    public TwoTriangleGraph setColor(BaseColor color) {
        this.color = color;
        return this;
    }

}
