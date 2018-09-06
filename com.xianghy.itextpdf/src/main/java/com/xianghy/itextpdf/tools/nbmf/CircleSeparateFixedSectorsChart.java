package com.xianghy.itextpdf.tools.nbmf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.TPoint;
import com.xianghy.itextpdf.tools.chart.AbstractChart;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 一个圆，被分隔为固定的扇形区域【一般为两个，根据给定的分数数量确认】，
 * 每个区域颜色不同，中间为空心【没有文字显示在扇形区域中】
 * @author cheny
 */
public class CircleSeparateFixedSectorsChart extends AbstractChart {

	// 初始化x,y的位置，及图形的大小，扇形宽度
	private float x;
	private float sectorWidth =30;//中间有颜色的区域宽度
	private float r=80;
	private float y;
	private float rotation=253;

	private String  scoreSuffix="分";//分数添加的后缀
	private float[] scores;//分数
	private int[] fillColors;// 背景颜色

	private String totalScoreName="总分";//总分文字描述
	private float totalScore;//总分分数
	private int totalScoreNameColor=0x16CA4;// 总分文字颜色
	private int totalScoreColor= 0x000000;// 总分分数颜色
	private float totalScoreColorFontSize =10;// 总分字体大小
	private float fontSize =8;// 字体大小
	private int fontColor = 0xFFFFFF;// 文字颜色

	private boolean showPoint=false;//是否显示分数指针
	private float pointWidth=5;//指针宽度
	private float pointHeight=5;//指针高度
	private float positionY;// 画完表格之后，当前所在的横坐标
	
	private final int SIDE_NUM=3600;//将一个圆分成的等分
	private final float MAX=180f;//画圆的一个常量数据
	
	private BaseColor[] fillColors_;//填充的颜色,方便使用
	private BaseColor fontColor_ ;//文字的颜色,方便使用

	public CircleSeparateFixedSectorsChart() {
		super();
	}

	public CircleSeparateFixedSectorsChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;

		if (ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测scores数据是否存在！");
		
		if (ObjectUtils.equals(null, this.fillColors)) {
			this.fillColors = new int[] { 0xD9D9D9, 0x16CA4, 0xBFBFBF, 0x16CA4};
		}
		
		this.fontColor_ = new BaseColor(this.fontColor);
		
		this.fillColors_=new BaseColor[this.fillColors.length];
		for(int i=0;i<this.fillColors.length;i++){
			this.fillColors_[i]=new BaseColor(this.fillColors[i]);
		}
		
		float sum=0;
		 for(float f:this.scores){
			 sum+=f;
		 }
		
		try {
			this.contentByte.setLineWidth(1f);
			this.contentByte.setColorFill(this.fontColor_);
			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小
			
			drawOneFillCircle(this.r,this.fillColors_,sum);
			this.contentByte.setColorStroke(BaseColor.WHITE);
			this.moveCircle(this.contentByte,this.x, this.y, this.r-this.sectorWidth, true);
			
			if(this.showPoint)
				drawPoint(this.fillColors_,sum);
			
			//写总分数
			this.contentByte.setFontAndSize(this.baseFont, this.totalScoreColorFontSize);
			this.contentByte.setColorFill(new BaseColor(this.totalScoreNameColor));
			this.moveText(this.contentByte, this.totalScoreName, this.x,
					this.y+this.totalScoreColorFontSize/2, Element.ALIGN_CENTER, 0);
			this.contentByte.setColorFill(new BaseColor(this.totalScoreColor));
			this.moveText(this.contentByte, this.totalScore+this.scoreSuffix, this.x,
					this.y-this.totalScoreColorFontSize/2, Element.ALIGN_CENTER, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 画出指针
	 * @param curR
	 * @param fillColors
	 */
	private void drawPoint(BaseColor[] fillColors,float sum){
		float space = 360f / this.SIDE_NUM;
		float x0 = 0, y0 = 0;
		TPoint top=new TPoint();
		
		int num= this.scores.length;
		float everyErea=this.SIDE_NUM*this.scores[0]/sum;
		space =everyErea*space/2;
		// 开始画出图形
		for (int i = 0; i <num; i++) {
			this.contentByte.setColorStroke(fillColors[i]);
			top.setX(this.x + (float) ((this.r-this.sectorWidth-this.pointHeight) * 
					Math.sin(Math.PI * (space+ this.rotation) / this.MAX)));
			top.setY( this.y + (float) ((this.r-this.sectorWidth-this.pointHeight)* 
					Math.cos(Math.PI * (space+ this.rotation) / this.MAX)));
			
			for(float f=0;f<=this.pointWidth*2;f+=0.1){
				x0=this.x + (float) ((this.r-this.sectorWidth*9/10) *  
						Math.sin(Math.PI * (space-this.pointWidth+f + this.rotation)/ this.MAX));
				y0= this.y + (float) ((this.r-this.sectorWidth*9/10)* 
						Math.cos(Math.PI * (space-this.pointWidth+f + this.rotation) / this.MAX));
				this.moveLine(this.contentByte, top.getX(), top.getY(), x0, y0);
			}
			everyErea=this.SIDE_NUM*this.scores[i]/sum;
			space+= everyErea/2;
		}
	}
	
	/**
	 * 画出一个填充的圆
	 * @param curR
	 * @param fillColors
	 */
	private void drawOneFillCircle(float curR,BaseColor[] fillColors,float sum){
		float space = 360f / this.SIDE_NUM, spaceSize = space;

		float x0 = 0, y0 = 0;
		
		int num= this.scores.length;
		float[] everyEreas = new float[num];
		for (int i = 0; i <num; i++) {
			everyEreas[i] =this.SIDE_NUM*this.scores[i]/sum;
		}

		// 开始画出图形
		int areaNum = 0;
		float areaSum = everyEreas[areaNum];
		for (int i = 0; i < this.SIDE_NUM; i++) {
			if (i <= areaSum) {
				this.contentByte.setColorStroke(fillColors[areaNum]);
			} else {
				areaSum += everyEreas[++areaNum];
			}
			x0 = this.x + (float) (curR * Math.sin(Math.PI * (space + this.rotation) / this.MAX));
			y0 = this.y + (float) (curR * Math.cos(Math.PI * (space + this.rotation) /  this.MAX));
			this.moveLine(this.contentByte, this.x, this.y, x0, y0);
			space += spaceSize;
		}
	}
	
	/**
	 * X坐标
	 * @param x
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * 中间有颜色的区域宽度
	 * @param sectorWidth
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setSectorWidth(float sectorWidth) {
		this.sectorWidth = sectorWidth;
		return this;
	}

	/**
	 * 半径
	 * @param r
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setR(float r) {
		this.r = r;
		return this;
	}

	/**
	 * 分数添加的后缀
	 * @param scoreSuffix
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setScoreSuffix(String scoreSuffix) {
		this.scoreSuffix = scoreSuffix;
		return this;
	}

	/**
	 * 分数
	 * @param scores
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setScores(float[] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 总分文字描述
	 * @param totalScoreName
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setTotalScoreName(String totalScoreName) {
		this.totalScoreName = totalScoreName;
		return this;
	}

	/**
	 * 总分分数
	 * @param totalScore
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setTotalScore(float totalScore) {
		this.totalScore = totalScore;
		return this;
	}

	/**
	 * 是否显示分数指针
	 * @param showPoint
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setShowPoint(boolean showPoint) {
		this.showPoint = showPoint;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 文字颜色
	 * @param fontColor
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY = this.y -this.r-5;
		return this.positionY;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 旋转角度
	 * @param rotation
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}

	/**
	 * 背景颜色
	 * @param fillColors
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setFillColors(int[] fillColors) {
		this.fillColors = fillColors;
		return this;
	}

	/**
	 * 总分文字颜色
	 * @param totalScoreNameColor
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setTotalScoreNameColor(int totalScoreNameColor) {
		this.totalScoreNameColor = totalScoreNameColor;
		return this;
	}

	/**
	 * 总分分数颜色
	 * @param totalScoreColor
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setTotalScoreColor(int totalScoreColor) {
		this.totalScoreColor = totalScoreColor;
		return this;
	}

	/**
	 * 总分字体大小
	 * @param totalScoreColorFontSize
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setTotalScoreColorFontSize(float totalScoreColorFontSize) {
		this.totalScoreColorFontSize = totalScoreColorFontSize;
		return this;
	}

	/**
	 * 指针宽度
	 * @param pointWidth
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setPointWidth(float pointWidth) {
		this.pointWidth = pointWidth;
		return this;
	}

	/**
	 * 指针高度
	 * @param pointHeight
	 * @return CircleSeparateManySectorsChart
	 */
	public CircleSeparateFixedSectorsChart setPointHeight(float pointHeight) {
		this.pointHeight = pointHeight;
		return this;
	}
}
