package com.xiaoleilu.hutool.lang;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.exceptions.ValidateException;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.ReUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 字段验证器
 * 
 * @author Looly
 *
 */
public final class Validator {
	
	private Validator() {}

	/** 英文字母 、数字和下划线 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/** 数字 */
	public final static Pattern NUMBERS = Pattern.compile("\\d+");
	/** 分组 */
	public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
	/** IP v4 */
	public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
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
	/** 生日 */
	public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-\\.年]?)(\\d{1,2})([/\\-\\.月]?)(\\d{1,2})日?$");
	/** URL */
	public final static Pattern URL = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
	/** 中文字、英文字母、数字和下划线 */
	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");
	/** UUID */
	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
	/** 不带横线的UUID */
	public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
	/** 中国车牌号码 */
	public final static Pattern PLATE_NUMBER = Pattern.compile("^[京,津,渝,沪,冀,晋,辽,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,琼,川,贵,云,陕,秦,甘,陇,青,台,蒙,桂,宁,新,藏,澳,军,海,航,警][A-Z][0-9,A-Z]{5}$");

	/**
	 * 验证是否为空<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 * 
	 * @param value 值
	 * @return 是否为空
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object value) {
		return (null == value || (value instanceof String && StrUtil.isEmpty((String) value)));
	}
	
	/**
	 * 验证是否为空<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 * 
	 * @param value 值
	 * @return 是否为空
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Object value) {
		return false == isEmpty(value);
	}
	
	/**
	 * 验证是否为空，为空时抛出异常<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNotEmpty(Object value, String errorMsg) throws ValidateException {
		if (isEmpty(value)) {
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否相等<br>
	 * 当两值都为null返回true
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @return 当两值都为null或相等返回true
	 */
	public static boolean equal(Object t1, Object t2) {
		return ObjectUtil.equal(t1, t2);
	}
	
	/**
	 * 验证是否相等，不相等抛出异常<br>
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @param errorMsg 错误信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		if (false == equal(t1, t2)) {
			throw new ValidateException(errorMsg);
		}
	}
	
	/**
	 * 验证是否不等，相等抛出异常<br>
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @param errorMsg 错误信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNotEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		if (equal(t1, t2)) {
			throw new ValidateException(errorMsg);
		}
	}
	
	/**
	 * 验证是否非空且与指定值相等<br>
	 * 当数据为空时抛出验证异常<br>
	 * 当两值不等时抛出异常
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @param errorMsg 错误信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNotEmptyAndEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		validateNotEmpty(t1, errorMsg);
		validateEqual(t1, t2, errorMsg);
	}
	
	/**
	 * 验证是否非空且与指定值相等<br>
	 * 当数据为空时抛出验证异常<br>
	 * 当两值相等时抛出异常
	 * 
	 * @param t1 对象1
	 * @param t2 对象2
	 * @param errorMsg 错误信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNotEmptyAndNotEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		validateNotEmpty(t1, errorMsg);
		validateNotEqual(t1, t2, errorMsg);
	}

	/**
	 * 通过正则表达式验证
	 * 
	 * @param regex 正则
	 * @param value 值
	 * @return 是否匹配正则
	 */
	public static boolean isMactchRegex(String regex, String value) {
		return ReUtil.isMatch(regex, value);
	}
	
	/**
	 * 通过正则表达式验证<br>
	 * 不符合正则
	 * 
	 * @param regex 正则
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateMatchRegex(String regex, String value, String errorMsg) throws ValidateException {
		if (false == isMactchRegex(regex, value)) {
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 通过正则表达式验证
	 * 
	 * @param pattern 正则模式
	 * @param value 值
	 * @return 是否匹配正则
	 */
	public static boolean isMactchRegex(Pattern pattern, String value) {
		return ReUtil.isMatch(pattern, value);
	}

	/**
	 * 验证是否为英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @return 是否为英文字母 、数字和下划线
	 */
	public static boolean isGeneral(String value) {
		return isMactchRegex(GENERAL, value);
	}
	
	/**
	 * 验证是否为英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateGeneral(String value, String errorMsg) throws ValidateException {
		if(false == isGeneral(value)){
			throw new ValidateException(errorMsg);
		}
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
		return isMactchRegex(reg, value);
	}
	
	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @param min 最小长度，负数自动识别为0
	 * @param max 最大长度，0或负数表示不限制最大长度
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateGeneral(String value, int min, int max, String errorMsg) throws ValidateException {
		if(false == isGeneral(value, min, max)){
			throw new ValidateException(errorMsg);
		}
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
	 * 验证是否为给定最小长度的英文字母 、数字和下划线
	 * 
	 * @param value 值
	 * @param min 最小长度，负数自动识别为0
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateGeneral(String value, int min, String errorMsg) throws ValidateException {
		validateGeneral(value, min, 0, errorMsg);
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
		return isMactchRegex(NUMBERS, value);
	}
	
	/**
	 * 验证是否为数字
	 * 
	 * @param value 表单值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNumbers(String value, String errorMsg) throws ValidateException {
		if(false == isNumber(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为货币
	 * 
	 * @param value 值
	 * @return 是否为货币
	 */
	public static boolean isMoney(String value) {
		return isMactchRegex(MONEY, value);
	}
	
	/**
	 * 验证是否为货币
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateMoney(String value, String errorMsg) throws ValidateException {
		if(false == isMoney(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为邮政编码（中国）
	 * 
	 * @param value 值
	 * @return 是否为邮政编码（中国）
	 */
	public static boolean isZipCode(String value) {
		return isMactchRegex(ZIP_CODE, value);
	}
	
	/**
	 * 验证是否为邮政编码（中国）
	 * 
	 * @param value 表单值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateZipCode(String value, String errorMsg) throws ValidateException {
		if(false == isZipCode(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为可用邮箱地址
	 * 
	 * @param value 值
	 * @return 否为可用邮箱地址
	 */
	public static boolean isEmail(String value) {
		return isMactchRegex(EMAIL, value);
	}
	
	/**
	 * 验证是否为可用邮箱地址
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateEmail(String value, String errorMsg) throws ValidateException {
		if(false == isEmail(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为手机号码（中国）
	 * 
	 * @param value 值
	 * @return 是否为手机号码（中国）
	 */
	public static boolean isMobile(String value) {
		return isMactchRegex(MOBILE, value);
	}
	
	/**
	 * 验证是否为手机号码（中国）
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateMobile(String value, String errorMsg) throws ValidateException {
		if(false == isMobile(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为身份证号码（18位中国）<br>
	 * 出生日期只支持到到2999年
	 * 
	 * @param value 值
	 * @return 是否为身份证号码（18位中国）
	 */
	public static boolean isCitizenId(String value) {
		return isMactchRegex(CITIZEN_ID, value);
	}
	
	/**
	 * 验证是否为身份证号码（18位中国）<br>
	 * 出生日期只支持到到2999年
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateCitizenIdNumber(String value, String errorMsg) throws ValidateException {
		if(false == isCitizenId(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为生日<br>
	 * 
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 是否为生日
	 */
	public static boolean isBirthday(int year, int month, int day) {
		//验证年
		int thisYear = DateUtil.thisYear();
		if(year < 1930 || year > thisYear){
			return false;
		}
		
		//验证月
		if (month < 1 || month > 12) {
			return false;
		}
		
		//验证日
		if (day < 1 || day > 31) {
			return false;
		}
		if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
			return false;
		}
		if (month == 2) {
			if (day > 29 || (day == 29 && false ==DateUtil.isLeapYear(year))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证是否为生日<br>
	 * 只支持以下几种格式：
	 * <ul>
	 * 	<li>yyyyMMdd</li>
	 * 	<li>yyyy-MM-dd</li>
	 * 	<li>yyyy/MM/dd</li>
	 * 	<li>yyyyMMdd</li>
	 * 	<li>yyyy年MM月dd日</li>
	 * </ul>
	 * 
	 * @param value 值
	 * @return 是否为生日
	 */
	public static boolean isBirthday(String value) {
		if(isMactchRegex(BIRTHDAY, value)){
			Matcher matcher = BIRTHDAY.matcher(value);
			if(matcher.find()){
				int year = Convert.toInt(matcher.group(1));
				int month = Convert.toInt(matcher.group(3));
				int day = Convert.toInt(matcher.group(5));
				return isBirthday(year, month, day);
			}
		}
		return false;
	}
	
	/**
	 * 验证验证是否为生日<br>
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateBirthday(String value, String errorMsg) throws ValidateException {
		if(false == isBirthday(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为IPV4地址
	 * 
	 * @param value 值
	 * @return 是否为IPV4地址
	 */
	public static boolean isIpv4(String value) {
		return isMactchRegex(IPV4, value);
	}
	
	/**
	 * 验证是否为IPV4地址
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateIpv4(String value, String errorMsg) throws ValidateException {
		if(false == isIpv4(value)){
			throw new ValidateException(errorMsg);
		}
	}
	
	/**
	 * 验证是否为中国车牌号
	 * 
	 * @param value 值
	 * @return 是否为IPV4地址
	 * @since 3.0.6
	 */
	public static boolean isPlateNumber(String value) {
		return isMactchRegex(PLATE_NUMBER, value);
	}
	
	/**
	 * 验证是否为中国车牌号
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 * @since 3.0.6
	 */
	public static void validatePlateNumber(String value, String errorMsg) throws ValidateException {
		if(false == isPlateNumber(value)){
			throw new ValidateException(errorMsg);
		}
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
	 * 验证是否为URL
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateUrl(String value, String errorMsg) throws ValidateException {
		if(false == isUrl(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为汉字
	 * 
	 * @param value 值
	 * @return 是否为汉字
	 */
	public static boolean isChinese(String value) {
		return isMactchRegex("^" + ReUtil.RE_CHINESE + "+$", value);
	}
	
	/**
	 * 验证是否为汉字
	 * 
	 * @param value 表单值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateChinese(String value, String errorMsg) throws ValidateException {
		if(false == isChinese(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为中文字、英文字母、数字和下划线
	 * 
	 * @param value 值
	 * @return 是否为中文字、英文字母、数字和下划线
	 */
	public static boolean isGeneralWithChinese(String value) {
		return isMactchRegex(GENERAL_WITH_CHINESE, value);
	}
	
	/**
	 * 验证是否为中文字、英文字母、数字和下划线
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateGeneralWithChinese(String value, String errorMsg) throws ValidateException {
		if(false == isGeneralWithChinese(value)){
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 验证是否为UUID<br>
	 * 包括带横线标准格式和不带横线的简单模式
	 * 
	 * @param value 值
	 * @return 是否为UUID
	 */
	public static boolean isUUID(String value) {
		return isMactchRegex(UUID, value) || isMactchRegex(UUID_SIMPLE, value);
	}
	
	/**
	 * 验证是否为UUID<br>
	 * 包括带横线标准格式和不带横线的简单模式
	 * 
	 * @param value 值
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateUUID(String value, String errorMsg) throws ValidateException {
		if(false == isUUID(value)){
			throw new ValidateException(errorMsg);
		}
	}
}
