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

package cn.hutool.core.text.replacer;

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
