package com.xianghy.itextpdf.tools.base;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author cheny
 */
public abstract class AbstractBaseChart {

	/**
	 * 换行
	 * @param num
	 * @param doc
	 * @throws DocumentException
	 */
	public void setLine(int num, Document doc) throws DocumentException {
		for (int i = 0; i < num; i++) {
			doc.add(new Paragraph(" "));
		}
	}

	/**
	 * 指定的字体大小换行
	 * @param num
	 * @param fontSize
	 * @param doc
	 * @throws DocumentException
	 */
	public void setLine(int num, float fontSize, Document doc) throws DocumentException {
		Font font = new Font(Font.FontFamily.COURIER, fontSize);
		for (int i = 0; i < num; i++) {
			doc.add(new Paragraph(" ", font));
		}
	}

	/**
	 * @param table
	 * @param pFont
	 * @param fontSize
	 * @param fontStyle
	 * @param fontColor
	 * @param str
	 * @param backgroundColor
	 * @param num
	 * @param height
	 * @param hasBorder
	 */
	public void addCell(PdfPTable table, BaseFont pFont, float fontSize, int fontStyle, int fontColor, String str,
			BaseColor backgroundColor, int num, float height, boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(
				new Paragraph(str, new Font(pFont, fontSize, fontStyle, new BaseColor(fontColor))));
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderWidth(PdfPCell.NO_BORDER);// 表格边框为0
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	/**
	 * @param table
	 * @param pFont
	 * @param str
	 * @param backgroundColor
	 * @param num
	 * @param height
	 * @param hasBorder
	 */
	public void addCell(PdfPTable table, Font pFont, String str, BaseColor backgroundColor, int num, float height,
			boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(new Paragraph(str, pFont));
		pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderWidth(PdfPCell.NO_BORDER);// 表格边框为0
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	/**
	 * 边框与背景色相同的
	 * @param table
	 * @param pFont
	 * @param str
	 * @param backgroundColor
	 * @param num
	 * @param height
	 * @param hasBorder
	 */
	public void addBorderColorCell(PdfPTable table, Font pFont, String str, BaseColor backgroundColor, int num,
			float height,Integer align, boolean... hasBorder) {
		PdfPCell pdfPCell = new PdfPCell(new Paragraph(str, pFont));
		pdfPCell.setHorizontalAlignment(ObjectUtils.equals(null, align)?Element.ALIGN_CENTER:align);// 水平居中Element.ALIGN_CENTER
		pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 垂直居中
		pdfPCell.setBackgroundColor(backgroundColor);
		pdfPCell.setFixedHeight(height);
		if (hasBorder.length > 0 && !hasBorder[0])
			pdfPCell.setBorderColor(backgroundColor);
		pdfPCell.setRowspan(num);
		table.addCell(pdfPCell);
	}

	/**
	 * @param pFont
	 * @param str
	 * @param height
	 * @param borderWidth
	 * @param backColor
	 * @return PdfPCell
	 */
	public PdfPCell addCell(Font pFont,String str, float height,float borderWidth, BaseColor backColor) {
		PdfPCell cell = new PdfPCell(new Paragraph(str, pFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 定义水平方向
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);// 定义垂直方向
		cell.setBackgroundColor(backColor);// 背景颜色
		cell.setPaddingTop(6);// 设置上边距
		cell.setPaddingBottom(6);// 设置下边距
		cell.setFixedHeight(height);
		cell.setBorderColor(BaseColor.BLACK);// 设置线的颜色
		cell.setBorderWidth(borderWidth);
		return cell;
	}

	/**
	 * @param cb
	 * @param text
	 * @param x1
	 * @param y1
	 * @param align
	 * @param rotation
	 */
	public void moveText(PdfContentByte cb, String text, float x1, float y1, int align, float rotation) {
		cb.beginText();
		cb.showTextAligned(align, text, x1, y1, rotation);
		cb.endText();
	}

	/**
	 * @param cb
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void moveLine(PdfContentByte cb, float x1, float y1, float x2, float y2) {
		cb.moveTo(x1, y1);
		cb.lineTo(x2, y2);
		cb.stroke();
	}

	/**
	 * @param cb
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 * @param hasBorder
	 */
	public void moveRect(PdfContentByte cb, float x1, float y1, float x2, float y2, int color, boolean... hasBorder) {
		Rectangle rect = new Rectangle(x1, y1, x2, y2);
		if (hasBorder.length > 1 && hasBorder[0])
			rect.setBorder(1);// 有边框
		rect.setBackgroundColor(new BaseColor(color));
		cb.rectangle(rect);
	}

	/**
	 * 圆角矩形
	 * @param cb
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void moveRoundRect(PdfContentByte cb, float x, float y, float w, float h, boolean... isFill) {
		cb.roundRectangle(x, y, w, h, h / 2);
		if (isFill.length < 1 || isFill[0])
			cb.fillStroke();
		else
			cb.stroke();
	}

	/**
	 * 有一定弧度的矩形
	 * @param cb
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param radian
	 * @param isFill
	 */
	public void moveRoundRect(PdfContentByte cb, float x, float y, float w, 
								float h,float radian, boolean... isFill) {
		cb.roundRectangle(x, y, w, h,radian);
		if (isFill.length < 1 || isFill[0])
			cb.fillStroke();
		else
			cb.stroke();
	}
	
	/**
	 * 填充圆形
	 * @param cb
	 * @param x1
	 * @param y1
	 * @param r
	 */
	public void moveCircle(PdfContentByte cb, float x1, float y1, float r, boolean... isFill) {
		cb.circle(x1, y1, r);
		if (isFill.length < 1 || isFill[0])
			cb.fillStroke();
		else
			cb.stroke();
	}
	
	/**
	 *显示窄的文本:主要是含有数字的文本
	 * 根据字体的大小，计算一段文本的所占用的宽度
	 * @param fontSize
	 * @param text
	 * @return float
	 */
	public float calTextWidth(float fontSize,String text){
		float width=0;
		for(int i=0,len=text.length();i<len;i++){
			if(text.charAt(i)<255){
				width+=fontSize/2;
			}else{
				width+=fontSize;
			}
		}
		return width;
	}
	
	/**
	 *  显示窄的文本:主要是含有数字的文本
	 *  根据文本的长度和指定的绘画区域的宽度，换行或不换行显示出文本，在根据行高来文本垂直居中
	 * 根据宽度水平居中;如果要将文本纵向显示，请自行计算X(文字居中的时候且仅仅显示一个字)
	 * ，width指定为字体大小即可
	 * @param cb
	 * @param text
	 * @param fontSize
	 * @param width
	 * @param lineHeight
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public void moveMultiLineText(PdfContentByte cb,String text,float fontSize,
											float width,float lineHeight, float x, float y,int rotation){
		float textTotalWidth=this.calTextWidth(fontSize, text);
		
		int lines=(int)Math.ceil(textTotalWidth/width);//根据文字宽度和区域宽度，计算出行数
		int everyLength=text.length()/lines;//在多行的情况下，平均分配每段文本的长度
		String textTemp=null;
		
		float x0=0,y0=y-(lineHeight-lines*fontSize)/2-fontSize*4.3f/5;
		for(int i=0;i<lines;i++){
			textTemp=text.substring(i*everyLength,i!=lines-1?(i+1)*everyLength:text.length());
			x0=x+(width-this.calTextWidth(fontSize, textTemp))/2;
			this.moveText(cb,textTemp, x0, y0, Element.ALIGN_LEFT, rotation);
			y0-=fontSize;
		}
	}
	
	/**
	 * 主要是汉字
	 * 通过即将显示的文本、宽度、字体和固定行高，计算出文字多行显示的高度 
	 * @param text
	 * @param fontSize
	 * @param width
	 * @param lineHeight
	 * @return float
	 */
	public float calRealHeight(String text,float fontSize,float width,float lineHeight){
		int everyLength=(int)(width/fontSize);//在多行的情况下，平均分配每段文本的长度
		int lines=(int)Math.ceil(text.length()/everyLength);//根据文字宽度和区域宽度，计算出行数
		lines=text.length()%everyLength==0?lines:lines+1;
		return lines*fontSize+lineHeight;
	}
	
	/**
	 * 主要是汉字
	 *  根据文本的长度和指定的绘画区域的宽度，换行或不换行显示出文本，在根据行高来文本垂直居中
	 * 根据宽度水平居中;如果要将文本纵向显示，请自行计算X(文字居中的时候且仅仅显示一个字)
	 * ，width指定为字体大小即可
	 * @param cb
	 * @param text
	 * @param fontSize
	 * @param width
	 * @param lineHeight
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public void moveMultiLineWText(PdfContentByte cb,String text,float fontSize,
											float width,float lineHeight, float x, float y,int rotation){
		int everyLength=(int)(width/fontSize);//在多行的情况下，平均分配每段文本的长度
		int lines=(int)Math.ceil(text.length()/everyLength);//根据文字宽度和区域宽度，计算出行数
		lines=text.length()%everyLength==0?lines:lines+1;
		String textTemp=null;
		
		float x0=0,y0=y-(lineHeight-lines*fontSize)/2-fontSize*4.3f/5;
		for(int i=0;i<lines;i++){
			textTemp=text.substring(i*everyLength,i!=lines-1?(i+1)*everyLength:text.length());
			x0=x+(width-this.calTextWidth(fontSize, textTemp))/2;
			this.moveText(cb,textTemp, x0, y0, Element.ALIGN_LEFT, rotation);
			y0-=fontSize;
		}
	}
}
