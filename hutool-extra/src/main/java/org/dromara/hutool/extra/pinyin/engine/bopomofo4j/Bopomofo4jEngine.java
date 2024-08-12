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

package org.dromara.hutool.extra.pinyin.engine.bopomofo4j;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.pinyin.engine.PinyinEngine;
import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;

/**
 * 封装了Bopomofo4j的引擎。
 *
 * <p>
 * Bopomofo4j封装，项目：https://gitee.com/rnkrsoft/Bopomofo4j。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.rnkrsoft.bopomofo4j&lt;/groupId&gt;
 *     &lt;artifactId&gt;bopomofo4j&lt;/artifactId&gt;
 *     &lt;version&gt;1.0.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 * @since 5.4.5
 */
public class Bopomofo4jEngine implements PinyinEngine {

	/**
	 * 构造
	 */
	public Bopomofo4jEngine(){
		Bopomofo4j.local();
	}

	@Override
	public String getPinyin(final char c) {
		return Bopomofo4j.pinyin(String.valueOf(c), ToneType.WITHOUT_TONE, false, false, StrUtil.EMPTY);
	}

	@Override
	public String getPinyin(final String str, final String separator) {
		return Bopomofo4j.pinyin(str, ToneType.WITHOUT_TONE, false, false, separator);
	}
}
