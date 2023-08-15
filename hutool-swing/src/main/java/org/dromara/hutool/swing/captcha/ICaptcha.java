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

package org.dromara.hutool.swing.captcha;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * 验证码接口，提供验证码对象接口定义
 *
 * @author looly
 *
 */
public interface ICaptcha extends Serializable{

	/**
	 * 创建验证码，实现类需同时生成随机验证码字符串和验证码图片
	 */
	void createCode();

	/**
	 * 获取验证码的文字内容
	 *
	 * @return 验证码文字内容
	 */
	String getCode();

	/**
	 * 验证验证码是否正确，建议忽略大小写
	 *
	 * @param userInputCode 用户输入的验证码
	 * @return 是否与生成的一直
	 */
	boolean verify(String userInputCode);

	/**
	 * 将验证码写出到目标流中
	 *
	 * @param out 目标流
	 */
	void write(OutputStream out);
}
