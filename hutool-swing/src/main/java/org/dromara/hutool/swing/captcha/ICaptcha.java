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
