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

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Date;

/**
 * 身份证相关工具类，参考标准：GB 11643-1999<br>
 * 标准描述见：<a href="http://openstd.samr.gov.cn/bzgk/gb/newGbInfo?hcno=080D6FBF2BB468F9007657F26D60013E">http://openstd.samr.gov.cn/bzgk/gb/newGbInfo?hcno=080D6FBF2BB468F9007657F26D60013E</a>
 *
 * <p>
 * 本工具并没有对行政区划代码做校验，如有需求，请参阅（2020年12月）：
 * <a href="http://www.mca.gov.cn/article/sj/xzqh/2020/20201201.html">http://www.mca.gov.cn/article/sj/xzqh/2020/20201201.html</a>
 * </p>
 *
 * @author Looly
 * @since 3.0.4
 */
public class IdcardUtil {

	/**
	 * 将15位身份证号码转换为18位<br>
	 * 15位身份证号码遵循GB 11643-1989标准。
	 *
	 * @param idCard 15位身份编码
	 * @return 18位身份编码
	 */
	public static String convert15To18(final String idCard) {
		return CIN.convert15To18(idCard);
	}

	/**
	 * 将18位身份证号码转换为15位
	 *
	 * @param idCard 18位身份编码
	 * @return 15位身份编码
	 */
	public static String convert18To15(final String idCard) {
		return CIN.convert18To15(idCard);
	}

	/**
	 * 是否有效身份证号，忽略X的大小写<br>
	 * 如果身份证号码中含有空格始终返回{@code false}
	 *
	 * @param idCard 身份证号，支持18位、15位和港澳台的10位
	 * @return 是否有效
	 */
	public static boolean isValidCard(final String idCard) {
		if (StrUtil.isBlank(idCard)) {
			return false;
		}

		//idCard = idCard.trim();
		final int length = idCard.length();
		switch (length) {
			case 18:// 18位身份证
				return isValidCard18(idCard);
			case 15:// 15位身份证
				try{
					return isValidCard18(CIN.convert15To18(idCard));
				} catch (final Exception ignore){
					return false;
				}
			case 10: {// 10位身份证，港澳台地区
				return isValidCard10(idCard);
			}
			default:
				return false;
		}
	}

	/**
	 * <p>
	 * 判断18位身份证的合法性
	 * </p>
	 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。<br>
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * <p>
	 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
	 * </p>
	 * <ol>
	 * <li>第1、2位数字表示：所在省份的代码</li>
	 * <li>第3、4位数字表示：所在城市的代码</li>
	 * <li>第5、6位数字表示：所在区县的代码</li>
	 * <li>第7~14位数字表示：出生年、月、日</li>
	 * <li>第15、16位数字表示：所在地的派出所的代码</li>
	 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性</li>
	 * <li>第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示</li>
	 * </ol>
	 * <p>
	 * 第十八位数字(校验码)的计算方法为：
	 * <ol>
	 * <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 * <li>将这17位数字和系数相乘的结果相加</li>
	 * <li>用加出来和除以11，看余数是多少</li>
	 * <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 * <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 * <ol>
	 * 	     <li>香港人在大陆的身份证，【810000】开头；同样可以直接获取到 性别、出生日期</li>
	 * 	     <li>81000019980902013X: 文绎循 男 1998-09-02</li>
	 * 	     <li>810000201011210153: 辛烨 男 2010-11-21</li>
	 * 	 </ol>
	 * 	 <ol>
	 *       <li>澳门人在大陆的身份证，【820000】开头；同样可以直接获取到 性别、出生日期</li>
	 *       <li>820000200009100032: 黄敬杰 男 2000-09-10</li>
	 *  </ol>
	 *  <ol>
	 *     <li>台湾人在大陆的身份证，【830000】开头；同样可以直接获取到 性别、出生日期</li>
	 *     <li>830000200209060065: 王宜妃 女 2002-09-06</li>
	 *     <li>830000194609150010: 苏建文 男 1946-09-14</li>
	 *     <li>83000019810715006X: 刁婉琇 女 1981-07-15</li>
	 * </ol>
	 *
	 * @param idcard 待验证的身份证
	 * @return 是否有效的18位身份证，忽略x的大小写
	 */
	public static boolean isValidCard18(final String idcard) {
		return isValidCard18(idcard, true);
	}

	/**
	 * <p>
	 * 判断18位身份证的合法性
	 * </p>
	 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。<br>
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * <p>
	 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
	 * </p>
	 * <ol>
	 * <li>第1、2位数字表示：所在省份的代码</li>
	 * <li>第3、4位数字表示：所在城市的代码</li>
	 * <li>第5、6位数字表示：所在区县的代码</li>
	 * <li>第7~14位数字表示：出生年、月、日</li>
	 * <li>第15、16位数字表示：所在地的派出所的代码</li>
	 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性</li>
	 * <li>第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示</li>
	 * </ol>
	 * <p>
	 * 第十八位数字(校验码)的计算方法为：
	 * <ol>
	 * <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 * <li>将这17位数字和系数相乘的结果相加</li>
	 * <li>用加出来和除以11，看余数是多少</li>
	 * <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 * <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 *
	 * @param idcard     待验证的身份证
	 * @param ignoreCase 是否忽略大小写。{@code true}则忽略X大小写，否则严格匹配大写。
	 * @return 是否有效的18位身份证
	 * @since 5.5.7
	 */
	public static boolean isValidCard18(final String idcard, final boolean ignoreCase) {
		return CIN.verify(idcard, ignoreCase);
	}

	/**
	 * 是否有效的10位身份证号码，一般用于判断和验证台湾、澳门、香港身份证
	 * @param idcard 台湾、澳门、香港身份证号码
	 * @return 是否有效的10位身份证号码
	 */
	public static boolean isValidCard10(final String idcard){
		try{
			return CIN10.of(idcard).isVerified();
		}catch (final IllegalArgumentException e){
			return false;
		}
	}

	/**
	 * 根据身份编号获取生日，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirth(final String idCard) {
		return getCIN(idCard).getBirth();
	}

	/**
	 * 从身份证号码中获取生日日期，只支持15或18位身份证号码
	 *
	 * @param idCard 身份证号码
	 * @return 日期
	 */
	public static DateTime getBirthDate(final String idCard) {
		return getCIN(idCard).getBirthDate();
	}

	/**
	 * 根据身份编号获取年龄，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 年龄
	 */
	public static int getAge(final String idcard) {
		return getAge(idcard, DateUtil.now());
	}

	/**
	 * 根据身份编号获取指定日期当时的年龄年龄，只支持15或18位身份证号码
	 *
	 * @param idcard        身份编号
	 * @param dateToCompare 以此日期为界，计算年龄。
	 * @return 年龄
	 */
	public static int getAge(final String idcard, final Date dateToCompare) {
		return DateUtil.age(getBirthDate(idcard), dateToCompare);
	}

	/**
	 * 根据身份编号获取生日年，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(yyyy)
	 */
	public static Short getBirthYear(final String idcard) {
		return getCIN(idcard).getBirthYear();
	}

	/**
	 * 根据身份编号获取生日月，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(MM)
	 */
	public static Short getBirthMonth(final String idcard) {
		return getCIN(idcard).getBirthMonth();
	}

	/**
	 * 根据身份编号获取生日天，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(dd)
	 */
	public static Short getBirthDay(final String idcard) {
		return getCIN(idcard).getBirthDay();
	}

	/**
	 * 根据身份编号获取性别，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 性别(1 : 男 ， 0 : 女)
	 */
	public static int getGender(final String idcard) {
		return getCIN(idcard).getGender();
	}

	/**
	 * 根据身份编号获取户籍省份编码，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编码
	 * @return 省份编码
	 * @since 5.7.2
	 */
	public static String getProvinceCode(final String idcard) {
		return getCIN(idcard).getProvinceCode();
	}

	/**
	 * 根据身份编号获取户籍省份，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编码
	 * @return 省份名称。
	 */
	public static String getProvince(final String idcard) {
		return getCIN(idcard).getProvince();
	}

	/**
	 * 根据身份编号获取地市级编码，只支持15或18位身份证号码<br>
	 * 获取编码为4位
	 *
	 * @param idcard 身份编码
	 * @return 地市级编码
	 */
	public static String getCityCode(final String idcard) {
		return getCIN(idcard).getCityCode();
	}

	/**
	 * 根据身份编号获取区县级编码，只支持15或18位身份证号码<br>
	 * 获取编码为6位
	 *
	 * @param idcard 身份编码
	 * @return 地市级编码
	 * @since 5.8.0
	 */
	public static String getDistrictCode(final String idcard) {
		return getCIN(idcard).getDistrictCode();
	}

	/**
	 * 隐藏指定位置的几个身份证号数字为“*”
	 *
	 * @param idcard       身份证号
	 * @param startInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @return 隐藏后的身份证号码
	 * @see StrUtil#hide(CharSequence, int, int)
	 * @since 3.2.2
	 */
	public static String hide(final String idcard, final int startInclude, final int endExclude) {
		return StrUtil.hide(idcard, startInclude, endExclude);
	}

	/**
	 * 获取公民身份证（CIN）信息，包括身份、城市代码、生日、性别等
	 *
	 * @param idcard 15或18位身份证
	 * @return {@link CIN}
	 */
	public static CIN getCIN(final String idcard) {
		return CIN.of(idcard);
	}

	/**
	 * 港澳居民来往内地通行证，俗称：回乡证，通行证号码组成规则：<br>
	 * <ul>
	 *     <li>通行证证件号码共11位。第1位为字母，“H”字头签发给香港居民，“M”字头签发给澳门居民。</li>
	 *     <li>第2位至第11位为数字，前8位数字为通行证持有人的终身号，后2位数字表示换证次数，首次发证为00，此后依次递增。</li>
	 * </ul>
	 * 示例：H12345678、M1234567801
	 *
	 * <p>
	 * 参考文档《港澳居民来往内地通行证号码规则》：
	 * <a href="https://www.hmo.gov.cn/fwga_new/wldjnd/201711/t20171120_1333.html">https://www.hmo.gov.cn/fwga_new/wldjnd/201711/t20171120_1333.html</a>
	 * </p>
	 *
	 * @param idCard 身份证号码
	 * @return 是否有效
	 */
	public static boolean isValidHkMoHomeReturn(final String idCard) {
		if (StrUtil.isEmpty(idCard)) {
			return false;
		}
		// 规则： H/M + 8位或10位数字
		// 样本： H1234567890
		final String reg = "^[HhMm](\\d{8}|\\d{10})$";
		return idCard.matches(reg);
	}
}
