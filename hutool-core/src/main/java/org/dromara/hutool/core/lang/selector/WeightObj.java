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

package org.dromara.hutool.core.lang.selector;

import java.util.Objects;

/**
 * 带有权重的对象包装
 *
 * @author looly
 *
 * @param <T> 对象类型
 */
public class WeightObj<T> {
	/** 对象 */
	protected T obj;
	/** 权重 */
	protected final int weight;

	/**
	 * 构造
	 *
	 * @param obj 对象
	 * @param weight 权重
	 */
	public WeightObj(final T obj, final int weight) {
		this.obj = obj;
		this.weight = weight;
	}

	/**
	 * 获取对象
	 *
	 * @return 对象
	 */
	public T getObj() {
		return obj;
	}

	/**
	 * 设置对象
	 *
	 * @param obj 对象
	 */
	public void setObj(final T obj) {
		this.obj = obj;
	}

	/**
	 * 获取权重
	 *
	 * @return 权重
	 */
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final WeightObj<?> weightObj = (WeightObj<?>) o;
		return weight == weightObj.weight && Objects.equals(obj, weightObj.obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(obj, weight);
	}
}
