package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 多条折线+表格+ 分区域
 * @author cheny
 * @see pdf.chart.PolyLineChart
 */
public class LineMulCurveLineAreaTableChart extends AbstractChart {

	private float x;
	private float y;
	private float height = 120;
	private float width = 450;
	private float fontSize = 9;

	private float[] levels;// 刻度
	private String[] itemNames;// 名称
	private float[][] scores;//各个批次的分数
	private int tableBorderColor=0xCFCFCF;//表格边框的颜色

	private int[] areaBackgroundColors;// 每块区域的背景颜色
	private int[] tableHeadFillColors;// 表头的填充颜色
	private int[] jionLineColors;// 点连接线的颜色

	private int fontColor = 0x000000;// 字体颜色
	private int borderColor = 0x868686;// 边框的颜色
	private int[] rowColors;// 表格行的背景颜色
	private float cellHeight = 20;// 表格的高度

	private float positionY;// 画完表格之后，当前所在的横坐标
	private int scoreDescWidth =60;// 文字描述的宽度
	private String[] scoreDescNames;//每个批次分数的描述名称

	/**
	 * 文字描述的宽度
	 * @param scoreDescWidth
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setScoreDescWidth(int scoreDescWidth) {
		this.scoreDescWidth = scoreDescWidth;
		return this;
	}

	/**
	 * 每个批次分数的描述名称
	 * @param scoreDescNames
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setScoreDescNames(String[] scoreDescNames) {
		this.scoreDescNames = scoreDescNames;
		return this;
	}

	public LineMulCurveLineAreaTableChart() {
		super();
	}

	public LineMulCurveLineAreaTableChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;

		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测itemNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new float[] { 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f };
		}

		if (ObjectUtils.equals(null, this.areaBackgroundColors)) {
			this.areaBackgroundColors = new int[] { 0xDEF5FF, 0xEEF7DF, 0xFFF5DD};
		}

		if (ObjectUtils.equals(null, this.tableHeadFillColors)) {
			this.tableHeadFillColors = new int[] { 0x59CFFF, 0xA9D961, 0xFFCE54};
		}

		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors =new int[] { 0xFFFFFF, 0xF2F2F2 };
		}

		if (ObjectUtils.equals(null, this.scoreDescNames)) {
			this.scoreDescNames = new String[] { "第一批次", "第二批次", "第三批次","第四批次","第五批次" };
		}
		
		if (ObjectUtils.equals(null, this.jionLineColors)) {
			this.jionLineColors = new int[] {0x9792DC,0xFFCE54,0x72CBAD,0xE47D54,0x59CFFF};
		}

		try {
			BaseColor fontColor_ = new BaseColor(this.fontColor);
			BaseColor borderColor_ = new BaseColor(this.borderColor);
			BaseColor tableBorderColor_ = new BaseColor(this.tableBorderColor);
			
			BaseColor[] jionLineColors_=new BaseColor[this.jionLineColors.length];
			for(int i=0,len=this.jionLineColors.length;i<len;i++){
				jionLineColors_[i]=new BaseColor(this.jionLineColors[i]);
			}
			

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

			this.contentByte.setColorFill(fontColor_);

			this.drawEveryArea();
			// 绘画柱状图框架
			this.drawFrame(borderColor_, fontColor_);

			this.drawTable(fontColor_, tableBorderColor_,jionLineColors_);

			this.drawScore(jionLineColors_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画出点折线
	 * @param jionLineColors
	 */
	private void drawScore(BaseColor[] jionLineColors) {
		float sepWidth = this.width / this.itemNames.length,
				kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]);
		
		float x0 = this.x + sepWidth /2;
		for(int i=0,len=this.scores.length;i<len;i++){
			this.contentByte.setColorStroke(jionLineColors[i]);
			this.contentByte.setColorFill(jionLineColors[i]);
			for(int j=0,l=this.scores[i].length;j<l;j++){
				if(j>0){
					this.moveLine(this.contentByte, x0-2, this.y+kHeight*(this.scores[i][j]- this.levels[0])
							, x0-sepWidth+2, this.y+kHeight*(this.scores[i][j-1]- this.levels[0]));
				}
				
				this.moveCircle(this.contentByte, x0, 
						this.y+kHeight*(this.scores[i][j]- this.levels[0]),2, false);
				
				x0+= sepWidth;
			}
			x0 = this.x + sepWidth /2;
		}
	}

	/**
	 * 画出各个itemName对应的区域
	 */
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

	/**
	 * 计算出表格的头部宽度
	 * @param width
	 * @return
	 */
	private float calTableHeadHeight(float width) {
		float sum = 0;
		for (int i = 0; i < this.itemNames.length; i++) {
			sum = Math.max(sum, this.itemNames[i].length());
		}
		sum = sum * this.fontSize % width == 0 ? sum * this.fontSize / width
				: (float) (Math.floor(sum * this.fontSize / width) + 1);
		return (sum - 1) * this.fontSize + this.cellHeight;
	}

	private void drawTable(BaseColor fontColor, BaseColor tabelBorderColor,BaseColor[] jionLineColors) {
		float sepWidth = this.width / this.itemNames.length;
		float temp = calTableHeadHeight(sepWidth);
		float x0 = this.x, y0 = this.y - temp;
		this.positionY+=temp;
		
		this.contentByte.setLineWidth(1f);
		this.contentByte.setLineDash(1);

		for (int i = 0; i < this.itemNames.length; i++) {
			this.contentByte.setColorFill(tabelBorderColor);
			this.contentByte.setColorStroke(tabelBorderColor);
			this.moveRect(this.contentByte, x0, this.y, x0 + sepWidth - 1, y0 + 1,
					this.tableHeadFillColors[i]);
			this.moveLine(this.contentByte, x0 + sepWidth-1, this.y,
										x0 + sepWidth-1, y0+ 1);

			this.contentByte.setColorFill(BaseColor.WHITE);
			drawMulRowText(temp - 1, sepWidth, this.itemNames[i], x0, this.y);

			x0 += sepWidth;
		}
		
		this.contentByte.setColorStroke(tabelBorderColor);
		this.moveLine(this.contentByte, x0-this.width-1, y0,
				x0-this.width-1, y0 +this.cellHeight + 1);

		x0 = this.x;
		for (int i = 0,len=this.scores.length; i <len; i++) {
			// 开始画出分数
			for (int j = 0,l= this.scores[i].length; j <l; j++) {

				this.contentByte.setColorStroke(tabelBorderColor);
				this.contentByte.setColorFill(tabelBorderColor);
				this.moveRect(this.contentByte, x0, y0, x0 + sepWidth - 1, y0 - this.cellHeight + 1,
						this.rowColors[i % 2]);
				this.moveLine(this.contentByte, x0 + sepWidth-1, y0,
																	x0 + sepWidth-1, y0 - this.cellHeight + 1);

				this.contentByte.setColorFill(fontColor);
				drawMulRowText(this.cellHeight - 1, sepWidth, this.scores[i][j] + "", x0, y0);

				if(i==len-1)
					this.moveLine(this.contentByte, x0, y0 - this.cellHeight + 1,
							x0 + sepWidth,y0 - this.cellHeight + 1);
				
				x0 += sepWidth;
			}
			x0 = this.x;
			this.moveRect(this.contentByte, x0 - 1, y0, x0 - 1 - this.scoreDescWidth, y0 - this.cellHeight + 1,
					this.rowColors[i % 2]);
			
			this.contentByte.setColorStroke(tabelBorderColor);
			this.moveLine(this.contentByte, x0 - 1, y0,x0 - 1, y0 - this.cellHeight + 1);
			this.moveLine(this.contentByte, x0 - 1 - this.scoreDescWidth, y0,
													x0 - 1 - this.scoreDescWidth, y0 - this.cellHeight + 1);
			if(i == 0)
				this.moveLine(this.contentByte, x0 - 1, y0,
												x0 - 1 - this.scoreDescWidth,y0);
			if(i==len-1)
				this.moveLine(this.contentByte, x0 - 1, y0 - this.cellHeight + 1,
						x0 - 1 - this.scoreDescWidth,y0 - this.cellHeight + 1);
			
			this.contentByte.setColorFill(fontColor);
			temp=this.scoreDescWidth*4f/5;
			drawMulRowText(this.cellHeight, temp, this.scoreDescNames[i], x0 - 1 - temp,y0);
			temp=(temp-this.calTextWidth(this.fontSize, this.scoreDescNames[i]))/2;
			temp=(this.scoreDescWidth/5+temp)/2;
			
			this.contentByte.setColorStroke(jionLineColors[i]);
			this.drawLineCircle(x0-1- this.scoreDescWidth+temp,
					y0-this.cellHeight/2,2,false);
			
			y0 -= this.cellHeight;
			this.positionY += this.cellHeight;
		}
	}

	/**
	 * 画出点贯穿线
	 * @param x
	 * @param y
	 * @param r
	 * @param isFill
	 */
	private void drawLineCircle(float x,float y,float r,boolean isFill){
		this.moveCircle(this.contentByte, x,y, r, isFill);
		this.moveLine(this.contentByte, x-r, y, x-r-5, y);
		this.moveLine(this.contentByte, x+r, y, x+r+5, y);
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
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Y轴高度
	 * @param height
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * X轴宽度
	 * @param width
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 刻度
	 * @param levels
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setLevels(float[] levels) {
		this.levels = levels;
		return this;
	}

	/**
	 * 名称。比如：指标
	 * @param itemNames
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	/**
	 * 每块区域的背景颜色
	 * @param areaBackgroundColors
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setAreaBackgroundColors(int[] areaBackgroundColors) {
		this.areaBackgroundColors = areaBackgroundColors;
		return this;
	}

	/**
	 * 表头的填充颜色
	 * @param tableHeadFillColors
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setTableHeadFillColors(int[] tableHeadFillColors) {
		this.tableHeadFillColors = tableHeadFillColors;
		return this;
	}

	/**
	 * 点连接线的颜色
	 * @param jionLineColors
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setJionLineColors(int[] jionLineColors) {
		this.jionLineColors = jionLineColors;
		return this;
	}

	/**
	 * 字体颜色
	 * @param fontColor
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 边框的颜色
	 * @param borderColor
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 各个批次的分数
	 * @param scores
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setScores(float[][] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 表格行的背景颜色
	 * @param rowColors
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	/**
	 * 表格的高度
	 * @param cellHeight
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	/**
	 * 表格边框的颜色
	 * @param tableBorderColor
	 * @return LineMulCurveLineAreaTableChart
	 */
	public LineMulCurveLineAreaTableChart setTableBorderColor(int tableBorderColor) {
		this.tableBorderColor = tableBorderColor;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY = this.y- this.positionY-5;
		return this.positionY;
	}
}
