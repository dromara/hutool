/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则列表方式的日期解析器<br>
 * 通过定义若干的日期正则，遍历匹配到给定正则后，按照正则方式解析为日期
 *
 * @author Looly
 */
public class RegexListDateParser implements DateParser, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 根据给定的正则列表，创建RegexListDateParser
	 *
	 * @param patterns 正则列表
	 * @return RegexListDateParser
	 */
	public static RegexListDateParser of(final Pattern... patterns) {
		return new RegexListDateParser(ListUtil.of(patterns));
	}

	private final List<Pattern> patterns;

	/**
	 * 构造
	 *
	 * @param patterns 正则列表
	 */
	public RegexListDateParser(final List<Pattern> patterns) {
		this.patterns = patterns;
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param regex 日期正则
	 * @return this
	 */
	public RegexListDateParser addRegex(final String regex) {
		// 日期正则忽略大小写
		return addPattern(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param pattern 日期正则
	 * @return this
	 */
	public RegexListDateParser addPattern(final Pattern pattern) {
		this.patterns.add(pattern);
		return this;
	}

	@Override
	public Date parse(final CharSequence source) throws DateException {
		Matcher matcher;
		for (final Pattern pattern : this.patterns) {
			matcher = pattern.matcher(source);
			if (matcher.matches()) {
				return RegexDateParser.parse(matcher);
			}
		}

		throw new DateException("No valid pattern for date string: [{}]", source);
	}
}
