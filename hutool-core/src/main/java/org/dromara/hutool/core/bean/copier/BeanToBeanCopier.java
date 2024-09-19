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

import org.dromara.hutool.core.bean.BeanException;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean属性拷贝到Bean中的拷贝器
 *
 * @param <S> 源Bean类型
 * @param <T> 目标Bean类型
 * @since 5.8.0
 */
public class BeanToBeanCopier<S, T> extends AbsCopier<S, T> {

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
	public BeanToBeanCopier(final S source, final T target, final Type targetType, final CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@Override
	public T copy() {
		final CopyOptions copyOptions = this.copyOptions;
		Class<?> actualEditable = target.getClass();
		if (null != copyOptions.editable) {
			// 检查限制类是否为target的父类或接口
			Assert.isTrue(copyOptions.editable.isInstance(target),
					"Target class [{}] not assignable to Editable class [{}]", actualEditable.getName(), copyOptions.editable.getName());
			actualEditable = copyOptions.editable;
		}
		final Map<String, PropDesc> targetPropDescMap = getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);
		if(MapUtil.isEmpty(targetPropDescMap)){
			if(copyOptions.ignoreError){
				return target;
			}
			throw new BeanException("No properties for target: {}", actualEditable);
		}

		final Map<String, PropDesc> sourcePropDescMap = getBeanDesc(source.getClass()).getPropMap(copyOptions.ignoreCase);
		if(MapUtil.isEmpty(sourcePropDescMap)){
			if(copyOptions.ignoreError){
				return target;
			}
			throw new BeanException("No properties for source: {}", source.getClass());
		}

		sourcePropDescMap.forEach((sFieldName, sDesc) -> {
			if (null == sFieldName || !sDesc.isReadable(copyOptions.transientSupport)) {
				// 字段空或不可读，跳过
				return;
			}

			// 检查源对象属性是否过滤属性
			Object sValue = sDesc.getValue(this.source, copyOptions.ignoreError);
			if (!this.copyOptions.testPropertyFilter(sDesc.getField(), sValue)) {
				return;
			}

			// 编辑键值对
			final MutableEntry<Object, Object> entry = copyOptions.editField(sFieldName, sValue);
			if(null == entry){
				return;
			}
			sFieldName = StrUtil.toStringOrNull(entry.getKey());
			// 对key做转换，转换后为null的跳过
			if (null == sFieldName) {
				return;
			}
			sValue = entry.getValue();

			// 检查目标字段可写性
			// 目标字段检查放在键值对编辑之后，因为键可能被编辑修改
			final PropDesc tDesc = copyOptions.findPropDesc(targetPropDescMap, sFieldName);
			if (null == tDesc || !tDesc.isWritable(copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			// 获取目标字段真实类型并转换源值
			final Type fieldType = TypeUtil.getActualType(this.targetType, tDesc.getFieldType());
			//sValue = Convert.convertWithCheck(fieldType, sValue, null, this.copyOptions.ignoreError);
			sValue = copyOptions.convertField(fieldType, sValue);

			// 目标赋值
			tDesc.setValue(this.target, sValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}
}
