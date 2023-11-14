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

/**
 * 空节点
 *
 * @author looly
 */
public class EmptyNode implements Node {

	/**
	 * 单例
	 */
	public static EmptyNode INSTANCE = new EmptyNode();

	@Override
	public Object getValue(final Object bean) {
		return null;
	}

	@Override
	public void setValue(final Object bean, final Object value) {
		// do nothing
	}
}
