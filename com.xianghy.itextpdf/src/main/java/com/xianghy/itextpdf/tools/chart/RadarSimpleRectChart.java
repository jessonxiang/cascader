package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.TPoint;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;

/**
 * 雷达+简单的区域填充+分数分布图
 *
 * @author cheny
 */
public class RadarSimpleRectChart extends AbstractChart {

    // 初始化x,y的位置，及图形的大小
    private float x;
    private float r = 100;
    private float y;

    private float fontSize = 9f;// 图像上文字字体大小
    private int fillColor = 0xFEA722;// 填充的颜色
    private float positionY;// 画完表格之后，当前所在的横坐标

    private String[] itemNames;// 名称
    private float[] scores;// 分数
    private BaseColor borderColor = BaseColor.LIGHT_GRAY;// 边框的颜色

    private int scale = 2;// 保留的小数点位数
    private float maxScore = 100;// 最高分数
    private float smallCircle = 5f;// 每个节点上小圆圈的半径

    public RadarSimpleRectChart() {
        super();
    }

    public RadarSimpleRectChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
        super(writer, contentByte, document, baseFont);
    }

    @Override
    public void chart() {
        this.positionY = 0;
        if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
                || ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
            throw new RuntimeException("请检测itemNames、scores数据是否存在！");

        BaseColor fillColor_ = new BaseColor(this.fillColor);
        try {
            addRadar(fillColor_);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRadar(BaseColor fillColor) {
        int count = this.itemNames.length;
        float y = writer.getVerticalPosition(true);
        float spaceSize = 360f / count;
        DecimalFormat df = new DecimalFormat("0" + (this.scale > 0 ? "." + StringUtils.repeat("#", this.scale) : ""));

        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
        this.contentByte.setColorFill(fillColor);

        float centerPointX = this.x;
        float centerPointY = this.y;

        float x0 = 0;
        TPoint pointAttr = null;
        TPoint centerPoint = new TPoint(centerPointX, centerPointY);
        float space = spaceSize;
        float nextX = 0;
        float nextY = 0;

        float tempRound = 0, tempRound_ = 0;
        for (int i = 0 ; i <= count ; i++) {
            // 画分数
            if (i == count) {
                tempRound = this.scores[0] * this.r / this.maxScore;
            } else {
                tempRound = this.scores[i] * this.r / this.maxScore;
            }

            tempRound = this.maxScore == 0 ? 0f : tempRound;
            x0 = centerPointX + (float) (tempRound_ * Math.sin(Math.PI * (space - spaceSize) / 180));
            y = centerPointY + (float) (tempRound_ * Math.cos(Math.PI * (space - spaceSize) / 180));
            nextX = centerPointX + (float) (tempRound * Math.sin(Math.PI * space / 180));
            nextY = centerPointY + (float) (tempRound * Math.cos(Math.PI * space / 180));
            tempRound_ = tempRound;
            if (i > 0) {
                this.contentByte.setColorStroke(borderColor);
                this.moveLine(this.contentByte, x0, y, nextX, nextY);
                this.contentByte.setColorStroke(fillColor);
                for (int j = 1 ; j < tempRound ; j++) {
                    nextX = centerPointX + (float) (j * Math.sin(Math.PI * (i == count ? spaceSize : space) / 180));
                    nextY = centerPointY + (float) (j * Math.cos(Math.PI * (i == count ? spaceSize : space) / 180));
                    if ((int) y == centerPointY) {
                        if (i < count / 2) {
                            this.moveLine(this.contentByte, x0, y - 1f, nextX, nextY);
                        } else {
                            this.moveLine(this.contentByte, x0, y + 1f, nextX, nextY);
                        }
                    } else {
                        if (y < centerPointY) {
                            this.moveLine(this.contentByte, x0 - 1.5f, y, nextX, nextY);
                        } else {
                            this.moveLine(this.contentByte, x0 + 1.5f, y, nextX, nextY);
                        }
                    }
                }
            }

            // 画外框
            this.contentByte.setColorStroke(borderColor);
            x0 = centerPointX + (float) (this.r * Math.sin(Math.PI * (space - spaceSize) / 180));
            y = centerPointY + (float) (this.r * Math.cos(Math.PI * (space - spaceSize) / 180));
            nextX = centerPointX + (float) (this.r * Math.sin(Math.PI * (i == count ? spaceSize : space) / 180));
            nextY = centerPointY + (float) (this.r * Math.cos(Math.PI * (i == count ? spaceSize : space) / 180));
            if (i != count) {
                moveLine(this.contentByte, centerPointX, centerPointY, nextX, nextY);
                this.contentByte.circle(nextX, nextY, this.smallCircle);
                this.contentByte.setColorFill(fillColor);
                this.contentByte.fillStroke();
            }
            if (i != 0) {
                this.moveLine(this.contentByte, x0, y, nextX, nextY);
                int i0 = i != count ? i : 0;
                String str = this.itemNames[i0] + " " + df.format(this.scores[i0]) + "%";
                float width = (this.itemNames[i0] + "").length() * this.fontSize / 2
                        + (df.format(this.scores[i0]) + "%").length() * this.fontSize / 2 + this.fontSize;
                this.contentByte.setColorFill(BaseColor.BLACK);

                pointAttr = changeRotation((i == count ? spaceSize : space), width, centerPoint);
                this.moveText(this.contentByte, str, pointAttr.getX(), pointAttr.getY(), Element.ALIGN_CENTER,
                        pointAttr.getR());
            }
            space += spaceSize;
        }
    }

    // 文本的最长度
    private int textMaxLength() {
        int max = 0;
        for (String s : this.itemNames) {
            max = s.length() > max ? s.length() : max;
        }
        return max;
    }

    private TPoint changeRotation(float space, float width, TPoint centerPoint) {
        TPoint pointAttr = new TPoint();
        pointAttr.setX(centerPoint.getX() + (float) ((this.r + width) * Math.sin(Math.PI * space / 180)));
        pointAttr.setY(centerPoint.getY() + (float) ((this.r + width) * Math.cos(Math.PI * space / 180)));
        if (space > 45 && space <= 180 + 45) {
            pointAttr.setR(90 - space);
        } else if (space <= 361 || (space <= 45)) {
            pointAttr.setR(270 - space);
        }
        return pointAttr;
    }

    /**
     * X坐标
     *
     * @param x
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 名称。比如：指标
     *
     * @param itemNames
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
        return this;
    }

    /**
     * 字体大小
     *
     * @param fontSize
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 半径
     *
     * @param r
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setR(float r) {
        this.r = r;
        return this;
    }

    /**
     * 填充的颜色
     *
     * @param fillColor
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * 分数
     *
     * @param scores
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setScores(float[] scores) {
        this.scores = scores;
        return this;
    }

    /**
     * 保留的小数点位数
     *
     * @param scale
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setScale(int scale) {
        this.scale = scale;
        return this;
    }

    /**
     * 最高分数
     *
     * @param maxScore
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setMaxScore(float maxScore) {
        this.maxScore = maxScore;
        return this;
    }

    /**
     * 边框的颜色
     *
     * @param borderColor
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setBorderColor(BaseColor borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * 每个节点上小圆圈的半径
     *
     * @param smallCircle
     * @return RadarSimpleRectChart
     */
    public RadarSimpleRectChart setSmallCircle(float smallCircle) {
        this.smallCircle = smallCircle;
        return this;
    }

    /**
     * 画完表格之后，当前所在的横坐标
     *
     * @return float
     */
    public float getPositionY() {
        this.positionY = this.y - this.r - textMaxLength() * this.fontSize
                - ("" + NumberUtils.max(this.scores)).length() * this.fontSize;
        return this.positionY;
    }
}
