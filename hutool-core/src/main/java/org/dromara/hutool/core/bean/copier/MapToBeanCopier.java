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

import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map属性拷贝到Bean中的拷贝器
 *
 * @param <T> 目标Bean类型
 * @since 5.8.0
 */
public class MapToBeanCopier<T> extends AbsCopier<Map<?, ?>, T> {

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
	public MapToBeanCopier(final Map<?, ?> source, final T target, final Type targetType, final CopyOptions copyOptions) {
		super(source, target, copyOptions);

		// 针对MapWrapper特殊处理，提供的Map包装了忽略大小写的Map，则默认转Bean的时候也忽略大小写，如JSONObject
		if(source instanceof MapWrapper){
			final Map<?, ?> raw = ((MapWrapper<?, ?>) source).getRaw();
			if(raw instanceof CaseInsensitiveMap){
				copyOptions.setIgnoreCase(true);
			}
		}

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

		this.source.forEach((sKey, sValue) -> {
			if (null == sKey) {
				return;
			}

			// 编辑键值对
			final MutableEntry<Object, Object> entry = copyOptions.editField(sKey, sValue);
			if(null == entry){
				return;
			}
			final Object sFieldName = entry.getKey();
			// 对key做转换，转换后为null的跳过
			if (null == sFieldName) {
				return;
			}

			// 检查目标字段可写性
			// 目标字段检查放在键值对编辑之后，因为键可能被编辑修改
			final PropDesc tDesc = this.copyOptions.findPropDesc(targetPropDescMap, sFieldName.toString());
			if (null == tDesc || !tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			Object newValue = entry.getValue();
			// 检查目标是否过滤属性
			if (!copyOptions.testPropertyFilter(tDesc.getField(), newValue)) {
				return;
			}

			// 获取目标字段真实类型并转换源值
			final Type fieldType = TypeUtil.getActualType(this.targetType, tDesc.getFieldType());
			newValue = this.copyOptions.convertField(fieldType, newValue);

			// 目标赋值
			tDesc.setValue(this.target, newValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}
}
