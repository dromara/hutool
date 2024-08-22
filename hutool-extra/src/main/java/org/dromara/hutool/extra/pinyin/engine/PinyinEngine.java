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

package org.dromara.hutool.extra.pinyin.engine;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.util.List;

/**
 * 拼音引擎接口，具体的拼音实现通过实现此接口，完成具体实现功能
 *
 * @author looly
 * @since 5.3.3
 */
public interface PinyinEngine {

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	String getPinyin(char c);

	/**
	 * 获取字符串对应的完整拼音，非中文返回原字符
	 *
	 * @param str       字符串
	 * @param separator 拼音之间的分隔符
	 * @return 拼音
	 */
	String getPinyin(String str, String separator);

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	default char getFirstLetter(final char c) {
		return getPinyin(c).charAt(0);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 分隔符
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	default String getFirstLetter(final String str, final String separator) {
		final String splitSeparator = StrUtil.isEmpty(separator) ? "#" : separator;
		final List<String> split = SplitUtil.split(getPinyin(str, splitSeparator), splitSeparator);
		return CollUtil.join(split, separator, (s) -> String.valueOf(!s.isEmpty() ? s.charAt(0) : StrUtil.EMPTY));
	}
}
