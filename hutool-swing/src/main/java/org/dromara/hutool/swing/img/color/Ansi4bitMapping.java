/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img.color;

import org.dromara.hutool.core.lang.ansi.Ansi4BitColor;

import java.util.LinkedHashMap;

/**
 * ANSI 4bit 颜色和Lab颜色映射关系
 *
 * @author Tom Xin
 */
public class Ansi4bitMapping extends AnsiLabMapping {

	/**
	 * 单例
	 */
	public static final Ansi4bitMapping INSTANCE = new Ansi4bitMapping();

	/**
	 * 构造
	 */
	public Ansi4bitMapping() {
		ansiLabMap = new LinkedHashMap<>(16, 1);
		ansiLabMap.put(Ansi4BitColor.BLACK, new LabColor(0x000000));
		ansiLabMap.put(Ansi4BitColor.RED, new LabColor(0xAA0000));
		ansiLabMap.put(Ansi4BitColor.GREEN, new LabColor(0x00AA00));
		ansiLabMap.put(Ansi4BitColor.YELLOW, new LabColor(0xAA5500));
		ansiLabMap.put(Ansi4BitColor.BLUE, new LabColor(0x0000AA));
		ansiLabMap.put(Ansi4BitColor.MAGENTA, new LabColor(0xAA00AA));
		ansiLabMap.put(Ansi4BitColor.CYAN, new LabColor(0x00AAAA));
		ansiLabMap.put(Ansi4BitColor.WHITE, new LabColor(0xAAAAAA));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_BLACK, new LabColor(0x555555));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_RED, new LabColor(0xFF5555));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_GREEN, new LabColor(0x55FF00));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_YELLOW, new LabColor(0xFFFF55));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_BLUE, new LabColor(0x5555FF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_MAGENTA, new LabColor(0xFF55FF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_CYAN, new LabColor(0x55FFFF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_WHITE, new LabColor(0xFFFFFF));
	}
}
