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
 * Bean路径节点接口
 *
 * @author looly
 */
public interface Node {
	/**
	 * 获取Bean对应节点的值
	 *
	 * @param bean bean对象
	 * @return 节点值
	 */
	Object getValue(Object bean);

	/**
	 * 设置节点值
	 *
	 * @param bean  bean对象
	 * @param value 节点值
	 * @return bean对象。如果在原Bean对象基础上设置值，返回原Bean，否则返回新的Bean
	 */
	Object setValue(Object bean, Object value);
}
