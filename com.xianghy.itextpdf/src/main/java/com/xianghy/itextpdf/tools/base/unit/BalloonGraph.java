package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import com.xianghy.itextpdf.tools.base.TPoint;
import org.apache.commons.lang3.ObjectUtils;
/**
 * 一个类似于气球的图形
 *
 * @author cheny
 */

/**
 * @author cheny
 *
 */
public class BalloonGraph extends AbstractBaseUnitChart {

    // 气球的半径
    private float ballR = 10;

    // 直线的下面的坐标，高度，宽度
    private float x;
    private float y;
    private float lineHeight = 20;//气球系线的高度
    private float lineWidth = 1.5f;//气球系线的宽度

    private BaseColor color;//图形的颜色
    private float balloonBorder = 2f;//气球的边框的宽度
    private float areaHeight = 8;//线到气球的填充区域的高度

    public BalloonGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte, Document document) {
        super(baseChart, writer, contentByte, document);
    }

    public BalloonGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
        super(writer, contentByte, document);
    }

    @Override
    public void chart() {
        if (ObjectUtils.equals(null, color)) {
            color = new BaseColor(0x16CA4);
        }

        // 画出气球的绳子系住的线
        this.contentByte.setColorStroke(color);
        this.contentByte.setLineWidth(lineWidth);
        this.getBaseChart().moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.lineHeight);

        // 开始画出下面的三角形
        TPoint leftPoint = new TPoint();
        TPoint rightPoint = new TPoint();

        leftPoint.setX(this.x - this.ballR);
        leftPoint.setY(this.y + this.lineHeight + this.ballR + this.areaHeight);

        rightPoint.setX(this.x + this.ballR);
        rightPoint.setY(this.y + this.lineHeight + this.ballR + this.areaHeight);

        this.getBaseChart().moveLine(this.contentByte, leftPoint.getX(), leftPoint.getY(), rightPoint.getX(),
                rightPoint.getY());
        for (float i = leftPoint.getX() ; i <= rightPoint.getX() ; i += 0.5f) {
            this.getBaseChart().moveLine(this.contentByte, i, leftPoint.getY(), this.x, this.y + this.lineHeight);
        }

        drawBalloon(this.x, this.y + this.lineHeight + this.ballR + this.areaHeight);
    }

    /**
     * 画个球，口已被系住了
     * @param x1
     * @param y1
     */
    private void drawBalloon(float x1, float y1) {
        int sideNum = 720;
        float space = 360f / sideNum, spaceSize = space;

        float x0 = 0, y0 = 0;
        final float max = 180f;

        // 画出外圈的圆
        this.contentByte.setColorStroke(this.color);
        float r = this.ballR, minR = this.ballR - 1.5f;
        float sep = (this.ballR - minR) / 180;
        boolean isAdd = true;
        for (int i = 0 ; i < sideNum ; i++) {
            if (isAdd && r + sep > this.ballR) {
                isAdd = false;
            } else if (!isAdd && r - sep < minR) {
                isAdd = true;
            }
            r = isAdd ? r + sep : r - sep;

            x0 = x1 + (float) (r * Math.sin(Math.PI * (space + 90) / max));
            y0 = y1 + (float) (r * Math.cos(Math.PI * (space + 90) / max));
            this.getBaseChart().moveLine(this.contentByte, x1, y1, x0, y0);
            this.getBaseChart().moveLine(this.contentByte, this.x, this.y + this.lineHeight, x0, y0);
            space += spaceSize;
        }

        // 画出内圈的圆
        space = spaceSize;
        isAdd = true;
        this.contentByte.setColorStroke(BaseColor.WHITE);
        r = this.ballR - this.balloonBorder;
        minR = this.ballR - this.balloonBorder - 1.5f;
        for (int i = 0 ; i < sideNum ; i++) {
            if (isAdd && r + sep > this.ballR - this.balloonBorder) {
                isAdd = false;
            } else if (!isAdd && r - sep < minR) {
                isAdd = true;
            }
            r = isAdd ? r + sep : r - sep;

            x0 = x1 + (float) (r * Math.sin(Math.PI * (space + 90) / max));
            y0 = y1 + (float) (r * Math.cos(Math.PI * (space + 90) / max));
            this.getBaseChart().moveLine(this.contentByte, x1, y1, x0, y0);
            space += spaceSize;
        }
    }

    /**
     * 气球系线的横坐标
     * @param x
     * @return BalloonGraph
     */
    public BalloonGraph setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * 气球的半径
     * @param ballR
     * @return BalloonGraph
     */
    public BalloonGraph setBallR(float ballR) {
        this.ballR = ballR;
        return this;
    }

    /**
     * 气球系线的纵坐标
     * @param y
     * @return BalloonGraph
     */
    public BalloonGraph setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 气球系线的高度
     * @param lineHeight
     * @return BalloonGraph
     */
    public BalloonGraph setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    /**
     * 气球系线的宽度
     * @param lineWidth
     * @return BalloonGraph
     */
    public BalloonGraph setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    /**
     * 图形的颜色
     * @param color
     * @return BalloonGraph
     */
    public BalloonGraph setColor(BaseColor color) {
        this.color = color;
        return this;
    }

    /**
     * 气球的边框的宽度
     * @param balloonBorder
     * @return BalloonGraph
     */
    public BalloonGraph setBalloonBorder(float balloonBorder) {
        this.balloonBorder = balloonBorder;
        return this;
    }

    /**
     * 线到气球的填充区域的高度
     * @param areaHeight
     * @return BalloonGraph
     */
    public BalloonGraph setAreaHeight(float areaHeight) {
        this.areaHeight = areaHeight;
        return this;
    }

}
