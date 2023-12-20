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

package org.dromara.hutool.extra.pinyin.engine.tinypinyin;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.pinyin.engine.PinyinEngine;
import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 封装了TinyPinyin的引擎。
 *
 * <p>
 * TinyPinyin(https://github.com/promeG/TinyPinyin)提供者未提交Maven中央库，<br>
 * 因此使用
 * https://github.com/biezhi/TinyPinyin打包的版本
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;io.github.biezhi&lt;/groupId&gt;
 *     &lt;artifactId&gt;TinyPinyin&lt;/artifactId&gt;
 *     &lt;version&gt;2.0.3.RELEASE&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class TinyPinyinEngine implements PinyinEngine {

	/**
	 * 构造
	 */
	public TinyPinyinEngine() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public TinyPinyinEngine(final Pinyin.Config config) {
		Pinyin.init(config);
	}

	@Override
	public String getPinyin(final char c) {
		if (!Pinyin.isChinese(c)) {
			return String.valueOf(c);
		}
		return Pinyin.toPinyin(c).toLowerCase();
	}

	@Override
	public String getPinyin(final String str, final String separator) {
		final String pinyin = Pinyin.toPinyin(str, separator);
		return StrUtil.isEmpty(pinyin) ? pinyin : pinyin.toLowerCase();
	}

}
