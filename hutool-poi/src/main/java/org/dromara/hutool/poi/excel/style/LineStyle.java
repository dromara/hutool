/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.style;

import org.apache.poi.hssf.usermodel.HSSFShape;

/**
 * SimpleShape中的线条风格枚举
 *
 * @author Looly
 * @see HSSFShape
 * @since 6.0.0
 */
public enum LineStyle {
	/**
	 * Solid (continuous) pen
	 */
	SOLID(HSSFShape.LINESTYLE_SOLID),
	/**
	 * PS_DASH system   dash style
	 */
	DASHSYS(HSSFShape.LINESTYLE_DASHSYS),
	/**
	 * PS_DOT system   dash style
	 */
	DOTSYS(HSSFShape.LINESTYLE_DOTSYS),
	/**
	 * PS_DASHDOT system dash style
	 */
	DASHDOTSYS(HSSFShape.LINESTYLE_DASHDOTSYS),
	/**
	 * PS_DASHDOTDOT system dash style
	 */
	DASHDOTDOTSYS(HSSFShape.LINESTYLE_DASHDOTDOTSYS),
	/**
	 * square dot style
	 */
	DOTGEL(HSSFShape.LINESTYLE_DOTGEL),
	/**
	 * dash style
	 */
	DASHGEL(HSSFShape.LINESTYLE_DASHGEL),
	/**
	 * long dash style
	 */
	LONGDASHGEL(HSSFShape.LINESTYLE_LONGDASHGEL),
	/**
	 * dash short dash
	 */
	DASHDOTGEL(HSSFShape.LINESTYLE_DASHDOTGEL),
	/**
	 * long dash short dash
	 */
	LONGDASHDOTGEL(HSSFShape.LINESTYLE_LONGDASHDOTGEL),
	/**
	 * long dash short dash short dash
	 */
	LONGDASHDOTDOTGEL(HSSFShape.LINESTYLE_LONGDASHDOTDOTGEL),
	/**
	 * 无
	 */
	NONE(HSSFShape.LINESTYLE_NONE);

	private final int value;

	/**
	 * 构造
	 *
	 * @param value 样式编码
	 */
	LineStyle(final int value) {
		this.value = value;
	}

	/**
	 * 获取样式编码
	 *
	 * @return 样式编码
	 */
	public int getValue() {
		return value;
	}
}
