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

import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 公民身份号码（Citizen Identification Number），参考GB11643-1999标准。
 * <ul>
 *     <li>1-2位：  2位省份代码。</li>
 *     <li>3-4位：  2位城市代码。</li>
 *     <li>5-6位：  2位区县代码。</li>
 *     <li>7-14位： 8位数字出生日期码。</li>
 *     <li>15-17位：3位数字顺序码。第17位奇数表示男性，偶数表示女性</li>
 *     <li>18位：   1位数字校验码。校检码可以是0~9的数字，有时也用X表示</li>
 * </ul>
 */
public class CIN {

	/**
	 * 中国公民身份证号码最小长度。
	 */
	public static final int CHINA_ID_MIN_LENGTH = 15;
	/**
	 * 中国公民身份证号码最大长度。
	 */
	public static final int CHINA_ID_MAX_LENGTH = 18;

	/**
	 * 每位加权因子
	 */
	private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	/**
	 * 省市代码表
	 */
	private static final Map<String, String> CITY_CODES = new HashMap<>();

	static {
		CITY_CODES.put("11", "北京");
		CITY_CODES.put("12", "天津");
		CITY_CODES.put("13", "河北");
		CITY_CODES.put("14", "山西");
		CITY_CODES.put("15", "内蒙古");
		CITY_CODES.put("21", "辽宁");
		CITY_CODES.put("22", "吉林");
		CITY_CODES.put("23", "黑龙江");
		CITY_CODES.put("31", "上海");
		CITY_CODES.put("32", "江苏");
		CITY_CODES.put("33", "浙江");
		CITY_CODES.put("34", "安徽");
		CITY_CODES.put("35", "福建");
		CITY_CODES.put("36", "江西");
		CITY_CODES.put("37", "山东");
		CITY_CODES.put("41", "河南");
		CITY_CODES.put("42", "湖北");
		CITY_CODES.put("43", "湖南");
		CITY_CODES.put("44", "广东");
		CITY_CODES.put("45", "广西");
		CITY_CODES.put("46", "海南");
		CITY_CODES.put("50", "重庆");
		CITY_CODES.put("51", "四川");
		CITY_CODES.put("52", "贵州");
		CITY_CODES.put("53", "云南");
		CITY_CODES.put("54", "西藏");
		CITY_CODES.put("61", "陕西");
		CITY_CODES.put("62", "甘肃");
		CITY_CODES.put("63", "青海");
		CITY_CODES.put("64", "宁夏");
		CITY_CODES.put("65", "新疆");
		CITY_CODES.put("71", "台湾");
		CITY_CODES.put("81", "香港");
		CITY_CODES.put("82", "澳门");
		//issue#1277，台湾身份证号码以83开头，但是行政区划为71
		CITY_CODES.put("83", "台湾");
		CITY_CODES.put("91", "国外");
	}

	/**
	 * 创建CIN
	 *
	 * @param code 身份证号码
	 * @return CIN
	 */
	public static CIN of(final String code) {
		return new CIN(code);
	}

	private final String code;

	/**
	 * 构造
	 *
	 * @param code 身份证号码
	 */
	public CIN(String code) {
		final int length = code.length();
		Assert.isTrue(length == CHINA_ID_MIN_LENGTH || length == CHINA_ID_MAX_LENGTH,
			"CIN length must be 15 or 18!");
		if (length == CHINA_ID_MIN_LENGTH) {
			// 15位身份证号码转换为18位
			code = convert15To18(code);
		}
		Assert.isTrue(verify(code, true), "Invalid CIN code!");
		this.code = code;
	}

	/**
	 * 根据身份编号获取户籍省份编码
	 *
	 * @return 省份编码
	 */
	public String getProvinceCode() {
		return this.code.substring(0, 2);
	}

	/**
	 * 根据身份编号获取户籍省份
	 *
	 * @return 省份名称。
	 */
	public String getProvince() {
		final String code = getProvinceCode();
		if (StrUtil.isNotBlank(code)) {
			return CITY_CODES.get(code);
		}
		return null;
	}

	/**
	 * 根据身份编号获取地市级编码<br>
	 * 获取编码为4位
	 *
	 * @return 地市级编码
	 */
	public String getCityCode() {
		return this.code.substring(0, 4);
	}

	/**
	 * 根据身份编号获取区县级编码<br>
	 * 获取编码为6位
	 *
	 * @return 地市级编码
	 */
	public String getDistrictCode() {
		return this.code.substring(0, 6);
	}

	/**
	 * 根据身份编号获取生日
	 *
	 * @return 生日(yyyyMMdd)
	 */
	public String getBirth() {
		return this.code.substring(6, 14);
	}

	/**
	 * 从身份证号码中获取生日日期
	 *
	 * @return 日期
	 */
	public DateTime getBirthDate() {
		final String birth = getBirth();
		return DateUtil.parse(birth, DatePattern.PURE_DATE_FORMAT);
	}

	/**
	 * 根据身份编号获取年龄
	 *
	 * @return 年龄
	 */
	public int getAge() {
		return getAge(DateUtil.now());
	}

	/**
	 * 根据身份编号获取指定日期当时的年龄年龄
	 *
	 * @param dateToCompare 以此日期为界，计算年龄。
	 * @return 年龄
	 */
	public int getAge(final Date dateToCompare) {
		return DateUtil.age(getBirthDate(), dateToCompare);
	}

	/**
	 * 根据身份编号获取生日年
	 *
	 * @return 生日(yyyy)
	 */
	public Short getBirthYear() {
		return Short.valueOf(this.code.substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月
	 *
	 * @return 生日(MM)
	 */
	public Short getBirthMonth() {
		return Short.valueOf(this.code.substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天
	 *
	 * @return 生日(dd)
	 */
	public Short getBirthDay() {
		return Short.valueOf(this.code.substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别
	 *
	 * @return 性别(1 : 男 ， 0 : 女)
	 */
	public int getGender() {
		final char sCardChar = this.code.charAt(16);
		return (sCardChar % 2 != 0) ? 1 : 0;
	}

	/**
	 * 将15位身份证号码转换为18位<br>
	 * 15位身份证号码遵循GB 11643-1989标准。
	 *
	 * @param idCard 15位身份编码
	 * @return 18位身份编码
	 */
	public static String convert15To18(final String idCard) {
		final StringBuilder idCard18;
		if (idCard.length() != CIN.CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (ReUtil.isMatch(PatternPool.NUMBERS, idCard)) {
			// 获取出生年月日
			final String birthday = idCard.substring(6, 12);
			final Date birthDate;
			try {
				birthDate = DateUtil.parse(birthday, "yyMMdd");
			} catch (final Exception ignore) {
				throw new IllegalArgumentException("Invalid birthday: " + birthday);
			}
			// 获取出生年(完全表现形式,如：2010)
			int sYear = DateUtil.year(birthDate);
			if (sYear > 2000) {
				// 2000年之后不存在15位身份证号，此处用于修复此问题的判断
				sYear -= 100;
			}
			idCard18 = StrUtil.builder().append(idCard, 0, 6).append(sYear).append(idCard.substring(8));
			// 获取校验位
			final char sVal = getVerifyCode18(idCard18.toString());
			idCard18.append(sVal);
		} else {
			return null;
		}
		return idCard18.toString();
	}

	/**
	 * 将18位身份证号码转换为15位
	 *
	 * @param idCard 18位身份编码
	 * @return 15位身份编码
	 */
	public static String convert18To15(final String idCard) {
		if (StrUtil.isNotBlank(idCard) && IdcardUtil.isValidCard18(idCard)) {
			return idCard.substring(0, 6) + idCard.substring(8, idCard.length() - 1);
		}
		return idCard;
	}

	/**
	 * 判断18位身份证的合法性，第十八位数字(校验码)的计算方法为：
	 * <ol>
	 *   <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 *   <li>将这17位数字和系数相乘的结果相加</li>
	 *   <li>用加出来和除以11，看余数是多少</li>
	 *   <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 *   <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 *
	 * @param idCard     待验证的身份证
	 * @param ignoreCase 是否忽略大小写。{@code true}则忽略X大小写，否则严格匹配大写。
	 * @return 是否有效的18位身份证
	 */
	public static boolean verify(final String idCard, final boolean ignoreCase) {
		if (StrUtil.isBlank(idCard) || CHINA_ID_MAX_LENGTH != idCard.length()) {
			return false;
		}

		// 省份
		final String proCode = idCard.substring(0, 2);
		if (null == CITY_CODES.get(proCode)) {
			return false;
		}

		//校验生日
		if (!Validator.isBirthday(idCard.substring(6, 14))) {
			return false;
		}

		// 前17位
		final String code17 = idCard.substring(0, 17);
		if (ReUtil.isMatch(PatternPool.NUMBERS, code17)) {
			// 获取校验位
			final char val = getVerifyCode18(code17);
			// 第18位
			return CharUtil.equals(val, idCard.charAt(17), ignoreCase);
		}
		return false;
	}

	/**
	 * 获得18位身份证校验码
	 *
	 * @param code17 18位身份证号中的前17位
	 * @return 第18位
	 */
	private static char getVerifyCode18(final String code17) {
		final int sum = getPowerSum(code17.toCharArray());
		return getVerifyCode18(sum);
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 *
	 * @param iSum 加权和
	 * @return 校验位
	 */
	private static char getVerifyCode18(final int iSum) {
		switch (iSum % 11) {
			case 10:
				return '2';
			case 9:
				return '3';
			case 8:
				return '4';
			case 7:
				return '5';
			case 6:
				return '6';
			case 5:
				return '7';
			case 4:
				return '8';
			case 3:
				return '9';
			case 2:
				return 'X';
			case 1:
				return '0';
			case 0:
				return '1';
			default:
				return CharUtil.SPACE;
		}
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 *
	 * @param iArr 身份证号码的数组
	 * @return 身份证编码
	 */
	private static int getPowerSum(final char[] iArr) {
		int iSum = 0;
		if (POWER.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
			}
		}
		return iSum;
	}
}
