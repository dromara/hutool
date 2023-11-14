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

import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 节点简单工厂
 *
 * @author looly
 * @since 6.0.0
 */
public class NodeFactory {

	/**
	 * 根据表达式创建对应的节点
	 *
	 * @param expression 表达式
	 * @return 节点
	 */
	public static Node createNode(final String expression) {
		if (StrUtil.isEmpty(expression)) {
			return EmptyNode.INSTANCE;
		}

		if (StrUtil.contains(expression, CharUtil.COLON)) {
			return new RangeNode(expression);
		}

		if (StrUtil.contains(expression, CharUtil.COMMA)) {
			return new ListNode(expression);
		}

		return new NameNode(expression);
	}
}
