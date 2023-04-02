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

package org.dromara.hutool.text.escape;

/**
 * 内部Escape工具类
 * @author looly
 *
 */
class InternalEscapeUtil {

	/**
	 * 将数组中的0和1位置的值互换，即键值转换
	 *
	 * @param array String[][] 被转换的数组
	 * @return String[][] 转换后的数组
	 */
	public static String[][] invert(final String[][] array) {
		final String[][] newarray = new String[array.length][2];
		for (int i = 0; i < array.length; i++) {
			newarray[i][0] = array[i][1];
			newarray[i][1] = array[i][0];
		}
		return newarray;
	}
}
