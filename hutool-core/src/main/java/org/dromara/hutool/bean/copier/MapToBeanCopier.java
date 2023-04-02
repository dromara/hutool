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
import org.dromara.hutool.map.CaseInsensitiveMap;
import org.dromara.hutool.map.MapWrapper;
import org.dromara.hutool.reflect.TypeUtil;
import org.dromara.hutool.text.StrUtil;

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
		final Map<String, PropDesc> targetPropDescMap = BeanUtil.getBeanDesc(actualEditable).getPropMap(copyOptions.ignoreCase);

		this.source.forEach((sKey, sValue) -> {
			if (null == sKey) {
				return;
			}

			// 编辑键值对
			final MutableEntry<String, Object> entry = copyOptions.editField(sKey.toString(), sValue);
			if(null == entry){
				return;
			}
			final String sFieldName = entry.getKey();
			// 对key做转换，转换后为null的跳过
			if (null == sFieldName) {
				return;
			}

			// 检查目标字段可写性
			// 目标字段检查放在键值对编辑之后，因为键可能被编辑修改
			final PropDesc tDesc = findPropDesc(targetPropDescMap, sFieldName);
			if (null == tDesc || false == tDesc.isWritable(this.copyOptions.transientSupport)) {
				// 字段不可写，跳过之
				return;
			}

			Object newValue = entry.getValue();
			// 检查目标是否过滤属性
			if (false == copyOptions.testPropertyFilter(tDesc.getField(), newValue)) {
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

	/**
	 * 查找Map对应Bean的名称<br>
	 * 尝试原名称、转驼峰名称、isXxx去掉is的名称
	 *
	 * @param targetPropDescMap 目标bean的属性描述Map
	 * @param sKeyStr 键或字段名
	 * @return {@link PropDesc}
	 */
	private PropDesc findPropDesc(final Map<String, PropDesc> targetPropDescMap, String sKeyStr){
		PropDesc propDesc = targetPropDescMap.get(sKeyStr);
		if(null != propDesc){
			return propDesc;
		}

		// 转驼峰尝试查找
		sKeyStr = StrUtil.toCamelCase(sKeyStr);
		propDesc = targetPropDescMap.get(sKeyStr);
		return propDesc;
	}
}
