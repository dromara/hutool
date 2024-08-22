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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.map.reference.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.JdkProxyUtil;

/**
 * Bean描述信息工厂类<br>
 * 通过不同的类和策略，生成对应的{@link BeanDesc}，策略包括：
 * <ul>
 *     <li>当类为Record时，生成{@link RecordBeanDesc}</li>
 *     <li>当类为普通Bean时，生成{@link StrictBeanDesc}</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public class BeanDescFactory {

	private static final WeakConcurrentMap<Class<?>, BeanDesc> bdCache = new WeakConcurrentMap<>();

	/**
	 * 获取{@link BeanDesc} Bean描述信息，使用Weak缓存
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 */
	public static BeanDesc getBeanDesc(final Class<?> clazz) {
		return bdCache.computeIfAbsent(clazz, (key) -> getBeanDescWithoutCache(clazz));
	}

	/**
	 * 获取{@link BeanDesc} Bean描述信息，不使用缓存
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 */
	public static BeanDesc getBeanDescWithoutCache(final Class<?> clazz) {
		if (RecordUtil.isRecord(clazz)) {
			return new RecordBeanDesc(clazz);
		} else if (JdkProxyUtil.isProxyClass(clazz) || ArrayUtil.isEmpty(FieldUtil.getFields(clazz))) {
			// 代理类和空字段的Bean不支持属性获取，直接使用方法方式
			return new SimpleBeanDesc(clazz);
		} else {
			return new StrictBeanDesc(clazz);
		}
	}

	/**
	 * 清空全局的Bean属性缓存
	 *
	 * @since 5.7.21
	 */
	public static void clearCache() {
		bdCache.clear();
	}
}
