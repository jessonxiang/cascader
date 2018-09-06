package com.xianghy.itextpdf.tools.base;

import com.itextpdf.text.BaseColor;

public class TPoint {
		private float x;
		
		private float y;
		
		private float r;
		
		private BaseColor bg;
		
		public TPoint(){}

		/**
		 * @param x
		 * @param y
		 */
		public TPoint(float x, float y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		/**
		 * @param x
		 * @param y
		 * @param r
		 */
		public TPoint(float x, float y, float r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}
		
		

		/**
		 * @param x
		 * @param y
		 * @param r
		 * @param bg
		 */
		public TPoint(float x, float y, float r, BaseColor bg) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			this.bg = bg;
		}

		/**半径
		 * @return the r
		 */
		public float getR() {
			return r;
		}

		/**半径
		 * @param r the r to set
		 */
		public void setR(float r) {
			this.r = r;
		}

		/**X坐标
		 * @return the x
		 */
		public float getX() {
			return x;
		}

		/**X坐标
		 * @param x the x to set
		 */
		public void setX(float x) {
			this.x = x;
		}

		/**Y坐标
		 * @return the y
		 */
		public float getY() {
			return y;
		}

		/**Y坐标
		 * @param y the y to set
		 */
		public void setY(float y) {
			this.y = y;
		}

		/**颜色
		 * @return the bg
		 */
		public BaseColor getBg() {
			return bg;
		}

		/**颜色
		 * @param bg the bg to set
		 */
		public void setBg(BaseColor bg) {
			this.bg = bg;
		}
}
