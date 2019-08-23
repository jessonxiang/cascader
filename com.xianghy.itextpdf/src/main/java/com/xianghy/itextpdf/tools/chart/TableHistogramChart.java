package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 表格+柱状图
 *
 * @author cheny
 */
public class TableHistogramChart extends AbstractChart {

    private float x;
    private float y;
    private float height = 120;
    private float width = 450;
    private float fontSize = 10;

    private int[] levels;// 刻度
    private String[] gradeNames;// 级别名称
    private float[] scores;// 分数
    private String[] scoreRanges;// 分数级别的区间
    private String[] peopleNumbers;// 每一个分数级别人数

    private int[] histogramBackgroundColors;// 柱状图背景颜色
    private int levelFontColor = 0x4E85C5;// 刻度字体颜色
    private int fontColor = 0x000000;// 字体颜色
    private int borderColor = 0xAAAAAA;// 边框的颜色
    private int levelBorderColor = 0xCCCCCC;// 刻度线的颜色
    private int cellBackgroundColor = 0xEEEEEE;// 单元格背景颜色

    private String tagName = "人数";// 标签名称
    private float positionY;// 画完表格之后，当前所在的横坐标

    public TableHistogramChart() {
        super();
    }

    public TableHistogramChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
        super(writer, contentByte, document, baseFont);
    }

    @Override
    public void chart() {
        this.positionY = 0;
        if (ObjectUtils.equals(null, this.peopleNumbers) || this.peopleNumbers.length < 1
                || ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
            throw new RuntimeException("请检测itemNames、scores数据是否存在！");

        if (ObjectUtils.equals(null, this.levels)) {
            this.levels = new int[]{5, 15, 25, 35, 45, 55};
        }

        if (ObjectUtils.equals(null, this.gradeNames)) {
            this.gradeNames = new String[]{"优秀", "良好", "合格", "待发展"};
        }

        if (ObjectUtils.equals(null, this.scoreRanges)) {
            this.scoreRanges = new String[]{"1.1-1.2", "1.0-1.1", "0.9-1.0", "0.8-0.9"};
        }

        if (ObjectUtils.equals(null, this.histogramBackgroundColors)) {
            this.histogramBackgroundColors = new int[]{0x5DD3B0, 0x92D050, 0xFFC000, 0xFC923B};
        }

        try {
            BaseColor fontColor_ = new BaseColor(this.fontColor);
            BaseColor borderColor_ = new BaseColor(this.borderColor);
            BaseColor levelFontColor_ = new BaseColor(this.levelFontColor);
            BaseColor levelBorderColor_ = new BaseColor(this.levelBorderColor);

            this.contentByte.setLineWidth(1f);
            this.contentByte.setColorStroke(borderColor_);
            this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小

            // 绘画柱状图框架
            this.contentByte.setColorFill(levelFontColor_);
            this.drawFrame(levelBorderColor_);
            this.contentByte.setColorFill(fontColor_);

            // 用图形表示分数
            this.drawScores();

            this.setLine(7, 11.55f, this.document);

            this.drawTable(levelBorderColor_);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 绘画表单
     *
     * @throws com.itextpdf.text.DocumentException
     */
    private void drawTable(BaseColor cellBackgroudColor) throws Exception {
        float y0 = this.y - 2;
        float x0 = this.x;
        float sepWidth = this.width / this.gradeNames.length - 1;
        float cellHeight = 40, temp = 0;
        this.positionY += cellHeight;
        for (int j = 0 ; j < 2 ; j++) {
            for (int i = 0 ; i < this.gradeNames.length ; i++) {
                this.moveRect(this.contentByte, x0, y0, x0 + sepWidth, y0 - cellHeight, this.cellBackgroundColor);

                switch (j) {
                    case 0: {
                        temp = (sepWidth - (this.gradeNames[i].length() * this.fontSize)) / 2;
                        // 级别文字变为粗体
                        this.moveText(this.contentByte, this.gradeNames[i], x0 + temp + 0.1f,
                                y0 - (cellHeight - this.fontSize) / 2, Element.ALIGN_LEFT, 0);
                        this.moveText(this.contentByte, this.gradeNames[i], x0 + temp - 0.1f,
                                y0 - (cellHeight - this.fontSize) / 2, Element.ALIGN_LEFT, 0);
                        this.moveText(this.contentByte, this.gradeNames[i], x0 + temp,
                                y0 - (cellHeight - this.fontSize) / 2 + 0.1f, Element.ALIGN_LEFT, 0);
                        this.moveText(this.contentByte, this.gradeNames[i], x0 + temp,
                                y0 - (cellHeight - this.fontSize) / 2 - 0.1f, Element.ALIGN_LEFT, 0);

                        // 级别范围变为粗体
                        temp = (sepWidth - (this.scoreRanges[i].length() * this.fontSize / 2f)) / 2;
                        this.moveText(this.contentByte, this.scoreRanges[i], x0 + temp + 0.1f,
                                y0 - cellHeight + (cellHeight - this.fontSize) / 2 - 2, Element.ALIGN_LEFT, 0);
                        this.moveText(this.contentByte, this.scoreRanges[i], x0 + temp - 0.1f,
                                y0 - cellHeight + (cellHeight - this.fontSize) / 2 - 2, Element.ALIGN_LEFT, 0);
                        this.moveText(this.contentByte, this.scoreRanges[i], x0 + temp,
                                y0 - cellHeight + (cellHeight - this.fontSize) / 2 - 2 + 0.1f + 0.1f, Element.ALIGN_LEFT,
                                0);
                        this.moveText(this.contentByte, this.scoreRanges[i], x0 + temp,
                                y0 - cellHeight + (cellHeight - this.fontSize) / 2 - 2 - 0.1f, Element.ALIGN_LEFT, 0);
                        break;
                    }
                    case 1: {
                        temp = (sepWidth - (this.peopleNumbers[i].length() * this.fontSize / 2f)) / 2;
                        this.moveText(this.contentByte, this.peopleNumbers[i], x0 + temp,
                                y0 - cellHeight + (cellHeight - this.fontSize) / 2, Element.ALIGN_LEFT, 0);
                        break;
                    }
                    default:
                        break;
                }

                x0 += sepWidth + 1;
            }
            y0 -= cellHeight + 1;
            x0 = this.x;
            cellHeight = 20;
            this.positionY += cellHeight;
        }

        // 画出标签
        temp = this.tagName.length() * this.fontSize * 3;
        this.moveRect(this.contentByte, this.x - temp, y0 + cellHeight, this.x - 1f, y0 + 1, this.cellBackgroundColor);

        temp = this.x - temp + (temp - this.tagName.length() * this.fontSize) / 2;
        this.moveText(this.contentByte, this.tagName, temp, y0 + 1 + (cellHeight - this.fontSize) / 2,
                Element.ALIGN_LEFT, 0);
    }

    /**
     * 绘画柱状图框架
     */
    private void drawFrame(BaseColor levelBorderColor) {
        // 绘画x,y
        this.moveLine(this.contentByte, this.x, this.y, this.x + this.width, this.y);
        moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
        this.contentByte.setColorStroke(levelBorderColor);

        // 画Y轴的刻度
        float x1 = this.x, y1 = 0, kHeight = height / (this.levels[0] - this.levels[this.levels.length - 1]);
        for (int i = 0 ; i < this.levels.length ; i++) {
            y1 = this.y + kHeight * (this.levels[0] - this.levels[i]);
            this.contentByte.setLineDash(1f);
            this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);
            this.moveText(this.contentByte, this.levels[i] + "%", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);

            this.contentByte.setLineDash(1f, 2f, 0f);
            this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
        }
    }

    /**
     * 绘画分数
     */
    private void drawScores() {
        float kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]);

        float x1 = this.x, y1 = this.y + 0.8f, kWidth = this.width / this.scores.length;
        int color = 0;
        for (int i = 0 ; i < this.scores.length ; i++) {
            float h = kHeight * (this.scores[i] - this.levels[0]);
            color = this.histogramBackgroundColors[i % this.histogramBackgroundColors.length];
            x1 += kWidth;

            this.contentByte.setLineDash(1f);
            this.moveRect(this.contentByte, x1 - 3 * kWidth / 4, y1, x1 - kWidth / 4, y1 + h - 0.8f, color);
            this.moveText(this.contentByte, this.scores[i] + "%", x1 - kWidth / 2, y1 + h + 2, Element.ALIGN_CENTER, 0);
        }
    }

    /**
     * X坐标
     *
     * @param x
     * @return TableHistogramChart
     */
    public TableHistogramChart setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return TableHistogramChart
     */
    public TableHistogramChart setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Y轴高度
     *
     * @param height
     * @return TableHistogramChart
     */
    public TableHistogramChart setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * X轴宽度
     *
     * @param width
     * @return TableHistogramChart
     */
    public TableHistogramChart setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * 字体大小
     *
     * @param fontSize
     * @return TableHistogramChart
     */
    public TableHistogramChart setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 刻度
     *
     * @param levels
     * @return TableHistogramChart
     */
    public TableHistogramChart setLevels(int[] levels) {
        this.levels = levels;
        return this;
    }

    /**
     * 级别名称
     *
     * @param gradeNames
     * @return TableHistogramChart
     */
    public TableHistogramChart setGradeNames(String[] gradeNames) {
        this.gradeNames = gradeNames;
        return this;
    }

    /**
     * 分数
     *
     * @param scores
     * @return TableHistogramChart
     */
    public TableHistogramChart setScores(float[] scores) {
        this.scores = scores;
        return this;
    }

    /**
     * 分数级别的区间
     *
     * @param scoreRanges
     * @return TableHistogramChart
     */
    public TableHistogramChart setScoreRanges(String[] scoreRanges) {
        this.scoreRanges = scoreRanges;
        return this;
    }

    /**
     * 每一个分数级别人数
     *
     * @param peopleNumbers
     * @return TableHistogramChart
     */
    public TableHistogramChart setPeopleNumbers(String[] peopleNumbers) {
        this.peopleNumbers = peopleNumbers;
        return this;
    }

    /**
     * 柱状图背景颜色
     *
     * @param histogramBackgroundColors
     * @return TableHistogramChart
     */
    public TableHistogramChart setHistogramBackgroundColors(int[] histogramBackgroundColors) {
        this.histogramBackgroundColors = histogramBackgroundColors;
        return this;
    }

    /**
     * 刻度字体颜色
     *
     * @param levelFontColor
     * @return TableHistogramChart
     */
    public TableHistogramChart setLevelFontColor(int levelFontColor) {
        this.levelFontColor = levelFontColor;
        return this;
    }

    /**
     * 字体颜色
     *
     * @param fontColor
     * @return TableHistogramChart
     */
    public TableHistogramChart setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    /**
     * 边框的颜色
     *
     * @param borderColor
     * @return TableHistogramChart
     */
    public TableHistogramChart setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * 刻度线的颜色
     *
     * @param levelBorderColor
     * @return TableHistogramChart
     */
    public TableHistogramChart setLevelBorderColor(int levelBorderColor) {
        this.levelBorderColor = levelBorderColor;
        return this;
    }

    /**
     * 标签名称
     *
     * @param tagName
     * @return TableHistogramChart
     */
    public TableHistogramChart setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    /**
     * 画完表格之后，当前所在的横坐标
     *
     * @return float
     */
    public float getPositionY() {
        this.positionY = this.y - this.positionY + 10;
        return this.positionY;
    }
}
