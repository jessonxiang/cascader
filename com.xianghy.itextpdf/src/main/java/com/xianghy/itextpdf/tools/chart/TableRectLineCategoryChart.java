package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * 表格+分数等级分布图
 *
 * @author cheny
 */
public class TableRectLineCategoryChart extends AbstractChart {

    // 初始化x,y的位置，及图形的大小
    private float x;
    private float height = 120;
    private float width = 450;
    private float y;
    private float gradeOffsetX = 3;//分数级别与坐标的偏移量

    private float[] levels;// 刻度
    private String[] itemNames;// 名称
    private String[] tagNames;// 标识名称
    private List<float[]> scores;// 分数
    private String[] gradeNames;// 分数等级
    private float gradeRectWidth = 50;//等级矩形框的宽度
    private float gradeRectHeight = 22;//等级矩形框的高度

    private int[] gradeFillColor;// 分数级别填充颜色
    private int tableBackColor = 0xDDDDDD;// 表格背景颜色
    private float fontSize = 10;// 字体大小
    private float positionY;// 画完表格之后，当前所在的横坐标

    private int levleLineColor = 0xDDDDDD;// 线条颜色
    private int levleTextColor = 0x4E85C5;// 刻度文字颜色
    private int scoreLineColor = 0xDDDDDD;// 垂直的分数线
    private int frameLineColor = 0xAAAAAA;// 坐标轴线

    private int fillRectColor = 0xDBEEF4;// 矩形填充颜色
    private int rectSepLineColor = 0xA40007;// 矩形中间分隔线颜色
    private int scorePointColor = 0x4E85C5;// 分数填充点的颜色
    private int rectLineColor = 0x4E85C5;// 矩形边框颜色

    private int maxScoreColNum = 2;// 最大分数在集合的下标
    private int minScoreColNum = 3;// 最小分数在集合的下标
    private int curScoreColNum = 0;// 分数在集合的下标
    private int[] showDataColInTables;//显示在表格中的数据，在集合的下标:请确保 scores 的顺序与tagNames一一对应

    public TableRectLineCategoryChart() {
        super();
    }

    public TableRectLineCategoryChart(PdfWriter writer, PdfContentByte contentByte, Document document,
                                      BaseFont baseFont) {
        super(writer, contentByte, document, baseFont);
    }

    @Override
    public void chart() {
        this.positionY = 0;
        if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
                || ObjectUtils.equals(null, this.scores) || this.scores.isEmpty())
            throw new RuntimeException("请检测itemNames、scores数据是否存在！");

        if (ObjectUtils.equals(null, this.levels)) {
            this.levels = new float[]{0.8f, 0.9f, 1.0f, 1.1f, 1.2f};
        }

        if (ObjectUtils.equals(null, this.tagNames)) {
            this.tagNames = new String[]{"指标得分", "平均分"};
        }

        if (ObjectUtils.equals(null, gradeNames)) {
            gradeNames = new String[]{"优秀", "良好", "合格", "待发展"};
        }

        if (ObjectUtils.equals(null, this.gradeFillColor)) {
            this.gradeFillColor = new int[]{0x5DD3B0, 0x92D050, 0xFFC000, 0x7F7F7F};
        }

        if (ObjectUtils.equals(null, this.showDataColInTables)) {
            this.showDataColInTables = new int[]{0, 1};
        }

        try {

            BaseColor frameLineColor_ = new BaseColor(this.frameLineColor);
            BaseColor levleTextColor_ = new BaseColor(this.levleTextColor);
            BaseColor leveLineColor_ = new BaseColor(this.levleLineColor);
            BaseColor scoreLineColor_ = new BaseColor(this.scoreLineColor);

            final float cellHeight = 20;

            this.contentByte.setLineWidth(1f);
            this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小

            // 绘画柱状图框架
            this.drawFrame(frameLineColor_, leveLineColor_, levleTextColor_);

            this.setLine(7, 11.55f, this.document);
            float rowHeight = this.drawTable(cellHeight, leveLineColor_);

            this.drawScores(scoreLineColor_);
            // 画出优秀、良好等级划分
            this.drawGrade();

            this.drawTag(rowHeight, cellHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 画出分数等级
     *
     * @param this.contentByte
     * @param gradeImgs
     * @param y0
     */
    private void drawGrade() {
        float levelHeight = this.height / (this.levels.length - 1);

        float y0 = this.y;
        float x0 = this.x + this.width - gradeOffsetX;

        BaseColor color = null;

        for (int i = this.gradeNames.length - 1 ; i >= 0 ; i--) {
            color = new BaseColor(this.gradeFillColor[i]);
            this.contentByte.setColorStroke(color);
            this.contentByte.setColorFill(color);
            this.contentByte.roundRectangle(x0, y0 + (levelHeight - this.gradeRectHeight) / 2,
                    this.gradeRectWidth, this.gradeRectHeight, 5);
            this.contentByte.fillStroke();

            this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
            this.contentByte.setColorFill(BaseColor.WHITE);

            drawMulRowText(this.gradeRectHeight, this.gradeRectWidth, this.gradeNames[i],
                    x0, y0 + levelHeight - (levelHeight - this.gradeRectHeight) / 2);
            y0 += levelHeight;
        }
    }

    /**
     * 绘画柱状图框架
     */
    private void drawFrame(BaseColor frameLineColor, BaseColor leveLineColor, BaseColor levleTextColor_) {
        this.contentByte.setColorStroke(frameLineColor);
        // 绘画x,y
        this.moveLine(this.contentByte, this.x - 2, this.y, this.x + this.width, this.y);
        this.moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
        // 画Y轴的刻度
        float x1 = this.x, kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]),
                kWidth = this.width / this.itemNames.length, y1 = 0;

        for (int i = 0 ; i < this.levels.length ; i++) {
            y1 = this.y + kHeight * (this.levels[i] - this.levels[0]);
            this.contentByte.setColorStroke(frameLineColor);
            this.contentByte.setLineDash(1f);
            this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);

            this.contentByte.setColorFill(levleTextColor_);
            this.moveText(this.contentByte, this.levels[i] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);

            if (i > 0) {
                this.contentByte.setColorStroke(leveLineColor);
                this.contentByte.setLineDash(1f, 2f, 0f);
                this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
            }
        }

        this.moveText(this.contentByte, this.levels[this.levels.length - 1] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT,
                0);

        // 画X轴的名称
        this.contentByte.setColorStroke(frameLineColor);
        float x2 = this.x, y2 = this.y;
        for (int i = 0 ; i < this.itemNames.length ; i++) {
            x2 += kWidth;
            this.contentByte.setLineDash(1f);
            this.moveLine(this.contentByte, x2, y2, x2, y2 + 2);
        }
    }

    /**
     * 绘画表单
     */
    private float drawTable(final float cellHeight, BaseColor leveLineColor_) throws Exception {
        float sepWidth = this.width / this.itemNames.length - 1f;
        float y0 = this.y - 2;
        float x0 = this.x;

        /**
         * 表头
         */
        float rowHeight = getRealRowHeight(sepWidth, cellHeight);
        for (int i = 0 ; i < this.itemNames.length ; i++) {
            this.moveRect(this.contentByte, x0, y0 - rowHeight, x0 + sepWidth, y0, this.tableBackColor);
            this.contentByte.setColorFill(BaseColor.BLACK);
            //字体加粗
            drawMulRowText(rowHeight, sepWidth, this.itemNames[i], x0 + 0.1f, y0);
            drawMulRowText(rowHeight, sepWidth, this.itemNames[i], x0 - 0.1f, y0);
            drawMulRowText(rowHeight, sepWidth, this.itemNames[i], x0, y0 - 0.1f);
            drawMulRowText(rowHeight, sepWidth, this.itemNames[i], x0, y0 + 0.1f);
            x0 += sepWidth + 1f;
        }

        y0 -= rowHeight + 1;
        x0 = this.x;

        /**
         * 标题数据
         */
        float[] rowScores = null;
        for (int j = 0 ; j < this.scores.size() ; j++) {
            if (ArrayUtils.contains(this.showDataColInTables, j)) {
                rowScores = this.scores.get(j);
                for (int k = 0 ; k < rowScores.length ; k++) {
                    this.moveRect(this.contentByte, x0, y0 - cellHeight, x0 + sepWidth, y0, this.tableBackColor);
                    drawMulRowText(cellHeight, sepWidth, rowScores[k] + "", x0, y0);
                    x0 += sepWidth + 1f;
                }
                y0 -= cellHeight + 1;
            }
            x0 = this.x;
        }

        return rowHeight;
    }

    /**
     * 将文字居中画出来
     *
     * @param lineHeight
     * @param witdh
     * @param text
     * @param x
     * @param y
     */
    private void drawMulRowText(float lineHeight, float witdh, String text, float x, float y) {
        this.moveMultiLineText(this.contentByte, text, this.fontSize, witdh, lineHeight, x, y, 0);
    }

    // 表头的行高
    private float getRealRowHeight(float width, final float cellHeight) {
        float rowHeight = 0;
        float lineNumbers = 0;
        for (int i = 0 ; i < this.itemNames.length ; i++) {
            lineNumbers = this.itemNames[i].length() * this.fontSize / width;
            lineNumbers = lineNumbers > 1 ? (float) Math.ceil(lineNumbers) : 1;
            rowHeight = lineNumbers * cellHeight;
        }
        return rowHeight;
    }

    /**
     * 绘画分数
     */
    private void drawScores(BaseColor scoreLineColor_) {
        float downScore = this.levels[0];
        float subScore = this.levels[this.levels.length - 1] - this.levels[0];

        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小

        float[] aStr = this.scores.get(curScoreColNum);
        float[] max = this.scores.get(maxScoreColNum);
        float[] min = this.scores.get(minScoreColNum);
        float maxScore = 0.0f, minScore = 0.0f;
        BaseColor rectSepLineColor_ = new BaseColor(this.rectSepLineColor);
        BaseColor scorePointColor_ = new BaseColor(this.scorePointColor);
        BaseColor rectLineColor_ = new BaseColor(this.rectLineColor);

        float x1 = this.x, x2 = this.x, y3 = this.y + 1f, y2 = this.y - 1f;
        for (int j = 0 ; j < aStr.length ; j++) {// 画出第一个分数的折线图
            x1 += (this.width / this.itemNames.length);
            float scores = aStr[j] - downScore;
            float y1 = y3 + ((scores * this.height) / subScore);
            // 画出当前分数列中的最大与最小值及其中间值
            this.contentByte.setLineWidth(0.3f);
            maxScore = max[j] - downScore;
            minScore = min[j] - downScore;
            int num = this.itemNames.length;
            this.moveRect(this.contentByte, x1 - (this.width / (2 * num)) - 5, y3 + (maxScore * this.height / subScore),
                    x1 - (this.width / (2 * num)) + 10, y3 + ((minScore * this.height) / subScore), this.fillRectColor);
            this.contentByte.setColorStroke(rectLineColor_);
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) - 5, y3 + (maxScore * this.height / subScore),
                    x1 - (this.width / (2 * num)) + 10, y3 + (maxScore * this.height / subScore));// 画出上边框
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) - 5, y3 + (minScore * this.height / subScore),
                    x1 - (this.width / (2 * num)) + 10, y3 + minScore * this.height / subScore);// 画出下边框
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) - 5, y3 + (maxScore * this.height / subScore),
                    x1 - (this.width / (2 * num)) - 5, y3 + minScore * this.height / subScore);// 画出右边框
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) + 10, y3 + (maxScore * this.height / subScore),
                    x1 - (this.width / (2 * num)) + 10, y3 + minScore * this.height / subScore);// 画出左边框
            this.contentByte.setColorStroke(rectSepLineColor_);
            this.contentByte.setLineWidth(0.7f);
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) - 5,
                    y3 + (maxScore + minScore) / 2f * this.height / subScore, x1 - (this.width / (2 * num)) + 10,
                    y3 + (maxScore + minScore) / 2f * this.height / subScore);// 划中间的线
            this.contentByte.setColorStroke(rectLineColor_);
            this.contentByte.setColorFill(scorePointColor_);
            if (j == 0) {
                x2 = x1;
                y2 = y1;
            } else {
                this.moveLine(this.contentByte, x1 - (this.width / (2 * num)), y1, x2 - (this.width / (2 * num)), y2);// 划线
                x2 = x1;
                y2 = y1;
            }
            this.moveText(this.contentByte, "●", x1 - (this.width / (2 * num)) + 5, (float) (y1 - 2.5), Element.ALIGN_RIGHT,
                    0);

            // 画出当前分数在横坐标的垂直线
            this.contentByte.setColorStroke(scoreLineColor_);
            this.contentByte.setLineWidth(1f);
            this.moveLine(this.contentByte, x1 - (this.width / (2 * num)) + 1.5f, y1, x1 - (this.width / (2 * num)) + 1.5f, y3);// 划线
        }

    }

    /**
     * 画标识
     */
    private void drawTag(final float rowHeight, final float cellHeight) {
        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小
        this.contentByte.setColorFill(BaseColor.BLACK);

        float sepWidth = this.width / this.itemNames.length - 1f;
        float y1 = this.y - rowHeight - 3;
        float x1 = this.x - sepWidth;
        this.positionY = rowHeight;

        for (int i = 0 ; i < this.tagNames.length ; i++) {
            if (ArrayUtils.contains(this.showDataColInTables, i)) {
                this.moveRect(this.contentByte, this.x - sepWidth, y1, this.x - 1, y1 - cellHeight, this.tableBackColor);
                drawMulRowText(cellHeight, sepWidth, this.tagNames[i], x1, y1);
                y1 -= cellHeight + 1;
                this.positionY += cellHeight;
            }
        }
    }

    /**
     * Y坐标
     *
     * @param x
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Y轴高度
     *
     * @param height
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * X轴宽度
     *
     * @param width
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * Y坐标
     *
     * @param y
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 刻度
     *
     * @param levels
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setLevels(float[] levels) {
        this.levels = levels;
        return this;
    }

    /**
     * 名称
     *
     * @param itemNames
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setItemNames(String[] itemNames) {
        this.itemNames = itemNames;
        return this;
    }

    /**
     * 标识名称
     *
     * @param tagNames
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setTagNames(String[] tagNames) {
        this.tagNames = tagNames;
        return this;
    }

    /**
     * 分数
     *
     * @param scores
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setScores(List<float[]> scores) {
        this.scores = scores;
        return this;
    }

    /**
     * 分数等级
     *
     * @param gradeNames
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setGradeNames(String[] gradeNames) {
        this.gradeNames = gradeNames;
        return this;
    }

    /**
     * 线条颜色
     *
     * @param levleLineColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setLevleLineColor(int levleLineColor) {
        this.levleLineColor = levleLineColor;
        return this;
    }

    /**
     * 矩形填充颜色
     *
     * @param fillRectColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setFillRectColor(int fillRectColor) {
        this.fillRectColor = fillRectColor;
        return this;
    }

    /**
     * 分数级别填充颜色
     *
     * @param gradeFillColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setGradeFillColor(int[] gradeFillColor) {
        this.gradeFillColor = gradeFillColor;
        return this;
    }

    /**
     * 垂直的分数线
     *
     * @param scoreLineColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setScoreLineColor(int scoreLineColor) {
        this.scoreLineColor = scoreLineColor;
        return this;
    }

    /**
     * 坐标轴线
     *
     * @param frameLineColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setFrameLineColor(int frameLineColor) {
        this.frameLineColor = frameLineColor;
        return this;
    }

    /**
     * 刻度文字颜色
     *
     * @param levleTextColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setLevleTextColor(int levleTextColor) {
        this.levleTextColor = levleTextColor;
        return this;
    }

    /**
     * 矩形中间分隔线颜色
     *
     * @param rectSepLineColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setRectSepLineColor(int rectSepLineColor) {
        this.rectSepLineColor = rectSepLineColor;
        return this;
    }

    /**
     * 分数填充点的颜色
     *
     * @param scorePointColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setScorePointColor(int scorePointColor) {
        this.scorePointColor = scorePointColor;
        return this;
    }

    /**
     * 矩形边框颜色
     *
     * @param rectLineColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setRectLineColor(int rectLineColor) {
        this.rectLineColor = rectLineColor;
        return this;
    }

    /**
     * 最大分数在集合的下标
     *
     * @param maxScoreColNum
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setMaxScoreColNum(int maxScoreColNum) {
        this.maxScoreColNum = maxScoreColNum;
        return this;
    }

    /**
     * 最小分数在集合的下标
     *
     * @param minScoreColNum
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setMinScoreColNum(int minScoreColNum) {
        this.minScoreColNum = minScoreColNum;
        return this;
    }

    /**
     * 分数在集合的下标
     *
     * @param curScoreColNum
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setCurScoreColNum(int curScoreColNum) {
        this.curScoreColNum = curScoreColNum;
        return this;
    }

    /**
     * 表格背景颜色
     *
     * @param tableBackColor
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setTableBackColor(int tableBackColor) {
        this.tableBackColor = tableBackColor;
        return this;
    }

    /**
     * 字体大小
     *
     * @param fontSize
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 等级矩形框的宽度
     *
     * @param gradeRectWidth
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setGradeRectWidth(float gradeRectWidth) {
        this.gradeRectWidth = gradeRectWidth;
        return this;
    }

    /**
     * 等级矩形框的高度
     *
     * @param gradeRectHeight
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setGradeRectHeight(float gradeRectHeight) {
        this.gradeRectHeight = gradeRectHeight;
        return this;
    }

    /**
     * 显示在表格中的数据，在集合的下标:请确保 scores 的顺序与tagNames一一对应
     *
     * @param showDataColInTables
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setShowDataColInTables(int[] showDataColInTables) {
        this.showDataColInTables = showDataColInTables;
        return this;
    }

    /**
     * 画完表格之后，当前所在的横坐标
     *
     * @return float
     */
    public float getPositionY() {
        this.positionY = this.y - this.positionY - 10;
        return this.positionY;
    }

    /**
     * 分数级别与坐标的偏移量
     *
     * @param gradeOffsetX
     * @return TableRectLineCategoryChart
     */
    public TableRectLineCategoryChart setGradeOffsetX(float gradeOffsetX) {
        this.gradeOffsetX = gradeOffsetX;
        return this;
    }
}
