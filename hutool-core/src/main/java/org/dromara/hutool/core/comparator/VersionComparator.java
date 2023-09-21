/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 版本比较器<br>
 * 比较两个版本的大小<br>
 * 排序时版本从小到大排序，即比较时小版本在前，大版本在后<br>
 * 支持如：1.3.20.8，6.82.20160101，8.5a/8.5c等版本形式<br>
 * 参考：https://www.cnblogs.com/shihaiming/p/6286575.html
 *
 * @author Looly
 * @since 4.0.2
 */
public class VersionComparator extends NullComparator<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Pattern PATTERN_PRE_NUMBERS= Pattern.compile("^\\d+");

	/**
	 * 单例
	 */
	public static final VersionComparator INSTANCE = new VersionComparator();

	/**
	 * 默认构造
	 */
	public VersionComparator() {
		this(false);
	}

	/**
	 * 默认构造
	 *
	 * @param nullGreater 是否{@code null}最大，排在最后
	 */
	public VersionComparator(final boolean nullGreater) {
		super(nullGreater, (VersionComparator::compareVersion));
	}

	// -----------------------------------------------------------------------------------------------------

	/**
	 * 比较两个版本<br>
	 * null版本排在最小：即：
	 * <pre>
	 * compare(null, "v1") &lt; 0
	 * compare("v1", "v1")  = 0
	 * compare(null, null)   = 0
	 * compare("v1", null) &gt; 0
	 * compare("1.0.0", "1.0.2") &lt; 0
	 * compare("1.0.2", "1.0.2a") &lt; 0
	 * compare("1.13.0", "1.12.1c") &gt; 0
	 * compare("V0.0.20170102", "V0.0.20170101") &gt; 0
	 * </pre>
	 *
	 * @param version1 版本1
	 * @param version2 版本2
	 */
	private static int compareVersion(final String version1, final String version2) {
		final List<String> v1s = SplitUtil.splitTrim(version1, StrUtil.DOT);
		final List<String> v2s = SplitUtil.splitTrim(version2, StrUtil.DOT);

		int diff = 0;
		final int minSize = Math.min(v1s.size(), v2s.size());// 取最小长度值
		String v1;
		String v2;
		for (int i = 0; i < minSize; i++) {
			v1 = v1s.get(i);
			v2 = v2s.get(i);
			// 先比较长度
			diff = v1.length() - v2.length();
			if (0 == diff) {
				// 长度相同，直接比较字符或数字
				diff = v1.compareTo(v2);
			} else {
				// 不同长度，且含有字母
				if(!NumberUtil.isNumber(v1) || !NumberUtil.isNumber(v2)){
					//不同长度的先比较前面的数字；前面数字不相等时，按数字大小比较；数字相等的时候，继续按长度比较，类似于 103 > 102a
					final int v1Num = Convert.toInt(ReUtil.get(PATTERN_PRE_NUMBERS, v1, 0), 0);
					final int v2Num = Convert.toInt(ReUtil.get(PATTERN_PRE_NUMBERS, v2, 0), 0);
					final int diff1 = v1Num - v2Num;
					if (diff1 != 0) {
						diff = diff1;
					}
				}
			}
			if (diff != 0) {
				//已有结果，结束
				break;
			}
		}

		// 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		return (diff != 0) ? diff : v1s.size() - v2s.size();
	}
}
