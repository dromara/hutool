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

package org.dromara.hutool.swing.captcha.generator;

import java.io.Serializable;

/**
 * 验证码文字生成器
 *
 * @author looly
 * @since 4.1.2
 */
public interface CodeGenerator extends Serializable{

	/**
	 * 生成验证码
	 *
	 * @return 验证码
	 */
	String generate();

	/**
	 * 验证用户输入的字符串是否与生成的验证码匹配<br>
	 * 用户通过实现此方法定义验证码匹配方式
	 *
	 * @param code 生成的随机验证码
	 * @param userInputCode 用户输入的验证码
	 * @return 是否验证通过
	 */
	boolean verify(String code, String userInputCode);
}
