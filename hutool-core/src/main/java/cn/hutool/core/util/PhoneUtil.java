package cn.hutool.core.util;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;

import java.util.regex.Pattern;


/**
 * 手机号工具类
 *
 * @author dahuoyzs
 * @since 5.3.11
 */
public class PhoneUtil {

	/**
	 * 座机号码
	 */
	private static Pattern TEL = Pattern.compile("0\\d{2,3}-[1-9]\\d{6,7}");

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
	 * 验证是否为座机号码（中国）
	 *
	 * @param value 值
	 * @return 是否为座机号码（中国）
	 * @since 5.3.11
	 */
	public static boolean isTel(CharSequence value) {
		return Validator.isMatchRegex(TEL, value);
	}

	/**
	 * 验证是否为座机号码+手机号码（中国）
	 *
	 * @param value 值
	 * @return 是否为座机号码+手机号码（中国）
	 * @since 5.3.11
	 */
	public static boolean isPhone(CharSequence value) {
		return isMobile(value) || isTel(value);
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
