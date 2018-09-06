package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 柱状图+区域区分+ 极值标注
 * 
 * @author cheny
 *
 */
public class HistogramAreaExtremumChart extends AbstractChart {

	private float x;
	private float y;
	private float height = 200;
	private float width = 450;
	private float fontSize = 10;

	private int[] levels;//刻度
	private String[] itemNames;//分数对应的名称
	private float[] scores;//分数
	private int borderColor=0xAAAAAA;//坐标轴框的颜色
	private int fontColor=0x000000;//字体的颜色
	
	private int[] extremumIndex;//极值的分数所在的列
	private String[] extremumNames;//极值的描述名称
	private int[] extremumColors;//极值填充的颜色
	private int[] extremumAreaColors;//极值区域块的颜色
	
	private float positionY;// 画完表格之后，当前所在的横坐标
	private float showScoreAreaHeight=30;//显示分数的矩形区域高度
	

	public HistogramAreaExtremumChart() {
		super();
	}

	public HistogramAreaExtremumChart(PdfWriter writer, PdfContentByte contentByte,
												Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		
		this.positionY=0;
		
		if (ObjectUtils.equals(null, this.itemNames) || this.itemNames.length < 1
				|| ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测itemNames、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.levels)) {
			this.levels = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		}

		if (ObjectUtils.equals(null, this.extremumIndex)) {
			this.extremumIndex = new int[] {0,2};
		}

		if (ObjectUtils.equals(null, this.extremumNames)) {
			this.extremumNames = new String[] { "最高", "最低"};
		}

		if (ObjectUtils.equals(null, this.extremumColors)) {
			this.extremumColors = new int[] {0x72CBAD,0x7F7F7F,0xE47D54,0x7F7F7F};
		}

		if (ObjectUtils.equals(null, this.extremumAreaColors)) {
			this.extremumAreaColors = new int[] {0xC7EADE,0xFFFFFF,0xF4CBBB,0xFFFFFF};
		}

		try {
			BaseColor borderColor_ = new BaseColor(this.borderColor);
			BaseColor fontColor_ = new BaseColor(this.fontColor);

			 this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
			 
			 this.contentByte.setColorFill(fontColor_);
			 this.drawEveryArea();
			 
			// 绘画柱状图框架
			this.drawFrame(borderColor_, fontColor_);
			
			drawScore();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void drawScore(){
		float sepWidth = this.width / this.itemNames.length ;
		float kHeight =this.height/(this.levels[this.levels.length - 1]-this.levels[0]);
		float x0=this.x,y0=0,offset=10,textHeight=0;
		int index=0;
		
		for(int i=0;i<this.itemNames.length;i++){
			y0=kHeight*(this.scores[i]-this.levels[0]);
			
			this.contentByte.setColorFill(BaseColor.BLACK);
			this.moveRect(this.contentByte, x0+sepWidth/4,this.y+1, x0+sepWidth*3/4,
							this.y+y0, this.extremumColors[i], false);
			
			this.moveRect(this.contentByte, x0 ,this.y, x0+sepWidth-1,
					this.y-this.showScoreAreaHeight, this.extremumColors[i], false);
			
			this.contentByte.setColorFill(BaseColor.WHITE);
			drawMulRowText(this.showScoreAreaHeight, 
					sepWidth, this.scores[i]+"", x0, this.y);
			
			textHeight=this.itemNames[i].length()*this.fontSize;
			textHeight=textHeight%sepWidth==0?textHeight/sepWidth:1+textHeight/sepWidth;
			textHeight=textHeight>1?textHeight*this.fontSize:this.showScoreAreaHeight;
			
			this.contentByte.setColorFill(BaseColor.BLACK);
			drawMulRowText(textHeight,
					sepWidth, this.itemNames[i], x0, this.y-this.showScoreAreaHeight);
			this.positionY=Math.max(this.positionY, textHeight+this.showScoreAreaHeight);
			
			if(ArrayUtils.contains(this.extremumIndex, i)){
				textHeight=this.extremumNames[index].length()*this.fontSize;
				textHeight=textHeight%(sepWidth/2)==0?textHeight/(sepWidth/2):1+textHeight/(sepWidth/2);
				textHeight=textHeight*this.fontSize;
				
				if(y0<=(10+textHeight)){
					offset=y0+textHeight+2;
				}else{
					offset=y0-10;
				}
				 
				this.moveRect(this.contentByte, x0+sepWidth/4+1,this.y+offset, x0+sepWidth*3/4-1,
						this.y+offset-textHeight, BaseColor.WHITE.getRGB(), false);
				
				drawMulRowText(textHeight,sepWidth/2-2<this.fontSize?this.fontSize:sepWidth/2-2,
						 			this.extremumNames[index++], x0+sepWidth/4+1, this.y+offset);
			}
			
			x0+=sepWidth;
		}
	}
	
	private void drawEveryArea(){
		float sepWidth = this.width / this.itemNames.length;
		float x0=this.x;
		
		for(int i=0;i<this.itemNames.length;i++){
			this.moveRect(this.contentByte, i==0?x0+1:x0,this.y+1, x0+sepWidth-1,
							this.y+this.height, this.extremumAreaColors[i], false);
			x0+=sepWidth;
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
	 * 绘画柱状图框架
	 */
	private void drawFrame(BaseColor borderColor, BaseColor fontColor) {
		this.contentByte.setColorStroke(borderColor);
		// 绘画x,y
		this.moveLine(this.contentByte, this.x, this.y, this.x, this.y+this.height);
		//画Y轴的刻度
		float x1 = this.x, kHeight =this.height/(this.levels[this.levels.length - 1]-this.levels[0]), y1 =0;

		for (int i = 0; i < this.levels.length; i++) {
			y1 = this.y+kHeight*(this.levels[i]-this.levels[0]);
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineDash(1f);
			this.moveLine(this.contentByte, x1, y1, this.x - 2, y1);

			this.contentByte.setColorFill(fontColor);
			this.moveText(this.contentByte, this.levels[i] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT, 0);

			if(i>0){
				this.contentByte.setColorStroke(borderColor);
				this.contentByte.setLineDash(3f, 2f);
				this.moveLine(this.contentByte, x1, y1, x1 + this.width, y1);
			}
		}

		this.moveText(this.contentByte, this.levels[this.levels.length - 1] + "", x1 - 6, y1 - 3, Element.ALIGN_RIGHT,
				0);
		
		// 画X轴的名称
		float sepWidth = this.width / this.itemNames.length;
		this.contentByte.setColorStroke(BaseColor.BLACK);
		float x2 = this.x+sepWidth, y2 = this.y;
		for (int i = 0; i < this.itemNames.length-1; i++) {
			this.contentByte.setLineDash(1f);
			this.moveLine(this.contentByte, x2, y2, x2, y2 +this.height);
			x2 += sepWidth;
		}
	}
	
	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY=this.y-this.positionY-10;
		return this.positionY;
	}

	/**
	 * X坐标
	 * @param x
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Y轴高度
	 * @param height
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setHeight(float height) {
		this.height = height;
		return this;
	}

	/**
	 * X轴宽度
	 * @param width
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 刻度
	 * @param levels
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setLevels(int[] levels) {
		this.levels = levels;
		return this;
	}

	/**
	 * 分数对应的名称
	 * @param itemNames
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
		return this;
	}

	/**
	 * 分数
	 * @param scores
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 极值的分数所在的列
	 * @param extremumIndex
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setExtremumIndex(int[] extremumIndex) {
		this.extremumIndex = extremumIndex;
		return this;
	}

	/**
	 * 极值的描述名称
	 * @param extremumNames
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setExtremumNames(String[] extremumNames) {
		this.extremumNames = extremumNames;
		return this;
	}

	/**
	 * 极值填充的颜色
	 * @param extremumColors
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setExtremumColors(int[] extremumColors) {
		this.extremumColors = extremumColors;
		return this;
	}

	/**
	 * 极值区域块的颜色
	 * @param extremumAreaColors
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setExtremumAreaColors(int[] extremumAreaColors) {
		this.extremumAreaColors = extremumAreaColors;
		return this;
	}

	/**
	 * 坐标轴框的颜色
	 * @param borderColor
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 字体的颜色
	 * @param fontColor
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 显示分数的矩形区域高度
	 * @param showScoreAreaHeight
	 * @return HistogramAreaExtremumChart
	 */
	public HistogramAreaExtremumChart setShowScoreAreaHeight(float showScoreAreaHeight) {
		this.showScoreAreaHeight = showScoreAreaHeight;
		return this;
	}
}
