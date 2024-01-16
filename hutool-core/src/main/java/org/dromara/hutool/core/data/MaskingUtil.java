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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.CharUtil;

/**
 * 数据脱敏（Data Masking）工具类，对某些敏感信息（比如，身份证号、手机号、卡号、姓名、地址、邮箱等 ）屏蔽敏感数据。<br>
 * 原名称：DesensitizedUtil <br>
 * <p>支持以下类型信息的脱敏自动处理：</p>
 *
 * <ul>
 *     <li>用户ID</li>
 *     <li>中文名</li>
 *     <li>身份证</li>
 *     <li>座机号</li>
 *     <li>手机号</li>
 *     <li>地址</li>
 *     <li>电子邮件</li>
 *     <li>密码</li>
 *     <li>车牌</li>
 *     <li>银行卡号</li>
 *     <li>IPv4</li>
 *     <li>IPv6</li>
 * </ul>
 *
 * @author dazer and neusoft and qiaomu
 * @since 5.6.2
 */
public class MaskingUtil {

	/**
	 * 支持的脱敏类型枚举
	 *
	 * @author dazer and neusoft and qiaomu
	 */
	public enum MaskingType {
		/**
		 * 用户id
		 */
		USER_ID,
		/**
		 * 中文名
		 */
		CHINESE_NAME,
		/**
		 * 身份证号
		 */
		ID_CARD,
		/**
		 * 座机号
		 */
		FIXED_PHONE,
		/**
		 * 手机号
		 */
		MOBILE_PHONE,
		/**
		 * 地址
		 */
		ADDRESS,
		/**
		 * 电子邮件
		 */
		EMAIL,
		/**
		 * 密码
		 */
		PASSWORD,
		/**
		 * 中国大陆车牌，包含普通车辆、新能源车辆
		 */
		CAR_LICENSE,
		/**
		 * 银行卡
		 */
		BANK_CARD,
		/**
		 * IPv4地址
		 */
		IPV4,
		/**
		 * IPv6地址
		 */
		IPV6,
		/**
		 * 定义了一个first_mask的规则，只显示第一个字符。
		 */
		FIRST_MASK,
		/**
		 * 清空为null
		 */
		CLEAR_TO_NULL,
		/**
		 * 清空为""
		 */
		CLEAR_TO_EMPTY
	}

	/**
	 * 脱敏，使用默认的脱敏策略
	 * <pre>
	 * MaskingUtil.masking("100", MaskingUtil.DesensitizedType.USER_ID)) =  "0"
	 * MaskingUtil.masking("段正淳", MaskingUtil.DesensitizedType.CHINESE_NAME)) = "段**"
	 * MaskingUtil.masking("51343620000320711X", MaskingUtil.DesensitizedType.ID_CARD)) = "5***************1X"
	 * MaskingUtil.masking("09157518479", MaskingUtil.DesensitizedType.FIXED_PHONE)) = "0915*****79"
	 * MaskingUtil.masking("18049531999", MaskingUtil.DesensitizedType.MOBILE_PHONE)) = "180****1999"
	 * MaskingUtil.masking("北京市海淀区马连洼街道289号", MaskingUtil.DesensitizedType.ADDRESS)) = "北京市海淀区马********"
	 * MaskingUtil.masking("duandazhi-jack@gmail.com.cn", MaskingUtil.DesensitizedType.EMAIL)) = "d*************@gmail.com.cn"
	 * MaskingUtil.masking("1234567890", MaskingUtil.DesensitizedType.PASSWORD)) = "**********"
	 * MaskingUtil.masking("苏D40000", MaskingUtil.DesensitizedType.CAR_LICENSE)) = "苏D4***0"
	 * MaskingUtil.masking("11011111222233333256", MaskingUtil.DesensitizedType.BANK_CARD)) = "1101 **** **** **** 3256"
	 * MaskingUtil.masking("192.168.1.1", MaskingUtil.DesensitizedType.IPV4)) = "192.*.*.*"
	 * </pre>
	 *
	 * @param str         字符串
	 * @param maskingType 脱敏类型;可以脱敏：用户id、中文名、身份证号、座机号、手机号、地址、电子邮件、密码
	 * @return 脱敏之后的字符串
	 * @author dazer and neusoft and qiaomu
	 * @since 5.6.2
	 */
	public static String masking(final CharSequence str, final MaskingType maskingType) {
		if (StrUtil.isBlank(str)) {
			return StrUtil.EMPTY;
		}
		String newStr = String.valueOf(str);
		switch (maskingType) {
			case USER_ID:
				newStr = String.valueOf(userId());
				break;
			case CHINESE_NAME:
				newStr = chineseName(String.valueOf(str));
				break;
			case ID_CARD:
				newStr = idCardNum(String.valueOf(str), 1, 2);
				break;
			case FIXED_PHONE:
				newStr = fixedPhone(String.valueOf(str));
				break;
			case MOBILE_PHONE:
				newStr = mobilePhone(String.valueOf(str));
				break;
			case ADDRESS:
				newStr = address(String.valueOf(str), 8);
				break;
			case EMAIL:
				newStr = email(String.valueOf(str));
				break;
			case PASSWORD:
				newStr = password(String.valueOf(str));
				break;
			case CAR_LICENSE:
				newStr = carLicense(String.valueOf(str));
				break;
			case BANK_CARD:
				newStr = bankCard(String.valueOf(str));
				break;
			case IPV4:
				newStr = ipv4(String.valueOf(str));
				break;
			case IPV6:
				newStr = ipv6(String.valueOf(str));
				break;
			case FIRST_MASK:
				newStr = firstMask(String.valueOf(str));
				break;
			case CLEAR_TO_EMPTY:
				newStr = clear();
				break;
			case CLEAR_TO_NULL:
				newStr = clearToNull();
				break;
			default:
		}
		return newStr;
	}

	/**
	 * 清空为空字符串
	 *
	 * @return 清空后的值
	 * @since 5.8.22
	 */
	public static String clear() {
		return StrUtil.EMPTY;
	}

	/**
	 * 清空为{@code null}
	 *
	 * @return 清空后的值(null)
	 * @since 5.8.22
	 */
	public static String clearToNull() {
		return null;
	}

	/**
	 * 【用户id】不对外提供userId
	 *
	 * @return 脱敏后的主键
	 */
	public static Long userId() {
		return 0L;
	}

	/**
	 * 定义了一个first_mask的规则，只显示第一个字符。<br>
	 * 脱敏前：123456789；脱敏后：1********。
	 *
	 * @param str 字符串
	 * @return 脱敏后的字符串
	 */
	public static String firstMask(final String str) {
		if (StrUtil.isBlank(str)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(str, 1, str.length());
	}

	/**
	 * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
	 *
	 * @param fullName 姓名
	 * @return 脱敏后的姓名
	 */
	public static String chineseName(final String fullName) {
		return firstMask(fullName);
	}

	/**
	 * 【身份证号】前1位 和后2位
	 *
	 * @param idCardNum 身份证
	 * @param front     保留：前面的front位数；从1开始
	 * @param end       保留：后面的end位数；从1开始
	 * @return 脱敏后的身份证
	 */
	public static String idCardNum(final String idCardNum, final int front, final int end) {
		//身份证不能为空
		if (StrUtil.isBlank(idCardNum)) {
			return StrUtil.EMPTY;
		}
		//需要截取的长度不能大于身份证号长度
		if ((front + end) > idCardNum.length()) {
			return StrUtil.EMPTY;
		}
		//需要截取的不能小于0
		if (front < 0 || end < 0) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(idCardNum, front, idCardNum.length() - end);
	}

	/**
	 * 【固定电话 前四位，后两位
	 *
	 * @param num 固定电话
	 * @return 脱敏后的固定电话；
	 */
	public static String fixedPhone(final String num) {
		if (StrUtil.isBlank(num)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(num, 4, num.length() - 2);
	}

	/**
	 * 【手机号码】前三位，后4位，其他隐藏，比如135****2210
	 *
	 * @param num 移动电话；
	 * @return 脱敏后的移动电话；
	 */
	public static String mobilePhone(final String num) {
		if (StrUtil.isBlank(num)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.hide(num, 3, num.length() - 4);
	}

	/**
	 * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
	 *
	 * @param address       家庭住址
	 * @param sensitiveSize 敏感信息长度
	 * @return 脱敏后的家庭地址
	 */
	public static String address(final String address, final int sensitiveSize) {
		if (StrUtil.isBlank(address)) {
			return StrUtil.EMPTY;
		}
		final int length = address.length();
		return StrUtil.hide(address, length - sensitiveSize, length);
	}

	/**
	 * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
	 *
	 * @param email 邮箱
	 * @return 脱敏后的邮箱
	 */
	public static String email(final String email) {
		if (StrUtil.isBlank(email)) {
			return StrUtil.EMPTY;
		}
		final int index = StrUtil.indexOf(email, '@');
		if (index <= 1) {
			return email;
		}
		return StrUtil.hide(email, 1, index);
	}

	/**
	 * 【密码】密码的全部字符都用*代替，比如：******
	 *
	 * @param password 密码
	 * @return 脱敏后的密码
	 */
	public static String password(final String password) {
		if (StrUtil.isBlank(password)) {
			return StrUtil.EMPTY;
		}
		return StrUtil.repeat('*', password.length());
	}

	/**
	 * 【中国车牌】车牌中间用*代替
	 * eg1：null       -》 ""
	 * eg1：""         -》 ""
	 * eg3：苏D40000   -》 苏D4***0
	 * eg4：陕A12345D  -》 陕A1****D
	 * eg5：京A123     -》 京A123     如果是错误的车牌，不处理
	 *
	 * @param carLicense 完整的车牌号
	 * @return 脱敏后的车牌
	 */
	public static String carLicense(String carLicense) {
		if (StrUtil.isBlank(carLicense)) {
			return StrUtil.EMPTY;
		}
		// 普通车牌
		if (carLicense.length() == 7) {
			carLicense = StrUtil.hide(carLicense, 3, 6);
		} else if (carLicense.length() == 8) {
			// 新能源车牌
			carLicense = StrUtil.hide(carLicense, 3, 7);
		}
		return carLicense;
	}

	/**
	 * 银行卡号脱敏
	 * eg: 1101 **** **** **** 3256
	 *
	 * @param bankCardNo 银行卡号
	 * @return 脱敏之后的银行卡号
	 * @since 5.6.3
	 */
	public static String bankCard(String bankCardNo) {
		if (StrUtil.isBlank(bankCardNo)) {
			return bankCardNo;
		}
		bankCardNo = StrUtil.cleanBlank(bankCardNo);
		if (bankCardNo.length() < 9) {
			return bankCardNo;
		}

		final int length = bankCardNo.length();
		final int endLength = length % 4 == 0 ? 4 : length % 4;
		final int midLength = length - 4 - endLength;
		final StringBuilder buf = new StringBuilder();

		buf.append(bankCardNo, 0, 4);
		for (int i = 0; i < midLength; ++i) {
			if (i % 4 == 0) {
				buf.append(CharUtil.SPACE);
			}
			buf.append('*');
		}
		buf.append(CharUtil.SPACE).append(bankCardNo, length - endLength, length);
		return buf.toString();
	}

	/**
	 * IPv4脱敏，如：脱敏前：192.0.2.1；脱敏后：192.*.*.*。
	 *
	 * @param ipv4 IPv4地址
	 * @return 脱敏后的地址
	 */
	public static String ipv4(final String ipv4) {
		return StrUtil.subBefore(ipv4, '.', false) + ".*.*.*";
	}

	/**
	 * IPv4脱敏，如：脱敏前：2001:0db8:86a3:08d3:1319:8a2e:0370:7344；脱敏后：2001:*:*:*:*:*:*:*
	 *
	 * @param ipv6 IPv4地址
	 * @return 脱敏后的地址
	 */
	public static String ipv6(final String ipv6) {
		return StrUtil.subBefore(ipv6, ':', false) + ":*:*:*:*:*:*:*";
	}
}
