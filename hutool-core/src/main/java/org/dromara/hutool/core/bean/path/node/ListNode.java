/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean.path.node;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 列表节点
 * [num0,num1,num2...]模式或者['key0','key1']模式
 *
 * @author looly
 */
public class ListNode implements Node{

	final List<String> names;

	/**
	 * 列表节点
	 * @param expression 表达式
	 */
	public ListNode(final String expression) {
		this.names = SplitUtil.splitTrim(expression, StrUtil.COMMA);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(final Object bean) {
		final List<String> names = this.names;

		if (bean instanceof Collection) {
			return CollUtil.getAny((Collection<?>) bean, Convert.convert(int[].class, names));
		} else if (ArrayUtil.isArray(bean)) {
			return ArrayUtil.getAny(bean, Convert.convert(int[].class, names));
		} else {
			final String[] unWrappedNames = getUnWrappedNames(names);
			if (bean instanceof Map) {
				// 只支持String为key的Map
				return MapUtil.getAny((Map<String, ?>) bean, unWrappedNames);
			} else {
				final Map<String, Object> map = BeanUtil.beanToMap(bean);
				return MapUtil.getAny(map, unWrappedNames);
			}
		}
	}

	@Override
	public void setValue(final Object bean, final Object value) {
		throw new UnsupportedOperationException("Can not set value to multi names.");
	}

	@Override
	public String toString() {
		return this.names.toString();
	}

	/**
	 * 将列表中的name，去除单引号
	 * @param names name列表
	 * @return 处理后的name列表
	 */
	private String[] getUnWrappedNames(final List<String> names){
		final String[] unWrappedNames = new String[names.size()];
		for (int i = 0; i < unWrappedNames.length; i++) {
			unWrappedNames[i] = StrUtil.unWrap(names.get(i), CharUtil.SINGLE_QUOTE);
		}

		return unWrappedNames;
	}
}
