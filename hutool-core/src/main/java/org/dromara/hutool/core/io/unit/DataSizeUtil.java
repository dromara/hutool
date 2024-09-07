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

package org.dromara.hutool.core.io.unit;

import org.dromara.hutool.core.text.StrUtil;

import java.text.DecimalFormat;

/**
 * 数据大小工具类
 *
 * @author looly
 * @since 5.3.10
 */
public class DataSizeUtil {

	/**
	 * 解析数据大小字符串，转换为bytes大小
	 *
	 * @param text 数据大小字符串，类似于：12KB, 5MB等
	 * @return bytes大小
	 */
	public static long parse(final String text) {
		return DataSize.parse(text).toBytes();
	}

	/**
	 * 可读的文件大小<br>
	 * 参考 <a href="http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc">http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc</a>
	 *
	 * @param size Long类型大小
	 * @return 大小
	 */
	public static String format(final long size) {
		return format(size, false);
	}

	/**
	 * 可读的文件大小<br>
	 * 参考 <a href="http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc">http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc</a>
	 *
	 * @param size          Long类型大小
	 * @param useSimpleName 是否使用简写，例如：1KB 简写成 1K
	 * @return 大小
	 */
	public static String format(final long size, final boolean useSimpleName) {
		return format(size, 2, useSimpleName ? DataUnit.UNIT_NAMES_SIMPLE : DataUnit.UNIT_NAMES, StrUtil.SPACE);
	}

	/**
	 * 可读的文件大小<br>
	 * 参考 <a href="http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc">http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc</a>
	 *
	 * @param size      Long类型大小
	 * @param scale     小数点位数，四舍五入
	 * @param unitNames 单位数组
	 * @param delimiter 数字和单位的分隔符
	 * @return 大小
	 */
	public static String format(final long size, final int scale, final String[] unitNames, final String delimiter) {
		if (size <= 0) {
			return "0";
		}
		final int digitGroups = Math.min(unitNames.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
		return new DecimalFormat("#,##0." + StrUtil.repeat('#', scale))
			.format(size / Math.pow(1024, digitGroups)) + delimiter + unitNames[digitGroups];
	}
}
