package cn.hutool.core.lang;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字段验证器
 *
 * @author Looly
 */
public class Validator {

	/**
	 * 英文字母 、数字和下划线
	 */
	public final static Pattern GENERAL = PatternPool.GENERAL;
	/**
	 * 数字
	 */
	public final static Pattern NUMBERS = PatternPool.NUMBERS;
	/**
	 * 分组
	 */
	public final static Pattern GROUP_VAR = PatternPool.GROUP_VAR;
	/**
	 * IP v4
	 */
	public final static Pattern IPV4 = PatternPool.IPV4;
	/**
	 * IP v6
	 */
	public final static Pattern IPV6 = PatternPool.IPV6;
	/**
	 * 货币
	 */
	public final static Pattern MONEY = PatternPool.MONEY;
	/**
	 * 邮件
	 */
	public final static Pattern EMAIL = PatternPool.EMAIL;
	/**
	 * 移动电话
	 */
	public final static Pattern MOBILE = PatternPool.MOBILE;
	/**
	 * 身份证号码
	 */
	public final static Pattern CITIZEN_ID = PatternPool.CITIZEN_ID;
	/**
	 * 邮编
	 */
	public final static Pattern ZIP_CODE = PatternPool.ZIP_CODE;
	/**
	 * 生日
	 */
	public final static Pattern BIRTHDAY = PatternPool.BIRTHDAY;
	/**
	 * URL
	 */
	public final static Pattern URL = PatternPool.URL;
	/**
	 * Http URL
	 */
	public final static Pattern URL_HTTP = PatternPool.URL_HTTP;
	/**
	 * 中文字、英文字母、数字和下划线
	 */
	public final static Pattern GENERAL_WITH_CHINESE = PatternPool.GENERAL_WITH_CHINESE;
	/**
	 * UUID
	 */
	public final static Pattern UUID = PatternPool.UUID;
	/**
	 * 不带横线的UUID
	 */
	public final static Pattern UUID_SIMPLE = PatternPool.UUID_SIMPLE;
	/**
	 * 中国车牌号码
	 */
	public final static Pattern PLATE_NUMBER = PatternPool.PLATE_NUMBER;

	/**
	 * 给定值是否为<code>true</code>
	 *
	 * @param value 值
	 * @return 是否为<code>true</code>
	 * @since 4.4.5
	 */
	public static boolean isTrue(boolean value) {
		return value;
	}

	/**
	 * 给定值是否不为<code>false</code>
	 *
	 * @param value 值
	 * @return 是否不为<code>false</code>
	 * @since 4.4.5
	 */
	public static boolean isFalse(boolean value) {
		return false == value;
	}

	/**
	 * 检查指定值是否为<code>true</code>
	 *
	 * @param value            值
	 * @param errorMsgTemplate 错误消息内容模板（变量使用{}表示）
	 * @param params           模板中变量替换后的值
	 * @return 检查过后的值
	 * @throws ValidateException 检查不满足条件抛出的异常
	 * @since 4.4.5
	 */
	public static boolean validateTrue(boolean value, String errorMsgTemplate, Object... params) throws ValidateException {
		if (isFalse(value)) {
			throw new ValidateException(errorMsgTemplate, params);
		}
		return true;
	}

	/**
	 * 检查指定值是否为<code>false</code>
	 *
	 * @param value            值
	 * @param errorMsgTemplate 错误消息内容模板（变量使用{}表示）
	 * @param params           模板中变量替换后的值
	 * @return 检查过后的值
	 * @throws ValidateException 检查不满足条件抛出的异常
	 * @since 4.4.5
	 */
	public static boolean validateFalse(boolean value, String errorMsgTemplate, Object... params) throws ValidateException {
		if (isTrue(value)) {
			throw new ValidateException(errorMsgTemplate, params);
		}
		return false;
	}

	/**
	 * 给定值是否为<code>null</code>
	 *
	 * @param value 值
	 * @return 是否为<code>null</code>
	 */
	public static boolean isNull(Object value) {
		return null == value;
	}

	/**
	 * 给定值是否不为<code>null</code>
	 *
	 * @param value 值
	 * @return 是否不为<code>null</code>
	 */
	public static boolean isNotNull(Object value) {
		return null != value;
	}

	/**
	 * 检查指定值是否为<code>null</code>
	 *
	 * @param <T>              被检查的对象类型
	 * @param value            值
	 * @param errorMsgTemplate 错误消息内容模板（变量使用{}表示）
	 * @param params           模板中变量替换后的值
	 * @return 检查过后的值
	 * @throws ValidateException 检查不满足条件抛出的异常
	 * @since 4.4.5
	 */
	public static <T> T validateNull(T value, String errorMsgTemplate, Object... params) throws ValidateException {
		if (isNotNull(value)) {
			throw new ValidateException(errorMsgTemplate, params);
		}
		return null;
	}

	/**
	 * 检查指定值是否非<code>null</code>
	 *
	 * @param <T>              被检查的对象类型
	 * @param value            值
	 * @param errorMsgTemplate 错误消息内容模板（变量使用{}表示）
	 * @param params           模板中变量替换后的值
	 * @return 检查过后的值
	 * @throws ValidateException 检查不满足条件抛出的异常
	 */
	public static <T> T validateNotNull(T value, String errorMsgTemplate, Object... params) throws ValidateException {
		if (isNull(value)) {
			throw new ValidateException(errorMsgTemplate, params);
		}
		return value;
	}

	/**
	 * 验证是否为空<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 *
	 * @param value 值
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object value) {
		return (null == value || (value instanceof String && StrUtil.isEmpty((String) value)));
	}

	/**
	 * 验证是否为非空<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 *
	 * @param value 值
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Object value) {
		return false == isEmpty(value);
	}

	/**
	 * 验证是否为空，非空时抛出异常<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 *
	 * @param <T>      值类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值，验证通过返回此值，空值
	 * @throws ValidateException 验证异常
	 */
	public static <T> T validateEmpty(T value, String errorMsg) throws ValidateException {
		if (isNotEmpty(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为非空，为空时抛出异常<br>
	 * 对于String类型判定是否为empty(null 或 "")<br>
	 *
	 * @param <T>      值类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值，验证通过返回此值，非空值
	 * @throws ValidateException 验证异常
	 */
	public static <T> T validateNotEmpty(T value, String errorMsg) throws ValidateException {
		if (isEmpty(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
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
	 * @param t1       对象1
	 * @param t2       对象2
	 * @param errorMsg 错误信息
	 * @return 相同值
	 * @throws ValidateException 验证异常
	 */
	public static Object validateEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		if (false == equal(t1, t2)) {
			throw new ValidateException(errorMsg);
		}
		return t1;
	}

	/**
	 * 验证是否不等，相等抛出异常<br>
	 *
	 * @param t1       对象1
	 * @param t2       对象2
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
	 * @param t1       对象1
	 * @param t2       对象2
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
	 * @param t1       对象1
	 * @param t2       对象2
	 * @param errorMsg 错误信息
	 * @throws ValidateException 验证异常
	 */
	public static void validateNotEmptyAndNotEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
		validateNotEmpty(t1, errorMsg);
		validateNotEqual(t1, t2, errorMsg);
	}

	/**
	 * 通过正则表达式验证<br>
	 * 不符合正则抛出{@link ValidateException} 异常
	 *
	 * @param <T>      字符串类型
	 * @param regex    正则
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateMatchRegex(String regex, T value, String errorMsg) throws ValidateException {
		if (false == isMatchRegex(regex, value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 通过正则表达式验证
	 *
	 * @param pattern 正则模式
	 * @param value   值
	 * @return 是否匹配正则
	 * @deprecated 请使用 {@link #isMatchRegex(Pattern, CharSequence)}
	 */
	@Deprecated
	public static boolean isMactchRegex(Pattern pattern, CharSequence value) {
		return ReUtil.isMatch(pattern, value);
	}

	/**
	 * 通过正则表达式验证
	 *
	 * @param regex 正则
	 * @param value 值
	 * @return 是否匹配正则
	 * @deprecated 拼写错误，请使用{@link #isMatchRegex(String, CharSequence)}
	 */
	@Deprecated
	public static boolean isMactchRegex(String regex, CharSequence value) {
		return ReUtil.isMatch(regex, value);
	}

	/**
	 * 通过正则表达式验证
	 *
	 * @param pattern 正则模式
	 * @param value   值
	 * @return 是否匹配正则
	 */
	public static boolean isMatchRegex(Pattern pattern, CharSequence value) {
		return ReUtil.isMatch(pattern, value);
	}

	/**
	 * 通过正则表达式验证
	 *
	 * @param regex 正则
	 * @param value 值
	 * @return 是否匹配正则
	 */
	public static boolean isMatchRegex(String regex, CharSequence value) {
		return ReUtil.isMatch(regex, value);
	}

	/**
	 * 验证是否为英文字母 、数字和下划线
	 *
	 * @param value 值
	 * @return 是否为英文字母 、数字和下划线
	 */
	public static boolean isGeneral(CharSequence value) {
		return isMatchRegex(GENERAL, value);
	}

	/**
	 * 验证是否为英文字母 、数字和下划线
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateGeneral(T value, String errorMsg) throws ValidateException {
		if (false == isGeneral(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 *
	 * @param value 值
	 * @param min   最小长度，负数自动识别为0
	 * @param max   最大长度，0或负数表示不限制最大长度
	 * @return 是否为给定长度范围的英文字母 、数字和下划线
	 */
	public static boolean isGeneral(CharSequence value, int min, int max) {
		if (min < 0) {
			min = 0;
		}
		String reg = "^\\w{" + min + "," + max + "}$";
		if (max <= 0) {
			reg = "^\\w{" + min + ",}$";
		}
		return isMatchRegex(reg, value);
	}

	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param min      最小长度，负数自动识别为0
	 * @param max      最大长度，0或负数表示不限制最大长度
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateGeneral(T value, int min, int max, String errorMsg) throws ValidateException {
		if (false == isGeneral(value, min, max)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为给定最小长度的英文字母 、数字和下划线
	 *
	 * @param value 值
	 * @param min   最小长度，负数自动识别为0
	 * @return 是否为给定最小长度的英文字母 、数字和下划线
	 */
	public static boolean isGeneral(CharSequence value, int min) {
		return isGeneral(value, min, 0);
	}

	/**
	 * 验证是否为给定最小长度的英文字母 、数字和下划线
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param min      最小长度，负数自动识别为0
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateGeneral(T value, int min, String errorMsg) throws ValidateException {
		return validateGeneral(value, min, 0, errorMsg);
	}

	/**
	 * 判断字符串是否全部为字母组成，包括大写和小写字母和汉字
	 *
	 * @param value 值
	 * @return 是否全部为字母组成，包括大写和小写字母和汉字
	 * @since 3.3.0
	 */
	public static boolean isLetter(CharSequence value) {
		return StrUtil.isAllCharMatch(value, Character::isLetter);
	}

	/**
	 * 验证是否全部为字母组成，包括大写和小写字母和汉字
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 3.3.0
	 */
	public static <T extends CharSequence> T validateLetter(T value, String errorMsg) throws ValidateException {
		if (false == isLetter(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 判断字符串是否全部为大写字母
	 *
	 * @param value 值
	 * @return 是否全部为大写字母
	 * @since 3.3.0
	 */
	public static boolean isUpperCase(CharSequence value) {
		return StrUtil.isAllCharMatch(value, Character::isUpperCase);
	}

	/**
	 * 验证字符串是否全部为大写字母
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 3.3.0
	 */
	public static <T extends CharSequence> T validateUpperCase(T value, String errorMsg) throws ValidateException {
		if (false == isUpperCase(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 判断字符串是否全部为小写字母
	 *
	 * @param value 值
	 * @return 是否全部为小写字母
	 * @since 3.3.0
	 */
	public static boolean isLowerCase(CharSequence value) {
		return StrUtil.isAllCharMatch(value, Character::isLowerCase);
	}

	/**
	 * 验证字符串是否全部为小写字母
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 3.3.0
	 */
	public static <T extends CharSequence> T validateLowerCase(T value, String errorMsg) throws ValidateException {
		if (false == isLowerCase(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证该字符串是否是数字
	 *
	 * @param value 字符串内容
	 * @return 是否是数字
	 */
	public static boolean isNumber(CharSequence value) {
		return NumberUtil.isNumber(value);
	}

	/**
	 * 验证是否为数字
	 *
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static String validateNumber(String value, String errorMsg) throws ValidateException {
		if (false == isNumber(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证该字符串是否是字母（包括大写和小写字母）
	 *
	 * @param value 字符串内容
	 * @return 是否是字母（包括大写和小写字母）
	 * @since 4.1.8
	 */
	public static boolean isWord(CharSequence value) {
		return isMatchRegex(PatternPool.WORD, value);
	}

	/**
	 * 验证是否为字母（包括大写和小写字母）
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 4.1.8
	 */
	public static <T extends CharSequence> T validateWord(T value, String errorMsg) throws ValidateException {
		if (false == isWord(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为货币
	 *
	 * @param value 值
	 * @return 是否为货币
	 */
	public static boolean isMoney(CharSequence value) {
		return isMatchRegex(MONEY, value);
	}

	/**
	 * 验证是否为货币
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateMoney(T value, String errorMsg) throws ValidateException {
		if (false == isMoney(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;

	}

	/**
	 * 验证是否为邮政编码（中国）
	 *
	 * @param value 值
	 * @return 是否为邮政编码（中国）
	 */
	public static boolean isZipCode(CharSequence value) {
		return isMatchRegex(ZIP_CODE, value);
	}

	/**
	 * 验证是否为邮政编码（中国）
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateZipCode(T value, String errorMsg) throws ValidateException {
		if (false == isZipCode(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为可用邮箱地址
	 *
	 * @param value 值
	 * @return 否为可用邮箱地址
	 */
	public static boolean isEmail(CharSequence value) {
		return isMatchRegex(EMAIL, value);
	}

	/**
	 * 验证是否为可用邮箱地址
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateEmail(T value, String errorMsg) throws ValidateException {
		if (false == isEmail(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为手机号码（中国）
	 *
	 * @param value 值
	 * @return 是否为手机号码（中国）
	 */
	public static boolean isMobile(CharSequence value) {
		return isMatchRegex(MOBILE, value);
	}

	/**
	 * 验证是否为手机号码（中国）
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateMobile(T value, String errorMsg) throws ValidateException {
		if (false == isMobile(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为身份证号码（18位中国）<br>
	 * 出生日期只支持到到2999年
	 *
	 * @param value 值
	 * @return 是否为身份证号码（18位中国）
	 */
	public static boolean isCitizenId(CharSequence value) {
		return isMatchRegex(CITIZEN_ID, value);
	}

	/**
	 * 验证是否为身份证号码（18位中国）<br>
	 * 出生日期只支持到到2999年
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateCitizenIdNumber(T value, String errorMsg) throws ValidateException {
		if (false == isCitizenId(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为生日
	 *
	 * @param year  年，从1900年开始计算
	 * @param month 月，从1开始计数
	 * @param day   日，从1开始计数
	 * @return 是否为生日
	 */
	public static boolean isBirthday(int year, int month, int day) {
		// 验证年
		int thisYear = DateUtil.thisYear();
		if (year < 1900 || year > thisYear) {
			return false;
		}

		// 验证月
		if (month < 1 || month > 12) {
			return false;
		}

		// 验证日
		if (day < 1 || day > 31) {
			return false;
		}
		// 检查几个特殊月的最大天数
		if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			return false;
		}
		if (month == 2) {
			// 在2月，非闰年最大28，闰年最大29
			return day < 29 || (day == 29 && DateUtil.isLeapYear(year));
		}
		return true;
	}

	/**
	 * 验证是否为生日<br>
	 * 只支持以下几种格式：
	 * <ul>
	 * <li>yyyyMMdd</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy/MM/dd</li>
	 * <li>yyyy.MM.dd</li>
	 * <li>yyyy年MM月dd日</li>
	 * </ul>
	 *
	 * @param value 值
	 * @return 是否为生日
	 */
	public static boolean isBirthday(CharSequence value) {
		final Matcher matcher = BIRTHDAY.matcher(value);
		if (matcher.find()) {
			int year = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(3));
			int day = Integer.parseInt(matcher.group(5));
			return isBirthday(year, month, day);
		}
		return false;
	}

	/**
	 * 验证验证是否为生日
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateBirthday(T value, String errorMsg) throws ValidateException {
		if (false == isBirthday(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为IPV4地址
	 *
	 * @param value 值
	 * @return 是否为IPV4地址
	 */
	public static boolean isIpv4(CharSequence value) {
		return isMatchRegex(IPV4, value);
	}

	/**
	 * 验证是否为IPV4地址
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateIpv4(T value, String errorMsg) throws ValidateException {
		if (false == isIpv4(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为IPV6地址
	 *
	 * @param value 值
	 * @return 是否为IPV6地址
	 */
	public static boolean isIpv6(CharSequence value) {
		return isMatchRegex(IPV6, value);
	}

	/**
	 * 验证是否为IPV6地址
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateIpv6(T value, String errorMsg) throws ValidateException {
		if (false == isIpv6(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为MAC地址
	 *
	 * @param value 值
	 * @return 是否为MAC地址
	 * @since 4.1.3
	 */
	public static boolean isMac(CharSequence value) {
		return isMatchRegex(PatternPool.MAC_ADDRESS, value);
	}

	/**
	 * 验证是否为MAC地址
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 4.1.3
	 */
	public static <T extends CharSequence> T validateMac(T value, String errorMsg) throws ValidateException {
		if (false == isMac(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为中国车牌号
	 *
	 * @param value 值
	 * @return 是否为中国车牌号
	 * @since 3.0.6
	 */
	public static boolean isPlateNumber(CharSequence value) {
		return isMatchRegex(PLATE_NUMBER, value);
	}

	/**
	 * 验证是否为中国车牌号
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 3.0.6
	 */
	public static <T extends CharSequence> T validatePlateNumber(T value, String errorMsg) throws ValidateException {
		if (false == isPlateNumber(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为URL
	 *
	 * @param value 值
	 * @return 是否为URL
	 */
	public static boolean isUrl(CharSequence value) {
		try {
			new java.net.URL(StrUtil.str(value));
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为URL
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateUrl(T value, String errorMsg) throws ValidateException {
		if (false == isUrl(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否都为汉字
	 *
	 * @param value 值
	 * @return 是否为汉字
	 */
	public static boolean isChinese(CharSequence value) {
		return isMatchRegex(PatternPool.CHINESES, value);
	}

	/**
	 * 验证是否包含汉字
	 *
	 * @param value 值
	 * @return 是否包含汉字
	 * @since 5.2.1
	 */
	public static boolean hasChinese(CharSequence value) {
		return ReUtil.contains(ReUtil.RE_CHINESES, value);
	}

	/**
	 * 验证是否为汉字
	 *
	 * @param <T>      字符串类型
	 * @param value    表单值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateChinese(T value, String errorMsg) throws ValidateException {
		if (false == isChinese(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为中文字、英文字母、数字和下划线
	 *
	 * @param value 值
	 * @return 是否为中文字、英文字母、数字和下划线
	 */
	public static boolean isGeneralWithChinese(CharSequence value) {
		return isMatchRegex(GENERAL_WITH_CHINESE, value);
	}

	/**
	 * 验证是否为中文字、英文字母、数字和下划线
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateGeneralWithChinese(T value, String errorMsg) throws ValidateException {
		if (false == isGeneralWithChinese(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为UUID<br>
	 * 包括带横线标准格式和不带横线的简单模式
	 *
	 * @param value 值
	 * @return 是否为UUID
	 */
	public static boolean isUUID(CharSequence value) {
		return isMatchRegex(UUID, value) || isMatchRegex(UUID_SIMPLE, value);
	}

	/**
	 * 验证是否为UUID<br>
	 * 包括带横线标准格式和不带横线的简单模式
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 */
	public static <T extends CharSequence> T validateUUID(T value, String errorMsg) throws ValidateException {
		if (false == isUUID(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 验证是否为Hex（16进制）字符串
	 *
	 * @param value 值
	 * @return 是否为Hex（16进制）字符串
	 * @since 4.3.3
	 */
	public static boolean isHex(CharSequence value) {
		return isMatchRegex(PatternPool.HEX, value);
	}

	/**
	 * 验证是否为Hex（16进制）字符串
	 *
	 * @param <T>      字符串类型
	 * @param value    值
	 * @param errorMsg 验证错误的信息
	 * @return 验证后的值
	 * @throws ValidateException 验证异常
	 * @since 4.3.3
	 */
	public static <T extends CharSequence> T validateHex(T value, String errorMsg) throws ValidateException {
		if (false == isHex(value)) {
			throw new ValidateException(errorMsg);
		}
		return value;
	}

	/**
	 * 检查给定的数字是否在指定范围内
	 *
	 * @param value 值
	 * @param min   最小值（包含）
	 * @param max   最大值（包含）
	 * @return 是否满足
	 * @since 4.1.10
	 */
	public static boolean isBetween(Number value, Number min, Number max) {
		Assert.notNull(value);
		Assert.notNull(min);
		Assert.notNull(max);
		final double doubleValue = value.doubleValue();
		return (doubleValue >= min.doubleValue()) && (doubleValue <= max.doubleValue());
	}

	/**
	 * 检查给定的数字是否在指定范围内
	 *
	 * @param value    值
	 * @param min      最小值（包含）
	 * @param max      最大值（包含）
	 * @param errorMsg 验证错误的信息
	 * @throws ValidateException 验证异常
	 * @since 4.1.10
	 */
	public static void validateBetween(Number value, Number min, Number max, String errorMsg) throws ValidateException {
		if (false == isBetween(value, min, max)) {
			throw new ValidateException(errorMsg);
		}
	}

	/**
	 * 是否是有效的统一社会信用代码
	 * <pre>
	 * 第一部分：登记管理部门代码1位 (数字或大写英文字母)
	 * 第二部分：机构类别代码1位 (数字或大写英文字母)
	 * 第三部分：登记管理机关行政区划码6位 (数字)
	 * 第四部分：主体标识码（组织机构代码）9位 (数字或大写英文字母)
	 * 第五部分：校验码1位 (数字或大写英文字母)
	 * </pre>
	 *
	 * @param creditCode 统一社会信用代码
	 * @return 校验结果
	 * @since 5.2.4
	 */
	public static boolean isCreditCode(CharSequence creditCode) {
		return CreditCodeUtil.isCreditCode(creditCode);
	}
}
