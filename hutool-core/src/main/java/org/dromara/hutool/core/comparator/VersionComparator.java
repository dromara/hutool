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

import org.dromara.hutool.core.lang.Version;

import java.io.Serializable;

/**
 * 版本比较器<br>
 * 比较两个版本的大小<br>
 * 排序时版本从小到大排序，即比较时小版本在前，大版本在后<br>
 * 支持如：1.3.20.8，6.82.20160101，8.5a/8.5c等版本形式<br>
 * 参考：java.lang.module.ModuleDescriptor.Version
 *
 * @author Looly
 * @since 4.0.2
 */
public class VersionComparator extends NullComparator<String> implements Serializable {
	private static final long serialVersionUID = 1L;

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
		return CompareUtil.compare(Version.of(version1), Version.of(version2));
	}
}
