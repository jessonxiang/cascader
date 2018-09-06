package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 极值+点线+ 柱状图
 * 
 * @author cheny
 *
 */
public class HistogramCurveExtremumChart extends AbstractChart {

	private float x;
	private float y;
	private float height = 120;
	private float width = 450;
	private float fontSize = 10;

	private float[] levels;// 刻度
	private String[] itemNames;// 名称
	private float[] maxScores;// 最高分数
	private float[] minScores;// 最低分数
	private float[] avgScores;// 当前分数

	private int[] areaBackgroundColors;// 每块区域的背景颜色
	private int[] histogramFillColors;// 柱状图的填充颜色
	private int jionLineColor = 0x7EAACF;// 点连接线的颜色

	private int fontColor = 0x000000;// 字体颜色
	private int borderColor = 0x868686;// 边框的颜色
	private int[] rowBackgroundColors;// 表格行的背景颜色
	private float cellHeight = 20;// 表格的高度

	private float positionY;// 画完表格之后，当前所在的横坐标
	private int scoreDescWidth = 50;// 文字描述的宽度
	private String[] scoreDescNames;
	private int[] areaScoreColors;//各个分数区域的颜色
	private int maxScoreIndex = 0;
	private int minScoreIndex = 1;
	private int avgScoreIndex = 2;

	/**
	 * scoreDescName的所在列的宽度
	 * @param scoreDescWidth
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setScoreDescWidth(int scoreDescWidth) {
		this.scoreDescWidth = scoreDescWidth;
		return this;
	}

	/**
	 * 表格中每一行分数的描述名称
	 * @param scoreDescNames
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setScoreDescNames(String[] scoreDescNames) {
		this.scoreDescNames = scoreDescNames;
		return this;
	}

	/**
	 * 表格中每一行分数的描述名称，所在单元格的颜色 或 各个分数区域的颜色
	 * @param areaScoreColors
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setAreaScoreColors(int[] areaScoreColors) {
		this.areaScoreColors = areaScoreColors;
		return this;
	}

	public HistogramCurveExtremumChart() {
		super();
	}

	public HistogramCurveExtremumChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;

		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.maxScores) || this.maxScores.length < 1
				|| ObjectUtils.equals(null, this.minScores) || this.minScores.length < 1
				|| ObjectUtils.equals(null, this.avgScores) || this.avgScores.length < 1)
			throw new RuntimeException("请检测itemNames" + 
												"、maxScores、minScores、avgScores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new float[] { 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f };
		}

		if (ObjectUtils.equals(null, this.areaBackgroundColors)) {
			this.areaBackgroundColors = new int[] { 0xD8FDCC, 0xDEF5FF, 0xEEF7DF, 0xFFF5DD };
		}

		if (ObjectUtils.equals(null, this.histogramFillColors)) {
			this.histogramFillColors = new int[] { 0x7F7F7F, 0x59CFFF, 0xA9D961, 0xFFCE54 };
		}

		if (ObjectUtils.equals(null, this.rowBackgroundColors)) {
			this.rowBackgroundColors = new int[] { 0xE3F5EF, 0xFAE5DD, 0xD7E5F4 };
		}

		if (ObjectUtils.equals(null, this.areaScoreColors)) {
			this.areaScoreColors = new int[] { 0x72CBAD, 0xE47D54, 0x397BC6 };
		}

		if (ObjectUtils.equals(null, this.scoreDescNames)) {
			this.scoreDescNames = new String[] { "最大值", "最小值", "平均值" };
		}

		try {
			BaseColor fontColor_ = new BaseColor(this.fontColor);
			BaseColor borderColor_ = new BaseColor(this.borderColor);
			BaseColor jionLineColor_ = new BaseColor(this.jionLineColor);

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

			this.contentByte.setColorFill(fontColor_);

			this.drawEveryArea();
			// 绘画柱状图框架
			this.drawFrame(borderColor_, fontColor_);

			this.drawTable(fontColor_, borderColor_);

			this.drawScore(jionLineColor_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawScore(BaseColor jionLineColor) {
		float sepWidth = this.width / this.itemNames.length,
				kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]);
		float x0 = this.x + sepWidth / 4, minY = 0, maxY = 0, curY = 0, temY = 0, nextY = 0;
		for (int i = 0; i < this.itemNames.length; i++) {
			minY = (this.minScores[i] - this.levels[0]) * kHeight;
			maxY = (this.maxScores[i] - this.levels[0]) * kHeight;
			curY = (this.avgScores[i] - this.levels[0]) * kHeight;
			this.moveRect(this.contentByte, x0, this.y + minY, x0 + sepWidth / 2, this.y + maxY,
					this.histogramFillColors[i]);
			this.contentByte.setLineDash(1);
			this.contentByte.setLineWidth(1);
			this.contentByte.setColorStroke(BaseColor.WHITE);
			this.moveCircle(this.contentByte, x0 + sepWidth / 4, this.y + curY, 2, true);
			this.contentByte.setColorStroke(jionLineColor);
			this.contentByte.setLineWidth(2);
			this.contentByte.circle(x0 + sepWidth / 4, this.y + curY, 2);
			this.contentByte.stroke();

			if (minY - this.fontSize - 1f >= 0) {
				temY = this.y + minY - 1f;
				nextY = temY - this.fontSize;
			} else if (minY - this.fontSize - 1f < 0 && curY - 4 - this.fontSize >= 0) {
				temY = this.y + curY - 4f;
				nextY = temY - this.fontSize;
			} else {
				nextY = this.y + curY + 4f;
				temY = nextY + this.fontSize;
			}
			this.moveRect(this.contentByte, x0+1.5f, temY, x0 + sepWidth / 2-1.5f,
						nextY, this.areaScoreColors[minScoreIndex]);
			this.moveText(this.contentByte, this.minScores[i] + "",
						x0 + sepWidth / 4, nextY + 1f, Element.ALIGN_CENTER,0);

			if (temY >= this.y + curY + 4f) {
				nextY = temY + 1.5f;
				temY = nextY + this.fontSize;
			} else {
				nextY = this.y + curY + 3;
				temY = nextY + this.fontSize;
			}

			this.moveRect(this.contentByte, x0+1.5f, temY,
					x0 + sepWidth / 2-1.5f, nextY, this.areaScoreColors[avgScoreIndex]);
			this.moveText(this.contentByte, this.avgScores[i] + "",
					x0 + sepWidth / 4, nextY + 1f, Element.ALIGN_CENTER,0);

			if (temY >= this.y + maxY + 1f) {
				nextY = temY + 1f;
				temY = nextY + this.fontSize;
			} else {
				nextY = this.y + maxY + 1f;
				temY = nextY + this.fontSize;
			}

			this.moveRect(this.contentByte, x0+1.5f, temY, x0 + sepWidth / 2-1.5f,
					nextY, this.areaScoreColors[maxScoreIndex]);
			this.moveText(this.contentByte, this.maxScores[i] + "", x0 + sepWidth / 4
					, nextY + 1, Element.ALIGN_CENTER,0);

			if (i > 0) {
				this.contentByte.setLineDash(1);
				this.contentByte.setLineWidth(2);
				temY = (this.avgScores[i - 1] - this.levels[0]) * kHeight;
				this.moveLine(this.contentByte, x0 - sepWidth * 3 / 4 + 2,
						this.y + temY, x0 + sepWidth / 4 - 2,this.y + curY);
			}
			x0 += sepWidth;
		}
	}

	private void drawEveryArea() {
		float sepWidth = this.width / this.itemNames.length;
		float x0 = this.x;

		for (int i = 0; i < this.itemNames.length; i++) {
			this.moveRect(this.contentByte, i == 0 ? x0 + 1 : x0, 
					this.y + 1, x0 + sepWidth - 1, this.y + this.height,
					this.areaBackgroundColors[i], false);
			x0 += sepWidth;
		}
	}

	/**
	 * 将文字居中画出来
	 * @param lineHeight
	 * @param witdh
	 * @param text
	 * @param x
	 * @param y
	 */
	private void drawMulRowText(float lineHeight, float witdh, String text, float x, float y) {
		this.moveMultiLineText(this.contentByte, text,this.fontSize, witdh, lineHeight, x, y, 0);
	}

	private float calTableHeadHeight(float width) {
		float sum = 0;
		for (int i = 0; i < this.itemNames.length; i++) {
			sum = Math.max(sum, this.itemNames[i].length());
		}
		sum = sum * this.fontSize % width == 0 ? sum * this.fontSize / width
				: (float) (Math.floor(sum * this.fontSize / width) + 1);
		return (sum - 1) * this.fontSize + this.cellHeight;
	}

	private void drawTable(BaseColor fontColor, BaseColor tabelBorderColor) {
		float sepWidth = this.width / this.itemNames.length;
		float temp = calTableHeadHeight(sepWidth);
		float x0 = this.x, y0 = this.y - temp;

		for (int i = 0; i < this.itemNames.length; i++) {
			this.moveRect(this.contentByte, x0, this.y, x0 + sepWidth - 1, y0 + 1,
					this.histogramFillColors[i % this.histogramFillColors.length]);

			this.contentByte.setColorFill(BaseColor.WHITE);
			drawMulRowText(temp - 1, sepWidth, this.itemNames[i], x0, this.y);

			x0 += sepWidth;
		}

		x0 = this.x;
		for (int i = 0; i < this.rowBackgroundColors.length; i++) {
			// 开始画出分数
			for (int j = 0; j < this.avgScores.length; j++) {

				this.moveRect(this.contentByte, x0, y0, x0 + sepWidth - 1, y0 - this.cellHeight + 1,
						this.rowBackgroundColors[i % 2]);

				this.contentByte.setColorFill(fontColor);
				drawMulRowText(this.cellHeight - 1, sepWidth, this.maxScores[j] + "", x0, y0);

				x0 += sepWidth;
			}
			x0 = this.x;
			this.moveRect(this.contentByte, x0 - 1, y0, x0 - 1 - this.scoreDescWidth, y0 - this.cellHeight + 1,
					this.areaScoreColors[i]);

			this.contentByte.setColorFill(BaseColor.WHITE);
			drawMulRowText(this.cellHeight, this.scoreDescWidth, 
					this.scoreDescNames[i], x0 - 1 - this.scoreDescWidth,y0);

			y0 -= this.cellHeight;
			this.positionY += this.cellHeight;
		}
	}

	/**
	 * 绘画柱状图框架
	 */
	private void drawFrame(BaseColor borderColor, BaseColor fontColor) {
		this.contentByte.setColorStroke(borderColor);
		// 绘画x,y
		this.moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
		// 画Y轴的刻度
		float x1 = this.x, kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]), y1 = 0;

		for (int i = 0; i < this.levels.length; i++) {
			y1 = this.y + kHeight * (this.levels[i] - this.levels[0]);
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineDash(1f);
			this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);

			this.contentByte.setColorFill(fontColor);
			this.moveText(this.contentByte, this.levels[i] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);

			if (i > 0) {
				this.contentByte.setColorStroke(borderColor);
				this.contentByte.setLineDash(2f, 3f, 3f);
				this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
				this.contentByte.setLineDash(6f, 1f);
				this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
			}
		}

		this.moveText(this.contentByte, this.levels[this.levels.length - 1] + ""
				, x1 - 6, y1 - 3, Element.ALIGN_RIGHT,0);
	}

	/**
	 * X坐标
	 * @param x
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Y轴高度
	 * @param height
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * X轴宽度
	 * @param width
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 *  刻度
	 * @param levels
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setLevels(float[] levels) {
		this.levels = levels;
		return this;
	}

	/**
	 * 名称。比如：指标名称
	 * @param itemNames
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	/**
	 * 最高分数
	 * @param maxScores
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setMaxScores(float[] maxScores) {
		this.maxScores = maxScores;
		return this;
	}

	/**
	 * 最低分数
	 * @param minScores
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setMinScores(float[] minScores) {
		this.minScores = minScores;
		return this;
	}

	/**
	 * 当前分数
	 * @param avgScores
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setAvgScores(float[] avgScores) {
		this.avgScores = avgScores;
		return this;
	}

	/**
	 * 每块区域的背景颜色
	 * @param areaBackgroundColors
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setAreaBackgroundColors(int[] areaBackgroundColors) {
		this.areaBackgroundColors = areaBackgroundColors;
		return this;
	}

	/**
	 * 柱状图的填充颜色
	 * @param histogramFillColors
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setHistogramFillColors(int[] histogramFillColors) {
		this.histogramFillColors = histogramFillColors;
		return this;
	}

	/**
	 * 点连接线的颜色
	 * @param jionLineColor
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setJionLineColor(int jionLineColor) {
		this.jionLineColor = jionLineColor;
		return this;
	}

	/**
	 * 字体颜色
	 * @param fontColor
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 边框的颜色
	 * @param borderColor
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 表格行的背景颜色
	 * @param rowBackgroundColors
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setRowBackgroundColors(int[] rowBackgroundColors) {
		this.rowBackgroundColors = rowBackgroundColors;
		return this;
	}

	/**
	 * 单元格的高度
	 * @param cellHeight
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	/**
	 * 最高分所在的列索引
	 * @param maxScoreIndex
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setMaxScoreIndex(int maxScoreIndex) {
		this.maxScoreIndex = maxScoreIndex;
		return this;
	}

	/**
	 * 最低分所在的列索引
	 * @param minScoreIndex
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setMinScoreIndex(int minScoreIndex) {
		this.minScoreIndex = minScoreIndex;
		return this;
	}

	/**
	 * 平均分所在的列索引
	 * @param avgScoreIndex
	 * @return HistogramCurveExtremumChart
	 */
	public HistogramCurveExtremumChart setAvgScoreIndex(int avgScoreIndex) {
		this.avgScoreIndex = avgScoreIndex;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY = this.y - this.height - this.positionY + 10;
		return this.positionY;
	}
}
