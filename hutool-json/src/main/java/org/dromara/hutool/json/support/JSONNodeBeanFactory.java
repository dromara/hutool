/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.support;

import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.bean.path.NodeBeanFactory;
import org.dromara.hutool.core.bean.path.node.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.json.*;

/**
 * JSON节点Bean创建器
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONNodeBeanFactory implements NodeBeanFactory<JSON> {

	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param config JSON配置
	 */
	public JSONNodeBeanFactory(final JSONConfig config) {
		this.config = config;
	}

	@Override
	public JSON create(final JSON parent, final BeanPath<JSON> beanPath) {
		final BeanPath<JSON> next = beanPath.next();
		if (null != next) {
			final Node node = next.getNode();
			if (node instanceof NameNode) {
				final NameNode nameNode = (NameNode) node;
				if (nameNode.isNumber()) {
					return JSONUtil.ofArray(config);
				}
				return JSONUtil.ofObj(config);
			}
		}
		return JSONUtil.ofObj(config);
	}

	@Override
	public Object getValue(final JSON bean, final BeanPath<JSON> beanPath) {
		final Node node = beanPath.getNode();
		if (null == node || node instanceof EmptyNode) {
			return null;
		} else if (node instanceof ListNode) {
			return getValueByListNode(bean, (ListNode) node);
		} else if (node instanceof NameNode) {
			return getValueByNameNode(bean, (NameNode) node);
		} else if (node instanceof RangeNode) {
			return getValueByRangeNode(bean, (RangeNode) node);
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}

	@Override
	public JSON setValue(final JSON bean, final Object value, final BeanPath<JSON> beanPath) {
		final Node node = beanPath.getNode();
		if (node instanceof EmptyNode) {
			return bean;
		} else if (node instanceof NameNode) {
			if(bean instanceof JSONObject){
				((JSONObject) bean).set(((NameNode) node).getName(), value);
			} else if(bean instanceof JSONArray){
				((JSONArray) bean).setValue(Integer.parseInt(((NameNode) node).getName()), value);
			}
			return bean;
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}

	/**
	 * 获取指定下标的值<br>
	 * 如果Bean为JSONArray，则返回指定下标数组的值，如果Bean为JSONObject，则返回指定key数组的值
	 *
	 * @param bean Bean
	 * @param node 下标节点
	 * @return 值
	 */
	private Object getValueByListNode(final JSON bean, final ListNode node) {
		final String[] names = node.getUnWrappedNames();
		if (bean instanceof JSONArray) {
			return CollUtil.getAny((JSONArray) bean, ConvertUtil.convert(int[].class, names));
		} else if (bean instanceof JSONObject) {
			MapUtil.getAny((JSONObject) bean, names);
		}

		throw new UnsupportedOperationException("Can not get by list for: " + bean.getClass());
	}

	/**
	 * 获取指定key的值<br>
	 * 如果Bean为JSONObject，则返回指定key的值，如果Bean为JSONArray，则返回指定下标数组的值
	 *
	 * @param bean Bean
	 * @param node key节点
	 * @return 值
	 */
	private Object getValueByNameNode(final JSON bean, final NameNode node) {
		final String name = node.getName();
		if ("$".equals(name)) {
			return bean;
		}

		if (bean instanceof JSONObject) {
			return ((JSONObject) bean).get(name);
		} else if (bean instanceof JSONArray) {
			return ((JSONArray) bean).get(Integer.parseInt(name));
		}

		throw new UnsupportedOperationException("Can not get by name for: " + bean.getClass());
	}

	/**
	 * 获取指定下标范围的值<br>
	 * 如果Bean为JSONArray，则返回指定下标范围的值
	 *
	 * @param bean Bean
	 * @param node 下标range节点
	 * @return 值
	 */
	private Object getValueByRangeNode(final JSON bean, final RangeNode node) {
		if (bean instanceof JSONArray) {
			return CollUtil.sub((JSONArray) bean, node.getStart(), node.getEnd(), node.getStep());
		}

		throw new UnsupportedOperationException("Can not get range value for: " + bean.getClass());
	}
}
