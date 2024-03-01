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

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 随机字符验证码生成器<br>
 * 可以通过传入的基础集合和长度随机生成验证码字符
 *
 * @author looly
 * @since 4.1.2
 */
public class RandomGenerator extends AbstractGenerator {
	private static final long serialVersionUID = -7802758587765561876L;

	/**
	 * 构造，使用字母+数字做为基础
	 *
	 * @param count 生成验证码长度
	 */
	public RandomGenerator(final int count) {
		super(count);
	}

	/**
	 * 构造
	 *
	 * @param baseStr 基础字符集合，用于随机获取字符串的字符集合
	 * @param length 生成验证码长度
	 */
	public RandomGenerator(final String baseStr, final int length) {
		super(baseStr, length);
	}

	@Override
	public String generate() {
		return RandomUtil.randomString(this.baseStr, this.length);
	}

	@Override
	public boolean verify(final String code, final String userInputCode) {
		if (StrUtil.isNotBlank(userInputCode)) {
			return StrUtil.equalsIgnoreCase(code, userInputCode);
		}
		return false;
	}
}
