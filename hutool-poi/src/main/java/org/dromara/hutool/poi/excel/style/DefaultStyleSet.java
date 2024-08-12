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

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 默认样式集合，定义了标题、数字、日期等默认样式
 *
 * @author Looly
 * @since 6.0.0
 */
public class DefaultStyleSet implements StyleSet, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 工作簿引用
	 */
	private final Workbook workbook;
	/**
	 * 标题样式
	 */
	private final CellStyle headCellStyle;
	/**
	 * 默认样式
	 */
	private final CellStyle cellStyle;
	/**
	 * 默认数字样式
	 */
	private final CellStyle cellStyleForNumber;
	/**
	 * 默认日期样式
	 */
	private final CellStyle cellStyleForDate;
	/**
	 * 默认链接样式
	 */
	private final CellStyle cellStyleForHyperlink;

	/**
	 * 构造
	 *
	 * @param workbook 工作簿
	 */
	public DefaultStyleSet(final Workbook workbook) {
		this.workbook = workbook;
		this.headCellStyle = StyleUtil.createHeadCellStyle(workbook);
		this.cellStyle = StyleUtil.createDefaultCellStyle(workbook);

		// 默认数字格式
		cellStyleForNumber = StyleUtil.cloneCellStyle(workbook, this.cellStyle);
		// 0表示：General
		cellStyleForNumber.setDataFormat((short) 0);

		// 默认日期格式
		this.cellStyleForDate = StyleUtil.cloneCellStyle(workbook, this.cellStyle);
		// 22表示：m/d/yy h:mm
		this.cellStyleForDate.setDataFormat((short) 22);


		// 默认链接样式
		this.cellStyleForHyperlink = StyleUtil.cloneCellStyle(workbook, this.cellStyle);
		final Font font = workbook.createFont();
		font.setUnderline((byte) 1);
		font.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
		this.cellStyleForHyperlink.setFont(font);
	}


	@Override
	public CellStyle getStyleFor(final CellReference reference, final Object cellValue, final boolean isHeader) {
		CellStyle style = null;

		if (isHeader && null != this.headCellStyle) {
			style = headCellStyle;
		} else if (null != cellStyle) {
			style = cellStyle;
		}

		if (cellValue instanceof Date
			|| cellValue instanceof TemporalAccessor
			|| cellValue instanceof Calendar) {
			// 日期单独定义格式
			if (null != this.cellStyleForDate) {
				style = this.cellStyleForDate;
			}
		} else if (cellValue instanceof Number) {
			// 数字单独定义格式
			if ((cellValue instanceof Double || cellValue instanceof Float || cellValue instanceof BigDecimal) &&
				null != this.cellStyleForNumber) {
				style = this.cellStyleForNumber;
			}
		} else if (cellValue instanceof Hyperlink) {
			// 自定义超链接样式
			if (null != this.cellStyleForHyperlink) {
				style = this.cellStyleForHyperlink;
			}
		}

		return style;
	}

	/**
	 * 获取头部样式，获取后可以定义整体头部样式
	 *
	 * @return 头部样式
	 */
	public CellStyle getHeadCellStyle() {
		return this.headCellStyle;
	}

	/**
	 * 获取常规单元格样式，获取后可以定义整体头部样式
	 *
	 * @return 常规单元格样式
	 */
	public CellStyle getCellStyle() {
		return this.cellStyle;
	}

	/**
	 * 获取数字（带小数点）单元格样式，获取后可以定义整体数字样式
	 *
	 * @return 数字（带小数点）单元格样式
	 */
	public CellStyle getCellStyleForNumber() {
		return this.cellStyleForNumber;
	}

	/**
	 * 获取日期单元格样式，获取后可以定义整体日期样式
	 *
	 * @return 日期单元格样式
	 */
	public CellStyle getCellStyleForDate() {
		return this.cellStyleForDate;
	}

	/**
	 * 获取链接单元格样式，获取后可以定义整体链接样式
	 *
	 * @return 链接单元格样式
	 * @since 5.7.13
	 */
	public CellStyle getCellStyleForHyperlink() {
		return this.cellStyleForHyperlink;
	}

	/**
	 * 定义所有单元格的边框类型
	 *
	 * @param borderSize 边框粗细{@link BorderStyle}枚举
	 * @param colorIndex 颜色的short值
	 * @return this
	 * @since 4.0.0
	 */
	public DefaultStyleSet setBorder(final BorderStyle borderSize, final IndexedColors colorIndex) {
		StyleUtil.setBorder(this.headCellStyle, borderSize, colorIndex);
		StyleUtil.setBorder(this.cellStyle, borderSize, colorIndex);
		StyleUtil.setBorder(this.cellStyleForNumber, borderSize, colorIndex);
		StyleUtil.setBorder(this.cellStyleForDate, borderSize, colorIndex);
		StyleUtil.setBorder(this.cellStyleForHyperlink, borderSize, colorIndex);
		return this;
	}

	/**
	 * 设置cell文本对齐样式
	 *
	 * @param halign 横向位置
	 * @param valign 纵向位置
	 * @return this
	 * @since 4.0.0
	 */
	public DefaultStyleSet setAlign(final HorizontalAlignment halign, final VerticalAlignment valign) {
		StyleUtil.setAlign(this.headCellStyle, halign, valign);
		StyleUtil.setAlign(this.cellStyle, halign, valign);
		StyleUtil.setAlign(this.cellStyleForNumber, halign, valign);
		StyleUtil.setAlign(this.cellStyleForDate, halign, valign);
		StyleUtil.setAlign(this.cellStyleForHyperlink, halign, valign);
		return this;
	}

	/**
	 * 设置单元格背景样式
	 *
	 * @param backgroundColor 背景色
	 * @param withHeadCell    是否也定义头部样式
	 * @return this
	 * @since 4.0.0
	 */
	public DefaultStyleSet setBackgroundColor(final IndexedColors backgroundColor, final boolean withHeadCell) {
		if (withHeadCell) {
			StyleUtil.setColor(this.headCellStyle, backgroundColor, FillPatternType.SOLID_FOREGROUND);
		}
		StyleUtil.setColor(this.cellStyle, backgroundColor, FillPatternType.SOLID_FOREGROUND);
		StyleUtil.setColor(this.cellStyleForNumber, backgroundColor, FillPatternType.SOLID_FOREGROUND);
		StyleUtil.setColor(this.cellStyleForDate, backgroundColor, FillPatternType.SOLID_FOREGROUND);
		StyleUtil.setColor(this.cellStyleForHyperlink, backgroundColor, FillPatternType.SOLID_FOREGROUND);
		return this;
	}

	/**
	 * 设置全局字体
	 *
	 * @param color      字体颜色
	 * @param fontSize   字体大小，-1表示默认大小
	 * @param fontName   字体名，null表示默认字体
	 * @param ignoreHead 是否跳过头部样式
	 * @return this
	 */
	public DefaultStyleSet setFont(final short color, final short fontSize, final String fontName, final boolean ignoreHead) {
		final Font font = StyleUtil.createFont(this.workbook, color, fontSize, fontName);
		return setFont(font, ignoreHead);
	}

	/**
	 * 设置全局字体
	 *
	 * @param font       字体，可以通过{@link StyleUtil#createFont(Workbook, short, short, String)}创建
	 * @param ignoreHead 是否跳过头部样式
	 * @return this
	 * @since 4.1.0
	 */
	public DefaultStyleSet setFont(final Font font, final boolean ignoreHead) {
		if (!ignoreHead) {
			this.headCellStyle.setFont(font);
		}
		this.cellStyle.setFont(font);
		this.cellStyleForNumber.setFont(font);
		this.cellStyleForDate.setFont(font);
		this.cellStyleForHyperlink.setFont(font);
		return this;
	}

	/**
	 * 设置单元格文本自动换行
	 *
	 * @return this
	 * @since 4.5.16
	 */
	public DefaultStyleSet setWrapText() {
		this.cellStyle.setWrapText(true);
		this.cellStyleForNumber.setWrapText(true);
		this.cellStyleForDate.setWrapText(true);
		this.cellStyleForHyperlink.setWrapText(true);
		return this;
	}
}
