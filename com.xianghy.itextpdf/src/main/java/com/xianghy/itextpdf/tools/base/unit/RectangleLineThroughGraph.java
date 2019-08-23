package com.xianghy.itextpdf.tools.base.unit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.AbstractBaseChart;
import com.xianghy.itextpdf.tools.base.AbstractBaseUnitChart;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 一个填充的矩形，可以带有贯穿线，也可以没有
 *
 * @author cheny
 */
public class RectangleLineThroughGraph extends AbstractBaseUnitChart {

    // 贯穿线是横向还是纵向
    public static final int LINE_THROUGH_X = 1;
    public static final int LINE_THROUGH_Y = 2;

    private float height = 15;
    private float width = 30;
    private int color = 0x5DD3B0;
    private float fontSize = 9;//字体大小
    private int fontColor = 0xFFFFFF;//字体颜色
    private String text;//需要显示的文字

    private int lineThrough = LINE_THROUGH_X;
    private float lineThroughLength = 30;// 贯穿线的长度
    private float lineThroughWidth = 2;// 贯穿线的宽度

    // 贯穿线 LINE_THROUGH_X是横向 LINE_THROUGH_Y纵向
    private float x;
    private float y;
    private boolean hasLineThrough = true;//是否画贯穿线

    public RectangleLineThroughGraph(AbstractBaseChart baseChart, PdfWriter writer, PdfContentByte contentByte,
                                     Document document) {
        super(baseChart, writer, contentByte, document);
    }

    public RectangleLineThroughGraph(PdfWriter writer, PdfContentByte contentByte, Document document) {
        super(writer, contentByte, document);
    }

    @Override
    public void chart() {
        if (ObjectUtils.equals(null, this.baseFont))
            throw new RuntimeException("请确保baseFont被添加！");

        BaseColor color_ = new BaseColor(this.color);
        BaseColor fontColor_ = new BaseColor(this.fontColor);
        this.contentByte.setLineWidth(this.lineThroughWidth);
        this.contentByte.setColorFill(color_);
        this.contentByte.setColorStroke(color_);
        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

        switch (this.lineThrough) {// 根据方向确定箭头的坐标
            case LINE_THROUGH_X: {
                if (hasLineThrough)
                    this.getBaseChart().moveLine(this.contentByte,
                            this.x - this.lineThroughLength / 2,
                            this.y + this.height / 2,
                            this.x + this.width + this.lineThroughLength / 2,
                            this.y + this.height / 2);

                this.getBaseChart().moveRoundRect(this.contentByte, this.x,
                        this.y, this.width, this.height, 2f, true);

                this.contentByte.setColorFill(fontColor_);
                this.getBaseChart().moveMultiLineText(this.contentByte,
                        StringUtils.defaultString(this.text, " "), this.fontSize,
                        this.width, this.height, this.x, this.y + this.height, 0);
                break;
            }
            case LINE_THROUGH_Y: {
                if (hasLineThrough)
                    this.getBaseChart().moveLine(this.contentByte,
                            this.x + this.height / 2,
                            this.y + this.width + this.lineThroughLength / 2,
                            this.x + this.height / 2,
                            this.y - this.lineThroughLength / 2);

                this.getBaseChart().moveRoundRect(this.contentByte, this.x,
                        this.y, this.height, this.width, 2f, true);

                this.contentByte.setColorFill(fontColor_);
                this.getBaseChart().moveMultiLineText(this.contentByte,
                        StringUtils.defaultString(this.text, " "), this.fontSize,
                        this.fontSize + 0.5f, this.width, this.x + (this.height - this.fontSize - 0.5f) / 2,
                        this.y + this.width, 0);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 矩形区域高度
     *
     * @param height
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * 矩形区域宽度
     *
     * @param width
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * X坐标
     *
     * @param x
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 图形颜色
     *
     * @param color
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * 贯穿线所在的水平线：LINE_THROUGH_X 和 LINE_THROUGH_Y
     *
     * @param lineThrough
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setLineThrough(int lineThrough) {
        this.lineThrough = lineThrough;
        return this;
    }

    /**
     * 字体大小
     *
     * @param fontSize
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 字体颜色
     *
     * @param fontColor
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    /**
     * 在矩形中显示的文本
     *
     * @param text
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 贯穿线的长度
     *
     * @param lineThroughLength
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setLineThroughLength(float lineThroughLength) {
        this.lineThroughLength = lineThroughLength;
        return this;
    }

    /**
     * 贯穿线的宽度
     *
     * @param lineThroughWidth
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setLineThroughWidth(float lineThroughWidth) {
        this.lineThroughWidth = lineThroughWidth;
        return this;
    }

    /**
     * 是否有贯穿线
     *
     * @param hasLineThrough
     * @return RectangleLineThroughGraph
     */
    public RectangleLineThroughGraph setHasLineThrough(boolean hasLineThrough) {
        this.hasLineThrough = hasLineThrough;
        return this;
    }
}
