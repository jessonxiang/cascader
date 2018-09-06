package com.xianghy.itextpdf.tools.nbmf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.xianghy.itextpdf.tools.base.unit.FourTopQuadrilateralGraph;
import com.xianghy.itextpdf.tools.chart.AbstractChart;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 可以在两页显示的表格+一条点连接线+阴影区域图，positionY为画好图形后当前的Y坐标
 * @author cheny
 */
public class TableChartedPositionAreaCurveScoresChart extends AbstractChart {

	private float width = 450;// 表格的宽度
	private float fontSize = 8;// 除了刻线之外的文字大小
	private int fontColor=0x000000;//字体的颜色
	private float lineHeight = 15;// 每一行的高度
	private String[] tableHeads;// 表格表头信息
	private int[] scoreLevels;// 分数的刻度值
	
	private int tableHeadColor = 0x016CA4;// 表头背景颜色
	private int tableHeadFontColor = 0xFFFFFF;// 表头字体颜色
	private String[] parentTypes;// 父类名称
	private int parentTypeColor = 0xDCF3FB;// 父类背景颜色
	private List<String[]> childrenTypes;// 子类分组及每一行名称
	/**
	 * 所有的分数
	 */
	private float[][] scores;// 每一行的每列的分数
	private float tableHeadFontSize=10;//表头字体大小
	private float firstColumnFontSize=9;//第一列字体的大小
	/**
	 * 行的颜色，仅仅取前面两种颜色
	 */
	private int[] rowColors;// 行的交替颜色
	/**
	 * 指定那列及其颜色
	 */
	private float positionY;// 画完表格之后，当前所在的横坐标
	private int maxScoreColNum = 3;// 最大分数的列
	private int minScoreColNum = 2;// 最小分数的列
	private int curScoreColNum = 0;// 分数的列
	/**
	 * 曲线的颜色
	 */
	private int curveColor = 0x4F81BD;// 折现的颜色
	private float[] widths;//表格每一列的宽度
	private float levelFontSize = 7;// 刻度文本的字体大小
	private int borderColor=0x31859C;//表格边框的颜色
	
	private int verticalLineColor=0xD0D0D0;//垂直线的颜色
	private int verticalAreaColor=0xF1F5FA;//垂直线所在区域的颜色
	private float gradeRectangeGap=15;//级别颜色标注框与表格间的空隙
	 
	// 真实的行高
	private float[] realRowHeight;// 每一列的实际高度
	private int lineNumber;// 当表格可以在当前文档显示一半的时候，记录可以显示的行数
	
	private FourTopQuadrilateralGraph fillAreaGraph;//画填充区域的工具

	public TableChartedPositionAreaCurveScoresChart() {
		super();
	}

	public TableChartedPositionAreaCurveScoresChart(PdfWriter writer, PdfContentByte contentByte, Document document,
			BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		if (ObjectUtils.equals(null, this.childrenTypes)
				|| this.childrenTypes.isEmpty() || ObjectUtils.equals(null, this.tableHeads)
				|| this.tableHeads.length < 1 || ObjectUtils.equals(null, this.parentTypes)
				|| this.parentTypes.length < 1 || ObjectUtils.equals(null, this.scores) || this.scores.length < 1)
			throw new RuntimeException("请检测tableHeads、childrenTypes、parentTypes、scores数据是否存在！");

		if (ObjectUtils.equals(null, this.scoreLevels)) {
			this.scoreLevels = new int[] { 0, 1, 2, 3, 4, 5};
		}
		
		if (ObjectUtils.equals(null, this.widths)) {
			this.widths = new float[] {10,30,5,5,5,5,50};
		}

		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors = new int[] { 0xFFFFFF, 0xF2F2F2 };
		}

		this.fillAreaGraph=new FourTopQuadrilateralGraph(this.writer, this.contentByte, this.document);
		this.fillAreaGraph.setBaseChart(this);
		this.fillAreaGraph.setColor(this.verticalAreaColor);
		
		try {

			float sum = 0;
			for (float w : this.widths) {
				sum += w;
			}

			float y0 = this.writer.getVerticalPosition(true);
			checkPositionHeight(y0, sum);
			
			BaseColor borderColor_ = new BaseColor(this.borderColor);
			if (this.lineNumber <= 0) {
				this.document.newPage();
			}
			
			//开始画出表格部分
			addTableHead(sum);
			addTableBody(borderColor_, sum);
			this.lineNumber = 0;
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 根据数据，计算整个表格的高度，确定是否要换一个新的页面，或者可以在当前画出一部分表格。
	 * @param y0
	 * @param sum
	 * @return 表格体部分的行数
	 */
	private int checkPositionHeight(float y0, float sum) {
		float curHeight = this.lineHeight+this.gradeRectangeGap+15,
				temp = calTableHeadRowHeight(sum);
		 
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
	 * @param lineHeight
	 * @param witdh
	 * @param text
	 * @param x
	 * @param y
	 */
	private void drawMulRowText(float lineHeight, float witdh,float fontSize, String text, float x, float y) {
		this.moveMultiLineText(this.contentByte, text, fontSize, witdh, lineHeight, x, y, 0);
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
		BaseColor fontColor_=new BaseColor(this.fontColor);

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
				drawTextAndScore(y, colSpan, sum,borderColor, true);
				this.document.newPage();
				this.setLine(1,this.gradeRectangeGap, this.document);
				addTableHead(sum);
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
			this.contentByte.setColorFill(fontColor_);
			this.contentByte.setLineWidth(0.5f);
			this.moveLine(this.contentByte, x0 + 0.5f, y0, x0 + 0.5f, y0 - this.realRowHeight[i]);
			for (int curCol = 1; curCol < this.widths.length; curCol++) {
				this.contentByte.setLineWidth(1.5f);
				if (curCol == this.widths.length - 1 || (i + 1) % 2 == 1) {
					curColColor = this.rowColors[0];
				} else {
					curColColor = this.rowColors[1];
				}

				if (curCol == 1) {
					text = childStr.get(i);
				} else if ((curCol != this.widths.length - 1)) {
					text = rowScores[curCol - 2]+"";
				}

				this.contentByte.setColorStroke(borderColor);
				x0 += this.widths[curCol - 1] * this.width / sum;
				this.moveLine(this.contentByte, x0, y0, x0, y0 - this.realRowHeight[i]+0.5f);
				this.moveRect(this.contentByte, x0, y0, x0 + this.widths[curCol] * this.width / sum,
						y0 - this.realRowHeight[i], curColColor);

				if (curCol != this.widths.length - 1) {
					this.contentByte.setColorFill(fontColor_);
					drawMulRowText(this.realRowHeight[i], 
							this.widths[curCol] * this.width / sum,this.fontSize,
							text, x0, y0);
				}

				if ((isNewPage && this.lineNumber > 0 && i + 1 >= this.lineNumber)
						|| ((!isNewPage || this.lineNumber <= 0) && i == this.scores.length - 1)) {
					
					this.contentByte.setLineWidth(0.5f);
					this.moveLine(this.contentByte, x0 - this.widths[curCol-1] * this.width / sum,
												y0 - this.realRowHeight[i], x0, y0 - this.realRowHeight[i]);
					
					if(this.widths.length-1==curCol){
						this.moveLine(this.contentByte, x0,
								y0 - this.realRowHeight[i], x0+this.widths[curCol] * this.width / sum,
								y0 - this.realRowHeight[i]);
					}
					
				}
			}
			y0 -= this.realRowHeight[i];
			x0 = x;
		}
		drawTextAndScore(y, colSpan, sum,borderColor);
		calPositionY(y);
	}

	/**
	 * 画出填充的区域
	 * @param y0
	 * @param x0
	 * @param sepWidth
	 * @param isNewPage
	 */
	private void drawArea(float y0,float x0,float sepWidth,boolean... isNewPage){
		if(this.lineNumber>0&&this.lineNumber!=this.scores.length
				&&isNewPage.length<1){
			y0-=this.realRowHeight[this.lineNumber];
		}else{
			y0-=this.realRowHeight[0];
		}
		
		float[] rowScores=null;
		for(int i=this.lineNumber>0&&this.lineNumber!=this.scores.length
					&&isNewPage.length<1?
					this.lineNumber+1:1,l=this.scores.length;i<l;i++){
			
			rowScores=this.scores[i-1];
			
			this.fillAreaGraph.setFirst_x(x0+sepWidth*(rowScores[this.maxScoreColNum]-this.scoreLevels[0]))
			.setFirst_y(y0+this.realRowHeight[i-1]/2)
			.setFourth_x(x0+sepWidth*(rowScores[this.minScoreColNum]-this.scoreLevels[0]))
			.setFourth_y(y0+this.realRowHeight[i-1]/2);
			
			rowScores=this.scores[i];
			this.fillAreaGraph.setSecond_x(x0+sepWidth*(rowScores[this.maxScoreColNum]-this.scoreLevels[0]))
			.setSecond_y(y0-this.realRowHeight[i]/2)
			.setThird_x(x0+sepWidth*(rowScores[this.minScoreColNum]-this.scoreLevels[0]))
			.setThird_y(y0-this.realRowHeight[i]/2);
			
			this.fillAreaGraph.chart();
			
			y0-=this.realRowHeight[i];
			if (isNewPage.length > 0 && isNewPage[0] && (i + 1) >= this.lineNumber) {
				break;
			}
		}
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
	private void drawTextAndScore(float y, int[] colSpan, float sum,BaseColor borderColor, boolean... isNewPage) {
		// 画出父类型名称文本
		float y0 = y - this.fontSize, cellHeight = 0, yt = y0;
		float x0 = (this.document.getPageSize().getWidth() - this.width) / 2 + this.widths[0] * this.width / sum / 2;
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);

		/**
		 * 添加各个级别的分隔线的最小单位
		 */
		float everyCell = this.width * widths[widths.length - 1] / sum;
		float sepWidth = everyCell / (this.scoreLevels[this.scoreLevels.length - 1] - this.scoreLevels[0]);
		
		int rows = 0;

		for (int i1 = 0; i1 < colSpan.length; i1++) {
			rows += colSpan[i1];
			if ((isNewPage.length < 1 || !isNewPage[0]) && this.lineNumber > 0
					&& this.lineNumber < this.realRowHeight.length && rows <= this.lineNumber) {
				continue;
			}

			cellHeight = rowHeightSum(i1, colSpan);
			this.contentByte.setLineWidth(1f);
			this.moveRect(this.contentByte, x0 - this.widths[0] * this.width / sum / 2+1f,
					i1 == 0? y0 + this.fontSize : y0 + this.fontSize, x0 + this.widths[0] * this.width / sum / 2 - 2,
					y0 - cellHeight + this.fontSize + 1.2f, this.parentTypeColor, true);

			yt -= cellHeight;
			this.contentByte.setFontAndSize(this.baseFont, this.firstColumnFontSize);
			drawMulRowText(cellHeight, this.widths[0] * this.width / sum
					,this.firstColumnFontSize, this.parentTypes[i1],
					x0 - this.widths[0] * this.width / sum / 2 + 1, y0+this.firstColumnFontSize);

			y0 = yt;

			if (isNewPage.length > 0 && isNewPage[0] && rows >= this.lineNumber) {
				break;
			}
		}
		
		rows = 0;
		
		// 开始画出各个分数段的分隔线
		y0 = y;
		
		BaseColor curveColor_=new BaseColor(this.curveColor);
		BaseColor verticalLineColor_=new BaseColor(this.verticalLineColor);
		
		x0+=this.width-this.widths[this.widths.length-1] * this.width / sum;
		x0-= this.widths[0] * this.width / sum / 2;
		
		drawArea(y0,x0,sepWidth,isNewPage);
		
		float[] rowScores=null;
		 
		for(int i=(isNewPage.length <1 ||!isNewPage[0])
				&&this.lineNumber<this.scores.length?
				this.lineNumber:0,l=this.scores.length;i<l;i++){
			
			rowScores=this.scores[i];
			y0-=this.realRowHeight[i];
			
			//连接线
			if(i>0&&i!=this.lineNumber){
				this.contentByte.setColorStroke(curveColor_);
				this.moveLine(this.contentByte,
						x0+sepWidth*(this.scores[i-1][this.curScoreColNum]-this.scoreLevels[0]),
						y0+this.realRowHeight[i]+this.realRowHeight[i-1]/2,
						x0+sepWidth*(rowScores[this.curScoreColNum]-this.scoreLevels[0]),
						y0+this.realRowHeight[i]/2);
			}
			
			//表格的右边的边框
			this.contentByte.setColorStroke(borderColor);
			this.contentByte.setLineWidth(0.5f);
			this.moveLine(this.contentByte,
					x0+everyCell, y0,
					x0+everyCell,
					y0+this.realRowHeight[i]);
			
			//当前点的垂直线
			this.contentByte.setColorStroke(verticalLineColor_);
			this.contentByte.setLineWidth(1f);
			this.moveLine(this.contentByte, x0, y0+this.realRowHeight[i]/2,
					x0+sepWidth*(rowScores[this.curScoreColNum]-this.scoreLevels[0]),
					y0+this.realRowHeight[i]/2);
			
			//画出点的圆心
			this.contentByte.setColorStroke(curveColor_);
			this.moveCircle(this.contentByte, 
					x0+sepWidth*(rowScores[this.curScoreColNum]-this.scoreLevels[0]),
					y0+this.realRowHeight[i]/2, 2.5f, true);
			
			if (isNewPage.length > 0 && isNewPage[0] && (i + 1) >= this.lineNumber) {
				break;
			}
		}
	}

	/**
	 * 添加表格的表头信息
	 * @throws Exception
	 */
	private void addTableHead(float sum) throws Exception {
		PdfPTable headTable = new PdfPTable(this.widths.length);
		headTable.setTotalWidth(this.width);
		headTable.setLockedWidth(true);
		
		headTable.setWidths(this.widths);
		PdfPCell cell = null;
		BaseColor baseColor_=new BaseColor(this.tableHeadColor);
		BaseColor tableHeadFontColor_=new BaseColor(this.tableHeadFontColor);
		float rowHeight=calTableHeadRowHeight(sum);

		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < this.tableHeads.length; i++) {
				if (k == 0 && i != this.tableHeads.length - 1) {
					cell = this.addOneCell(new Font(this.baseFont, 
							this.tableHeadFontSize, Font.NORMAL,tableHeadFontColor_),
							this.tableHeads[i], rowHeight,
							baseColor_, true);
				} else {
					cell = this.addOneCell(new Font(this.baseFont, 
							this.tableHeadFontSize, Font.NORMAL, BaseColor.BLACK), "", 0,
							baseColor_, false);
				}

				cell.setBorderColor(BaseColor.WHITE);
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
		this.contentByte.setColorFill(tableHeadFontColor_);
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
		extendHeight =calTextWidth(this.fontSize, str)/ everyCell;
		extendHeight = extendHeight > 1 ? (float) Math.ceil(extendHeight) : 1;
		return extendHeight * this.lineHeight;
	}

	/**
	 * 计算出表头的行高
	 * @return
	 */
	private float calTableHeadRowHeight(float colWidth){
		float sum=0,width=0;
		for(int i=0,len=this.tableHeads.length;i<len;i++){
			width=this.width*this.widths[i]/colWidth;
			sum=Math.max(sum, 
					this.calTextWidth(this.tableHeadFontSize, this.tableHeads[i])/width);
		}
		return ((int)Math.ceil(sum))*this.tableHeadFontSize+this.lineHeight;
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
			cell.setPaddingTop(0);// 设置上边距
			cell.setPaddingBottom(3);// 设置下边距
			cell.setFixedHeight(height);
		} else {
			cell.setPaddingTop(0);// 设置上边距
			cell.setPaddingBottom(0);// 设置下边距
			cell.setFixedHeight(height);
		}
		cell.setNoWrap(false);
		return cell;
	}

	/**
	 * 表格的宽度
	 * @param width
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 表头字体颜色
	 * @param tableHeadFontColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setTableHeadFontColor(int tableHeadFontColor) {
		this.tableHeadFontColor = tableHeadFontColor;
		return this;
	}

	/**
	 * 表格表头信息
	 * @param tableHeads
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setTableHeads(String[] tableHeads) {
		this.tableHeads = tableHeads;
		return this;
	}

	/**
	 * 分数的刻度值
	 * @param scoreLevels
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setScoreLevels(int[] scoreLevels) {
		this.scoreLevels = scoreLevels;
		return this;
	}

	/**
	 * 表头背景颜色
	 * @param tableHeadColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setTableHeadColor(int tableHeadColor) {
		this.tableHeadColor = tableHeadColor;
		return this;
	}

	/**
	 * 父类名称
	 * @param parentTypes
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setParentTypes(String[] parentTypes) {
		this.parentTypes = parentTypes;
		return this;
	}

	/**
	 *  父类背景颜色
	 * @param parentTypeColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setParentTypeColor(int parentTypeColor) {
		this.parentTypeColor = parentTypeColor;
		return this;
	}

	/**
	 *  子类分组及每一行名称
	 * @param childrenTypes
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setChildrenTypes(List<String[]> childrenTypes) {
		this.childrenTypes = childrenTypes;
		return this;
	}

	/**
	 * 每一行的每列的分数
	 * @param scores
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setScores(float[][] scores) {
		this.scores = scores;
		return this;
	}

	/**
	 * 行的交替颜色
	 * @param rowColors
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	/**
	 *  折现的颜色
	 * @param curveColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setCurveColor(int curveColor) {
		this.curveColor = curveColor;
		return this;
	}

	/**
	 * 每一行的高度
	 * @param lineHeight
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	/**
	 *  除了刻线之外的文字大小
	 * @param fontSize
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 表格每一列的宽度
	 * @param widths
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setWidths(float[] widths) {
		this.widths = widths;
		return this;
	}

	/**
	 * 刻度文本的字体大小
	 * @param levelFontSize
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setLevelFontSize(float levelFontSize) {
		this.levelFontSize = levelFontSize;
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
	 * 字体的颜色
	 * @param fontColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 表格边框的颜色
	 * @param borderColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * 垂直线的颜色
	 * @param verticalLineColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setVerticalLineColor(int verticalLineColor) {
		this.verticalLineColor = verticalLineColor;
		return this;
	}

	/**
	 * 垂直线所在区域的颜色
	 * @param verticalAreaColor
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setVerticalAreaColor(int verticalAreaColor) {
		this.verticalAreaColor = verticalAreaColor;
		return this;
	}

	/**
	 * 级别颜色标注框与表格间的空隙
	 * @param gradeRectangeGap
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setGradeRectangeGap(float gradeRectangeGap) {
		this.gradeRectangeGap = gradeRectangeGap;
		return this;
	}

	/**
	 * 表头字体大小
	 * @param tableHeadFontSize
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setTableHeadFontSize(float tableHeadFontSize) {
		this.tableHeadFontSize = tableHeadFontSize;
		return this;
	}

	/**
	 * 第一列字体的大小
	 * @param firstColumnFontSize
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setFirstColumnFontSize(float firstColumnFontSize) {
		this.firstColumnFontSize = firstColumnFontSize;
		return this;
	}

	/**
	 * 最大分数的列
	 * @param maxScoreColNum
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setMaxScoreColNum(int maxScoreColNum) {
		this.maxScoreColNum = maxScoreColNum;
		return this;
	}

	/**
	 * 最小分数的列
	 * @param minScoreColNum
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setMinScoreColNum(int minScoreColNum) {
		this.minScoreColNum = minScoreColNum;
		return this;
	}

	/**
	 * 分数的列
	 * @param curScoreColNum
	 * @return TableChartedPositionAreaCurveScoresChart
	 */
	public TableChartedPositionAreaCurveScoresChart setCurScoreColNum(int curScoreColNum) {
		this.curScoreColNum = curScoreColNum;
		return this;
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
