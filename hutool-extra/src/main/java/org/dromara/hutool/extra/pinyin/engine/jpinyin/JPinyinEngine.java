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

package org.dromara.hutool.extra.pinyin.engine.jpinyin;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.extra.pinyin.engine.PinyinEngine;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 封装了Jpinyin的引擎。
 *
 * <p>
 * jpinyin（github库作者已删除）封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.github.stuxuhai&lt;/groupId&gt;
 *     &lt;artifactId&gt;jpinyin&lt;/artifactId&gt;
 *     &lt;version&gt;1.1.8&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class JPinyinEngine implements PinyinEngine {

	//设置汉子拼音输出的格式
	private PinyinFormat format;

	/**
	 * 构造
	 */
	public JPinyinEngine() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param format {@link PinyinFormat}
	 */
	public JPinyinEngine(final PinyinFormat format) {
		init(format);
	}

	/**
	 * 初始化格式
	 *
	 * @param format 格式{@link PinyinFormat}
	 */
	public void init(PinyinFormat format) {
		if (null == format) {
			// 不加声调
			format = PinyinFormat.WITHOUT_TONE;
		}
		this.format = format;
	}


	@Override
	public String getPinyin(final char c) {
		final String[] results = PinyinHelper.convertToPinyinArray(c, format);
		return ArrayUtil.isEmpty(results) ? String.valueOf(c) : results[0];
	}

	@Override
	public String getPinyin(final String str, final String separator) {
		try {
			return PinyinHelper.convertToPinyinString(str, separator, format);
		} catch (final PinyinException e) {
			throw new org.dromara.hutool.extra.pinyin.PinyinException(e);
		}
	}
}
