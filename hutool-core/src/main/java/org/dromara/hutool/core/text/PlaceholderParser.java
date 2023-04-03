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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.util.CharUtil;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * <p>一个简单的占位符解析器。给定占位符的左右边界符号以及转义符，
 * 将允许把一段字符串中的占位符解析并替换为指定内容，支持指定转义符对边界符号进行转义。<br>
 * 比如：
 * <pre>{@code
 * String text = "select * from #[tableName] where id = #[id]";
 * PlaceholderParser parser = new PlaceholderParser(str -> "?", "#[", "]");
 * parser.apply(text); // = select * from ? where id = ?
 * }</pre>
 *
 * @author huangchengxing
 * @author emptypoint
 */
public class PlaceholderParser implements UnaryOperator<String> {

	/**
	 * processor
	 */
	private final UnaryOperator<String> processor;

	/**
	 * 占位符开始符号
	 */
	private final String open;

	/**
	 * 结束符号长度
	 */
	private final int openLength;

	/**
	 * 占位符结束符号
	 */
	private final String close;

	/**
	 * 结束符号长度
	 */
	private final int closeLength;

	/**
	 * 转义符
	 */
	private final char escape;

	/**
	 * 创建一个占位符解析器，默认转义符为{@code "\"}
	 *
	 * @param processor 占位符处理器
	 * @param prefix    占位符开始符号，不允许为空
	 * @param suffix    占位符结束符号，不允许为空
	 */
	public PlaceholderParser(
			final UnaryOperator<String> processor, final String prefix, final String suffix) {
		this(processor, prefix, suffix, CharUtil.BACKSLASH);
	}

	/**
	 * 创建一个占位符解析器
	 *
	 * @param processor 占位符处理器
	 * @param prefix      占位符开始符号，不允许为空
	 * @param suffix     占位符结束符号，不允许为空
	 * @param escape    转义符
	 */
	public PlaceholderParser(
			final UnaryOperator<String> processor, final String prefix, final String suffix, final char escape) {
		Assert.isFalse(StrChecker.isEmpty(prefix), "开始符号不能为空");
		Assert.isFalse(StrChecker.isEmpty(suffix), "结束符号不能为空");
		this.processor = Objects.requireNonNull(processor);
		this.open = prefix;
		this.openLength = prefix.length();
		this.close = suffix;
		this.closeLength = suffix.length();
		this.escape = escape;
	}

	/**
	 * 解析并替换字符串中的占位符
	 *
	 * @param text 待解析的字符串
	 * @return 处理后的字符串
	 */
	@Override
	public String apply(final String text) {
		if (StrChecker.isEmpty(text)) {
			return StrChecker.EMPTY;
		}

		// 寻找第一个开始符号
		int closeCursor = 0;
		int openCursor = text.indexOf(open, closeCursor);
		if (openCursor == -1) {
			return text;
		}

		// 开始匹配
		final char[] src = text.toCharArray();
		final StringBuilder result = new StringBuilder(src.length);
		final StringBuilder expression = new StringBuilder();
		while (openCursor > -1) {

			// 开始符号是否被转义，若是则跳过并寻找下一个开始符号
			if (openCursor > 0 && src[openCursor - 1] == escape) {
				result.append(src, closeCursor, openCursor - closeCursor - 1).append(open);
				closeCursor = openCursor + openLength;
				openCursor = text.indexOf(open, closeCursor);
				continue;
			}

			// 记录当前位符的开始符号与上一占位符的结束符号间的字符串
			result.append(src, closeCursor, openCursor - closeCursor);

			// 重置结束游标至当前占位符的开始处
			closeCursor = openCursor + openLength;

			// 寻找结束符号下标
			int end = text.indexOf(close, closeCursor);
			while (end > -1) {
				// 结束符号被转义，寻找下一个结束符号
				if (end > closeCursor && src[end - 1] == escape) {
					expression.append(src, closeCursor, end - closeCursor - 1).append(close);
					closeCursor = end + closeLength;
					end = text.indexOf(close, closeCursor);
				}
				// 找到结束符号
				else {
					expression.append(src, closeCursor, end - closeCursor);
					break;
				}
			}

			// 未能找到结束符号，说明匹配异常
			if (end == -1) {
				throw new UtilException("\"{}\" 中字符下标 {} 处的开始符没有找到对应的结束符", text, openCursor);
			}
			// 找到结束符号，将开始到结束符号之间的字符串替换为指定表达式
			else {
				result.append(processor.apply(expression.toString()));
				expression.setLength(0);
				// 完成当前占位符的处理匹配，寻找下一个
				closeCursor = end + close.length();
			}

			// 寻找下一个开始符号
			openCursor = text.indexOf(open, closeCursor);
		}

		// 若匹配结束后仍有未处理的字符串，则直接将其拼接到表达式上
		if (closeCursor < src.length) {
			result.append(src, closeCursor, src.length - closeCursor);
		}
		return result.toString();
	}

}
