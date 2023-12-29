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

package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.exception.ValidateException;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * 验证器单元测试
 *
 * @author Looly
 */
public class ValidatorTest {

	@Test
	public void isNumberTest() {
		Assertions.assertTrue(Validator.isNumber("45345365465"));
		Assertions.assertTrue(Validator.isNumber("0004545435"));
		Assertions.assertTrue(Validator.isNumber("5.222"));
		Assertions.assertTrue(Validator.isNumber("0.33323"));
	}

	@Test
	public void hasNumberTest() {
		final String var1 = "";
		final String var2 = "str";
		final String var3 = "180";
		final String var4 = "身高180体重180";
		Assertions.assertFalse(Validator.hasNumber(var1));
		Assertions.assertFalse(Validator.hasNumber(var2));
		Assertions.assertTrue(Validator.hasNumber(var3));
		Assertions.assertTrue(Validator.hasNumber(var4));
	}

	@Test
	public void isLetterTest() {
		Assertions.assertTrue(Validator.isLetter("asfdsdsfds"));
		Assertions.assertTrue(Validator.isLetter("asfdsdfdsfVCDFDFGdsfds"));
		Assertions.assertTrue(Validator.isLetter("asfdsdf你好dsfVCDFDFGdsfds"));
	}

	@Test
	public void isUperCaseTest() {
		Assertions.assertTrue(Validator.isUpperCase("VCDFDFG"));
		Assertions.assertTrue(Validator.isUpperCase("ASSFD"));

		Assertions.assertFalse(Validator.isUpperCase("asfdsdsfds"));
		Assertions.assertFalse(Validator.isUpperCase("ASSFD你好"));
	}

	@Test
	public void isLowerCaseTest() {
		Assertions.assertTrue(Validator.isLowerCase("asfdsdsfds"));

		Assertions.assertFalse(Validator.isLowerCase("aaaa你好"));
		Assertions.assertFalse(Validator.isLowerCase("VCDFDFG"));
		Assertions.assertFalse(Validator.isLowerCase("ASSFD"));
		Assertions.assertFalse(Validator.isLowerCase("ASSFD你好"));
	}

	@Test
	public void isBirthdayTest() {
		final boolean b = Validator.isBirthday("20150101");
		Assertions.assertTrue(b);
		final boolean b2 = Validator.isBirthday("2015-01-01");
		Assertions.assertTrue(b2);
		final boolean b3 = Validator.isBirthday("2015.01.01");
		Assertions.assertTrue(b3);
		final boolean b4 = Validator.isBirthday("2015年01月01日");
		Assertions.assertTrue(b4);
		final boolean b5 = Validator.isBirthday("2015.01.01");
		Assertions.assertTrue(b5);
		final boolean b6 = Validator.isBirthday("2018-08-15");
		Assertions.assertTrue(b6);

		//验证年非法
		Assertions.assertFalse(Validator.isBirthday("2095.05.01"));
		//验证月非法
		Assertions.assertFalse(Validator.isBirthday("2015.13.01"));
		//验证日非法
		Assertions.assertFalse(Validator.isBirthday("2015.02.29"));
	}

	@Test
	public void isCitizenIdTest() {
		// 18为身份证号码验证
		final boolean b = Validator.isCitizenId("110101199003074477");
		Assertions.assertTrue(b);

		// 15位身份证号码验证
		final boolean b1 = Validator.isCitizenId("410001910101123");
		Assertions.assertTrue(b1);

		// 10位身份证号码验证
		final boolean b2 = Validator.isCitizenId("U193683453");
		Assertions.assertTrue(b2);
	}

	@Test
	public void validateTest() throws ValidateException {
		Assertions.assertThrows(ValidateException.class, ()-> Validator.validateChinese("我是一段zhongwen", "内容中包含非中文"));
	}

	@Test
	public void isEmailTest() {
		final boolean email = Validator.isEmail("abc_cde@163.com");
		Assertions.assertTrue(email);
		final boolean email1 = Validator.isEmail("abc_%cde@163.com");
		Assertions.assertTrue(email1);
		final boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		Assertions.assertTrue(email2);
		final boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		Assertions.assertTrue(email3);
		final boolean email4 = Validator.isEmail("xiaolei.Lu@aaa.b");
		Assertions.assertTrue(email4);
		final boolean email5 = Validator.isEmail("luxiaolei_小磊@小磊.com");
		Assertions.assertTrue(email5);
	}

	@Test
	public void isMobileTest() {
		final boolean m1 = Validator.isMobile("13900221432");
		Assertions.assertTrue(m1);
		final boolean m2 = Validator.isMobile("015100221432");
		Assertions.assertTrue(m2);
		final boolean m3 = Validator.isMobile("+8618600221432");
		Assertions.assertTrue(m3);
	}

	@Test
	public void isMatchTest() {
		String url = "http://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assertions.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assertions.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com:8080/a.php?a=b&c=2";
		Assertions.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));
	}

	@Test
	public void isGeneralTest() {
		String str = "";
		boolean general = Validator.isGeneral(str, -1, 5);
		Assertions.assertTrue(general);

		str = "123_abc_ccc";
		general = Validator.isGeneral(str, -1, 100);
		Assertions.assertTrue(general);

		// 不允许中文
		str = "123_abc_ccc中文";
		general = Validator.isGeneral(str, -1, 100);
		Assertions.assertFalse(general);
	}

	@Test
	public void isPlateNumberTest(){
		Assertions.assertTrue(Validator.isPlateNumber("粤BA03205"));
		Assertions.assertTrue(Validator.isPlateNumber("闽20401领"));
	}

	@Test
	public void isChineseTest(){
		Assertions.assertTrue(Validator.isChinese("全都是中文"));
		Assertions.assertTrue(Validator.isChinese("㐓㐘"));
		Assertions.assertFalse(Validator.isChinese("not全都是中文"));
	}

	@Test
	public void hasChineseTest() {
		Assertions.assertTrue(Validator.hasChinese("黄单桑米"));
		Assertions.assertTrue(Validator.hasChinese("Kn 四兄弟"));
		Assertions.assertTrue(Validator.hasChinese("\uD840\uDDA3"));
		Assertions.assertFalse(Validator.hasChinese("Abc"));
	}

	@Test
	public void isUUIDTest(){
		Assertions.assertTrue(Validator.isUUID(IdUtil.randomUUID()));
		Assertions.assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID()));

		Assertions.assertTrue(Validator.isUUID(IdUtil.randomUUID().toUpperCase()));
		Assertions.assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID().toUpperCase()));
	}

	@Test
	public void isZipCodeTest(){
		// 港
		boolean zipCode = Validator.isZipCode("999077");
		Assertions.assertTrue(zipCode);
		// 澳
		zipCode = Validator.isZipCode("999078");
		Assertions.assertTrue(zipCode);
		// 台（2020年3月起改用6位邮编，3+3）
		zipCode = Validator.isZipCode("822001");
		Assertions.assertTrue(zipCode);

		// 内蒙
		zipCode = Validator.isZipCode("016063");
		Assertions.assertTrue(zipCode);
		// 山西
		zipCode = Validator.isZipCode("045246");
		Assertions.assertTrue(zipCode);
		// 河北
		zipCode = Validator.isZipCode("066502");
		Assertions.assertTrue(zipCode);
		// 北京
		zipCode = Validator.isZipCode("102629");
		Assertions.assertTrue(zipCode);
	}

	@Test
	public void isBetweenTest() {
		Assertions.assertTrue(Validator.isBetween(0, 0, 1));
		Assertions.assertTrue(Validator.isBetween(1L, 0L, 1L));
		Assertions.assertTrue(Validator.isBetween(0.19f, 0.1f, 0.2f));
		Assertions.assertTrue(Validator.isBetween(0.19, 0.1, 0.2));
	}

	@Test
	public void isCarVinTest(){
		Assertions.assertTrue(Validator.isCarVin("LSJA24U62JG269225"));
		Assertions.assertTrue(Validator.isCarVin("LDC613P23A1305189"));
		Assertions.assertFalse(Validator.isCarVin("LOC613P23A1305189"));

		Assertions.assertTrue(Validator.isCarVin("LSJA24U62JG269225"));	//标准分类1
		Assertions.assertTrue(Validator.isCarVin("LDC613P23A1305189"));	//标准分类1
		Assertions.assertTrue(Validator.isCarVin("LBV5S3102ESJ25655"));	//标准分类1
		Assertions.assertTrue(Validator.isCarVin("LBV5S3102ESJPE655"));	//标准分类2
		Assertions.assertFalse(Validator.isCarVin("LOC613P23A1305189"));	//错误示例
	}

	@Test
	public void isCarDrivingLicenceTest(){
		Assertions.assertTrue(Validator.isCarDrivingLicence("430101758218"));
	}

	@Test
	public void validateIpv4Test(){
		Validator.validateIpv4("192.168.1.1", "Error ip");
		Validator.validateIpv4("8.8.8.8", "Error ip");
		Validator.validateIpv4("0.0.0.0", "Error ip");
		Validator.validateIpv4("255.255.255.255", "Error ip");
		Validator.validateIpv4("127.0.0.0", "Error ip");
	}

	@Test
	public void isUrlTest(){
		final String content = "https://detail.tmall.com/item.htm?" +
				"id=639428931841&ali_refid=a3_430582_1006:1152464078:N:Sk5vwkMVsn5O6DcnvicELrFucL21A32m:0af8611e23c1d07697e";

		Assertions.assertTrue(Validator.isMatchRegex(PatternPool.URL, content));
		Assertions.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, content));
	}

	@Test
	public void validateBetweenTest(){
		final Date value = DateUtil.parse("2022-08-09 21:22:00");
		final Date start = DateUtil.parse("2022-08-01 21:22:00");
		final Date end = DateUtil.parse("2022-09-09 21:22:00");

		Validator.validateBetween(value, start, end, "您选择的日期超出规定范围!");
		Assertions.assertTrue(true);

		final Date value2 = DateUtil.parse("2023-08-09 21:22:00");
		Assertions.assertThrows(ValidateException.class, ()->
				Validator.validateBetween(value2, start, end, "您选择的日期超出规定范围!")
		);
	}

	@Test
	public void validateLengthTest() {
		final String s1 = "abc";
		Assertions.assertThrows(ValidateException.class, ()->
				Validator.validateLength(s1, 6, 8, "请输入6到8位的字符！"));
	}

	@Test
	public void validateByteLengthTest() {
		final String s1 = "abc";
		final int len1 = StrUtil.byteLength(s1, CharsetUtil.UTF_8);
		Assertions.assertEquals(len1, 3);

		final String s2 = "我ab";
		final int len2 = StrUtil.byteLength(s2, CharsetUtil.UTF_8);
		Assertions.assertEquals(len2, 5);

		//一个汉字在utf-8编码下，占3个字节。
		Assertions.assertThrows(ValidateException.class, ()->
				Validator.validateByteLength(s2, 1, 3, "您输入的字符串长度超出限制！")
		);
	}

	@Test
	public void isChineseNameTest(){
		Assertions.assertTrue(Validator.isChineseName("阿卜杜尼亚孜·毛力尼亚孜"));
		Assertions.assertFalse(Validator.isChineseName("阿卜杜尼亚孜./毛力尼亚孜"));
		Assertions.assertTrue(Validator.isChineseName("段正淳"));
		Assertions.assertFalse(Validator.isChineseName("孟  伟"));
		Assertions.assertFalse(Validator.isChineseName("李"));
		Assertions.assertFalse(Validator.isChineseName("连逍遥0"));
		Assertions.assertFalse(Validator.isChineseName("SHE"));
	}

	@Test
	void issue3352Test() {
		Validator.validateEmail("Zdd@hutool.cn", "test");
	}
}
