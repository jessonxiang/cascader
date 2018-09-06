package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;
import pdf.base.unit.BalloonGraph;

import java.util.List;

/**
 * 表格+分数分级别+数据图
 * 
 * @author cheny
 *
 */
public class TableGradeDistributionChart extends AbstractChart {

	// 初始化x,y的位置，及图形的大小
	private float x;
	private float width = 450;
	private float y;

	private String[] gradeNames;// 分数各个级别名称
	private List<String[]> dataAreas;// 各个级别所占百分比,人数
	private int[] gradeBackgroundColors;// 级别的背景颜色

	private float[] levels;// 刻度
	private String[] tagNames;// 各行名称
	private float curScore;// 当前分数
	private int[] percentageLevels;// 百分比刻度

	private float fontSize = 10;// 字体大小
	private float firstColumnFontSize = 8;// 第一列字体大小
	private int borderColor = 0x999999;// 表格线条颜色
	private int fontColor = 0x000000;// 文字颜色
	private String percentageName = "百分比";// 百分比刻度的中文名称

	private float positionY;// 画完表格之后，当前所在的横坐标
	private int autoAddPercentageSymbolIndex = 0;// 直接添加百分号的数据索引
	private int firstColumnBackgroundColor = 0xEEEEEE;// 第一列背景颜色

	public TableGradeDistributionChart() {
		super();
	}

	public TableGradeDistributionChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		if (ObjectUtils.equals(null, this.dataAreas) || this.dataAreas.isEmpty())
			throw new RuntimeException("请检测dataAreas数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new float[] { 1.2f, 1.1f, 1.0f, 0.9f, 0.8f };
		}

		if (ObjectUtils.equals(null, this.gradeNames)) {
			this.gradeNames = new String[] { "优秀", "良好", "合格", "待发展" };
		}

		if (ObjectUtils.equals(null, this.tagNames)) {
			this.tagNames = new String[] { "等级", "所占比例", "人数", "分段数" };
		}

		if (ObjectUtils.equals(null, this.percentageLevels)) {
			this.percentageLevels = new int[] { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
		}

		if (ObjectUtils.equals(null, this.gradeBackgroundColors)) {
			this.gradeBackgroundColors = new int[] { 0x5DD3B0, 0x92D050, 0xFFC000, 0xFC923B };
		}

		BaseColor borderColor_ = new BaseColor(this.borderColor);
		BaseColor fontColor_ = new BaseColor(this.fontColor);
		BaseColor[] gradeBackgroundColors_ = new BaseColor[this.gradeBackgroundColors.length];

		for (int i = 0; i < this.gradeBackgroundColors.length; i++) {
			gradeBackgroundColors_[i] = new BaseColor(this.gradeBackgroundColors[i]);
		}

		try {
			this.contentByte.setLineWidth(1f);
			this.contentByte.setColorStroke(borderColor_);
			this.contentByte.setColorFill(fontColor_);
			float colHeihgt = 30;

			drawPercentage();

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小
			this.setLine(1, 8f, this.document);

			drawTable(colHeihgt);

			drawTag(colHeihgt, borderColor_);

			this.contentByte.setColorStroke(borderColor_);
			drawLine(colHeihgt, this.y - 13, this.dataAreas.size() + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawTable(float colHeihgt) {
		String[] columnWidth = this.dataAreas.get(autoAddPercentageSymbolIndex);
		float sepWidth = this.width
				/ (this.percentageLevels[this.percentageLevels.length - 1] - this.percentageLevels[0]);

		float x0 = this.x;
		positionY += colHeihgt;
		for (int i = 0; i < this.gradeNames.length; i++) {
			this.moveRect(this.contentByte, x0, this.y - 15,
					x0 + sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]) - 1,
					this.y - colHeihgt - 15, this.gradeBackgroundColors[i % this.gradeBackgroundColors.length]);

			this.contentByte.setColorFill(BaseColor.BLACK);
			//将表头粗体显示,偏移量0.1画四次，可表现为粗体特征
			drawMulRowText(colHeihgt, 
					sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]), this.gradeNames[i], x0-0.1f,
					this.y - this.fontSize / 2 - (colHeihgt - this.fontSize) / 2);
			
			drawMulRowText(colHeihgt, 
					sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]), this.gradeNames[i], x0+0.1f,
					this.y - this.fontSize / 2 - (colHeihgt - this.fontSize) / 2);
			
			drawMulRowText(colHeihgt,
					sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]), this.gradeNames[i], x0,
					0.1f+this.y - this.fontSize / 2 - (colHeihgt - this.fontSize) / 2);
			
			drawMulRowText(colHeihgt,
					sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]), this.gradeNames[i], x0,
					-0.1f+this.y - this.fontSize / 2 - (colHeihgt - this.fontSize) / 2);

			x0 += sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]);
		}

		// 添加数据部分
		String[] rowCell = null;
		float y0 = this.y;
		for (int j = 0; j < this.dataAreas.size(); j++) {
			x0 = this.x;
			y0 -= colHeihgt;
			positionY += colHeihgt;
			rowCell = this.dataAreas.get(j);
			for (int i = 0; i < rowCell.length; i++) {
				drawMulRowText(colHeihgt,
						sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]),
						rowCell[i] + (j == autoAddPercentageSymbolIndex ? "%" : ""),
						j == autoAddPercentageSymbolIndex ? x0 + this.fontSize : x0,
						y0 - this.fontSize / 2 - (colHeihgt - this.fontSize) / 2);
				x0 += sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]);
			}
		}
		positionY += colHeihgt;
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
		// 如果是数值，宽度折半
		float fontSize = text.matches("^(?:\\d{1,})|(?:\\d{1,}\\.\\d{1,})$") || text.endsWith("%") ? this.fontSize / 2
				: this.fontSize;

		float lineNumber = text.length() * fontSize / witdh;

		lineNumber = lineNumber > 1 ? (float) Math.ceil(lineNumber) : 1;

		int everyLen = (int) (Math.round(text.length() / lineNumber));

		String tempText = null;
		float x0 = 0, y0 = y - (lineHeight - lineNumber * this.fontSize) / 2 - this.fontSize + 1f;

		for (int i = 0; i < lineNumber; i++) {
			tempText = text.substring(i * everyLen,
					(lineNumber > 1 && i == lineNumber - 1 ? text.length() : everyLen * (i + 1)));

			x0 = x + (witdh - tempText.length() * fontSize) / 2;

			if (text.endsWith("%")) {
				this.moveText(this.contentByte, tempText, x0, y0, Element.ALIGN_CENTER, 0);
			} else {
				this.moveText(this.contentByte, tempText, x0, y0, Element.ALIGN_LEFT, 0);
			}
			y0 -= this.fontSize + 1f;
		}
	}

	/**
	 * 为表格添加额外的边框
	 */
	private void drawLine(float colHeihgt, float y0, int rows) {
		float offset = 3;
		/**
		 * 画出行的虚线
		 */
		this.contentByte.setLineDash(4f, 2f, 0f);
		for (int i = 0; i < rows; i++) {
			moveLine(this.contentByte, this.x, y0 - offset, this.x + this.width, y0 - offset);
			offset += colHeihgt;
		}
		moveLine(this.contentByte, this.x, y0 - offset, this.x + this.width, y0 - offset);

		/**
		 * 画出列的实线
		 */
		String[] columnWidth = this.dataAreas.get(autoAddPercentageSymbolIndex);
		this.contentByte.setLineWidth(1f);
		this.contentByte.setLineDash(1f);
		float sepWidth = this.width
				/ (this.percentageLevels[this.percentageLevels.length - 1] - this.percentageLevels[0]);
		offset = 0;

		rows = this.dataAreas.size() + 1;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < columnWidth.length; i++) {
				moveLine(this.contentByte, x + offset, y0 - 3, x + offset, y0 - colHeihgt - 3);
				offset += sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]);
				;
			}
			moveLine(this.contentByte, x + offset, y0 - 3, x + offset, y0 - colHeihgt - 3);
			y0 -= colHeihgt;
			offset = 0;
		}
	}

	/**
	 * 画出表格的标签名称
	 */
	private void drawTag(float colHeihgt, BaseColor borderColor) {
		String[] columnWidth = this.dataAreas.get(autoAddPercentageSymbolIndex);

		this.contentByte.setFontAndSize(this.baseFont, this.firstColumnFontSize);// 设置文字大小
		this.contentByte.setColorFill(BaseColor.BLACK);
		float y1 = this.y - 10f;
		for (int i = 0; i < this.tagNames.length; i++) {
			moveRect(this.contentByte, this.x - this.firstColumnFontSize * 7, y1 - 5, this.x - 1.5f, y1 - colHeihgt - 5,
					i == this.tagNames.length - 1 ? 0xFFFFFF : this.firstColumnBackgroundColor);

			drawMulRowText(colHeihgt, this.firstColumnFontSize * 7, this.tagNames[i],
					this.x - this.firstColumnFontSize * 7, i == this.tagNames.length - 1
							? y1 + 10 - (colHeihgt - this.fontSize) / 2 : y1 + 5 - (colHeihgt - this.fontSize) / 2);

			y1 -= colHeihgt + 1f;
		}

		/**
		 * 画出分数段
		 */
		float rowY = this.y - colHeihgt * (this.dataAreas.size() + 1) - 10;
		float widthX = this.fontSize;
		float sepWidth = this.width
				/ (this.percentageLevels[this.percentageLevels.length - 1] - this.percentageLevels[0]);
		for (int i = 0; i < columnWidth.length; i++) {
			moveText(this.contentByte, this.levels[i] + "", this.x + widthX, rowY - 14 - this.fontSize,
					Element.ALIGN_RIGHT, 0);
			widthX += sepWidth * (Float.parseFloat(columnWidth[i]) - this.percentageLevels[0]);
		}
		moveText(this.contentByte, this.levels[this.levels.length - 1] + "", this.x + widthX, rowY - 14 - this.fontSize,
				Element.ALIGN_RIGHT, 0);
	}

	/**
	 * 画出百分比刻度
	 */
	private void drawPercentage() throws Exception {
		this.contentByte.setFontAndSize(this.baseFont, this.firstColumnFontSize);// 设置文字大小
		moveText(this.contentByte, this.percentageName, this.x - this.firstColumnFontSize * 3, this.y,
				Element.ALIGN_RIGHT, 0);

		moveLine(this.contentByte, this.x, this.y - 5, width + this.x, this.y - 5);
		float sepWidth = this.width
				/ (this.percentageLevels[this.percentageLevels.length - 1] - this.percentageLevels[0]);

		int length = this.percentageLevels.length;
		for (int i = 0; i < length; i++) {
			moveText(this.contentByte, this.percentageLevels[i] + "%",
					this.x + sepWidth * (this.percentageLevels[i] - this.percentageLevels[0]), this.y,
					Element.ALIGN_CENTER, 0);

			moveLine(this.contentByte, this.x + sepWidth * (this.percentageLevels[i] - this.percentageLevels[0]),
					this.y - 5, this.x + sepWidth * (this.percentageLevels[i] - this.percentageLevels[0]), this.y - 10);
		}

		/**
		 * 画出分数分布的位置
		 */
		BalloonGraph balloonGraph = new BalloonGraph(this.writer, this.contentByte, this.document);
		balloonGraph.setBaseChart(this);

		balloonGraph.setY(this.y + this.firstColumnFontSize + 2)
				.setX(this.x + sepWidth * (this.curScore - this.percentageLevels[0]) - 1.5f);

		balloonGraph.chart();
	}

	/**
	 * X坐标
	 * @param x
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * 宽度
	 * @param width
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 刻度
	 * @param levels
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setLevels(float[] levels) {
		this.levels = levels;
		return this;
	}

	/**
	 *  各行名称
	 * @param tagNames
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setTagNames(String[] tagNames) {
		this.tagNames = tagNames;
		return this;
	}

	/**
	 * 分数各个级别名称
	 * @param gradeNames
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setGradeNames(String[] gradeNames) {
		this.gradeNames = gradeNames;
		return this;
	}

	/**
	 * 各个级别所占百分比,人数
	 * @param dataAreas
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setDataAreas(List<String[]> dataAreas) {
		this.dataAreas = dataAreas;
		return this;
	}

	/**
	 * 级别的背景颜色
	 * @param gradeBackgroundColors
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setGradeBackgroundColors(int[] gradeBackgroundColors) {
		this.gradeBackgroundColors = gradeBackgroundColors;
		return this;
	}

	/**
	 *  当前分数
	 * @param curScore
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setCurScore(float curScore) {
		this.curScore = curScore;
		return this;
	}

	/**
	 * 百分比刻度
	 * @param percentageLevels
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setPercentageLevels(int[] percentageLevels) {
		this.percentageLevels = percentageLevels;
		return this;
	}

	/**
	 * 表格线条颜色
	 * @param borderColor
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 文字颜色
	 * @param fontColor
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 *  百分比刻度的中文名称
	 * @param percentageName
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setPercentageName(String percentageName) {
		this.percentageName = percentageName;
		return this;
	}

	/**
	 * 直接添加百分号的数据索引
	 * @param autoAddPercentageSymbolIndex
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setAutoAddPercentageSymbolIndex(int autoAddPercentageSymbolIndex) {
		this.autoAddPercentageSymbolIndex = autoAddPercentageSymbolIndex;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 第一列字体大小
	 * @param firstColumnFontSize
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setFirstColumnFontSize(float firstColumnFontSize) {
		this.firstColumnFontSize = firstColumnFontSize;
		return this;
	}

	/**
	 * 第一列背景颜色
	 * @param firstColumnBackgroundColor
	 * @return TableGradeDistributionChart
	 */
	public TableGradeDistributionChart setFirstColumnBackgroundColor(int firstColumnBackgroundColor) {
		this.firstColumnBackgroundColor = firstColumnBackgroundColor;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY = this.y - this.positionY - 10;
		return this.positionY;
	}
}
