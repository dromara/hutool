/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.swing.captcha.generator;

import cn.hutool.core.util.RandomUtil;

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
		this(RandomUtil.BASE_CHAR_NUMBER, count);
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
