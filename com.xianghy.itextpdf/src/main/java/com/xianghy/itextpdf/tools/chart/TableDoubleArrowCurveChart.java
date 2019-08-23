package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.xianghy.itextpdf.tools.base.TPoint;
import com.xianghy.itextpdf.tools.base.unit.CurveHasRadianGraph;
import com.xianghy.itextpdf.tools.base.unit.DoubleArrowGraph;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 表格+双箭头+圆滑曲线图，仅仅可以在一个显示
 *
 * @author cheny
 */
public class TableDoubleArrowCurveChart extends AbstractChart {

    private float width = 450;// 表格的宽度
    private String title;// 标题
    private float fontSize = 9;// 除了刻线之外的文字大小
    private int titleColor = 0x59CFFF;// 标题背景颜色
    private float lineHeight = 25;// 每一行的高度
    private String[] tableHeads;// 表格表头信息
    private int[] scoreLevels;// 分数的刻度值
    private int tableHeadColor = 0xACE7FF;// 表头背景颜色
    private String[] parentTypes;// 父类名称
    private int parentTypeColor = 0xDCF3FB;// 父类背景颜色
    private List<String[]> childrenTypes;// 子类分组及每一行名称
    /**
     * 所有的分数
     */
    private float[][] scores;// 每一行的每列的分数
    /**
     * 行的颜色，仅仅取前面两种颜色
     */
    private int[] rowColors;// 行的交替颜色
    /**
     * 指定那列及其颜色
     */
    private int colNumber = 3;// 指定那一列
    private int colColor = 0xFAE5DD;// 指定那一列的背景色
    /**
     * 曲线的颜色
     */
    private int curveColor = 0xCC3030;// 折现的颜色
    private float[] widths;// 表格每一列的宽度
    private float levelFontSize = 7;// 刻度文本的字体大小
    private int maxScoreColNum = 1;// 最大分数的列
    private int minScoreColNum = 2;// 最小分数的列
    private int curScoreColNum = 3;// 分数的列

    // 将分数用图形表示的两个图形
    private DoubleArrowGraph doubleArrowGraph;// 双箭头
    private CurveHasRadianGraph curveHasRadianGraph;// 曲线
    // 真实的行高
    private float[] realRowHeight;// 每一列的实际高度

    public TableDoubleArrowCurveChart() {
        super();
    }

    public TableDoubleArrowCurveChart(PdfWriter writer, PdfContentByte contentByte, Document document,
                                      BaseFont baseFont) {
        super(writer, contentByte, document, baseFont);
    }

    @Override
    public void chart() {

        if (ObjectUtils.equals(null, this.title) ||
                ObjectUtils.equals(null, this.childrenTypes) || this.childrenTypes.isEmpty()
                || ObjectUtils.equals(null, this.tableHeads) || this.tableHeads.length < 1
                || ObjectUtils.equals(null, this.parentTypes) || this.parentTypes.length < 1
                || ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
            throw new RuntimeException("请检测tableHeads、childrenTypes、parentTypes、scores数据是否存在！");

        if (ObjectUtils.equals(null, this.scoreLevels)) {
            this.scoreLevels = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        }

        if (ObjectUtils.equals(null, this.widths)) {
            this.widths = new float[]{10, 10, 10, 10, 10, 50};
        }

        if (ObjectUtils.equals(null, this.rowColors)) {
            this.rowColors = new int[]{0xFFFFFF, 0xF2F2F2};
        }


        try {

            float sum = 0;
            for (float w : this.widths) {
                sum += w;
            }

            newAddPage(sum);

            BaseColor borderColor = new BaseColor(0xBFBFBF);

            addTitleHead();
            addTableHead(borderColor, sum);
            addTableBody(borderColor, sum);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据数据，计算整个表格的高度，确定是否要换一个新的页面。
     *
     * @param y0
     * @param sum
     */
    private void newAddPage(float sum) {
        float curHeight = this.lineHeight + 52, temp = 0;
        float y0 = this.writer.getVerticalPosition(true);

        for (int i = 0 ; i < this.tableHeads.length ; i++) {
            temp = Math.max(everyColHeight(this.tableHeads[i], i, sum), temp);
        }
        curHeight += temp;

        for (int i = 0 ; i < this.childrenTypes.size() ; i++) {
            int j = 0;
            for ( ; j < this.childrenTypes.get(i).length ; j++) {
                temp = everyColHeight(this.childrenTypes.get(i)[j], 1, sum);
                curHeight += temp;
            }

            if (curHeight > y0) {
                this.document.newPage();
                return;
            }

        }
    }

    /**
     * 表格体，文本显示部分
     *
     * @param borderColor
     * @throws Exception
     */
    private void addTableBody(BaseColor borderColor, float sum) throws Exception {
        float y = this.writer.getVerticalPosition(true);

        PdfPTable tableBody = new PdfPTable(this.widths.length);
        tableBody.setTotalWidth(this.width);
        tableBody.setLockedWidth(true);
        tableBody.setWidths(this.widths);

        int[] colSpan = new int[this.childrenTypes.size()];
        List<String> childStr = new ArrayList<String>();

        for (int i = 0 ; i < childrenTypes.size() ; i++) {// 每一类中子类数量，将所有的子类都放在一起
            colSpan[i] = childrenTypes.get(i).length;
            childStr.addAll(Arrays.asList(childrenTypes.get(i)));
        }

        PdfPCell cell = null;
        BaseColor color = new BaseColor(this.parentTypeColor);
        BaseColor[] colors = new BaseColor[]{new BaseColor(this.rowColors[0]), new BaseColor(this.rowColors[1])};
        BaseColor thatColColor = new BaseColor(this.colColor);
        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

        int rows = 1, spanNum = 0, totalRows = colSpan[spanNum];
        float height_temp = 0;
        realRowHeight = new float[this.scores.length];
        for (int i = 0 ; i < this.scores.length ; i++) {
            if (rows > totalRows) {
                totalRows += colSpan[++spanNum];
            }

            if (rows == 1 || rows == totalRows - colSpan[spanNum] + 1) {// 第一列，合并了
                cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK), "",
                        this.lineHeight, color, true);
                cell.setRowspan(colSpan[spanNum]);
                cell.setBorderWidthBottom(1f);
                cell.setBorderColorBottom(borderColor);
                cell.setBorderWidthLeft(1f);
                cell.setBorderColorLeft(borderColor);
                tableBody.addCell(cell);
            }

            // 第二列
            height_temp = everyColHeight(childStr.get(rows - 1) + "", 1, sum);
            cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK),
                    childStr.get(rows - 1) + "", height_temp,
                    0 == this.colNumber ? thatColColor : rows % 2 == 0 ? colors[1] : colors[0], true);
            realRowHeight[i] = realRowHeight[i] > height_temp ? realRowHeight[i] : height_temp;

            cell.setBorderWidthLeft(1.5f);
            cell.setBorderColorLeft(borderColor);
            if (i == this.scores.length - 1) {
                cell.setBorderWidthBottom(1f);
                cell.setBorderColorBottom(borderColor);
            } else if (0 == this.colNumber) {
                cell.setBorderWidthBottom(1.5f);
                cell.setBorderColorBottom(BaseColor.WHITE);
            }
            tableBody.addCell(cell);

            // 文本分数区域
            for (int j = 0 ; j < this.scores[i].length ; j++) {
                cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK),
                        this.scores[i][j] + "", this.lineHeight,
                        j + 1 == this.colNumber ? thatColColor : rows % 2 == 0 ? colors[1] : colors[0], true);
                cell.setBorderWidthLeft(1.5f);
                cell.setBorderColorLeft(borderColor);
                if (i == this.scores.length - 1) {
                    cell.setBorderWidthBottom(1.5f);
                    cell.setBorderColorBottom(borderColor);
                } else if (j + 1 == this.colNumber) {
                    cell.setBorderWidthBottom(1.5f);
                    cell.setBorderColorBottom(BaseColor.WHITE);
                }
                tableBody.addCell(cell);
            }

            // 图形分数区域
            cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK), "", 0,
                    BaseColor.WHITE, false);
            cell.setBorderWidthLeft(1.5f);
            cell.setBorderColorLeft(borderColor);
            if (i == this.scores.length - 1) {
                cell.setBorderWidthBottom(1f);
                cell.setBorderColorBottom(borderColor);
            }
            tableBody.addCell(cell);
            rows++;
        }
        this.document.add(tableBody);

        drawTextAndScore(y, colSpan, sum);
    }

    private float rowHeightSum(int curRow, int[] colSpan) {
        int benRow = 0;
        for (int i = 0 ; i <= curRow - 1 ; i++) {
            benRow += colSpan[i];
        }

        float sum = 0;
        for (int i = benRow ; i < benRow + colSpan[curRow] ; i++) {
            sum += this.realRowHeight[i];
        }
        return sum;
    }

    /**
     * 纵向显示的文字和图形分数
     *
     * @param y
     * @param colSpan
     */
    private void drawTextAndScore(float y, int[] colSpan, float sum) {
        // 画出父类型名称文本
        float y0 = y - this.fontSize, cellHeight = 0, offsetY = 0, yt = y0;
        float x0 = (this.document.getPageSize().getWidth() - this.width) / 2 + this.widths[0] * this.width / sum / 2;
        char[] texts = null;
        this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
        for (int i = 0 ; i < colSpan.length ; i++) {
            cellHeight = rowHeightSum(i, colSpan);
            yt -= cellHeight;
            if (this.parentTypes[i].length() * this.fontSize > cellHeight) {
                drawMulRowText(cellHeight, this.widths[0] * this.width / sum,
                        this.parentTypes[i], x0 - this.widths[0] * this.width / sum / 2 + 1,
                        y0 + this.fontSize);
            } else {
                // 计算文本长度，确定是否需要换行处理
                offsetY = this.parentTypes[i].length() * this.fontSize;
                offsetY = (cellHeight - offsetY) / 2;
                texts = this.parentTypes[i].toCharArray();
                y0 -= offsetY;
                for (char c : texts) {
                    this.moveText(this.contentByte, c + "", x0, y0, Element.ALIGN_CENTER, 0);
                    y0 -= this.fontSize;
                }
            }
            y0 = yt;
        }

        // 开始画出分数的曲线表示
        y0 = y - this.realRowHeight[0];
        x0 = (this.document.getPageSize().getWidth() - this.width) / 2 + this.width
                - this.widths[this.widths.length - 1] * this.width / sum + 2;
        float endX = x0 + this.widths[this.widths.length - 1] * this.width / sum - 2;
        float sepWidth = (endX - x0) / (this.scoreLevels[this.scoreLevels.length - 1] - this.scoreLevels[0]);
        float offsetX = 0;

        BaseColor lineYColor = new BaseColor(this.titleColor);
        BaseColor lineXColor = new BaseColor(0xBFBFBF);
        TPoint prevPoint = null;

        // 画出刻度
        this.contentByte.setLineWidth(1.5f);
        for (int i = 0 ; i < scores.length ; i++) {
            this.contentByte.setLineDash(1.5f, 1);
            this.contentByte.setColorStroke(lineYColor);
            if (i + 1 != scores.length)
                this.moveLine(this.contentByte, x0, y0, endX, y0);
            this.contentByte.setColorStroke(lineXColor);
            for (int j = 0 ; j < scoreLevels.length ; j++) {
                if (j + 1 == scoreLevels.length - 1) {
                    this.contentByte.setLineDash(1);
                }
                if (j + 1 != scoreLevels.length) {
                    offsetX = (this.scoreLevels[j + 1] - this.scoreLevels[0]) * sepWidth;
                    this.moveLine(this.contentByte, x0 + offsetX, y0 + this.realRowHeight[i],
                            x0 + offsetX, y0);
                }
            }
            prevPoint = drawGraph(this.widths[this.widths.length - 1] * this.width / sum - 2, x0, y0, i, prevPoint);
            if (i + 1 != scores.length)
                y0 -= this.realRowHeight[i + 1];
        }
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

    /**
     * 用图形画出分数区域
     *
     * @param width
     * @param x
     * @param y
     * @param rowNum
     * @param prevPoint
     * @return
     */
    private TPoint drawGraph(float width, float x, float y, int rowNum, TPoint prevPoint) {
        // 找出指定分数的x，y坐标
        float[] curRowScores = this.scores[rowNum];
        float subLeveScore = width / (this.scoreLevels[this.scoreLevels.length - 1] - this.scoreLevels[0]);

        float maxScoreX = (curRowScores[this.maxScoreColNum - 1] - this.scoreLevels[0]) * subLeveScore;
        float minScoreX = (curRowScores[this.minScoreColNum - 1] - this.scoreLevels[0]) * subLeveScore;
        float curScoreX = (curRowScores[this.curScoreColNum - 1] - this.scoreLevels[0]) * subLeveScore;

        // 画出双箭头
        if (ObjectUtils.equals(null, this.doubleArrowGraph)) {
            this.doubleArrowGraph = new DoubleArrowGraph(this.writer, this.contentByte, this.document);
        }
        this.doubleArrowGraph.setBaseChart(this);

        this.doubleArrowGraph.setX(x + minScoreX).setY(y + this.realRowHeight[rowNum] * 1 / 2).setX0(x + maxScoreX)
                .setY0(y + this.realRowHeight[rowNum] * 1 / 2).setHeight(this.lineHeight * 2 / 3)
                .setColor(this.titleColor).setArrowWidth(5).setArrowLineWidth(1.2f);

        this.doubleArrowGraph.chart();

        // 将当前分数用平滑的曲线连接起来
        if (ObjectUtils.notEqual(null, prevPoint)) {
            if (ObjectUtils.equals(null, this.curveHasRadianGraph)) {
                this.curveHasRadianGraph = new CurveHasRadianGraph(this.writer, this.contentByte, this.document);
            }
            this.curveHasRadianGraph.setBaseChart(this);

            this.curveHasRadianGraph.setX(prevPoint.getX()).setY(prevPoint.getY()).setX0(x + curScoreX)
                    .setY0(y + this.realRowHeight[rowNum] * 1 / 2).setHasCircle(true).setColor(this.curveColor);

            this.curveHasRadianGraph.chart();
        }

        // 将当前行当前分数坐在的点保存，在下一个点的时候使用
        TPoint curPoint = new TPoint();
        curPoint.setX(x + curScoreX);
        curPoint.setY(y + this.realRowHeight[rowNum] * 1 / 2);
        return curPoint;
    }

    /**
     * 添加表格的表头信息
     *
     * @throws Exception
     */
    private void addTableHead(BaseColor borderColor, float sum) throws Exception {
        PdfPTable headTable = new PdfPTable(this.widths.length);
        headTable.setTotalWidth(this.width);
        headTable.setLockedWidth(true);

        // 微调表头单元格宽度
        float[] ws = new float[this.widths.length];
        for (int i = 0 ; i < this.widths.length ; i++) {
            if (i == 0) {
                ws[i] = this.widths[i] + 0.4f;
            } else {
                ws[i] = this.widths[i] + 0.1f;
            }
        }

        headTable.setWidths(ws);
        PdfPCell cell = null;

        for (int k = 0 ; k < 2 ; k++) {
            for (int i = 0 ; i < this.tableHeads.length ; i++) {
                if (k == 0) {
                    cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK),
                            this.tableHeads[i], everyColHeight(this.tableHeads[i], i, sum),
                            new BaseColor(this.tableHeadColor), true);
                } else {
                    cell = this.addOneCell(new Font(this.baseFont, this.fontSize, Font.NORMAL, BaseColor.BLACK), "", 0,
                            new BaseColor(this.tableHeadColor), false);
                }

                cell.setBorderColorRight(BaseColor.WHITE);
                cell.setBorderWidthRight(1.5f);
                cell.setBorderColorBottom(BaseColor.BLACK);
                cell.setBorderWidthBottom(0.5f);

                if (i == 0) {
                    cell.setBorderWidthLeft(1f);
                    cell.setBorderColorLeft(borderColor);
                } else if (i == tableHeads.length - 1) {
                    cell.setBorderColorRight(borderColor);
                    cell.setBorderWidthRight(1f);
                }
                headTable.addCell(cell);
            }
        }

        this.document.add(headTable);

        /**
         * 添加刻度
         */
        float everyCell = this.width * widths[widths.length - 1] / sum;
        float sepWidth = everyCell / (this.scoreLevels[this.scoreLevels.length - 1] - this.scoreLevels[0]);

        float posstion = (this.document.getPageSize().getWidth() - this.width) / 2 + this.width - everyCell + 3;
        float y = this.writer.getVerticalPosition(true) + 3;
        float offsetX = 0;

        this.contentByte.setFontAndSize(this.baseFont, this.levelFontSize);
        this.contentByte.setColorFill(BaseColor.BLACK);
        for (int i = 0 ; i < this.scoreLevels.length ; i++) {
            offsetX = (this.scoreLevels[i] - this.scoreLevels[0]) * sepWidth;
            if (i == this.scoreLevels.length - 1) {
                offsetX -= 5;
            }
            this.moveText(this.contentByte, this.scoreLevels[i] + "", offsetX + posstion, y,
                    i == this.scoreLevels.length - 1 ? Element.ALIGN_RIGHT : Element.ALIGN_CENTER, 0);
        }
    }

    // 计算文本长度，确定是否需要换行处理
    private float everyColHeight(String str, int curCol, float totalWidth) {
        float extendHeight = 0;
        float everyCell = this.width * widths[curCol] / totalWidth;
        extendHeight = str.length() * this.fontSize / everyCell;
        extendHeight = extendHeight > 1 ? (float) Math.ceil(extendHeight) : 1;
        return extendHeight * this.lineHeight;
    }

    /**
     * 添加标题
     *
     * @throws Exception
     */
    private void addTitleHead() throws Exception {
        PdfPTable headTable = new PdfPTable(1);
        headTable.setTotalWidth(this.width);
        headTable.setLockedWidth(true);
        this.addBorderColorCell(headTable, new Font(this.baseFont, this.fontSize, Font.BOLD, BaseColor.WHITE),
                this.title, new BaseColor(this.titleColor), 1, this.lineHeight, Element.ALIGN_LEFT, false);
        this.addBorderColorCell(headTable, new Font(this.baseFont, this.fontSize, Font.BOLD, BaseColor.WHITE), " ",
                BaseColor.BLACK, 1, 3, null, false);
        this.document.add(headTable);
    }

    /**
     * 添加单元格
     *
     * @param pFont
     * @param str
     * @param height
     * @param backColor
     * @param isHasHeight
     * @return
     */
    private PdfPCell addOneCell(Font pFont, String str, float height, BaseColor backColor, boolean isHasHeight) {
        PdfPCell cell = new PdfPCell(new Paragraph(str, pFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 定义水平方向
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 定义垂直方向
        cell.setBackgroundColor(backColor);// 背景颜色
        if (isHasHeight) {
            cell.setPaddingTop(6);// 设置上边距
            cell.setPaddingBottom(6);// 设置下边距
            cell.setFixedHeight(height);
        } else {
            cell.setPaddingTop(0);// 设置上边距
            cell.setPaddingBottom(0);// 设置下边距
            cell.setFixedHeight(height);
        }
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    /**
     * 表格的宽度
     *
     * @param width
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * 标题
     *
     * @param title
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 标题背景颜色
     *
     * @param titleColor
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * 表格表头信息
     *
     * @param tableHeads
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setTableHeads(String[] tableHeads) {
        this.tableHeads = tableHeads;
        return this;
    }

    /**
     * 分数的刻度值
     *
     * @param scoreLevels
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setScoreLevels(int[] scoreLevels) {
        this.scoreLevels = scoreLevels;
        return this;
    }

    /**
     * 表头背景颜色
     *
     * @param tableHeadColor
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setTableHeadColor(int tableHeadColor) {
        this.tableHeadColor = tableHeadColor;
        return this;
    }

    /**
     * 父类名称
     *
     * @param parentTypes
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setParentTypes(String[] parentTypes) {
        this.parentTypes = parentTypes;
        return this;
    }

    /**
     * 父类背景颜色
     *
     * @param parentTypeColor
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setParentTypeColor(int parentTypeColor) {
        this.parentTypeColor = parentTypeColor;
        return this;
    }

    /**
     * 子类分组及每一行名称
     *
     * @param childrenTypes
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setChildrenTypes(List<String[]> childrenTypes) {
        this.childrenTypes = childrenTypes;
        return this;
    }

    /**
     * 每一行的每列的分数
     *
     * @param scores
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setScores(float[][] scores) {
        this.scores = scores;
        return this;
    }

    /**
     * 行的交替颜色
     *
     * @param rowColors
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setRowColors(int[] rowColors) {
        this.rowColors = rowColors;
        return this;
    }

    /**
     * 指定那一列
     *
     * @param colNumber
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setColNumber(int colNumber) {
        this.colNumber = colNumber;
        return this;
    }

    /**
     * 指定那一列的背景色
     *
     * @param colColor
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setColColor(int colColor) {
        this.colColor = colColor;
        return this;
    }

    /**
     * 折现的颜色
     *
     * @param curveColor
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setCurveColor(int curveColor) {
        this.curveColor = curveColor;
        return this;
    }

    /**
     * 每一行的高度
     *
     * @param lineHeight
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
        return this;
    }

    /**
     * 除了刻线之外的文字大小
     *
     * @param fontSize
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 表格每一列的宽度
     *
     * @param widths
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setWidths(float[] widths) {
        this.widths = widths;
        return this;
    }

    /**
     * 刻度文本的字体大小
     *
     * @param levelFontSize
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setLevelFontSize(float levelFontSize) {
        this.levelFontSize = levelFontSize;
        return this;
    }

    /**
     * 最大分数的列
     *
     * @param maxScoreColNum
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setMaxScoreColNum(int maxScoreColNum) {
        this.maxScoreColNum = maxScoreColNum;
        return this;
    }

    /**
     * 最小分数的列
     *
     * @param minScoreColNum
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setMinScoreColNum(int minScoreColNum) {
        this.minScoreColNum = minScoreColNum;
        return this;
    }

    /**
     * 分数的列
     *
     * @param curScoreColNum
     * @return TableDoubleArrowCurveChart
     */
    public TableDoubleArrowCurveChart setCurScoreColNum(int curScoreColNum) {
        this.curScoreColNum = curScoreColNum;
        return this;
    }
}
