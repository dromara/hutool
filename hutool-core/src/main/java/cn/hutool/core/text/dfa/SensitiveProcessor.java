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

package cn.hutool.core.text.dfa;

/**
 * @author 肖海斌
 * 敏感词过滤处理器，默认按字符数替换成*
 */
public interface SensitiveProcessor {

	/**
	 * 敏感词过滤处理
	 * @param foundWord 敏感词匹配到的内容
	 * @return 敏感词过滤后的内容，默认按字符数替换成*
	 */
	default String process(final FoundWord foundWord) {
		final int length = foundWord.getFoundWord().length();
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append("*");
		}
		return sb.toString();
	}
}
