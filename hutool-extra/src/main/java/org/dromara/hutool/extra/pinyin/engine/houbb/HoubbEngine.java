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

package org.dromara.hutool.extra.pinyin.engine.houbb;

import org.dromara.hutool.extra.pinyin.engine.PinyinEngine;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;

/**
 * 封装了 houbb Pinyin 的引擎。
 *
 * <p>
 * houbb pinyin(https://github.com/houbb/pinyin)封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.github.houbb&lt;/groupId&gt;
 *     &lt;artifactId&gt;pinyin&lt;/artifactId&gt;
 *     &lt;version&gt;0.2.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class HoubbEngine implements PinyinEngine {

	// 汉字拼音输出的格式
	private PinyinStyleEnum format;

	/**
	 * 构造
	 */
	public HoubbEngine() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param format 格式
	 */
	public HoubbEngine(final PinyinStyleEnum format) {
		init(format);
	}

	/**
	 * 初始化
	 *
	 * @param format 格式
	 */
	public void init(PinyinStyleEnum format) {
		if (null == format) {
			format = PinyinStyleEnum.NORMAL;
		}
		this.format = format;
	}

	@Override
	public String getPinyin(final char c) {
		final String result;
		result = PinyinHelper.toPinyin(String.valueOf(c), format);
		return result;
	}

	@Override
	public String getPinyin(final String str, final String separator) {
		final String result;
		result = PinyinHelper.toPinyin(str, format, separator);
		return result;
	}
}
