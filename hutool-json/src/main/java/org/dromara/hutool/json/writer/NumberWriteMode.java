/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
