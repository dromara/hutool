package cn.hutool.core.text.replacer;

import cn.hutool.core.text.StrBuilder;

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
	private final Set<Character> prefixSet;
	private final int minLength;
	private final int maxLength;

	/**
	 * 构造
	 * 
	 * @param lookup 被查找的键值对
	 */
	public LookupReplacer(String[]... lookup) {
		this.lookupMap = new HashMap<>();
		this.prefixSet = new HashSet<>();

		int minLength = Integer.MAX_VALUE;
		int maxLength = 0;
		String key;
		int keySize;
		for (String[] pair : lookup) {
			key = pair[0];
			lookupMap.put(key, pair[1]);
			this.prefixSet.add(key.charAt(0));
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
	protected int replace(CharSequence str, int pos, StrBuilder out) {
		if (prefixSet.contains(str.charAt(pos))) {
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
