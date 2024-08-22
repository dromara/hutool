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

package org.dromara.hutool.cron.pattern.matcher;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Collections;
import java.util.List;

/**
 * 将表达式中的数字值列表转换为Boolean数组，匹配时匹配相应数组位
 *
 * @author Looly
 */
public class BoolArrayMatcher implements PartMatcher {

	/**
	 * 用户定义此字段的最小值
	 */
	protected final int minValue;
	protected final boolean[] bValues;

	/**
	 * 构造
	 *
	 * @param intValueList 匹配值列表
	 */
	public BoolArrayMatcher(final List<Integer> intValueList) {
		Assert.isTrue(CollUtil.isNotEmpty(intValueList), "Values must be not empty!");
		bValues = new boolean[Collections.max(intValueList) + 1];
		int min = Integer.MAX_VALUE;
		for (final Integer value : intValueList) {
			min = Math.min(min, value);
			bValues[value] = true;
		}
		this.minValue = min;
	}

	@Override
	public boolean test(final Integer value) {
		final boolean[] bValues = this.bValues;
		if (null == value || value >= bValues.length) {
			return false;
		}
		return bValues[value];
	}

	@Override
	public int nextAfter(int value) {
		final int minValue = this.minValue;
		if(value > minValue){
			final boolean[] bValues = this.bValues;
			while(value < bValues.length){
				if(bValues[value]){
					return value;
				}
				value++;
			}
		}

		// 两种情况返回最小值
		// 一是给定值小于最小值，那下一个匹配值就是最小值
		// 二是给定值大于最大值，那下一个匹配值也是下一轮的最小值
		return minValue;
	}

	/**
	 * 获取表达式定义的最小值
	 *
	 * @return 最小值
	 */
	public int getMinValue() {
		return this.minValue;
	}

	@Override
	public String toString() {
		return StrUtil.format("Matcher:{}", new Object[]{this.bValues});
	}
}
