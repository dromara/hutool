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

package org.dromara.hutool.core.lang.selector;

/**
 * 平滑权重对象
 *
 * @param <T> 对象类型
 * @author Wind, Loolt
 */
public class SmoothWeightObj<T> extends WeightObj<T> {

	private int currentWeight;

	/**
	 * 构造
	 *
	 * @param obj           对象
	 * @param weight        权重
	 */
	public SmoothWeightObj(final T obj, final int weight) {
		this(obj, weight, 0);
	}

	/**
	 * 构造
	 *
	 * @param obj           对象
	 * @param weight        权重
	 * @param currentWeight 当前权重
	 */
	public SmoothWeightObj(final T obj, final int weight, final int currentWeight) {
		super(obj, weight);
		this.currentWeight = currentWeight;
	}

	/**
	 * 获取当前权重
	 *
	 * @return int 临时权重
	 */
	public int getCurrentWeight() {
		return currentWeight;
	}

	/**
	 * setCurrentWeight
	 * <p>设置当前权重
	 *
	 * @param currentWeight 权重值
	 */
	public void setCurrentWeight(final int currentWeight) {
		this.currentWeight = currentWeight;
	}
}
