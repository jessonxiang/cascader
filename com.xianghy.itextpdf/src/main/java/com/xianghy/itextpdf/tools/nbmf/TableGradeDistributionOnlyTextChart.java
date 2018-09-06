package com.xianghy.itextpdf.tools.nbmf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xianghy.itextpdf.tools.base.unit.BalloonGraph;
import com.xianghy.itextpdf.tools.chart.AbstractChart;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 表格+分数分级别图(可以标出当前的位置 及 分隔线)
 * 
 * @author cheny
 *
 */
public class TableGradeDistributionOnlyTextChart extends AbstractChart {

	// 初始化x,y的位置，及图形的大小
	private float x;
	private float width = 450;
	private float y;

	private String[]  gradeNames;// 各个级别名称
	private String[]  gradeDescs;// 各个级别描述
	private int[] gradeBackgroundColors;// 级别的背景颜色

	private String curNumber;// 当前要画出的数值
	private float curPercentage;// 当前要画出的数值的位置比值
	private float maxNumber;//最大的数，用来找出当前数值要画出的位置
	private float fontSize =8;// 字体大小
	private int fontColor = 0x000000;// 文字颜色

	private boolean isMarkCurPositon=true;//是否标记处当前分数的位置
	private int sepLineColor=0xAEAEAE;//在不显示当前分数位置的情况下，会画出一条分隔线
	private float sepTopMargin=1.3f;//分隔线与表格的距离
	private float sepLineWidth=1.8f;//分隔线的宽度
	private boolean isShowSepLine;//是否显示分隔线
	
	private float positionY;// 画完表格之后，当前所在的横坐标
	private float cellHeight=15;//单元格的高度
	private float[] widths;//没一列的百分比

	public TableGradeDistributionOnlyTextChart() {
		super();
	}

	public TableGradeDistributionOnlyTextChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;

		if (ObjectUtils.equals(null, this.gradeNames)) {
			this.gradeNames = new String[] { "优秀", "良好", "合格", "待发展" };
		}

		if (ObjectUtils.equals(null, this.gradeBackgroundColors)) {
			this.gradeBackgroundColors = new int[] { 0x5DD3B0, 0x92D050, 0xFFC000, 0xFC923B };
		}
		
		if (ObjectUtils.equals(null, this.widths)) {
			this.widths = new float[] {10,30,50,10};
		}

		BaseColor fontColor_ = new BaseColor(this.fontColor);

		try {
			this.contentByte.setLineWidth(1f);
			this.contentByte.setColorFill(fontColor_);
			this.contentByte.setFontAndSize(this.baseFont, this.fontSize);// 设置文字大小
			
			float sum=0;
			for(float w:this.widths){
				sum+=w;
			}
			
			float celHeight=calRowHeight(sum);
			
			drawTable(sum,celHeight);
 
			if(isMarkCurPositon)
				drawCurPosition(celHeight);
			
			if(isShowSepLine){
				this.contentByte.setColorStroke(new BaseColor(this.sepLineColor));
				this.contentByte.setLineWidth(this.sepLineWidth);
				this.moveLine(this.contentByte, this.x,
						this.y-celHeight-this.sepTopMargin-this.sepLineWidth,
						this.x+this.width, this.y-celHeight-this.sepTopMargin-this.sepLineWidth);
				this.positionY=this.sepTopMargin+this.sepLineWidth;
			}
				
			this.positionY+=celHeight;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文字计算一行的高度
	 * @param sum
	 * @return
	 */
	private float  calRowHeight(float sum){
		float celHeight=0;
		int lineNo=0;
		for(int i=0;i<this.gradeNames.length;i++){
			lineNo=(int)Math.ceil(this.gradeNames[i].length()*this.fontSize/(this.width*this.widths[i]/sum));
			if(ObjectUtils.notEqual(null, this.gradeDescs))
				lineNo+=(int)Math.ceil(this.gradeDescs[i].length()*this.fontSize/(this.width*this.widths[i]/sum));
			celHeight=Math.max(celHeight,lineNo*this.fontSize+this.cellHeight+lineNo);
		}
		return celHeight;
	}
	
	/**
	 * 画出表格
	 * @param sum
	 * @param celHeight
	 */
	private void drawTable(float sum,float celHeight) {
		float x0 = this.x,tempH=0,yt=0;
		int nameLineNo=0,descLineNo=0,sepSpace=2;
		
		if(ObjectUtils.notEqual(null, this.gradeDescs)){
			sepSpace=3;
		}
		
		for(int i=0;i<this.gradeNames.length;i++){
			
			this.moveRect(this.contentByte, x0, this.y, x0 +this.width*this.widths[i]/sum-1,
									this.y-celHeight,this.gradeBackgroundColors[i]);
			nameLineNo=(int)Math.ceil(this.gradeNames[i].length()*this.fontSize/(this.width*this.widths[i]/sum));
			if(ObjectUtils.notEqual(null, this.gradeDescs)) 
				descLineNo=(int)Math.ceil(this.gradeDescs[i].length()*this.fontSize/(this.width*this.widths[i]/sum));
			tempH=this.fontSize*(nameLineNo+descLineNo);
			
			yt=(celHeight-tempH)/sepSpace;
			this.drawMulRowText(nameLineNo*this.fontSize+yt*2, this.width*this.widths[i]/sum,
					this.gradeNames[i], x0,this.y);
			
			if(ObjectUtils.notEqual(null, this.gradeDescs)){
				this.drawMulRowText(descLineNo*this.fontSize+yt*2, this.width*this.widths[i]/sum, 
						this.gradeDescs[i], x0,this.y-nameLineNo*this.fontSize-yt);
			}
			x0+=this.width*this.widths[i]/sum;
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
		this.moveMultiLineText(this.contentByte, text, this.fontSize, witdh, lineHeight, x, y, 0);
	}

	/**
	 * 画出当前位置及显示的分数
	 */
	private void drawCurPosition(float celHeight) throws Exception {
		/**
		 * 画出分数分布的位置
		 */
		BalloonGraph balloonGraph = new BalloonGraph(this.writer, this.contentByte, this.document);
		balloonGraph.setBaseChart(this);

		balloonGraph.setY(this.y -celHeight).setLineHeight(celHeight)
				.setAreaHeight(8)
				.setBallR(this.fontSize*(this.curNumber.length()>4?this.curNumber.length():4)*1.1f/3)
				.setX(this.x +this.curPercentage*this.width/this.maxNumber);

		balloonGraph.chart();
		
		this.moveText(this.contentByte, this.curNumber,
				this.x +this.curPercentage*this.width/this.maxNumber,
				this.y +8+this.fontSize*(this.curNumber.length()>4?
				this.curNumber.length():4)*1.1f/4, Element.ALIGN_CENTER, 0);
	}

	/**
	 * X坐标
	 * @param x
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * X轴宽度
	 * @param width
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * Y坐标
	 * @param y
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * 各个级别名称
	 * @param gradeNames
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setGradeNames(String[] gradeNames) {
		this.gradeNames = gradeNames;
		return this;
	}

	/**
	 *  级别的背景颜色
	 * @param gradeBackgroundColors
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setGradeBackgroundColors(int[] gradeBackgroundColors) {
		this.gradeBackgroundColors = gradeBackgroundColors;
		return this;
	}

	/**
	 *  文字颜色
	 * @param fontColor
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY = this.y - this.positionY-5;
		return this.positionY;
	}

	/**
	 * 各个级别描述
	 * @param gradeDescs
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setGradeDescs(String[] gradeDescs) {
		this.gradeDescs = gradeDescs;
		return this;
	}

	/**
	 * 当前要画出的数值
	 * @param curNumber
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setCurNumber(String curNumber) {
		this.curNumber = curNumber;
		return this;
	}

	/**
	 * 最大的数，用来找出当前数值要画出的位置
	 * @param maxNumber
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setMaxNumber(float maxNumber) {
		this.maxNumber = maxNumber;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 是否标记处当前分数的位置
	 * @param isMarkCurPositon
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setMarkCurPositon(boolean isMarkCurPositon) {
		this.isMarkCurPositon = isMarkCurPositon;
		return this;
	}

	/**
	 * 在不显示当前分数位置的情况下，会画出一条分隔线
	 * @param sepLineColor
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setSepLineColor(int sepLineColor) {
		this.sepLineColor = sepLineColor;
		return this;
	}

	/**
	 * 是否显示分隔线
	 * @param isShowSepLine
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setShowSepLine(boolean isShowSepLine) {
		this.isShowSepLine = isShowSepLine;
		return this;
	}

	/**
	 * 单元格的高度
	 * @param cellHeight
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	/**
	 * 没一列的百分比
	 * @param widths
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setWidths(float[] widths) {
		this.widths = widths;
		return this;
	}

	/**
	 * 分隔线与表格的距离
	 * @param sepTopMargin
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setSepTopMargin(float sepTopMargin) {
		this.sepTopMargin = sepTopMargin;
		return this;
	}

	/**
	 * 当前要画出的数值的位置比值
	 * @param curPercentage
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setCurPercentage(float curPercentage) {
		this.curPercentage = curPercentage;
		return this;
	}

	/**
	 * 分隔线的宽度
	 * @param sepLineWidth
	 * @return TableGradeDistributionOnlyTextChart
	 */
	public TableGradeDistributionOnlyTextChart setSepLineWidth(float sepLineWidth) {
		this.sepLineWidth = sepLineWidth;
		return this;
	}
}
