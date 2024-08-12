/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * {@link ValueProvider}属性拷贝到Bean中的拷贝器
 *
 * @param <T> 目标Bean类型
 * @since 5.8.0
 */
public class ValueProviderToBeanCopier<T> extends AbsCopier<ValueProvider<String>, T> {

	/**
	 * 目标的类型（用于泛型类注入）
	 */
	private final Type targetType;

	/**
	 * 构造
	 *
	 * @param source      来源Map
	 * @param target      目标Bean对象
	 * @param targetType  目标泛型类型
	 * @param copyOptions 拷贝选项
	 */
	public ValueProviderToBeanCopier(final ValueProvider<String> source, final T target, final Type targetType, final CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@Override
	public T copy() {
		Class<?> actualEditable = target.getClass();
		if (null != copyOptions.editable) {
			// 检查限制类是否为target的父类或接口
			Assert.isTrue(copyOptions.editable.isInstance(target),
					"Target class [{}] not assignable to Editable class [{}]", actualEditable.getName(), copyOptions.editable.getName());
			actualEditable = copyOptions.editable;
		}
		final Map<String, PropDesc> targetPropDescMap = getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);

		targetPropDescMap.forEach((tFieldName, tDesc) -> {
			if (null == tFieldName) {
				return;
			}

			// 检查目标字段可写性
			if (null == tDesc || !tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			// 获取目标字段真实类型
			final Type fieldType = TypeUtil.getActualType(this.targetType ,tDesc.getFieldType());
			// 编辑键值对
			final MutableEntry<Object, Object> entry = copyOptions.editField(tFieldName, null);
			if(null == entry){
				return;
			}
			tFieldName = StrUtil.toStringOrNull(entry.getKey());
			// 对key做转换，转换后为null的跳过
			if (null == tFieldName) {
				return;
			}
			// 无字段内容跳过
			if(!source.containsKey(tFieldName)){
				return;
			}
			final Object sValue = source.value(tFieldName, fieldType);

			// 检查目标对象属性是否过滤属性
			if (!copyOptions.testPropertyFilter(tDesc.getField(), sValue)) {
				return;
			}

			// 目标赋值
			tDesc.setValue(this.target, sValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}
}
