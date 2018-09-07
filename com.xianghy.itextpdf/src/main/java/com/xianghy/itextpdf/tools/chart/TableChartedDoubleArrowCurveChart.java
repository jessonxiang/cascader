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
 * 可以在两页显示的表格+双箭头+圆滑曲线图，positionY为画好图形后当前的Y坐标
 * 
 * @author cheny
 *
 */
public class TableChartedDoubleArrowCurveChart extends AbstractChart {

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
	private float positionY;// 画完表格之后，当前所在的横坐标
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
	private int lineNumber;// 当表格可以在当前文档显示一半的时候，记录可以显示的行数

	public TableChartedDoubleArrowCurveChart() {
		super();
	}

	public TableChartedDoubleArrowCurveChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		if (ObjectUtils.equals(null, this.title) || ObjectUtils.equals(null, this.childrenTypes)
				|| this.childrenTypes.isEmpty() || ObjectUtils.equals(null, this.tableHeads)
				|| this.tableHeads.length < 1 || ObjectUtils.equals(null, this.parentTypes)
				|| this.parentTypes.length < 1 || ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测tableHeads、childrenTypes、parentTypes、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.scoreLevels)) {
			this.scoreLevels = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		}

		if (ObjectUtils.equals(null, this.widths)) {
			this.widths = new float[] { 10, 10, 10, 10, 10, 50 };
		}

		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors = new int[] { 0xFFFFFF, 0xF2F2F2 };
		}

		try {

			float sum = 0;
			for (float w : this.widths) {
				sum += w;
			}

			float y0 = this.writer.getVerticalPosition(true);
			checkPositionHeight(y0, sum);

			BaseColor borderColor = new BaseColor(0xBFBFBF);
			if (this.lineNumber <= 0) {
				this.document.newPage();
			}

			addTitleHead();
			addTableHead(borderColor, sum);
			addTableBody(borderColor, sum);
			this.lineNumber = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据数据，计算整个表格的高度，确定是否要换一个新的页面，或者可以在当前画出一部分表格。
	 * 
	 * @param y0
	 * @param sum
	 * @return 表格体部分的行数
	 */
	private int checkPositionHeight(float y0, float sum) {
		float curHeight = this.lineHeight + 52, temp = 0;

		for (int i = 0; i < this.tableHeads.length; i++) {
			temp = Math.max(everyColHeight(this.tableHeads[i], i, sum), temp);
		}
		curHeight += temp;

		for (int i = 0; i < this.childrenTypes.size(); i++) {
			int j = 0;
			for (; j < this.childrenTypes.get(i).length; j++) {
				temp = everyColHeight(this.childrenTypes.get(i)[j], 1, sum);
				curHeight += temp;
			}

			if (curHeight > y0) {
				return this.lineNumber;
			}

			this.lineNumber += j;
		}
		return this.lineNumber;
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
		this.moveMultiLineText(this.contentByte, text,this.fontSize, witdh, lineHeight, x, y, 0);
	}

	/**
	 * 表格体，文本显示部分
	 * 
	 * @param borderColor
	 * @throws Exception
	 */
	private void addTableBody(BaseColor borderColor, float sum) throws Exception {
		float y = this.writer.getVerticalPosition(true), y0 = y;
		float x = (this.document.getPageSize().getWidth() - this.width) / 2, x0 = x;

		int[] colSpan = new int[this.childrenTypes.size()];
		List<String> childStr = new ArrayList<String>();

		for (int i = 0; i < childrenTypes.size(); i++) {// 每一类中子类数量，将所有的子类都放在一起
			colSpan[i] = childrenTypes.get(i).length;
			childStr.addAll(Arrays.asList(childrenTypes.get(i)));
		}

		realRowHeight = new float[this.scores.length];
		float[] rowScores = null;
		boolean isNewPage = true;
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

		for (int i = 0; i < this.scores.length; i++) {
			// 如果画了一部分表格之后，在接着添加标题和表头
			if (this.lineNumber > 0 && isNewPage && i + 1 > this.lineNumber) {
				drawTextAndScore(y, colSpan, sum, true);
				this.document.newPage();
				addTitleHead();
				addTableHead(borderColor, sum);
				y = y0 = this.writer.getVerticalPosition(true);
				isNewPage = false;
				this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
			}

			rowScores = this.scores[i];
			realRowHeight[i] = everyColHeight(childStr.get(i) + "", 1, sum);
			int curColColor = 0;
			String text = null;

			// 从第二列开始画
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineWidth(1f);
			this.moveLine(this.contentByte, x0 + 0.5f, y0, x0 + 0.5f, y0 - this.realRowHeight[i]);
			for (int curCol = 1; curCol < this.widths.length; curCol++) {
				this.contentByte.setLineWidth(3.5f);
				if (this.colNumber == curCol - 1) {
					curColColor = this.colColor;
				} else if (curCol == this.widths.length - 1 || (i + 1) % 2 == 1) {
					curColColor = this.rowColors[0];
				} else {
					curColColor = this.rowColors[1];
				}

				if (curCol == 1) {
					text = childStr.get(i);
				} else if ((curCol != this.widths.length - 1)) {
					text = rowScores[curCol - 2] + "";
				}

				this.contentByte.setColorStroke(borderColor);
				x0 += this.widths[curCol - 1] * this.width / sum;
				this.moveLine(this.contentByte, x0, y0, x0, y0 - this.realRowHeight[i]);
				this.moveRect(this.contentByte, x0, y0, x0 + this.widths[curCol] * this.width / sum,
						y0 - this.realRowHeight[i], curColColor);

				if (curCol != this.widths.length - 1) {
					drawMulRowText(this.realRowHeight[i], this.widths[curCol] * this.width / sum, text, x0, y0);
				}

				if ((isNewPage && this.lineNumber > 0 && i + 1 >= this.lineNumber)
						|| ((!isNewPage || this.lineNumber <= 0) && i == this.scores.length - 1)) {
					this.contentByte.setLineWidth(1f);
					this.moveLine(this.contentByte, x0 - this.widths[curCol] * this.width / sum,
							y0 - this.realRowHeight[i], x0, y0 - this.realRowHeight[i]);
					this.moveLine(this.contentByte, x0, y0 - this.realRowHeight[i],
							x0 + this.widths[curCol] * this.width / sum, y0 - this.realRowHeight[i]);
				}
			}
			y0 -= this.realRowHeight[i];
			x0 = x;
		}
		drawTextAndScore(y, colSpan, sum);
		calPositionY(y);
	}

	private float rowHeightSum(int curRow, int[] colSpan) {
		int benRow = 0;
		for (int i = 0; i <= curRow - 1; i++) {
			benRow += colSpan[i];
		}

		float sum = 0;
		for (int i = benRow; i < benRow + colSpan[curRow]; i++) {
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
	private void drawTextAndScore(float y, int[] colSpan, float sum, boolean... isNewPage) {
		// 画出父类型名称文本
		float y0 = y - this.fontSize, cellHeight = 0, offsetY = 0, yt = y0;
		float x0 = (this.document.getPageSize().getWidth() - this.width) / 2 + this.widths[0] * this.width / sum / 2;
		char[] texts = null;
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

		int rows = 0;

		for (int i1 = 0; i1 < colSpan.length; i1++) {
			rows += colSpan[i1];
			if ((isNewPage.length < 1 || !isNewPage[0]) && this.lineNumber > 0
					&& this.lineNumber < this.realRowHeight.length && rows <= this.lineNumber) {
				continue;
			}

			cellHeight = rowHeightSum(i1, colSpan);
			this.contentByte.setLineWidth(1f);
			// 为父类天聪背景色
			this.moveRect(this.contentByte, x0 - this.widths[0] * this.width / sum / 2 + 1, y0 + this.fontSize,
					x0 + this.widths[0] * this.width / sum / 2 - 2, y0 - cellHeight + this.fontSize, 0xBFBFBF);
			this.moveRect(this.contentByte, x0 - this.widths[0] * this.width / sum / 2 + 1,
					i1 == 0? y0 + this.fontSize : y0 + this.fontSize, x0 + this.widths[0] * this.width / sum / 2 - 2,
					y0 - cellHeight + this.fontSize + 1.2f, this.parentTypeColor, true);

			yt -= cellHeight;
			if (this.parentTypes[i1].length() * this.fontSize > cellHeight) {
				drawMulRowText(cellHeight, this.widths[0] * this.width / sum, this.parentTypes[i1],
						x0 - this.widths[0] * this.width / sum / 2 + 1, y0+this.fontSize);
			} else {
				// 计算文本长度，确定是否需要换行处理
				offsetY = this.parentTypes[i1].length() * this.fontSize;
				offsetY = (cellHeight - offsetY) / 2;
				texts = this.parentTypes[i1].toCharArray();
				y0 -= offsetY;
				for (char c : texts) {
					this.moveText(this.contentByte, c + "", x0, y0, Element.ALIGN_CENTER, 0);
					y0 -= this.fontSize;
				}
			}

			y0 = yt;

			if (isNewPage.length > 0 && isNewPage[0] && rows >= this.lineNumber) {
				break;
			}
		}

		rows = 0;
		// 开始画出分数的曲线表示
		if ((isNewPage.length > 0 && isNewPage[0]) || this.lineNumber >= this.realRowHeight.length) {
			y0 = y - this.realRowHeight[0];
		} else {
			y0 = y - this.realRowHeight[this.lineNumber];
		}

		x0 = (this.document.getPageSize().getWidth() - this.width) / 2 + this.width
				- this.widths[this.widths.length - 1] * this.width / sum;
		float endX = x0 + this.widths[this.widths.length - 1] * this.width / sum - 2;
		float sepWidth = (endX - x0) / (this.scoreLevels[this.scoreLevels.length - 1] - this.scoreLevels[0]);
		float offsetX = 0;

		BaseColor lineYColor = new BaseColor(this.titleColor);
		BaseColor lineXColor = new BaseColor(0xBFBFBF);
		TPoint prevPoint = null;

		// 画出刻度
		this.contentByte.setLineWidth(1.5f);
		for (int i = (isNewPage.length < 1 || !isNewPage[0]) && this.lineNumber < this.realRowHeight.length
				? this.lineNumber : 0; i < scores.length; i++) {
			if ((isNewPage.length > 0 && isNewPage[0] && i + 1 < this.lineNumber)
					|| ((isNewPage.length < 1 || !isNewPage[0]) && i + 1 != scores.length)) {

				this.contentByte.setLineDash(2.5f);
				this.contentByte.setColorStroke(BaseColor.WHITE);
				if (colNumber == 3) {
					this.moveLine(this.contentByte, x0 - this.widths[this.colNumber + 1] * this.width / sum - 3, y0,
							x0 - 2, y0);
				} else {
					float l = 0;
					for (int t = colNumber + 1; t < this.widths.length - 1; t++) {
						l += this.widths[t] * this.width / sum;
					}
					this.moveLine(this.contentByte, x0 - this.widths[this.colNumber + 1] * this.width / sum - 3, y0,
							x0 - l - 2, y0);
				}

				this.contentByte.setLineDash(1.5f, 1);
				this.contentByte.setColorStroke(lineYColor);
				this.moveLine(this.contentByte, x0, y0, endX, y0);
			}
			this.contentByte.setLineDash(1.5f, 1);
			this.contentByte.setColorStroke(lineXColor);

			for (int j = 0; j < scoreLevels.length; j++) {
				if (j + 1 == scoreLevels.length - 1) {
					this.contentByte.setLineDash(1);
					this.contentByte.setLineWidth(1);
				}
				if (j + 1 != scoreLevels.length) {
					offsetX = (this.scoreLevels[j + 1] - this.scoreLevels[0]) * sepWidth;
					this.moveLine(this.contentByte, x0 + offsetX, y0 + this.realRowHeight[i], x0 + offsetX, y0);
				}
			}

			this.contentByte.setLineDash(1);
			prevPoint = drawGraph(this.widths[this.widths.length - 1] * this.width / sum - 2, x0, y0, i, prevPoint);
			if (i + 1 != scores.length)
				y0 -= this.realRowHeight[i + 1];

			if (isNewPage.length > 0 && isNewPage[0] && (i + 1) >= this.lineNumber) {
				break;
			}

		}
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
		this.doubleArrowGraph.setX(x + minScoreX)
		.setY(y + this.realRowHeight[rowNum] * 1 / 2)
		.setX0(x + maxScoreX)
		.setY0(y + this.realRowHeight[rowNum] * 1 / 2)
		.setHeight(this.lineHeight * 2 / 3)
		.setColor(this.titleColor)
		.setArrowWidth(5)
		.setArrowLineWidth(1.2f);

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
		headTable.setWidths(this.widths);
		PdfPCell cell = null;

		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < this.tableHeads.length; i++) {
				if (k == 0 && i != this.tableHeads.length - 1) {
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
		for (int i = 0; i < this.scoreLevels.length; i++) {
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
		cell.setBorder(0);
		return cell;
	}

	/**
	 * 表格的宽度
	 * @param width
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 标题
	 * @param title
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * 标题背景颜色
	 * @param titleColor
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setTitleColor(int titleColor) {
		this.titleColor = titleColor;
		return this;
	}

	/**
	 * 表格表头信息
	 * @param tableHeads
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setTableHeads(String[] tableHeads) {
		this.tableHeads = tableHeads;
		return this;
	}

	/**
	 * 分数的刻度值
	 * @param scoreLevels
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setScoreLevels(int[] scoreLevels) {
		this.scoreLevels = scoreLevels;
		return this;
	}

	/**
	 * 表头背景颜色
	 * @param tableHeadColor
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setTableHeadColor(int tableHeadColor) {
		this.tableHeadColor = tableHeadColor;
		return this;
	}

	/**
	 * 父类名称
	 * @param parentTypes
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setParentTypes(String[] parentTypes) {
		this.parentTypes = parentTypes;
		return this;
	}

	/**
	 * 父类背景颜色
	 * @param parentTypeColor
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setParentTypeColor(int parentTypeColor) {
		this.parentTypeColor = parentTypeColor;
		return this;
	}

	/**
	 * 子类分组及每一行名称
	 * @param childrenTypes
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setChildrenTypes(List<String[]> childrenTypes) {
		this.childrenTypes = childrenTypes;
		return this;
	}

	/**
	 * 每一行的每列的分数
	 * @param scores
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setScores(float[][] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 行的交替颜色
	 * @param rowColors
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	/**
	 * 指定那一列
	 * @param colNumber
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setColNumber(int colNumber) {
		this.colNumber = colNumber;
		return this;
	}

	/**
	 * 指定那一列的背景色
	 * @param colColor
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setColColor(int colColor) {
		this.colColor = colColor;
		return this;
	}

	/**
	 * 折线的颜色
	 * @param curveColor
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setCurveColor(int curveColor) {
		this.curveColor = curveColor;
		return this;
	}

	/**
	 *  每一行的高度
	 * @param lineHeight
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 表格每一列的宽度
	 * @param widths
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setWidths(float[] widths) {
		this.widths = widths;
		return this;
	}

	/**
	 * 刻度文本的字体大小
	 * @param levelFontSize
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setLevelFontSize(float levelFontSize) {
		this.levelFontSize = levelFontSize;
		return this;
	}

	/**
	 * 最大分数的列
	 * @param maxScoreColNum
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setMaxScoreColNum(int maxScoreColNum) {
		this.maxScoreColNum = maxScoreColNum;
		return this;
	}

	/**
	 * 最小分数的列
	 * @param minScoreColNum
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setMinScoreColNum(int minScoreColNum) {
		this.minScoreColNum = minScoreColNum;
		return this;
	}

	/**
	 * 分数的列
	 * @param curScoreColNum
	 * @return TableChartedDoubleArrowCurveChart
	 */
	public TableChartedDoubleArrowCurveChart setCurScoreColNum(int curScoreColNum) {
		this.curScoreColNum = curScoreColNum;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		return this.positionY;
	}

	/**
	 * 获取底部的Y坐标
	 * 
	 * @return
	 */
	private void calPositionY(float y) {
		float y0 = 0;
		int i = this.lineNumber > 0 && this.lineNumber < this.realRowHeight.length ? this.lineNumber : 0;
		for (; i < this.realRowHeight.length; i++) {
			y0 += this.realRowHeight[i];
		}
		this.positionY = y - y0 - 5;
	}
}
