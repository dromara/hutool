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

package org.dromara.hutool.client.body;

import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.io.resource.FileResource;
import org.dromara.hutool.io.resource.MultiFileResource;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.map.TableMap;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.util.ObjUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;

/**
 * Form表单形式的消息体
 *
 * @param <T> this类型，用于链式调用
 * @author looly
 */
@SuppressWarnings("unchecked")
public abstract class FormBody<T extends FormBody<T>> implements HttpBody {
	/**
	 * 存储表单数据
	 */
	protected Map<String, Object> form;
	/**
	 * 编码
	 */
	protected final Charset charset;

	/**
	 * 构造
	 *
	 * @param form    表单
	 * @param charset 编码
	 */
	protected FormBody(final Map<String, Object> form, final Charset charset) {
		this.form = form;
		this.charset = charset;
	}

	/**
	 * 设置map类型表单数据
	 *
	 * @param formMap 表单内容
	 * @return this
	 */
	public T form(final Map<String, Object> formMap) {
		if (MapUtil.isNotEmpty(formMap)) {
			formMap.forEach(this::form);
		}
		return (T) this;
	}

	/**
	 * 设置表单数据<br>
	 * 如果传入值为{@code null}，则移除这个表单项
	 *
	 * @param name  名，blank则忽略之
	 * @param value 值为{@code null}，则移除这个表单项
	 * @return this
	 */
	public T form(final String name, final Object value) {
		if (StrUtil.isBlank(name)) {
			return (T) this; // 忽略非法的form表单项内容;
		}
		if(ObjUtil.isNull(value)){
			this.form.remove(name);
		}

		if (value instanceof File) {
			return putToForm(name, new FileResource((File) value));
		} else if(value instanceof Path){
			return putToForm(name, new FileResource((Path) value));
		}

		// 普通值
		final String strValue;
		if (value instanceof Iterable) {
			// 列表对象
			strValue = CollUtil.join((Iterable<?>) value, ",");
		} else if (ArrayUtil.isArray(value)) {
			final Class<?> componentType = ArrayUtil.getComponentType(value);
			// 多文件
			if (File.class == componentType) {
				return putToForm(name, new MultiFileResource((File[])value));
			} else if (Path.class == componentType) {
				return putToForm(name, new MultiFileResource((Path[])value));
			}
			// 数组对象
			strValue = ArrayUtil.join(value, ",");
		} else {
			// 其他对象一律转换为字符串
			strValue = Convert.toStr(value, null);
		}

		return putToForm(name, strValue);
	}

	/**
	 * 将参数加入到form中，如果form为空，新建之。
	 *
	 * @param name  表单属性名
	 * @param value 属性值
	 * @return this
	 */
	private T putToForm(final String name, final Object value) {
		if (null != name && null != value) {
			if (null == this.form) {
				this.form = new TableMap<>(16);
			}
			this.form.put(name, value);
		}
		return (T) this;
	}
}
