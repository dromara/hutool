package com.xiaoleilu.hutool.lang;

import java.util.regex.Pattern;

import com.xiaoleilu.hutool.util.ReUtil;

/**
 * 常用正则表达式集合
 * @author Looly
 *
 */
public class PatternPool {
	/** 英文字母 、数字和下划线 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/** 数字 */
	public final static Pattern NUMBERS = Pattern.compile("\\d+");
	/**单个中文汉字 */
	public final static Pattern CHINESE = Pattern.compile(ReUtil.RE_CHINESE);
	/**中文汉字 */
	public final static Pattern CHINESES = Pattern.compile(ReUtil.RE_CHINESES);
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
	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\u4E00-\u9FFF\\w]+$");
	/** UUID */
	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
	/** 不带横线的UUID */
	public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
	/** 中国车牌号码 */
	public final static Pattern PLATE_NUMBER = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$");
	/** MAC地址正则 */
	public static final Pattern MAC_ADDRESS = Pattern.compile("((?:[A-F0-9]{1,2}[:-]){5}[A-F0-9]{1,2})|(?:0x)(\\d{12})(?:.+ETHER)", Pattern.CASE_INSENSITIVE);
}
