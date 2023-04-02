/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.pinyin.engine.bopomofo4j;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.pinyin.PinyinEngine;
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
