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

package org.dromara.hutool.json.writer;

/**
 * Long写出模式<br>
 * 考虑到在JS或其他环境中，Long超过一定长度会丢失精度，因此针对Long类型值，可选写出规则
 *
 * @author Looly
 * @since 6.0.0
 */
public enum NumberWriteMode {
	/**
	 * 一般模式，所有Long值写出为普通数字，如{"value": 123456789}
	 */
	NORMAL,
	/**
	 * 浏览器中Javascript兼容模式，此模式下，如果Long输出长度大于JS中最大长度，则转为字符串形式
	 */
	JS,
	/**
	 * 所有Long类型的数字均转为字符串形式，如{"value": "123456789"}
	 */
	STRING
}
