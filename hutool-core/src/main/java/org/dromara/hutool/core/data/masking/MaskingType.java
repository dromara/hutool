/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.data.masking;

/**
 * 支持的脱敏类型枚举
 *
 * @author dazer and neusoft and qiaomu
 */
public enum MaskingType {
	/**
	 * 用户id
	 */
	USER_ID,
	/**
	 * 中文名
	 */
	CHINESE_NAME,
	/**
	 * 身份证号
	 */
	ID_CARD,
	/**
	 * 座机号
	 */
	FIXED_PHONE,
	/**
	 * 手机号
	 */
	MOBILE_PHONE,
	/**
	 * 地址
	 */
	ADDRESS,
	/**
	 * 电子邮件
	 */
	EMAIL,
	/**
	 * 密码
	 */
	PASSWORD,
	/**
	 * 中国大陆车牌，包含普通车辆、新能源车辆
	 */
	CAR_LICENSE,
	/**
	 * 银行卡
	 */
	BANK_CARD,
	/**
	 * IPv4地址
	 */
	IPV4,
	/**
	 * IPv6地址
	 */
	IPV6,
	/**
	 * 定义了一个first_mask的规则，只显示第一个字符。
	 */
	FIRST_MASK,
	/**
	 * 清空为null
	 */
	CLEAR_TO_NULL,
	/**
	 * 清空为""
	 */
	CLEAR_TO_EMPTY
}
