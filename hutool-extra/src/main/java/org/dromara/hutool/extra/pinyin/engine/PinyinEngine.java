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
