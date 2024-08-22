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

package org.dromara.hutool.core.text.escape;

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
