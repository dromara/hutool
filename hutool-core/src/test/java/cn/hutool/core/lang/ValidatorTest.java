package cn.hutool.core.lang;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		String var1 = "";
		String var2 = "str";
		String var3 = "180";
		String var4 = "身高180体重180";
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
		boolean b = Validator.isBirthday("20150101");
		Assertions.assertTrue(b);
		boolean b2 = Validator.isBirthday("2015-01-01");
		Assertions.assertTrue(b2);
		boolean b3 = Validator.isBirthday("2015.01.01");
		Assertions.assertTrue(b3);
		boolean b4 = Validator.isBirthday("2015年01月01日");
		Assertions.assertTrue(b4);
		boolean b5 = Validator.isBirthday("2015.01.01");
		Assertions.assertTrue(b5);
		boolean b6 = Validator.isBirthday("2018-08-15");
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
		boolean b = Validator.isCitizenId("110101199003074477");
		Assertions.assertTrue(b);

		// 15位身份证号码验证
		boolean b1 = Validator.isCitizenId("410001910101123");
		Assertions.assertTrue(b1);

		// 10位身份证号码验证
		boolean b2 = Validator.isCitizenId("U193683453");
		Assertions.assertTrue(b2);
	}

	@Test
	public void validateTest() throws ValidateException {
		Assertions.assertThrows(ValidateException.class, () -> {
			Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
		});
	}

	@Test
	public void isEmailTest() {
		boolean email = Validator.isEmail("abc_cde@163.com");
		Assertions.assertTrue(email);
		boolean email1 = Validator.isEmail("abc_%cde@163.com");
		Assertions.assertTrue(email1);
		boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		Assertions.assertTrue(email2);
		boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		Assertions.assertTrue(email3);
		boolean email4 = Validator.isEmail("xiaolei.Lu@aaa.b");
		Assertions.assertTrue(email4);
	}

	@Test
	public void isMobileTest() {
		boolean m1 = Validator.isMobile("13900221432");
		Assertions.assertTrue(m1);
		boolean m2 = Validator.isMobile("015100221432");
		Assertions.assertTrue(m2);
		boolean m3 = Validator.isMobile("+8618600221432");
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
		Assertions.assertFalse(Validator.isChinese("not全都是中文"));
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
		String content = "https://detail.tmall.com/item.htm?" +
				"id=639428931841&ali_refid=a3_430582_1006:1152464078:N:Sk5vwkMVsn5O6DcnvicELrFucL21A32m:0af8611e23c1d07697e";

		Assertions.assertTrue(Validator.isMatchRegex(Validator.URL, content));
		Assertions.assertTrue(Validator.isMatchRegex(Validator.URL_HTTP, content));
	}
}
