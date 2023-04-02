/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.bean.copier;

import org.dromara.hutool.bean.BeanUtil;
import org.dromara.hutool.bean.PropDesc;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.lang.mutable.MutableEntry;
import org.dromara.hutool.reflect.TypeUtil;

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
		final Map<String, PropDesc> targetPropDescMap = BeanUtil.getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);

		targetPropDescMap.forEach((tFieldName, tDesc) -> {
			if (null == tFieldName) {
				return;
			}

			// 检查目标字段可写性
			if (null == tDesc || false == tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			// 获取目标字段真实类型
			final Type fieldType = TypeUtil.getActualType(this.targetType ,tDesc.getFieldType());
			// 编辑键值对
			final MutableEntry<String, Object> entry = copyOptions.editField(tFieldName, null);
			if(null == entry){
				return;
			}
			tFieldName = entry.getKey();
			// 对key做转换，转换后为null的跳过
			if (null == tFieldName) {
				return;
			}
			// 无字段内容跳过
			if(false == source.containsKey(tFieldName)){
				return;
			}
			final Object sValue = source.value(tFieldName, fieldType);

			// 检查目标对象属性是否过滤属性
			if (false == copyOptions.testPropertyFilter(tDesc.getField(), sValue)) {
				return;
			}

			// 目标赋值
			tDesc.setValue(this.target, sValue, copyOptions.ignoreNullValue, copyOptions.ignoreError, copyOptions.override);
		});
		return this.target;
	}
}
