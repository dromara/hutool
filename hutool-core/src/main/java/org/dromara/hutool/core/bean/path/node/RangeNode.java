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

package org.dromara.hutool.core.bean.path.node;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.util.Collection;
import java.util.List;

/**
 * [start:end:step] 模式节点
 *
 * @author looly
 */
public class RangeNode implements Node {

	private final int start;
	private final int end;
	private final int step;

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public RangeNode(final String expression) {
		final List<String> parts = SplitUtil.splitTrim(expression, StrUtil.COLON);
		this.start = Integer.parseInt(parts.get(0));
		this.end = Integer.parseInt(parts.get(1));
		int step = 1;
		if (3 == parts.size()) {
			step = Integer.parseInt(parts.get(2));
		}
		this.step = step;
	}

	@Override
	public Object getValue(final Object bean) {
		if (bean instanceof Collection) {
			return CollUtil.sub((Collection<?>) bean, this.start, this.end, this.step);
		} else if (ArrayUtil.isArray(bean)) {
			return ArrayUtil.sub(bean, this.start, this.end, this.step);
		}

		throw new UnsupportedOperationException("Can not get range value for: " + bean.getClass());
	}

	@Override
	public Object setValue(final Object bean, final Object value) {
		throw new UnsupportedOperationException("Can not set value with step name.");
	}

	@Override
	public String toString() {
		return StrUtil.format("[{}:{}:{}]",  this.start, this.end, this.step);
	}
}
