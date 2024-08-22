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
