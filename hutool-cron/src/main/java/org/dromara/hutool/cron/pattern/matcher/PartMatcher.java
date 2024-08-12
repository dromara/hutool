/*
 * Copyright (c) 2013-2024 Hutool Team.
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
