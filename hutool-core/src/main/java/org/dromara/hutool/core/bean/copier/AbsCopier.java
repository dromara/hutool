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

package org.dromara.hutool.core.bean.copier;

import org.dromara.hutool.core.bean.BeanDesc;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.lang.copier.Copier;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.util.ObjUtil;

/**
 * 抽象的对象拷贝封装，提供来源对象、目标对象持有
 *
 * @param <S> 来源对象类型
 * @param <T> 目标对象类型
 * @author looly
 * @since 5.8.0
 */
public abstract class AbsCopier<S, T> implements Copier<T> {

	/**
	 * 来源对象
	 */
	protected final S source;
	/**
	 * 目标对象
	 */
	protected final T target;
	/**
	 * 拷贝选项
	 */
	protected final CopyOptions copyOptions;

	/**
	 * 构造
	 *
	 * @param source      源对象
	 * @param target      目标对象
	 * @param copyOptions 拷贝选项
	 */
	public AbsCopier(final S source, final T target, final CopyOptions copyOptions) {
		this.source = source;
		this.target = target;
		this.copyOptions = ObjUtil.defaultIfNull(copyOptions, CopyOptions::of);
	}

	/**
	 * 获取Bean描述信息<br>
	 * 如果用户自定义了{@link BeanDesc}实现，则使用，否则使用默认的规则
	 *
	 * @param actualEditable 需要解析的类
	 * @return {@link BeanDesc}
	 */
	protected BeanDesc getBeanDesc(final Class<?> actualEditable) {
		if (null != this.copyOptions) {
			final Class<BeanDesc> beanDescClass = copyOptions.beanDescClass;
			if (null != beanDescClass) {
				return ConstructorUtil.newInstance(beanDescClass, actualEditable);
			}
		}
		return BeanUtil.getBeanDesc(actualEditable);
	}
}
