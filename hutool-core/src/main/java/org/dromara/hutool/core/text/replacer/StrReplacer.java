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

package org.dromara.hutool.core.text.replacer;

import java.io.Serializable;
import java.util.function.UnaryOperator;

/**
 * 抽象字符串替换类<br>
 * 通过实现replace方法实现局部替换逻辑
 *
 * @author looly
 * @since 4.1.5
 */
public abstract class StrReplacer implements UnaryOperator<CharSequence>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 抽象的字符串替换方法，通过传入原字符串和当前位置，执行替换逻辑，返回处理或替换的字符串长度部分。
	 *
	 * @param str 被处理的字符串
	 * @param pos 当前位置
	 * @param out 输出
	 * @return 处理的原字符串长度，0表示跳过此字符
	 */
	protected abstract int replace(CharSequence str, int pos, StringBuilder out);

	/**
	 * 执行替换，按照{@link #replace(CharSequence, int, StringBuilder)}逻辑替换对应部分，其它部分保持原样
	 * @param str 被处理的字符串
	 * @return 替换后的字符串
	 */
	@Override
	public CharSequence apply(final CharSequence str) {
		final int len = str.length();
		final StringBuilder builder = new StringBuilder(len);
		int pos = 0;//当前位置
		int consumed;//处理过的字符数
		while (pos < len) {
			consumed = replace(str, pos, builder);
			if (0 == consumed) {
				//0表示未处理或替换任何字符，原样输出本字符并从下一个字符继续
				builder.append(str.charAt(pos));
				pos++;
			}
			pos += consumed;
		}
		return builder;
	}
}
