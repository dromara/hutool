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

package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 基于注册的日期解析器，通过遍历列表，找到合适的解析器，然后解析为日期<br>
 * 默认的，可以调用{@link #INSTANCE}使用全局的解析器，亦或者通过构造自定义独立的注册解析器
 *
 * @author looly
 * @since 6.0.0
 */
public class RegisterDateParser implements DateParser, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final RegisterDateParser INSTANCE = new RegisterDateParser();

	private final List<PredicateDateParser> parserList;

	/**
	 * 构造
	 */
	public RegisterDateParser() {
		parserList = ListUtil.of(
			// HH:mm:ss 或者 HH:mm 时间格式匹配单独解析
			TimeParser.INSTANCE,
			// 默认的正则解析器
			DefaultRegexDateParser.INSTANCE
		);
	}

	@Override
	public Date parse(final CharSequence source) throws DateException {
		return parserList
			.stream()
			.filter(predicateDateParser -> predicateDateParser.test(source))
			.findFirst()
			.map(predicateDateParser -> predicateDateParser.parse(source)).orElse(null);
	}

	/**
	 * 注册自定义的{@link PredicateDateParser}<br>
	 * 通过此方法，用户可以自定义日期字符串的匹配和解析，通过循环匹配，找到合适的解析器，解析之。
	 *
	 * @param dateParser {@link PredicateDateParser}
	 * @return this
	 */
	public RegisterDateParser register(final PredicateDateParser dateParser) {
		// 用户定义的规则优先
		this.parserList.add(0, dateParser);
		return this;
	}
}
