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

package org.dromara.hutool.core.bean.path.node;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

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

	/**
	 * 获取起始值
	 *
	 * @return 起始值
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 获取结束值
	 *
	 * @return 结束值
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * 获取步进值
	 *
	 * @return 步进值
	 */
	public int getStep() {
		return step;
	}

	@Override
	public String toString() {
		return StrUtil.format("[{}:{}:{}]",  this.start, this.end, this.step);
	}
}
