package com.xianghy.itextpdf.tools.table;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.xianghy.itextpdf.tools.chart.AbstractChart;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * 可以指定每一行、每一列的背景颜色【行颜色优先，否则列颜色】 指定边框的颜色 字体的颜色
 * 
 * @author cheny
 */
public class TableCommonTable extends AbstractChart {

	private float width = 500;
	// 每一行的数据
	private List<Object[]> rowDatas;
	private int borderColor = 0x1B1B1B;// 表格边框的颜色
	private float rowHeight =10;// 每一行的高度
	private float[] widths;// 每一列的宽度

	private Integer firstColumnColors;// 除了第一行的列,第一列背景颜色
	private int firstColumnFontColor = 0x000000;// 除了第一行的列,第一列文字颜色
	private int[] rowColors;// 除了第一行，行的交替背景颜色
	private float fontSize = 12;// 除了第一行，字体大小
	
	private int otherColumnFontColor=0x000000;// 除了第一列,其它文字颜色
	private int otherColumnAlign=Element.ALIGN_CENTER;// 除了第一列,其它列位置

	private int firstRowBackgroundColor = 0x548DD4;// 第一行的背景颜色
	private int firstRowFontColor = 0xFFFFFF;// 第一行文字颜色
	private float firstRowFontSize = 12;// 第一行字体大小
	private int firstRowFontType = Font.BOLD;// 第一行字体样式

	public TableCommonTable() {
		super();
	}

	public TableCommonTable(PdfWriter writer, PdfContentByte contentByte, Document document, BaseFont baseFont) {
		super(writer, contentByte, document, baseFont);
	}

	@Override
	public void chart() {
		if (ObjectUtils.equals(null, this.rowDatas) || this.rowDatas.isEmpty())
			throw new RuntimeException("请检测rowDatas数据是否存在！");

		if (ObjectUtils.equals(null, this.rowColors)) {
			this.rowColors = new int[] {0xF2F2F2,0xFFFFFF};
		}

		if (ObjectUtils.equals(null, this.widths)) {
			this.widths = new float[] { 20, 80 };
		}

		BaseColor firstRowFontColor_ = new BaseColor(this.firstRowFontColor);
		BaseColor firstRowBackgroundColor_ = new BaseColor(this.firstRowBackgroundColor);
		BaseColor borderColor_ = new BaseColor(this.borderColor);
		BaseColor firstColumnFontColor_ = new BaseColor(this.firstColumnFontColor);
		BaseColor otherColumnFontColor_ = new BaseColor(this.otherColumnFontColor);
		BaseColor[] rowColors_ = new BaseColor[]{new BaseColor(this.rowColors[0]),new BaseColor(this.rowColors[1])};
		
		BaseColor firstColumnColors_ =null;
		if(ObjectUtils.notEqual(null, this.firstColumnColors)){
			firstColumnColors_ = new BaseColor(this.firstColumnColors);
		}

		try {
			// 设置 Table
			PdfPTable pTable = new PdfPTable(this.widths.length);
			pTable.setTotalWidth(this.width);
			pTable.setLockedWidth(true);
			pTable.setWidths(this.widths);

			PdfPCell cell = null;
			Object[] datas = null;

			for (int i = 0; i < this.rowDatas.size(); i++) {
				datas = rowDatas.get(i);
				for (int j = 0; j < datas.length; j++) {
					
					if(i==0){
						cell = this.addOneCell(datas[j].toString(), this.firstRowFontSize, firstRowFontColor_,
								firstRowBackgroundColor_, borderColor_, firstRowFontType,Element.ALIGN_CENTER);
					}else if(j==0){
						cell = this.addOneCell(datas[j].toString(), this.fontSize, firstColumnFontColor_,
								ObjectUtils.notEqual(null, firstColumnColors_)?firstColumnColors_:rowColors_[i%2]
								 , borderColor_, Font.NORMAL,Element.ALIGN_CENTER);
					}else{
						cell = this.addOneCell(datas[j].toString(), this.fontSize, otherColumnFontColor_,
								rowColors_[i%2], borderColor_, Font.NORMAL,this.otherColumnAlign);
					}
					
					pTable.addCell(cell);	
					
				}
			}
			
			this.document.add(pTable);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 为了table的各个边框颜色不同
	 * 
	 * @param str
	 * @param type
	 * @param style
	 * @param fixedHeight
	 * @param padding
	 * @return
	 */
	protected PdfPCell addOneCell(String str, float fontSize, BaseColor fontColor, BaseColor backColor,
			BaseColor borderColor, int style,int align) {

		PdfPCell cell = new PdfPCell(new Paragraph(str, new Font(this.baseFont, fontSize, style, fontColor)));
		cell.setHorizontalAlignment(align);// 水平居中Element.ALIGN_CENTER
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		cell.setBackgroundColor(backColor);// 背景颜色
		cell.setBorderColor(borderColor);

		cell.setPaddingTop(this.rowHeight);// 设置上边距
		cell.setPaddingBottom(this.rowHeight);// 设置下边距
		return cell;
	}

	/**
	 * 表格宽度
	 * @param width
	 * @return TableCommonTable
	 */
	public TableCommonTable setWidth(float width) {
		this.width = width;
		return this;
	}

	/**
	 * 每一行的数据
	 * @param rowDatas
	 * @return TableCommonTable
	 */
	public TableCommonTable setRowDatas(List<Object[]> rowDatas) {
		this.rowDatas = rowDatas;
		return this;
	}

	/**
	 *  除了第一行，行的交替背景颜色
	 * @param rowColors
	 * @return TableCommonTable
	 */
	public TableCommonTable setRowColors(int[] rowColors) {
		this.rowColors = rowColors;
		return this;
	}

	/**
	 * 除了第一行，字体大小
	 * @param fontSize
	 * @return TableCommonTable
	 */
	public TableCommonTable setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	/**
	 * 表格边框的颜色
	 * @param borderColor
	 * @return TableCommonTable
	 */
	public TableCommonTable setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 *  每一行的高度
	 * @param rowHeight
	 * @return TableCommonTable
	 */
	public TableCommonTable setRowHeight(float rowHeight) {
		this.rowHeight = rowHeight;
		return this;
	}

	/**
	 * 每一列的宽度
	 * @param widths
	 * @return TableCommonTable
	 */
	public TableCommonTable setWidths(float[] widths) {
		this.widths = widths;
		return this;
	}

	/**
	 *  第一行字体样式
	 * @param firstRowFontType
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstRowFontType(int firstRowFontType) {
		this.firstRowFontType = firstRowFontType;
		return this;
	}

	/**
	 * 第一行的背景颜色
	 * @param firstRowBackgroundColor
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstRowBackgroundColor(int firstRowBackgroundColor) {
		this.firstRowBackgroundColor = firstRowBackgroundColor;
		return this;
	}

	/**
	 *  第一行文字颜色
	 * @param firstRowFontColor
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstRowFontColor(int firstRowFontColor) {
		this.firstRowFontColor = firstRowFontColor;
		return this;
	}

	/**
	 *  第一行字体大小
	 * @param firstRowFontSize
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstRowFontSize(float firstRowFontSize) {
		this.firstRowFontSize = firstRowFontSize;
		return this;
	}

	/**
	 * 除了第一行的列,第一列背景颜色
	 * @param firstColumnColors
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstColumnColors(Integer firstColumnColors) {
		this.firstColumnColors = firstColumnColors;
		return this;
	}

	/**
	 * 除了第一行的列,第一列文字颜色
	 * @param firstColumnFontColor
	 * @return TableCommonTable
	 */
	public TableCommonTable setFirstColumnFontColor(int firstColumnFontColor) {
		this.firstColumnFontColor = firstColumnFontColor;
		return this;
	}

	/**
	 * 除了第一列,其它文字颜色
	 * @param otherColumnFontColor
	 * @return TableCommonTable
	 */
	public TableCommonTable setOtherColumnFontColor(int otherColumnFontColor) {
		this.otherColumnFontColor = otherColumnFontColor;
		return this;
	}

	/**
	 *  除了第一列,其它列位置
	 * @param otherColumnAlign
	 * @return TableCommonTable
	 */
	public TableCommonTable setOtherColumnAlign(int otherColumnAlign) {
		this.otherColumnAlign = otherColumnAlign;
		return this;
	}
}
