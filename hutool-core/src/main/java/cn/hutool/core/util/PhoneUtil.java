package cn.hutool.core.util;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;


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
	public static boolean isMobile(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE, value);
	}

	/**
	 * 验证是否为手机号码（香港）
	 * @param value 手机号码
	 * @return 是否为香港手机号码
	 * @since 5.6.3
	 * @author dazer, ourslook
	 */
	public static boolean isMobileHk(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_HK, value);
	}

	/**
	 * 验证是否为手机号码（台湾）
	 * @param value 手机号码
	 * @return 是否为台湾手机号码
	 * @since 5.6.6
	 * @author ihao
	 */
	public static boolean isMobileTw(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_TW, value);
	}

	/**
	 * 验证是否为手机号码（澳门）
	 * @param value 手机号码
	 * @return 是否为澳门手机号码
	 * @since 5.6.6
	 * @author ihao
	 */
	public static boolean isMobileMo(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.MOBILE_MO, value);
	}

	/**
	 * 验证是否为座机号码（中国）
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国）
	 * @since 5.3.11
	 */
	public static boolean isTel(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.TEL, value);
	}

	/**
	 * 验证是否为座机号码（中国）+ 400 + 800
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国）
	 * @since 5.6.3
	 * @author dazer, ourslook
	 */
	public static boolean isTel400800(CharSequence value) {
		return Validator.isMatchRegex(PatternPool.TEL_400_800, value);
	}

	/**
	 * 验证是否为座机号码+手机号码（CharUtil中国）+ 400 + 800电话 + 手机号号码（香港）
	 *
	 * @param value 值
	 * @return 是否为座机号码+手机号码（中国）+手机号码（香港）+手机号码（台湾）+手机号码（澳门）
	 * @since 5.3.11
	 */
	public static boolean isPhone(CharSequence value) {
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
	public static CharSequence hideBefore(CharSequence phone) {
		return StrUtil.hide(phone, 0, 7);
	}

	/**
	 * 隐藏手机号中间4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideBetween(CharSequence phone) {
		return StrUtil.hide(phone, 3, 7);
	}

	/**
	 * 隐藏手机号最后4位  替换字符为"*"
	 *
	 * @param phone 手机号码
	 * @return 替换后的字符串
	 * @since 5.3.11
	 */
	public static CharSequence hideAfter(CharSequence phone) {
		return StrUtil.hide(phone, 7, 11);
	}

	/**
	 * 获取手机号前3位
	 *
	 * @param phone 手机号码
	 * @return 手机号前3位
	 * @since 5.3.11
	 */
	public static CharSequence subBefore(CharSequence phone) {
		return StrUtil.sub(phone, 0, 3);
	}

	/**
	 * 获取手机号中间4位
	 *
	 * @param phone 手机号码
	 * @return 手机号中间4位
	 * @since 5.3.11
	 */
	public static CharSequence subBetween(CharSequence phone) {
		return StrUtil.sub(phone, 3, 7);
	}

	/**
	 * 获取手机号后4位
	 *
	 * @param phone 手机号码
	 * @return 手机号后4位
	 * @since 5.3.11
	 */
	public static CharSequence subAfter(CharSequence phone) {
		return StrUtil.sub(phone, 7, 11);
	}
}
