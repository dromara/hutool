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

/**
 * BeanPath节点对应的Bean工厂，提供Bean的创建、获取和设置接口<br>
 *
 * @param <T> Bean类型
 * @author looly
 * @since 6.0.0
 */
public interface NodeBeanFactory<T> {

	/**
	 * 创建Bean<br>
	 * beanPath对应当前的路径，即如果父对象为：a，则beanPath为：a.b，则创建的Bean为：a.b.c对应的Bean对象<br>
	 * 给定的a一定存在，但是本路径中b对应的Bean不存在，则创建的对象是b的值，这个值用c表示
	 *
	 * @param parent   父Bean
	 * @param beanPath 当前路径
	 * @return Bean
	 */
	T create(final T parent, final BeanPath<T> beanPath);

	/**
	 * 获取Bean对应节点的值
	 *
	 * @param bean     bean对象
	 * @param beanPath 当前路径
	 * @return 节点值
	 */
	Object getValue(T bean, final BeanPath<T> beanPath);

	/**
	 * 设置节点值
	 *
	 * @param bean     bean对象
	 * @param value    节点值
	 * @param beanPath 当前路径
	 * @return bean对象。如果在原Bean对象基础上设置值，返回原Bean，否则返回新的Bean
	 */
	T setValue(T bean, Object value, final BeanPath<T> beanPath);
}
