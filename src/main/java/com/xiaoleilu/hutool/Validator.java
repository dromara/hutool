package com.xiaoleilu.hutool;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

import com.xiaoleilu.hutool.ReUtil;
import com.xiaoleilu.hutool.StrUtil;

/**
 * 字段验证器
 * 
 * @author Looly
 *
 */
public class Validator {

	/** 英文字母 、数字和下划线 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/** 数字 */
	public final static Pattern NUMBER = Pattern.compile("\\d+");
	/** 分组 */
	public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
	/** IP v4 */
	public final static Pattern IPV4 = Pattern
			.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	/** 货币 */
	public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
	/** 邮件 */
	public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
	/** 移动电话 */
	public final static Pattern MOBILE = Pattern.compile("1\\d{10}");
	/** 身份证号码 */
	public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	/** 邮编 */
	public final static Pattern ZIP_CODE = Pattern.compile("\\d{6}");
	/** 邮编 */
	public final static Pattern BIRTHDAY = Pattern.compile("(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})日?$");
	/** URL */
	public final static Pattern URL = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
	/** 中文字、英文字母、数字和下划线 */
	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");
	/** UUID */
	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");

	/**
	 * 验证是否为空<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 * 
	 * @param value 值
	 * @return 是否为空
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(T value) {
		return (null == value || (value instanceof String && StrUtil.isEmpty((String) value)));
	}

	/**
	 * 验证是否相等<br>
	 * 当两值都为null返回true
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @return 当两值都为null或相等返回true
	 */
	public static boolean equals(Object t1, Object t2) {
		return Func.equals(t1, t2);
	}

	/**
	 * 通过正则表达式验证
	 * 
	 * @param regex 正则
	 * @param value 值
	 * @return 是否匹配正则
	 */
	public static boolean isByRegex(String regex, String value) {
		return ReUtil.isMatch(regex, value);
	}

	/**
	 * 通过正则表达式验证
	 * 
	 * @param pattern 正则模式
	 * @param value 值
	 * @return 是否匹配正则
	 */
	public static boolean isByRegex(Pattern pattern, String value) {
		return ReUtil.isMatch(pattern, value);
	}

	/**
	 * 验证是否为英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @return 是否为英文字母 、数字和下划线
	 */
	public static boolean isGeneral(String value) {
		return isByRegex(GENERAL, value);
	}

	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @param min 最小长度，负数自动识别为0
	 * @param max 最大长度，0或负数表示不限制最大长度
	 * @return 是否为给定长度范围的英文字母 、数字和下划线
	 */
	public static boolean isGeneral(String value, int min, int max) {
		String reg = "^\\w{" + min + "," + max + "}$";
		if (min < 0) {
			min = 0;
		}
		if (max <= 0) {
			reg = "^\\w{" + min + ",}$";
		}
		return isByRegex(reg, value);
	}

	/**
	 * 验证是否为给定最小长度的英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @param min 最小长度，负数自动识别为0
	 * @return 是否为给定最小长度的英文字母 、数字和下划线
	 */
	public static boolean isGeneral(String value, int min) {
		return isGeneral(value, min, 0);
	}

	/**
	 * 验证该字符串是否是数字
	 * 
	 * @param value 字符串内容
	 * @return 是否是数字
	 */
	public static boolean isNumber(String value) {
		if (StrUtil.isBlank(value)) {
			return false;
		}
		return isByRegex(NUMBER, value);
	}

	/**
	 * 验证是否为货币
	 * 
	 * @param value 值
	 * @return 是否为货币
	 */
	public static boolean isMoney(String value) {
		return isByRegex(MONEY, value);
	}

	/**
	 * 验证是否为邮政编码（中国）
	 * 
	 * @param value 值
	 * @return 是否为邮政编码（中国）
	 */
	public static boolean isZipCode(String value) {
		return isByRegex(ZIP_CODE, value);
	}

	/**
	 * 验证是否为可用邮箱地址
	 * 
	 * @param value 值
	 * @return 否为可用邮箱地址
	 */
	public static boolean isEmail(String value) {
		return isByRegex(EMAIL, value);
	}

	/**
	 * 验证是否为手机号码（中国）
	 * 
	 * @param value 值
	 * @return 是否为手机号码（中国）
	 */
	public static boolean isMobile(String value) {
		return isByRegex(MOBILE, value);
	}

	/**
	 * 验证是否为身份证号码（18位中国）<br>
	 * 出生日期只支持到到2999年
	 * 
	 * @param value 值
	 * @return 是否为身份证号码（18位中国）
	 */
	public static boolean isCitizenId(String value) {
		return isByRegex(CITIZEN_ID, value);
	}

	/**
	 * 验证是否为生日<br>
	 * 
	 * @param value 值
	 * @return 是否为生日
	 */
	public static boolean isBirthday(String value) {
		if (isByRegex(BIRTHDAY, value)) {
			int year = Integer.parseInt(value.substring(0, 4));
			int month = Integer.parseInt(value.substring(5, 7));
			int day = Integer.parseInt(value.substring(8, 10));

			if (month < 1 || month > 12) {
				return false;
			}
			if (day < 1 || day > 31) {
				return false;
			}
			if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
				return false;
			}
			if (month == 2) {
				boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
				if (day > 29 || (day == 29 && !isleap)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 验证是否为IPV4地址
	 * 
	 * @param value 值
	 * @return 是否为IPV4地址
	 */
	public static boolean isIpv4(String value) {
		return isByRegex(IPV4, value);
	}

	/**
	 * 验证是否为URL
	 * 
	 * @param value 值
	 * @return 是否为URL
	 */
	public static boolean isUrl(String value) {
		try {
			new java.net.URL(value);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为汉字
	 * 
	 * @param value 值
	 * @return 是否为汉字
	 */
	public static boolean isChinese(String value) {
		return isByRegex("^" + ReUtil.RE_CHINESE + "+$", value);
	}

	/**
	 * 验证是否为中文字、英文字母、数字和下划线
	 * 
	 * @param value 值
	 * @return 是否为中文字、英文字母、数字和下划线
	 */
	public static boolean isGeneralWithChinese(String value) {
		return isByRegex(GENERAL_WITH_CHINESE, value);
	}

	/**
	 * 验证是否为UUID
	 * 
	 * @param value 值
	 * @return 是否为UUID
	 */
	public static boolean isUUID(String value) {
		return isByRegex(UUID, value);
	}

}
