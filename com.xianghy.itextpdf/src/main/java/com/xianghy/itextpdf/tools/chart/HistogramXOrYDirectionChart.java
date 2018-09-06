package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 柱状图+X方向显示的柱状图
 * 
 * @author cheny
 *
 */
public class HistogramXOrYDirectionChart extends AbstractChart {
//方向
	/**
	 * X轴方向
	 */
	public static final int DIRECTION_X=0;
	/**
	 * Y轴方向
	 */
	public static final int DIRECTION_Y=1;
	
	private float x;
	private float y;
	private float height = 120;
	private float width = 450;
	private float fontSize = 10;
	private int histogramDirection=DIRECTION_X;//设置坐标的方向

	private int[] levels;//刻度
	private String[] itemNames;// 名称
	private float[] scores;//分数
	private int[] itemColors;//柱状图的背景颜色
	
	private int fontColor = 0x000000;// 字体颜色
	private int borderColor = 0x000000;// 边框的颜色
	private float positionY;// 画完表格之后，当前所在的横坐标
 
	public HistogramXOrYDirectionChart() {
		super();
	}

	public HistogramXOrYDirectionChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY=0;
		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测itemNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new int[] {1, 2, 3, 4, 5, 6, 7, 8 ,9};
		}

		if (ObjectUtils.equals(null, this.itemColors)) {
			this.itemColors = new int[] {0x72CBAD,0xE47D54};
		}

		try {
			BaseColor fontColor_ = new BaseColor(this.fontColor);
			BaseColor borderColor_ = new BaseColor(this.borderColor);

			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
			
			this.drawFrame(borderColor_,fontColor_);

			drawHistogram();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 画出柱状图
	 */
	private void drawHistogram(){
		switch (this.histogramDirection) {
		case DIRECTION_X:{
			float sepHeight=this.height/this.itemNames.length;
			float  kWidth = this.width / (this.levels[this.levels.length - 1] - this.levels[0]),
					y0=this.y+sepHeight/4,x0=0,textY=0;
			for(int i=0;i<this.itemNames.length;i++){
				x0=kWidth* (this.scores[i] - this.levels[0]);
				textY=sepHeight>this.fontSize?y0+(sepHeight/2-this.fontSize)/2:y0+sepHeight/2;
				
				this.moveText(this.contentByte, this.itemNames[i], 
							this.x-this.fontSize/2,textY, Element.ALIGN_RIGHT, 0);
				
				this.moveRect(this.contentByte, this.x+1f, y0, this.x+x0, y0+sepHeight/2,
								this.itemColors[i%this.itemNames.length]);
				
				this.moveText(this.contentByte, this.scores[i]+"", 
						this.x+x0+this.fontSize/2,textY, Element.ALIGN_LEFT, 0);
				
				y0+=sepHeight;
			}
			break;
		}
		case DIRECTION_Y:{
			float sepWidht=this.width/this.itemNames.length;
			float  kHeight = this.height / (this.levels[this.levels.length - 1] - this.levels[0]),
					x0=this.x+sepWidht/4,y0=0,textX=0;
			for(int i=0;i<this.itemNames.length;i++){
				y0=kHeight* (this.scores[i] - this.levels[0]);
				textX=sepWidht>this.fontSize?
						x0+(sepWidht/2-this.fontSize*this.itemNames[i].length())/2
						:x0+sepWidht/2;
				
				this.moveText(this.contentByte, this.itemNames[i], 
						textX,this.y-14f, Element.ALIGN_LEFT, 0);
				
				this.moveRect(this.contentByte, x0,this.y+1f, x0+sepWidht/2,  this.y+y0,
								this.itemColors[i%this.itemNames.length]);
				
				this.moveText(this.contentByte, this.scores[i]+"", 
						textX, this.y+y0+this.fontSize/2,Element.ALIGN_LEFT, 0);
				
				x0+=sepWidht;
			}
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 绘画柱状图框架
	 */
	private void drawFrame(BaseColor borderColor, BaseColor fontColor) {
		this.contentByte.setColorStroke(borderColor);
		// 绘画x,y
		this.moveLine(this.contentByte, this.x, this.y, this.x, this.y + this.height);
		moveLine(this.contentByte, this.x, this.y, this.x+this.width, this.y);
		
		//画出X或Y轴的刻度
		float lineLength=this.width;
		float noChangedParam=this.y,changedParam=0;
		switch (this.histogramDirection) {
		case DIRECTION_X:{
			lineLength= this.width / (this.levels[this.levels.length - 1] - this.levels[0]);
			noChangedParam=this.y;
			changedParam=this.x;
			break;
		}
		case DIRECTION_Y:{
			lineLength= this.height / (this.levels[this.levels.length - 1] - this.levels[0]);
			noChangedParam=this.x;
			changedParam=this.y;
			break;
		}
		default:
			break;
		}
		
		float tempParam=0;

		for (int i = 0; i < this.levels.length; i++) {
			tempParam =changedParam + lineLength * (this.levels[i] - this.levels[0]);
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineDash(1f);
			switch (this.histogramDirection) {
			case DIRECTION_X:{
				this.moveLine(this.contentByte, tempParam, noChangedParam, tempParam, noChangedParam+3);
				
				this.contentByte.setColorFill(fontColor);
				this.moveText(this.contentByte, this.levels[i] + "", tempParam+3,
						noChangedParam-10, Element.ALIGN_RIGHT, 0);
				break;
			}
			case DIRECTION_Y:{
				this.moveLine(this.contentByte, noChangedParam, tempParam,  noChangedParam-3,tempParam);
				
				this.contentByte.setColorFill(fontColor);
				this.moveText(this.contentByte, this.levels[i] + "", noChangedParam-5,
						tempParam-3, Element.ALIGN_RIGHT, 0);
				break;
			}
			default:
				break;
			}
		}
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY=this.y-this.height-this.positionY+10;
		return this.positionY;
	}

	/**
	 * X坐标
	 * @param x
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Y轴高度
	 * @param height
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * X轴宽度
	 * @param width
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 刻度
	 * @param levels
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setLevels(int[] levels) {
		this.levels = levels;
		return this;
	}

	/**
	 * 名称。比如：指标
	 * @param itemNames
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	/**
	 * 分数
	 * @param scores
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 柱状图的背景颜色
	 * @param itemColors
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setItemColors(int[] itemColors) {
		this.itemColors = itemColors;
		return this;
	}

	/**
	 * 字体颜色
	 * @param fontColor
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 边框的颜色
	 * @param borderColor
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 设置坐标的方向 DIRECTION_X or DIRECTION_Y
	 * @param histogramDirection
	 * @return HistogramXOrYDirectionChart
	 */
	public HistogramXOrYDirectionChart setHistogramDirection(int histogramDirection) {
		this.histogramDirection = histogramDirection;
		return this;
	}
}
