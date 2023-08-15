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

package org.dromara.hutool.core.text.replacer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 查找替换器，通过查找指定关键字，替换对应的值
 *
 * @author looly
 * @since 4.1.5
 */
public class LookupReplacer extends StrReplacer {
	private static final long serialVersionUID = 1L;

	private final Map<String, String> lookupMap;
	private final Set<Character> keyPrefixSkeyet;
	private final int minLength;
	private final int maxLength;

	/**
	 * 构造
	 *
	 * @param lookup 被查找的键值对，每个String[]表示一个键值对
	 */
	public LookupReplacer(final String[]... lookup) {
		this.lookupMap = new HashMap<>(lookup.length, 1);
		this.keyPrefixSkeyet = new HashSet<>(lookup.length, 1);

		int minLength = Integer.MAX_VALUE;
		int maxLength = 0;
		String key;
		int keySize;
		for (final String[] pair : lookup) {
			key = pair[0];
			lookupMap.put(key, pair[1]);
			this.keyPrefixSkeyet.add(key.charAt(0));
			keySize = key.length();
			if (keySize > maxLength) {
				maxLength = keySize;
			}
			if (keySize < minLength) {
				minLength = keySize;
			}
		}
		this.maxLength = maxLength;
		this.minLength = minLength;
	}

	@Override
	protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
		if (keyPrefixSkeyet.contains(str.charAt(pos))) {
			int max = this.maxLength;
			if (pos + this.maxLength > str.length()) {
				max = str.length() - pos;
			}
			CharSequence subSeq;
			String result;
			for (int i = max; i >= this.minLength; i--) {
				subSeq = str.subSequence(pos, pos + i);
				result = lookupMap.get(subSeq.toString());
				if(null != result) {
					out.append(result);
					return i;
				}
			}
		}
		return 0;
	}

}
