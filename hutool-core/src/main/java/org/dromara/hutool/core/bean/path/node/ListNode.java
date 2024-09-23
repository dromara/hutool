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

import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;

import java.util.List;

/**
 * 列表节点
 * [num0,num1,num2...]模式或者['key0','key1']模式
 *
 * @author looly
 */
public class ListNode implements Node {

	final List<String> names;

	/**
	 * 列表节点
	 *
	 * @param expression 表达式
	 */
	public ListNode(final String expression) {
		this.names = SplitUtil.splitTrim(expression, StrUtil.COMMA);
	}

	/**
	 * 获取列表中的name，不去除单引号
	 *
	 * @return name列表
	 */
	public String[] getNames() {
		return this.names.toArray(new String[0]);
	}

	/**
	 * 将列表中的name，去除单引号
	 *
	 * @return 处理后的name列表
	 */
	public String[] getUnWrappedNames() {
		final String[] unWrappedNames = new String[names.size()];
		for (int i = 0; i < unWrappedNames.length; i++) {
			unWrappedNames[i] = StrUtil.unWrap(names.get(i), CharUtil.SINGLE_QUOTE);
		}

		return unWrappedNames;
	}

	@Override
	public String toString() {
		return this.names.toString();
	}
}
