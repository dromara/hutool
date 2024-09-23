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

package org.dromara.hutool.core.bean.path;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.path.node.NameNode;
import org.dromara.hutool.core.bean.path.node.Node;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.FieldUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的Bean创建器
 *
 * @author looly
 * @since 6.0.0
 */
public class DefaultNodeBeanCreator implements NodeBeanCreator {

	/**
	 * 单例
	 */
	public static final NodeBeanCreator INSTANCE = new DefaultNodeBeanCreator();

	@Override
	public Object create(final Object parent, final BeanPath beanPath) {
		if(parent instanceof Map || parent instanceof List || ArrayUtil.isArray(parent)){
			// 根据下一个节点类型，判断当前节点名称对应类型
			final Node node = beanPath.next().getNode();
			if (node instanceof NameNode) {
				return ((NameNode) node).isNumber() ? new ArrayList<>() : new HashMap<>();
			}
			return new HashMap<>();
		}

		// 普通Bean
		final Node node = beanPath.getNode();
		if(node instanceof NameNode){
			final String name = ((NameNode) node).getName();

			final Field field = FieldUtil.getField(parent.getClass(), name);
			if(null == field){
				throw new IllegalArgumentException("No field found for name: " + name);
			}
			return ConstructorUtil.newInstanceIfPossible(field.getType());
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}
}
