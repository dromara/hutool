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

package org.dromara.hutool.swing.captcha.generator;

import org.dromara.hutool.core.util.RandomUtil;

/**
 * 随机字符验证码生成器<br>
 * 可以通过传入的基础集合和长度随机生成验证码字符
 *
 * @author looly
 * @since 4.1.2
 */
public abstract class AbstractGenerator implements CodeGenerator {
	private static final long serialVersionUID = 8685744597154953479L;

	/** 基础字符集合，用于随机获取字符串的字符集合 */
	protected final String baseStr;
	/** 验证码长度 */
	protected final int length;

	/**
	 * 构造，使用字母+数字做为基础
	 *
	 * @param count 生成验证码长度
	 */
	public AbstractGenerator(final int count) {
		this(RandomUtil.BASE_CHAR_NUMBER_LOWER, count);
	}

	/**
	 * 构造
	 *
	 * @param baseStr 基础字符集合，用于随机获取字符串的字符集合
	 * @param length 生成验证码长度
	 */
	public AbstractGenerator(final String baseStr, final int length) {
		this.baseStr = baseStr;
		this.length = length;
	}

	/**
	 * 获取长度验证码
	 *
	 * @return 验证码长度
	 */
	public int getLength() {
		return this.length;
	}
}
