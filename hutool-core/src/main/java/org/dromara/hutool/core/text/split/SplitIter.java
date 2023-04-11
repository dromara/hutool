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

package org.dromara.hutool.core.text.split;

import org.dromara.hutool.core.collection.iter.ComputeIter;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.finder.TextFinder;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 字符串切分迭代器<br>
 * 此迭代器是字符串切分的懒模式实现，实例化后不完成切分，只有调用{@link #hasNext()}或遍历时才完成切分<br>
 * 此迭代器非线程安全
 *
 * @author looly
 * @since 5.7.14
 */
public class SplitIter extends ComputeIter<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String text;
	private final TextFinder finder;
	private final int limit;
	private final boolean ignoreEmpty;

	/**
	 * 上一次的结束位置
	 */
	private int offset;
	/**
	 * 计数器，用于判断是否超过limit
	 */
	private int count;

	/**
	 * 构造
	 *
	 * @param text            文本，不能为{@code null}
	 * @param separatorFinder 分隔符匹配器
	 * @param limit           限制数量，小于等于0表示无限制
	 * @param ignoreEmpty     是否忽略""
	 */
	public SplitIter(final CharSequence text, final TextFinder separatorFinder, final int limit, final boolean ignoreEmpty) {
		Assert.notNull(text, "Text must be not null!");
		this.text = text.toString();
		this.finder = separatorFinder.setText(text);
		this.limit = limit > 0 ? limit : Integer.MAX_VALUE;
		this.ignoreEmpty = ignoreEmpty;
	}

	@Override
	protected String computeNext() {
		// 达到数量上限或末尾，结束
		if (count >= limit || offset > text.length()) {
			return null;
		}

		// 达到数量上限
		if (count == (limit - 1)) {
			// 当到达限制次数时，最后一个元素为剩余部分
			if (ignoreEmpty && offset == text.length()) {
				// 最后一个是空串
				return null;
			}

			// 结尾整个作为一个元素
			count++;
			return text.substring(offset);
		}

		final int start = finder.start(offset);
		// 无分隔符，结束
		if (start < 0) {
			// 如果不再有分隔符，但是遗留了字符，则单独作为一个段
			if (offset <= text.length()) {
				final String result = text.substring(offset);
				if (!ignoreEmpty || !result.isEmpty()) {
					// 返回非空串
					offset = Integer.MAX_VALUE;
					return result;
				}
			}
			return null;
		}

		// 找到新的分隔符位置
		final String result = text.substring(offset, start);
		offset = finder.end(start);

		if (ignoreEmpty && result.isEmpty()) {
			// 发现空串且需要忽略时，跳过之
			return computeNext();
		}

		count++;
		return result;
	}

	/**
	 * 重置
	 */
	public void reset() {
		this.finder.reset();
		this.offset = 0;
		this.count = 0;
	}

	/**
	 * 获取切分后的对象数组
	 *
	 * @param trim 是否去除元素两边空格
	 * @return 切分后的列表
	 */
	public String[] toArray(final boolean trim) {
		return toList(trim).toArray(new String[0]);
	}

	/**
	 * 获取切分后的对象列表
	 *
	 * @param trim 是否去除元素两边空格
	 * @return 切分后的列表
	 */
	public List<String> toList(final boolean trim) {
		return toList(trim ? StrUtil::trim : Function.identity());
	}

	/**
	 * 获取切分后的对象列表
	 *
	 * @param <T>     元素类型
	 * @param mapping 字符串映射函数
	 * @return 切分后的列表
	 */
	public <T> List<T> toList(final Function<String, T> mapping) {
		final List<T> result = new ArrayList<>();
		while (this.hasNext()) {
			final T apply = mapping.apply(this.next());
			if (ignoreEmpty && StrUtil.isEmptyIfStr(apply)) {
				// 对于mapping之后依旧是String的情况，ignoreEmpty依旧有效
				continue;
			}
			result.add(apply);
		}
		if (result.isEmpty()) {
			return new ArrayList<>(0);
		}
		return result;
	}
}
