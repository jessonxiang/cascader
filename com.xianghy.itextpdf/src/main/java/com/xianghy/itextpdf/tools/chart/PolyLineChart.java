package com.xianghy.itextpdf.tools.chart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.ObjectUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 分区域+多条点线  (使用的PdfTemplate+Graphics2D)
 * @author ruany
 * @see pdf.chart.LineMulCurveLineAreaTableChart
 */
public class PolyLineChart extends AbstractChart {

	/**
	 * 数据
	 */
	private List<Float[]> dataList;
	private String[] dataRowNames;//每一行数据的中文名称
	private String[] dataColNames;//每一列的中文名称
	private int[] dataColColors;//每一列的背景颜色
	private float cellHeight = 20;// 表格的高度
	
	private float fontSize=9;//表格字体大小
	private int tableBorderColor=0xCFCFCF;//表格边框的颜色
	private int[] rowColors;// 表格行的背景颜色
	private int fontColor = 0x000000;// 字体颜色
	private int scoreDescWidth =60;// 文字描述的宽度
	/**
	 * 坐标最大值
	 */
	private float maxValue = 9f;
	/**
	 * 坐标行数
	 */
	private int rownum = 9;
	/**
	 * 背景矩形色
	 */
	private Integer[] normBGColor;
	/**
	 * 线色
	 */
	private Integer[] batchColor;
	/**
	 * 模板宽
	 */
	private int templateWidth = 470;
	/**
	 * 模板高
	 */
	private int templateHeight = 200;
	/**
	 * 垂直偏移量
	 */
	private int yOffset = 50;
	/**
	 * 水平偏移量
	 */
	private int xOffset = 50;
	/**
	 * 坐标系左侧文字占位
	 */
	private int tableTitleWidth = 20;
	/**
	 * 上留白
	 */
	private int before = 5;
	/**
	 * 左边距
	 */
	private int leftBlank = 20;
	
	private float positionY;// 画完表格之后，当前所在的横坐标

	public PolyLineChart() {
		super();
	}

	public PolyLineChart(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		this.positionY = 0;
		
		if (ObjectUtils.equals(null, this.dataList) || this.dataList.isEmpty()) {
			throw new RuntimeException("请检测dataList数据是否存在！");
		}

		if (ObjectUtils.equals(null, this.normBGColor) || this.normBGColor.length < 1) {
			this.normBGColor = new Integer[] { 0xDEF5FF, 0xEEF7DF, 0xFFF5DD };
		}

		if (ObjectUtils.equals(null, this.batchColor) || this.batchColor.length < 1) {
			this.batchColor = new Integer[] { 0x9792DD, 0xFFCE54, 0x72CCAD, 0xE57D54, 0x89CFFF };
		}
		
		if (ObjectUtils.equals(null, this.dataColColors)) {
			this.dataColColors = new int[] { 0x59CFFF, 0xA9D961, 0xFFCE54};
		}
		
		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors =new int[] { 0xFFFFFF, 0xF2F2F2 };
		}
		
		if (ObjectUtils.equals(null, this.dataRowNames)) {
			this.dataRowNames = new String[] { "第一批次", "第二批次", "第三批次","第四批次","第五批次" };
		}

		// 图形格宽
		int chartColWidth = (this.templateWidth - this.tableTitleWidth) / this.normBGColor.length;
		// 模板宽
		this.templateWidth = this.tableTitleWidth + chartColWidth * this.normBGColor.length;
		// 图形行高
		int chartRowHeight = (this.templateHeight - this.before) / (this.rownum);
		// 图形高
		int chartHeight = chartRowHeight * this.rownum;
		// 模板高
		this.templateHeight = chartHeight + this.before;

		FontMapper fm = new AsianFontMapper(AsianFontMapper.ChineseSimplifiedFont,
				AsianFontMapper.ChineseSimplifiedEncoding_H);
		PdfTemplate template = contentByte.createTemplate(this.templateWidth, this.templateHeight); // 绘制图形模板.
		// 模板位置 ,这个地方的 y 是从下面计算的（比较特殊）
		this.contentByte.addTemplate(template, this.xOffset, this.document.getPageSize().getHeight() - this.templateHeight -this.yOffset);
		Graphics2D g2d = template.createGraphics(this.templateWidth, this.templateHeight, fm); // 绘图接口，继承自Graphics2D

		// 画矩形底色
		for (int j = 0; j < this.normBGColor.length; j++) {
			g2d.setColor(new Color(this.normBGColor[j]));
			g2d.fillRect(this.tableTitleWidth + j * chartColWidth, this.before, chartColWidth, chartHeight);
		}

		Stroke dash = new BasicStroke(1f, BasicStroke.CAP_BUTT, 
				BasicStroke.JOIN_ROUND, 1f,
				 // 线长
				new float[] { 4, 3, 1, 3 },0f);
		// 画横线 画10根线 和 左边的参数值ֵ
		for (int i = 0; i < this.rownum + 1; i++) {
			// 虚线
			g2d.setColor(Color.GRAY);
			// 设置虚线样式
			
			g2d.setStroke(dash);
			if (i < this.rownum)
				g2d.drawLine(this.tableTitleWidth, before + i * chartRowHeight, this.templateWidth, this.before + i * chartRowHeight);
			// 左边的参数值
			g2d.setColor(Color.GRAY);
			if (i < this.rownum) {
				g2d.drawString((this.maxValue - i * (this.maxValue / this.rownum)) + "", this.tableTitleWidth - this.leftBlank,
						this.before + i * chartRowHeight + 4);
			} else {
				g2d.drawString((this.maxValue - i * (this.maxValue / this.rownum)) + "", this.tableTitleWidth - this.leftBlank,
						this.before + i * chartRowHeight);
			}
		}

		// 画左边的竖线
		dash = new BasicStroke(1);
		g2d.setStroke(dash);
		g2d.setColor(Color.GRAY);
		g2d.drawLine(this.tableTitleWidth, this.before, this.tableTitleWidth, this.before + chartHeight);

		// 数据操作
		Float[] datas;
		float score;
		int x;
		int y;
		List<List<int[]>> dots = new ArrayList<List<int[]>>();
		List<int[]> dot;
		for (int i = 0; i < this.dataList.size(); i++) {
			dot = new ArrayList<int[]>();
			dots.add(dot);
			datas = this.dataList.get(i);
			for (int j = 0; j < datas.length; j++) {
				score = datas[j];
				// 画数据圆点
				g2d.setColor(new Color(this.batchColor[i]));
				x = this.tableTitleWidth + j * chartColWidth + chartColWidth / 2;
				y = this.before + (int) ((this.maxValue - score) / this.maxValue * chartHeight);
				g2d.fillOval(x - 2, y - 2, 4, 4);
				g2d.setColor(Color.WHITE);
				g2d.fillOval(x - 1, y - 1, 2, 2);
				dot.add(new int[] { x, y });
			}
		}
		// 画折线
		for (int i = 0; i < dots.size(); i++) {
			g2d.setColor(new Color(this.batchColor[i]));
			dot = dots.get(i);
			for (int j = 0; j < dot.size() - 1; j++) {
				g2d.drawLine(dot.get(j)[0] + 1, dot.get(j)[1], dot.get(j + 1)[0] - 1, dot.get(j + 1)[1]);
			}
		}
		g2d.dispose();
		
		drawTable();
	}
	
	/**
	 * 计算出表格的头部宽度
	 * @param width
	 * @return
	 */
	private float calTableHeadHeight(float width) {
		float sum = 0;
		for (int i = 0; i < this.dataColNames.length; i++) {
			sum = Math.max(sum, this.dataColNames[i].length());
		}
		sum = sum * this.fontSize % width == 0 ? sum * this.fontSize / width
				: (float) (Math.floor(sum * this.fontSize / width) + 1);
		return (sum - 1) * this.fontSize + this.cellHeight;
	}
	
	/**
	 * 画出表格部分
	 */
	private void drawTable(){
		float curY=getPositionY()+5;
		float sepWidth=(this.templateWidth - this.tableTitleWidth) / this.normBGColor.length;
		float temp = calTableHeadHeight(sepWidth);
		float x0=this.xOffset+this.tableTitleWidth;
		
		BaseColor tabelBorderColor=new BaseColor(this.tableBorderColor);
		this.contentByte.setFontAndSize(this.baseFont, this.fontSize);
		
		//画出表头信息
		for(int i=0,len=this.dataColNames.length;i<len;i++){
			this.moveRect(this.contentByte,x0, curY, x0+sepWidth, curY-temp,this.dataColColors[i]);
			this.contentByte.setColorFill(BaseColor.WHITE);
			this.moveMultiLineText(this.contentByte, this.dataColNames[i], this.fontSize,
							sepWidth, temp, x0, curY, 0);	
			
			this.contentByte.setColorFill(tabelBorderColor);
			this.contentByte.setColorStroke(tabelBorderColor);
			this.moveLine(this.contentByte, x0+sepWidth, curY,
					x0+sepWidth, curY-this.cellHeight+ 1);
			
			x0+=sepWidth;
		}
		
		this.positionY=temp;
		curY-=temp;
		BaseColor fontColor_=new BaseColor(this.fontColor);
		BaseColor[] lineColors_=new BaseColor[this.batchColor.length];
		
		for(int k=0,len=this.batchColor.length;k<len;k++){
			lineColors_[k]=new BaseColor(this.batchColor[k]);
		}
		
		x0 =this.xOffset+this.tableTitleWidth;
		this.contentByte.setColorStroke(tabelBorderColor);
		this.moveLine(this.contentByte, x0, curY,x0, curY +this.cellHeight + 1);
		temp=0;
		
		for (int i = 0,len=this.dataList.size(); i <len; i++) {
			// 开始画出分数
			for (int j = 0,l= this.dataList.get(i).length; j <l; j++) {

				this.contentByte.setColorStroke(tabelBorderColor);
				this.contentByte.setColorFill(tabelBorderColor);
				this.moveRect(this.contentByte, x0, curY, x0 + sepWidth, curY - this.cellHeight + 1,
						this.rowColors[i % 2]);
				this.moveLine(this.contentByte, x0 + sepWidth, curY,
																	x0 + sepWidth, curY - this.cellHeight+1);

				this.contentByte.setColorFill(fontColor_);
				this.moveMultiLineText(this.contentByte, this.dataList.get(i)[j] + "", this.fontSize,
						sepWidth, this.cellHeight, x0, curY, 0);	

				if(i==len-1)
					this.moveLine(this.contentByte, x0, curY - this.cellHeight + 1,
							x0 + sepWidth,curY - this.cellHeight + 1);
				
				x0 += sepWidth;
			}
			x0 =this.xOffset+this.tableTitleWidth;
			this.moveRect(this.contentByte, x0 - 1, curY, x0 - 1 - this.scoreDescWidth, curY - this.cellHeight + 1,
					this.rowColors[i % 2]);
			
			this.contentByte.setColorStroke(tabelBorderColor);
			this.moveLine(this.contentByte, x0 - 1, curY,x0 - 1, curY - this.cellHeight + 1);
			this.moveLine(this.contentByte, x0 - 1 - this.scoreDescWidth, curY,
													x0 - 1 - this.scoreDescWidth, curY - this.cellHeight + 1);
			if(i == 0)
				this.moveLine(this.contentByte, x0 - 1, curY,
												x0 - 1 - this.scoreDescWidth,curY);
			if(i==len-1)
				this.moveLine(this.contentByte, x0 - 1, curY - this.cellHeight + 1,
						x0 - 1 - this.scoreDescWidth,curY - this.cellHeight + 1);
			
			this.contentByte.setColorFill(fontColor_);
			temp=this.scoreDescWidth*4f/5;
			this.moveMultiLineText(this.contentByte, this.dataRowNames[i], this.fontSize,
					temp, this.cellHeight, x0 - 1 - temp, curY, 0);	
			temp=(temp-this.calTextWidth(this.fontSize, this.dataRowNames[i]))/2;
			temp=(this.scoreDescWidth/5+temp)/2;
			
			this.contentByte.setColorStroke(lineColors_[i]);
			this.drawLineCircle(x0-1- this.scoreDescWidth+temp,
					curY-this.cellHeight/2,2,false);
			
			curY -= this.cellHeight;
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
	 * 数据
	 * @param dataList
	 * @return PolyLineChart
	 */
	public PolyLineChart setDataList(List<Float[]> dataList) {
		this.dataList = dataList;
		return this;
	}

	/**
	 * 坐标最大值
	 * @param maxValue
	 * @return PolyLineChart
	 */
	public PolyLineChart setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	/**
	 * 坐标行数
	 * @param rownum
	 * @return PolyLineChart
	 */
	public PolyLineChart setRownum(int rownum) {
		this.rownum = rownum;
		return this;
	}

	/**
	 * 背景矩形色
	 * @param normBGColor
	 * @return PolyLineChart
	 */
	public PolyLineChart setNormBGColor(Integer[] normBGColor) {
		this.normBGColor = normBGColor;
		return this;
	}

	/**
	 * 线色
	 * @param batchColor
	 * @return PolyLineChart
	 */
	public PolyLineChart setBatchColor(Integer[] batchColor) {
		this.batchColor = batchColor;
		return this;
	}

	/**
	 * 模板宽
	 * @param templateWidth
	 * @return PolyLineChart
	 */
	public PolyLineChart setTemplateWidth(int templateWidth) {
		this.templateWidth = templateWidth;
		return this;
	}

	/**
	 * 模板高
	 * @param templateHeight
	 * @return PolyLineChart
	 */
	public PolyLineChart setTemplateHeight(int templateHeight) {
		this.templateHeight = templateHeight;
		return this;
	}

	/**
	 * 垂直偏移量
	 * @param yOffset
	 * @return PolyLineChart
	 */
	public PolyLineChart setyOffset(int yOffset) {
		this.yOffset = yOffset;
		return this;
	}

	/**
	 * 水平偏移量
	 * @param xOffset
	 * @return PolyLineChart
	 */
	public PolyLineChart setxOffset(int xOffset) {
		this.xOffset = xOffset;
		return this;
	}

	/**
	 * 坐标系左侧文字占位
	 * @param tableTitleWidth
	 * @return PolyLineChart
	 */
	public PolyLineChart setTableTitleWidth(int tableTitleWidth) {
		this.tableTitleWidth = tableTitleWidth;
		return this;
	}

	/**
	 * 上留白
	 * @param before
	 * @return PolyLineChart
	 */
	public PolyLineChart setBefore(int before) {
		this.before = before;
		return this;
	}

	/**
	 * 左边距
	 * @param leftBlank
	 * @return PolyLineChart
	 */
	public PolyLineChart setLeftBlank(int leftBlank) {
		this.leftBlank = leftBlank;
		return this;
	}
	
	/**
	 * 每一行数据的中文名称
	 * @param dataRowNames
	 * @return PolyLineChart
	 */
	public PolyLineChart setDataRowNames(String[] dataRowNames) {
		this.dataRowNames = dataRowNames;
		return this;
	}

	/**
	 * 每一列的中文名称
	 * @param dataColNames
	 * @return PolyLineChart
	 */
	public PolyLineChart setDataColNames(String[] dataColNames) {
		this.dataColNames = dataColNames;
		return this;
	}

	/**
	 * 每一列的背景颜色
	 * @param dataColColors
	 * @return PolyLineChart
	 */
	public PolyLineChart setDataColColors(int[] dataColColors) {
		this.dataColColors = dataColColors;
		return this;
	}

	/**
	 * 表格的高度
	 * @param cellHeight
	 * @return PolyLineChart
	 */
	public PolyLineChart setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
		return this;
	}

	/**
	 * 字体大小
	 * @param fontSize
	 * @return PolyLineChart
	 */
	public PolyLineChart setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 表格边框的颜色
	 * @param tableBorderColor
	 * @return PolyLineChart
	 */
	public PolyLineChart setTableBorderColor(int tableBorderColor) {
		this.tableBorderColor = tableBorderColor;
		return this;
	}

	/**
	 * 表格行的背景颜色
	 * @param rowColors
	 * @return PolyLineChart
	 */
	public PolyLineChart setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	/**
	 * 字体颜色
	 * @param fontColor
	 * @return PolyLineChart
	 */
	public PolyLineChart setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 文字描述的宽度
	 * @param scoreDescWidth
	 * @return PolyLineChart
	 */
	public PolyLineChart setScoreDescWidth(int scoreDescWidth) {
		this.scoreDescWidth = scoreDescWidth;
		return this;
	}

	/**
	 * 画完表格之后，当前所在的横坐标
	 * @return float
	 */
	public float getPositionY() {
		this.positionY=this.document.getPageSize().getHeight()-this.positionY;
		this.positionY-=this.templateHeight;
		this.positionY-=this.yOffset;
		return this.positionY;
	}
}
