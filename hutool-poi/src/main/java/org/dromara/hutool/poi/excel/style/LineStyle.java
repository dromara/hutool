/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
