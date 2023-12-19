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

package org.dromara.hutool.extra.pinyin.engine.pinyin4j;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.extra.pinyin.engine.PinyinEngine;
import org.dromara.hutool.extra.pinyin.PinyinException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 封装了Pinyin4j的引擎。
 *
 * <p>
 * pinyin4j(<a href="http://sourceforge.net/projects/pinyin4j">http://sourceforge.net/projects/pinyin4j</a>)封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.belerweb&lt;/groupId&gt;
 *     &lt;artifactId&gt;pinyin4j&lt;/artifactId&gt;
 *     &lt;version&gt;2.5.1&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class Pinyin4jEngine implements PinyinEngine {

	//设置汉子拼音输出的格式
	private HanyuPinyinOutputFormat format;

	/**
	 * 构造
	 */
	public Pinyin4jEngine() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param format 格式
	 */
	public Pinyin4jEngine(final HanyuPinyinOutputFormat format) {
		init(format);
	}

	/**
	 * 初始化
	 *
	 * @param format 格式
	 */
	public void init(HanyuPinyinOutputFormat format) {
		if (null == format) {
			format = new HanyuPinyinOutputFormat();
			// 小写
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			// 不加声调
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			// 'ü' 使用 "v" 代替
			format.setVCharType(HanyuPinyinVCharType.WITH_V);
		}
		this.format = format;
	}

	@Override
	public String getPinyin(final char c) {
		String result;
		try {
			final String[] results = PinyinHelper.toHanyuPinyinStringArray(c, format);
			result = ArrayUtil.isEmpty(results) ? String.valueOf(c) : results[0];
		} catch (final BadHanyuPinyinOutputFormatCombination e) {
			result = String.valueOf(c);
		}
		return result;
	}

	@Override
	public String getPinyin(final String str, final String separator) {
		final StringBuilder result = new StringBuilder();
		boolean isFirst = true;
		final int strLen = str.length();
		try {
			for(int i = 0; i < strLen; i++){
				if(isFirst){
					isFirst = false;
				} else{
					result.append(separator);
				}
				final String[] pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), format);
				if(ArrayUtil.isEmpty(pinyinStringArray)){
					result.append(str.charAt(i));
				} else{
					result.append(pinyinStringArray[0]);
				}
			}
		} catch (final BadHanyuPinyinOutputFormatCombination e) {
			throw new PinyinException(e);
		}

		return result.toString();
	}
}
