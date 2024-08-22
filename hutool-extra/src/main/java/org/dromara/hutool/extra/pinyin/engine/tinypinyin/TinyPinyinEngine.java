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
