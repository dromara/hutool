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

import org.dromara.hutool.core.bean.DynaBean;
import org.dromara.hutool.core.math.NumberUtil;

/**
 * 处理名称节点或序号节点，如：
 * <ul>
 *     <li>name</li>
 *     <li>1</li>
 * </ul>
 *
 * @author looly
 */
public class NameNode implements Node {

	private final String name;

	/**
	 * 构造
	 *
	 * @param name 节点名
	 */
	public NameNode(final String name) {
		this.name = name;
	}

	/**
	 * 是否为数字节点
	 *
	 * @return 是否为数字节点
	 */
	public boolean isNumber() {
		return NumberUtil.isInteger(name);
	}

	@Override
	public Object getValue(final Object bean) {
		if ("$".equals(name)) {
			return bean;
		}
		return DynaBean.of(bean).get(this.name);
	}

	@Override
	public Object setValue(final Object bean, final Object value) {
		return DynaBean.of(bean).set(this.name, value).getBean();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
