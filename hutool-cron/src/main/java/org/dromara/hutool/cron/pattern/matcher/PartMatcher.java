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

package org.dromara.hutool.cron.pattern.matcher;

import java.util.function.Predicate;

/**
 * 表达式中的某个位置部分匹配器<br>
 * 用于匹配日期位中对应数字是否匹配
 *
 * @author Looly
 */
public interface PartMatcher extends Predicate<Integer> {

	/**
	 * 获取指定值之后的匹配值，也可以是指定值本身
	 *
	 * @param value 指定的值
	 * @return 匹配到的值或之后的值
	 */
	int nextAfter(int value);
}
