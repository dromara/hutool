package cn.hutool.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 身份证相关工具类<br>
 * see https://www.oschina.net/code/snippet_1611_2881
 *
 * <p>
 * 本工具并没有对行政区划代码做校验，如有需求，请参阅（2018年10月）：
 * http://www.mca.gov.cn/article/sj/xzqh/2018/201804-12/20181011221630.html
 * </p>
 *
 * @author Looly
 * @since 3.0.4
 */
public class IdcardUtil {

	/**
	 * 中国公民身份证号码最小长度。
	 */
	private static final int CHINA_ID_MIN_LENGTH = 15;
	/**
	 * 中国公民身份证号码最大长度。
	 */
	private static final int CHINA_ID_MAX_LENGTH = 18;
	/**
	 * 每位加权因子
	 */
	private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	/**
	 * 省市代码表
	 */
	private static final Map<String, String> CITY_CODES = new HashMap<>();
	/**
	 * 台湾身份首字母对应数字
	 */
	private static final Map<String, Integer> TW_FIRST_CODE = new HashMap<>();

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
		CITY_CODES.put("91", "国外");

		TW_FIRST_CODE.put("A", 10);
		TW_FIRST_CODE.put("B", 11);
		TW_FIRST_CODE.put("C", 12);
		TW_FIRST_CODE.put("D", 13);
		TW_FIRST_CODE.put("E", 14);
		TW_FIRST_CODE.put("F", 15);
		TW_FIRST_CODE.put("G", 16);
		TW_FIRST_CODE.put("H", 17);
		TW_FIRST_CODE.put("J", 18);
		TW_FIRST_CODE.put("K", 19);
		TW_FIRST_CODE.put("L", 20);
		TW_FIRST_CODE.put("M", 21);
		TW_FIRST_CODE.put("N", 22);
		TW_FIRST_CODE.put("P", 23);
		TW_FIRST_CODE.put("Q", 24);
		TW_FIRST_CODE.put("R", 25);
		TW_FIRST_CODE.put("S", 26);
		TW_FIRST_CODE.put("T", 27);
		TW_FIRST_CODE.put("U", 28);
		TW_FIRST_CODE.put("V", 29);
		TW_FIRST_CODE.put("X", 30);
		TW_FIRST_CODE.put("Y", 31);
		TW_FIRST_CODE.put("W", 32);
		TW_FIRST_CODE.put("Z", 33);
		TW_FIRST_CODE.put("I", 34);
		TW_FIRST_CODE.put("O", 35);
	}

	/**
	 * 将15位身份证号码转换为18位
	 *
	 * @param idCard 15位身份编码
	 * @return 18位身份编码
	 */
	public static String convert15To18(String idCard) {
		StringBuilder idCard18;
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (ReUtil.isMatch(PatternPool.NUMBERS, idCard)) {
			// 获取出生年月日
			String birthday = idCard.substring(6, 12);
			Date birthDate = DateUtil.parse(birthday, "yyMMdd");
			// 获取出生年(完全表现形式,如：2010)
			int sYear = DateUtil.year(birthDate);
			if (sYear > 2000) {
				// 2000年之后不存在15位身份证号，此处用于修复此问题的判断
				sYear -= 100;
			}
			idCard18 = StrUtil.builder().append(idCard, 0, 6).append(sYear).append(idCard.substring(8));
			// 获取校验位
			char sVal = getCheckCode18(idCard18.toString());
			idCard18.append(sVal);
		} else {
			return null;
		}
		return idCard18.toString();
	}

	/**
	 * 是否有效身份证号
	 *
	 * @param idCard 身份证号，支持18位、15位和港澳台的10位
	 * @return 是否有效
	 */
	public static boolean isValidCard(String idCard) {
		idCard = idCard.trim();
		int length = idCard.length();
		switch (length) {
			case 18:// 18位身份证
				return isValidCard18(idCard);
			case 15:// 15位身份证
				return isValidCard15(idCard);
			case 10: {// 10位身份证，港澳台地区
				String[] cardVal = isValidCard10(idCard);
				return null != cardVal && "true".equals(cardVal[2]);
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
	 *
	 * @param idcard 待验证的身份证
	 * @return 是否有效的18位身份证
	 */
	public static boolean isValidCard18(String idcard) {
		if (CHINA_ID_MAX_LENGTH != idcard.length()) {
			return false;
		}

		// 省份
		final String proCode = idcard.substring(0, 2);
		if (null == CITY_CODES.get(proCode)) {
			return false;
		}

		//校验生日
		if (false == Validator.isBirthday(idcard.substring(6, 14))) {
			return false;
		}

		// 前17位
		String code17 = idcard.substring(0, 17);
		// 第18位
		char code18 = Character.toLowerCase(idcard.charAt(17));
		if (ReUtil.isMatch(PatternPool.NUMBERS, code17)) {
			// 获取校验位
			char val = getCheckCode18(code17);
			return val == code18;
		}
		return false;
	}

	/**
	 * 验证15位身份编码是否合法
	 *
	 * @param idcard 身份编码
	 * @return 是否合法
	 */
	public static boolean isValidCard15(String idcard) {
		if (CHINA_ID_MIN_LENGTH != idcard.length()) {
			return false;
		}
		if (ReUtil.isMatch(PatternPool.NUMBERS, idcard)) {
			// 省份
			String proCode = idcard.substring(0, 2);
			if (null == CITY_CODES.get(proCode)) {
				return false;
			}

			//校验生日（两位年份，补充为19XX）
			return false != Validator.isBirthday("19" + idcard.substring(6, 12));
		} else {
			return false;
		}
	}

	/**
	 * 验证10位身份编码是否合法
	 *
	 * @param idcard 身份编码
	 * @return 身份证信息数组
	 * <p>
	 * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
	 * </p>
	 */
	public static String[] isValidCard10(String idcard) {
		if (StrUtil.isBlank(idcard)) {
			return null;
		}
		String[] info = new String[3];
		String card = idcard.replaceAll("[()]", "");
		if (card.length() != 8 && card.length() != 9 && idcard.length() != 10) {
			return null;
		}
		if (idcard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
			info[0] = "台湾";
			char char2 = idcard.charAt(1);
			if ('1' == char2) {
				info[1] = "M";
			} else if ('2' == char2) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = isValidTWCard(idcard) ? "true" : "false";
		} else if (idcard.matches("^[157][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
			info[0] = "澳门";
			info[1] = "N";
		} else if (idcard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
			info[0] = "香港";
			info[1] = "N";
			info[2] = isValidHKCard(idcard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码
	 *
	 * @param idcard 身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean isValidTWCard(String idcard) {
		if (StrUtil.isEmpty(idcard)) {
			return false;
		}
		String start = idcard.substring(0, 1);
		Integer iStart = TW_FIRST_CODE.get(start);
		if (null == iStart) {
			return false;
		}
		String mid = idcard.substring(1, 9);
		String end = idcard.substring(9, 10);
		int sum = iStart / 10 + (iStart % 10) * 9;
		final char[] chars = mid.toCharArray();
		int iflag = 8;
		for (char c : chars) {
			sum += Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
	}

	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
	 * <p>
	 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
	 * </p>
	 * <p>
	 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
	 * </p>
	 *
	 * @param idcard 身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean isValidHKCard(String idcard) {
		String card = idcard.replaceAll("[()]", "");
		int sum;
		if (card.length() == 9) {
			sum = (Character.toUpperCase(card.charAt(0)) - 55) * 9 + (Character.toUpperCase(card.charAt(1)) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Character.toUpperCase(card.charAt(0)) - 55) * 8;
		}

		// 首字母A-Z，A表示1，以此类推
		char start = idcard.charAt(0);
		int iStart = start - 'A' + 1;
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		int iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}
		if ("A".equals(end.toUpperCase())) {
			sum += 10;
		} else {
			sum += Integer.parseInt(end);
		}
		return sum % 11 == 0;
	}

	/**
	 * 根据身份编号获取生日，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(yyyyMMdd)
	 * @see #getBirth(String)
	 */
	public static String getBirthByIdCard(String idcard) {
		return getBirth(idcard);
	}

	/**
	 * 根据身份编号获取生日，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirth(String idCard) {
		Assert.notBlank(idCard, "id card must be not blank!");
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}

		return Objects.requireNonNull(idCard).substring(6, 14);
	}

	/**
	 * 从身份证号码中获取生日日期，只支持15或18位身份证号码
	 *
	 * @param idCard 身份证号码
	 * @return 日期
	 */
	public static DateTime getBirthDate(String idCard) {
		final String birthByIdCard = getBirthByIdCard(idCard);
		return null == birthByIdCard ? null : DateUtil.parse(birthByIdCard, DatePattern.PURE_DATE_FORMAT);
	}

	/**
	 * 根据身份编号获取年龄，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idcard) {
		return getAgeByIdCard(idcard, DateUtil.date());
	}

	/**
	 * 根据身份编号获取指定日期当时的年龄年龄，只支持15或18位身份证号码
	 *
	 * @param idcard        身份编号
	 * @param dateToCompare 以此日期为界，计算年龄。
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idcard, Date dateToCompare) {
		String birth = getBirthByIdCard(idcard);
		return DateUtil.age(DateUtil.parse(birth, "yyyyMMdd"), dateToCompare);
	}

	/**
	 * 根据身份编号获取生日年，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(yyyy)
	 */
	public static Short getYearByIdCard(String idcard) {
		final int len = idcard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idcard = convert15To18(idcard);
		}
		return Short.valueOf(Objects.requireNonNull(idcard).substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(MM)
	 */
	public static Short getMonthByIdCard(String idcard) {
		final int len = idcard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idcard = convert15To18(idcard);
		}
		return Short.valueOf(Objects.requireNonNull(idcard).substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 生日(dd)
	 */
	public static Short getDayByIdCard(String idcard) {
		final int len = idcard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idcard = convert15To18(idcard);
		}
		return Short.valueOf(Objects.requireNonNull(idcard).substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编号
	 * @return 性别(1 : 男 ， 0 : 女)
	 */
	public static int getGenderByIdCard(String idcard) {
		Assert.notBlank(idcard);
		final int len = idcard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			throw new IllegalArgumentException("ID Card length must be 15 or 18");
		}

		if (len == CHINA_ID_MIN_LENGTH) {
			idcard = convert15To18(idcard);
		}
		char sCardChar = Objects.requireNonNull(idcard).charAt(16);
		return (sCardChar % 2 != 0) ? 1 : 0;
	}

	/**
	 * 根据身份编号获取户籍省份，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编码
	 * @return 省份名称。
	 */
	public static String getProvinceByIdCard(String idcard) {
		int len = idcard.length();
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			String sProvinNum = idcard.substring(0, 2);
			return CITY_CODES.get(sProvinNum);
		}
		return null;
	}

	/**
	 * 根据身份编号获取户籍省份，只支持15或18位身份证号码
	 *
	 * @param idcard 身份编码
	 * @return 市级编码。
	 */
	public static String getCityCodeByIdCard(String idcard) {
		int len = idcard.length();
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			return idcard.substring(0, 5);
		}
		return null;
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
	public static String hide(String idcard, int startInclude, int endExclude) {
		return StrUtil.hide(idcard, startInclude, endExclude);
	}

	/**
	 * 获取身份证信息，包括身份、城市代码、生日、性别等
	 *
	 * @param idcard 15或18位身份证
	 * @return {@link Idcard}
	 * @since 5.4.3
	 */
	public static Idcard getIdcardInfo(String idcard){
		return new Idcard(idcard);
	}

	// ----------------------------------------------------------------------------------- Private method start

	/**
	 * 获得18位身份证校验码
	 *
	 * @param code17 18位身份证号中的前17位
	 * @return 第18位
	 */
	private static char getCheckCode18(String code17) {
		int sum = getPowerSum(code17.toCharArray());
		return getCheckCode18(sum);
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 *
	 * @param iSum 加权和
	 * @return 校验位
	 */
	private static char getCheckCode18(int iSum) {
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
				return 'x';
			case 1:
				return '0';
			case 0:
				return '1';
			default:
				return StrUtil.C_SPACE;
		}
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 *
	 * @param iArr 身份证号码的数组
	 * @return 身份证编码
	 */
	private static int getPowerSum(char[] iArr) {
		int iSum = 0;
		if (POWER.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
			}
		}
		return iSum;
	}
	// ----------------------------------------------------------------------------------- Private method end

	/**
	 * 身份证信息，包括身份、城市代码、生日、性别等
	 *
	 * @author looly
	 * @since 5.4.3
	 */
	public static class Idcard implements Serializable {
		private static final long serialVersionUID = 1L;

		private final String provinceCode;
		private final String cityCode;
		private final DateTime birthDate;
		private final Integer gender;

		/**
		 * 构造
		 *
		 * @param idcard 身份证号码
		 */
		public Idcard(String idcard) {
			this.provinceCode = IdcardUtil.getProvinceByIdCard(idcard);
			this.cityCode = IdcardUtil.getCityCodeByIdCard(idcard);
			this.birthDate = IdcardUtil.getBirthDate(idcard);
			this.gender = IdcardUtil.getGenderByIdCard(idcard);
		}

		/**
		 * 获取省份代码
		 *
		 * @return 省份代码
		 */
		public String getProvinceCode() {
			return this.provinceCode;
		}

		/**
		 * 获取省份名称
		 *
		 * @return 省份代码
		 */
		public String getProvince() {
			return CITY_CODES.get(this.provinceCode);
		}

		/**
		 * 获取省份代码
		 *
		 * @return 省份代码
		 */
		public String getCityCode() {
			return this.cityCode;
		}

		/**
		 * 获得生日日期
		 *
		 * @return 生日日期
		 */
		public DateTime getBirthDate() {
			return this.birthDate;
		}

		/**
		 * 获取性别代号，性别(1 : 男 ， 0 : 女)
		 *
		 * @return 性别(1 : 男 ， 0 : 女)
		 */
		public Integer getGender() {
			return this.gender;
		}
	}
}
