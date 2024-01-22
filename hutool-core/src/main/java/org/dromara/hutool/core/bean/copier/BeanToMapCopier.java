/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean.copier;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Bean属性拷贝到Map中的拷贝器
 *
 * @since 5.8.0
 */
@SuppressWarnings("rawtypes")
public class BeanToMapCopier extends AbsCopier<Object, Map> {

	/**
	 * 目标的Map类型（用于泛型类注入）
	 */
	private final Type targetType;

	/**
	 * 构造
	 *
	 * @param source      来源Map
	 * @param target      目标Map对象
	 * @param targetType  目标泛型类型
	 * @param copyOptions 拷贝选项
	 */
	public BeanToMapCopier(final Object source, final Map target, final Type targetType, final CopyOptions copyOptions) {
		super(source, target, copyOptions);
		this.targetType = targetType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map copy() {
		Class<?> actualEditable = source.getClass();
		if (null != copyOptions.editable) {
			// 检查限制类是否为target的父类或接口
			Assert.isTrue(copyOptions.editable.isInstance(source),
					"Source class [{}] not assignable to Editable class [{}]", actualEditable.getName(), copyOptions.editable.getName());
			actualEditable = copyOptions.editable;
		}

		final Map<String, PropDesc> sourcePropDescMap = BeanUtil.getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);
		sourcePropDescMap.forEach((sFieldName, sDesc) -> {
			if (null == sFieldName || !sDesc.isReadable(copyOptions.transientSupport)) {
				// 字段空或不可读，跳过
				return;
			}

			// 检查源对象属性是否过滤属性
			Object sValue = sDesc.getValue(this.source);
			if (!copyOptions.testPropertyFilter(sDesc.getField(), sValue)) {
				return;
			}

			// 编辑键值对
			final MutableEntry<String, Object> entry = copyOptions.editField(sFieldName, sValue);
			if(null == entry){
				return;
			}
			sFieldName = entry.getKey();
			// 对key做转换，转换后为null的跳过
			if (null == sFieldName) {
				return;
			}
			sValue = entry.getValue();

			// 获取目标值真实类型并转换源值
			final Type[] typeArguments = TypeUtil.getTypeArguments(this.targetType);
			if(null != typeArguments && typeArguments.length > 1){
				//sValue = Convert.convertWithCheck(typeArguments[1], sValue, null, this.copyOptions.ignoreError);
				sValue = this.copyOptions.convertField(typeArguments[1], sValue);
			}

			// 目标赋值
			if(null != sValue || !copyOptions.ignoreNullValue){
				target.put(sFieldName, sValue);
			}
		});
		return this.target;
	}
}
