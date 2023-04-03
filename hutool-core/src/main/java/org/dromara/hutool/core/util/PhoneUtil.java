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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;


/**
 * 电话号码工具类，包括：
 * <ul>
 *     <li>手机号码</li>
 *     <li>400、800号码</li>
 *     <li>座机号码</li>
 * </ul>
 *
 * @author dahuoyzs
 * @since 5.3.11
 */
public class PhoneUtil {

	/**
	 * 验证是否为手机号码（中国）
	 *
	 * @param value 值
	 * @return 是否为手机号码（中国）
	 * @since 5.3.11
	 */
	public static boolean isMobile(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE, value);
	}

	/**
	 * 验证是否为手机号码（中国香港）
	 * @param value 手机号码
	 * @return 是否为中国香港手机号码
	 * @since 5.6.3
	 * @author dazer, ourslook
	 */
	public static boolean isMobileHk(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_HK, value);
	}

	/**
	 * 验证是否为手机号码（中国台湾）
	 * @param value 手机号码
	 * @return 是否为中国台湾手机号码
	 * @since 5.6.6
	 * @author ihao
	 */
	public static boolean isMobileTw(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_TW, value);
	}

	/**
	 * 验证是否为手机号码（中国澳门）
	 * @param value 手机号码
	 * @return 是否为中国澳门手机号码
	 * @since 5.6.6
	 * @author ihao
	 */
	public static boolean isMobileMo(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_MO, value);
	}

	/**
	 * 验证是否为座机号码（中国大陆）
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国大陆）
	 * @since 5.3.11
	 */
	public static boolean isTel(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.TEL, value);
	}

	/**
	 * 验证是否为座机号码（中国大陆）+ 400 + 800
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国大陆）
	 * @since 5.6.3
	 * @author dazer, ourslook
	 */
	public static boolean isTel400800(final CharSequence value) {
		return Validator.isMatchRegex(PatternPool.TEL_400_800, value);
	}

	/**
	 * 验证是否为座机号码+手机号码（CharUtil中国大陆）+ 400 + 800电话 + 手机号号码（中国香港）
	 *
	 * @param value 值
	 * @return 是否为座机号码+手机号码（中国大陆）+手机号码（中国香港）+手机号码（中国台湾）+手机号码（中国澳门）
	 * @since 5.3.11
	 */
	public static boolean isPhone(final CharSequence value) {
		return isMobile(value) || isTel400800(value) || isMobileHk(value) || isMobileTw(value) || isMobileMo(value);
	}

	/**
	 * 隐藏手机号前7位  替换字符为"*"
	 * 栗子
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideBefore(final CharSequence phone) {
		return StrUtil.hide(phone, 0, 7);
	}

	/**
	 * 隐藏手机号中间4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideBetween(final CharSequence phone) {
		return StrUtil.hide(phone, 3, 7);
	}

	/**
	 * 隐藏手机号最后4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideAfter(final CharSequence phone) {
		return StrUtil.hide(phone, 7, 11);
	}

	/**
	 * 获取手机号前3位
	 *
	 * @param phone 手机号码
	 * @return 手机号前3位
	 * @since 5.3.11
	 */
	public static CharSequence subBefore(final CharSequence phone) {
		return StrUtil.sub(phone, 0, 3);
	}

	/**
	 * 获取手机号中间4位
	 *
	 * @param phone 手机号码
	 * @return 手机号中间4位
	 * @since 5.3.11
	 */
	public static CharSequence subBetween(final CharSequence phone) {
		return StrUtil.sub(phone, 3, 7);
	}

	/**
	 * 获取手机号后4位
	 *
	 * @param phone 手机号码
	 * @return 手机号后4位
	 * @since 5.3.11
	 */
	public static CharSequence subAfter(final CharSequence phone) {
		return StrUtil.sub(phone, 7, 11);
	}

	/**
	 * 获取固话号码中的区号
	 *
	 * @param value 完整的固话号码
	 * @return 固话号码的区号部分
	 * @since 5.7.7
	 */
	public static CharSequence subTelBefore(final CharSequence value)
	{
		return ReUtil.getGroup1(PatternPool.TEL, value);
	}

	/**
	 * 获取固话号码中的号码
	 *
	 * @param value 完整的固话号码
	 * @return 固话号码的号码部分
	 * @since 5.7.7
	 */
	public static CharSequence subTelAfter(final CharSequence value)
	{
		return ReUtil.get(PatternPool.TEL, value, 2);
	}
}
