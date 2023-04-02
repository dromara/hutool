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

package org.dromara.hutool.date.format.parser;

import org.dromara.hutool.date.DateException;
import org.dromara.hutool.date.DatePattern;
import org.dromara.hutool.date.DateTime;
import org.dromara.hutool.date.format.DefaultDateBasic;
import org.dromara.hutool.regex.ReUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.CharUtil;

/**
 * ISO8601日期字符串（JDK的Date对象toString默认格式）解析，支持格式；
 * <ol>
 *   <li>yyyy-MM-dd'T'HH:mm:ss'Z'</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ssZ</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss.SSSZ</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss+0800</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss+08:00</li>
 * </ol>
 *
 * @author looly
 * @since 6.0.0
 */
public class ISO8601DateParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例对象
	 */
	public static ISO8601DateParser INSTANCE = new ISO8601DateParser();

	@Override
	public DateTime parse(String source) {
		final int length = source.length();
		if (StrUtil.contains(source, 'Z')) {
			if (length == DatePattern.UTC_PATTERN.length() - 4) {
				// 格式类似：2018-09-13T05:34:31Z，-4表示减去4个单引号的长度
				return new DateTime(source, DatePattern.UTC_FORMAT);
			}

			final int patternLength = DatePattern.UTC_MS_PATTERN.length();
			// 格式类似：2018-09-13T05:34:31.999Z，-4表示减去4个单引号的长度
			// -4 ~ -6范围表示匹配毫秒1~3位的情况
			if (length <= patternLength - 4 && length >= patternLength - 6) {
				return new DateTime(source, DatePattern.UTC_MS_FORMAT);
			}
		} else if (StrUtil.contains(source, '+')) {
			// 去除类似2019-06-01T19:45:43 +08:00加号前的空格
			source = source.replace(" +", "+");
			final String zoneOffset = StrUtil.subAfter(source, '+', true);
			if (StrUtil.isBlank(zoneOffset)) {
				throw new DateException("Invalid format: [{}]", source);
			}
			if (false == StrUtil.contains(zoneOffset, ':')) {
				// +0800转换为+08:00
				final String pre = StrUtil.subBefore(source, '+', true);
				source = pre + "+" + zoneOffset.substring(0, 2) + ":" + "00";
			}

			if (StrUtil.contains(source, CharUtil.DOT)) {
				// 带毫秒，格式类似：2018-09-13T05:34:31.999+08:00
				source = normalizeMillSeconds(source, ".", "+");
				return new DateTime(source, DatePattern.ISO8601_MS_WITH_XXX_OFFSET_FORMAT);
			} else {
				// 格式类似：2018-09-13T05:34:31+08:00
				return new DateTime(source, DatePattern.ISO8601_WITH_XXX_OFFSET_FORMAT);
			}
		} else if(ReUtil.contains("-\\d{2}:?00", source)){
			// Issue#2612，类似 2022-09-14T23:59:00-08:00 或者 2022-09-14T23:59:00-0800

			// 去除类似2019-06-01T19:45:43 -08:00加号前的空格
			source = source.replace(" -", "-");
			if(':' != source.charAt(source.length() - 3)){
				source = source.substring(0, source.length() - 2) + ":00";
			}

			if (StrUtil.contains(source, CharUtil.DOT)) {
				// 带毫秒，格式类似：2018-09-13T05:34:31.999-08:00
				source = normalizeMillSeconds(source, ".", "-");
				return new DateTime(source, DatePattern.ISO8601_MS_WITH_XXX_OFFSET_FORMAT);
			} else {
				// 格式类似：2018-09-13T05:34:31-08:00
				return new DateTime(source, DatePattern.ISO8601_WITH_XXX_OFFSET_FORMAT);
			}
		} else {
			if (length == DatePattern.ISO8601_PATTERN.length() - 2) {
				// 格式类似：2018-09-13T05:34:31
				return new DateTime(source, DatePattern.ISO8601_FORMAT);
			} else if (length == DatePattern.ISO8601_PATTERN.length() - 5) {
				// 格式类似：2018-09-13T05:34
				return new DateTime(source + ":00", DatePattern.ISO8601_FORMAT);
			} else if (StrUtil.contains(source, CharUtil.DOT)) {
				// 可能为：  2021-03-17T06:31:33.99
				source = normalizeMillSeconds(source, ".", null);
				return new DateTime(source, DatePattern.ISO8601_MS_FORMAT);
			}
		}
		// 没有更多匹配的时间格式
		throw new DateException("No UTC format fit for date String [{}] !", source);
	}

	/**
	 * 如果日期中的毫秒部分超出3位，会导致秒数增加，因此只保留前三位<br>
	 * issue#2887
	 *
	 * @param dateStr 日期字符串
	 * @param before  毫秒部分的前一个字符
	 * @param after   毫秒部分的后一个字符
	 * @return 规范之后的毫秒部分
	 */
	@SuppressWarnings("SameParameterValue")
	private static String normalizeMillSeconds(final String dateStr, final CharSequence before, final CharSequence after) {
		if (StrUtil.isBlank(after)) {
			final String millOrNaco = StrUtil.subPre(StrUtil.subAfter(dateStr, before, true), 3);
			return StrUtil.subBefore(dateStr, before, true) + before + millOrNaco;
		}
		final String millOrNaco = StrUtil.subPre(StrUtil.subBetween(dateStr, before, after), 3);
		return StrUtil.subBefore(dateStr, before, true)
				+ before
				+ millOrNaco + after + StrUtil.subAfter(dateStr, after, true);
	}
}
