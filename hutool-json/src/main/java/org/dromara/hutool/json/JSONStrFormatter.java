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

package org.dromara.hutool.json;

import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * JSON字符串格式化工具，用于简单格式化JSON字符串<br>
 * from http://blog.csdn.net/lovelong8808/article/details/54580278
 *
 * @author looly
 * @since 3.1.2
 */
public class JSONStrFormatter {

	/**
	 * 默认实例
	 */
	public static final JSONStrFormatter INSTANCE = new JSONStrFormatter(2, StrUtil.LF);

	private int indentFactor;
	private CharSequence newLineChars;

	/**
	 * 构造
	 *
	 * @param indentFactor 缩进因子，即每个缩进空格数
	 * @param newLineChars 换行符
	 */
	public JSONStrFormatter(final int indentFactor, final CharSequence newLineChars){
		this.indentFactor = indentFactor;
		this.newLineChars = newLineChars;
	}

	/**
	 * 设置缩进因子，即每个缩进空格数
	 * @param indentFactor 缩进因子，即每个缩进空格数
	 * @return this
	 */
	public JSONStrFormatter setIndentFactor(final int indentFactor) {
		this.indentFactor = indentFactor;
		return this;
	}

	/**
	 * 设置换行符
	 * @param newLineChars 换行符
	 * @return this
	 */
	public JSONStrFormatter setNewLineChars(final CharSequence newLineChars) {
		this.newLineChars = newLineChars;
		return this;
	}

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public String format(final String json) {
		final StringBuilder result = new StringBuilder();

		Character wrapChar = null;
		boolean isEscapeMode = false;
		final int length = json.length();
		int number = 0;
		char key;
		for (int i = 0; i < length; i++) {
			key = json.charAt(i);

			if (CharUtil.DOUBLE_QUOTES == key || CharUtil.SINGLE_QUOTE == key) {
				if (null == wrapChar) {
					//字符串模式开始
					wrapChar = key;
				} else if (wrapChar.equals(key)) {
					if (isEscapeMode) {
						//字符串模式下，遇到结束符号，也同时结束转义
						isEscapeMode = false;
					}

					//字符串包装结束
					wrapChar = null;
				}

				if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
					// 值前加空格
					result.append(CharUtil.SPACE);
				}

				result.append(key);
				continue;
			}

			if (CharUtil.BACKSLASH == key) {
				if (null != wrapChar) {
					//字符串模式下转义有效
					isEscapeMode = !isEscapeMode;
					result.append(key);
					continue;
				} else {
					result.append(key);
				}
			}

			if (null != wrapChar) {
				//字符串模式
				result.append(key);
				continue;
			}

			//如果当前字符是前方括号、前花括号做如下处理：
			if ((key == CharUtil.BRACKET_START) || (key == CharUtil.DELIM_START)) {
				//如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
					result.append(newLineChars).append(indent(number));
				}
				//前方括号、前花括号，的后面必须换行。打印：换行。
				result.append(key).append(newLineChars);
				//每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				result.append(indent(++number));
				continue;
			}

			// 3、如果当前字符是后方括号、后花括号做如下处理：
			if ((key == CharUtil.BRACKET_END) || (key == CharUtil.DELIM_END)) {
				// （1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append(newLineChars);
				// （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				result.append(indent(--number));
				// （3）打印：当前字符。
				result.append(key);
				// （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
//				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
//					result.append(NEW_LINE);
//				}
				// （5）继续下一次循环。
				continue;
			}

			// 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if ((key == CharUtil.COMMA)) {
				result.append(key).append(newLineChars).append(indent(number));
				continue;
			}

			if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
				result.append(CharUtil.SPACE);
			}

			// 5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进4个空格，即SPACE。
	 *
	 * @param number 缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private String indent(final int number) {
		return StrUtil.repeat(StrUtil.SPACE, number * indentFactor);
	}
}
