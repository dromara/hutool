/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.poi.excel.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.Serializable;

/**
 * 单元格边框样式和颜色封装，边框按照“上右下左”的顺序定义，与CSS一致
 *
 * @author Looly
 * @since 6.0.0
 */
public class CellBorderStyle implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建单元格边框样式对象，四边框样式保持一致。
	 *
	 * @param borderStyle 边框样式
	 * @param colorIndex  颜色
	 * @return 单元格边框样式对象
	 */
	public static CellBorderStyle of(final BorderStyle borderStyle, final IndexedColors colorIndex) {
		return new CellBorderStyle()
			.setTopStyle(borderStyle)
			.setTopColor(colorIndex.getIndex())
			.setRightStyle(borderStyle)
			.setRightColor(colorIndex.getIndex())
			.setBottomStyle(borderStyle)
			.setBottomColor(colorIndex.getIndex())
			.setLeftStyle(borderStyle)
			.setLeftColor(colorIndex.getIndex());
	}

	private BorderStyle topStyle;
	private Short topColor;
	private BorderStyle rightStyle;
	private Short rightColor;
	private BorderStyle bottomStyle;
	private Short bottomColor;
	private BorderStyle leftStyle;
	private Short leftColor;

	/**
	 * 获取上边框的样式。
	 *
	 * @return 上边框的样式。
	 */
	public BorderStyle getTopStyle() {
		return topStyle;
	}

	/**
	 * 设置上边框的样式。
	 *
	 * @param topStyle 上边框的样式。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setTopStyle(final BorderStyle topStyle) {
		this.topStyle = topStyle;
		return this;
	}

	/**
	 * 获取上边框的颜色。
	 *
	 * @return 上边框的颜色。
	 */
	public Short getTopColor() {
		return topColor;
	}

	/**
	 * 设置上边框的颜色。
	 *
	 * @param topColor 上边框的颜色。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setTopColor(final Short topColor) {
		this.topColor = topColor;
		return this;
	}

	/**
	 * 获取右边框的样式。
	 *
	 * @return 右边框的样式。
	 */
	public BorderStyle getRightStyle() {
		return rightStyle;
	}

	/**
	 * 设置右边框的样式。
	 *
	 * @param rightStyle 右边框的样式。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setRightStyle(final BorderStyle rightStyle) {
		this.rightStyle = rightStyle;
		return this;
	}

	/**
	 * 获取右边框的颜色。
	 *
	 * @return 右边框的颜色。
	 */
	public Short getRightColor() {
		return rightColor;
	}

	/**
	 * 设置右边框的颜色。
	 *
	 * @param rightColor 右边框的颜色。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setRightColor(final Short rightColor) {
		this.rightColor = rightColor;
		return this;
	}

	/**
	 * 获取底边框的样式。
	 *
	 * @return 底边框的样式。
	 */
	public BorderStyle getBottomStyle() {
		return bottomStyle;
	}

	/**
	 * 设置底边框的样式。
	 *
	 * @param bottomStyle 底边框的样式。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setBottomStyle(final BorderStyle bottomStyle) {
		this.bottomStyle = bottomStyle;
		return this;
	}

	/**
	 * 获取底边框的颜色。
	 *
	 * @return 底边框的颜色。
	 */
	public Short getBottomColor() {
		return bottomColor;
	}

	/**
	 * 设置底边框的颜色。
	 *
	 * @param bottomColor 底边框的颜色。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setBottomColor(final Short bottomColor) {
		this.bottomColor = bottomColor;
		return this;
	}

	/**
	 * 获取左边框的样式。
	 *
	 * @return 左边框的样式。
	 */
	public BorderStyle getLeftStyle() {
		return leftStyle;
	}

	/**
	 * 设置左边框的样式。
	 *
	 * @param leftStyle 左边框的样式。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setLeftStyle(final BorderStyle leftStyle) {
		this.leftStyle = leftStyle;
		return this;
	}

	/**
	 * 获取左边框的颜色。
	 *
	 * @return 左边框的颜色。
	 */
	public Short getLeftColor() {
		return leftColor;
	}

	/**
	 * 设置左边框的颜色。
	 *
	 * @param leftColor 左边框的颜色。
	 * @return 当前的单元格边框样式对象，支持链式调用。
	 */
	public CellBorderStyle setLeftColor(final Short leftColor) {
		this.leftColor = leftColor;
		return this;
	}

	/**
	 * 将边框样式和颜色设置到CellStyle中
	 *
	 * @param cellStyle CellStyle
	 * @return CellStyle
	 */
	public CellStyle setTo(final CellStyle cellStyle){
		ObjUtil.accept(this.topStyle, cellStyle::setBorderTop);
		ObjUtil.accept(this.topColor, cellStyle::setTopBorderColor);

		ObjUtil.accept(this.rightStyle, cellStyle::setBorderRight);
		ObjUtil.accept(this.rightColor, cellStyle::setRightBorderColor);

		ObjUtil.accept(this.bottomStyle, cellStyle::setBorderBottom);
		ObjUtil.accept(this.bottomColor, cellStyle::setBottomBorderColor);

		ObjUtil.accept(this.leftStyle, cellStyle::setBorderLeft);
		ObjUtil.accept(this.leftColor, cellStyle::setLeftBorderColor);

		return cellStyle;
	}
}
