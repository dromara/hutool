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
