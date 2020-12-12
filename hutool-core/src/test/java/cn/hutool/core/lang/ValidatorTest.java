package cn.hutool.core.lang;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.IdUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 验证器单元测试
 *
 * @author Looly
 */
public class ValidatorTest {

	@Test
	public void isNumberTest() {
		Assert.assertTrue(Validator.isNumber("45345365465"));
		Assert.assertTrue(Validator.isNumber("0004545435"));
		Assert.assertTrue(Validator.isNumber("5.222"));
		Assert.assertTrue(Validator.isNumber("0.33323"));
	}

	@Test
	public void isLetterTest() {
		Assert.assertTrue(Validator.isLetter("asfdsdsfds"));
		Assert.assertTrue(Validator.isLetter("asfdsdfdsfVCDFDFGdsfds"));
		Assert.assertTrue(Validator.isLetter("asfdsdf你好dsfVCDFDFGdsfds"));
	}

	@Test
	public void isUperCaseTest() {
		Assert.assertTrue(Validator.isUpperCase("VCDFDFG"));
		Assert.assertTrue(Validator.isUpperCase("ASSFD"));

		Assert.assertFalse(Validator.isUpperCase("asfdsdsfds"));
		Assert.assertFalse(Validator.isUpperCase("ASSFD你好"));
	}

	@Test
	public void isLowerCaseTest() {
		Assert.assertTrue(Validator.isLowerCase("asfdsdsfds"));

		Assert.assertFalse(Validator.isLowerCase("aaaa你好"));
		Assert.assertFalse(Validator.isLowerCase("VCDFDFG"));
		Assert.assertFalse(Validator.isLowerCase("ASSFD"));
		Assert.assertFalse(Validator.isLowerCase("ASSFD你好"));
	}

	@Test
	public void isBirthdayTest() {
		boolean b = Validator.isBirthday("20150101");
		Assert.assertTrue(b);
		boolean b2 = Validator.isBirthday("2015-01-01");
		Assert.assertTrue(b2);
		boolean b3 = Validator.isBirthday("2015.01.01");
		Assert.assertTrue(b3);
		boolean b4 = Validator.isBirthday("2015年01月01日");
		Assert.assertTrue(b4);
		boolean b5 = Validator.isBirthday("2015.01.01");
		Assert.assertTrue(b5);
		boolean b6 = Validator.isBirthday("2018-08-15");
		Assert.assertTrue(b6);

		//验证年非法
		Assert.assertFalse(Validator.isBirthday("2095.05.01"));
		//验证月非法
		Assert.assertFalse(Validator.isBirthday("2015.13.01"));
		//验证日非法
		Assert.assertFalse(Validator.isBirthday("2015.02.29"));
	}

	@Test
	public void isCitizenIdTest() {
		// 18为身份证号码验证
		boolean b = Validator.isCitizenId("110101199003074477");
		Assert.assertTrue(b);

		// 15位身份证号码验证
		boolean b1 = Validator.isCitizenId("410001910101123");
		Assert.assertTrue(b1);

		// 10位身份证号码验证
		boolean b2 = Validator.isCitizenId("U193683453");
		Assert.assertTrue(b2);
	}

	@Test(expected = ValidateException.class)
	public void validateTest() throws ValidateException {
		Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
	}

	@Test
	public void isEmailTest() {
		boolean email = Validator.isEmail("abc_cde@163.com");
		Assert.assertTrue(email);
		boolean email1 = Validator.isEmail("abc_%cde@163.com");
		Assert.assertTrue(email1);
		boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		Assert.assertTrue(email2);
		boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		Assert.assertTrue(email3);
		boolean email4 = Validator.isEmail("xiaolei.Lu@aaa.b");
		Assert.assertTrue(email4);
	}

	@Test
	public void isMobileTest() {
		boolean m1 = Validator.isMobile("13900221432");
		Assert.assertTrue(m1);
		boolean m2 = Validator.isMobile("015100221432");
		Assert.assertTrue(m2);
		boolean m3 = Validator.isMobile("+8618600221432");
		Assert.assertTrue(m3);
	}

	@Test
	public void isMatchTest() {
		String url = "http://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assert.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assert.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com:8080/a.php?a=b&c=2";
		Assert.assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));
	}

	@Test
	public void isGeneralTest() {
		String str = "";
		boolean general = Validator.isGeneral(str, -1, 5);
		Assert.assertTrue(general);

		str = "123_abc_ccc";
		general = Validator.isGeneral(str, -1, 100);
		Assert.assertTrue(general);

		// 不允许中文
		str = "123_abc_ccc中文";
		general = Validator.isGeneral(str, -1, 100);
		Assert.assertFalse(general);
	}

	@Test
	public void isPlateNumberTest(){
		Assert.assertTrue(Validator.isPlateNumber("粤BA03205"));
		Assert.assertTrue(Validator.isPlateNumber("闽20401领"));
	}

	@Test
	public void isChineseTest(){
		Assert.assertTrue(Validator.isChinese("全都是中文"));
		Assert.assertFalse(Validator.isChinese("not全都是中文"));
	}

	@Test
	public void isUUIDTest(){
		Assert.assertTrue(Validator.isUUID(IdUtil.randomUUID()));
		Assert.assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID()));

		Assert.assertTrue(Validator.isUUID(IdUtil.randomUUID().toUpperCase()));
		Assert.assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID().toUpperCase()));
	}

	@Test
	public void isZipCodeTest(){
		// 港
		boolean zipCode = Validator.isZipCode("999077");
		Assert.assertTrue(zipCode);
		// 澳
		zipCode = Validator.isZipCode("999078");
		Assert.assertTrue(zipCode);
		// 台（2020年3月起改用6位邮编，3+3）
		zipCode = Validator.isZipCode("822001");
		Assert.assertTrue(zipCode);

		// 内蒙
		zipCode = Validator.isZipCode("016063");
		Assert.assertTrue(zipCode);
		// 山西
		zipCode = Validator.isZipCode("045246");
		Assert.assertTrue(zipCode);
		// 河北
		zipCode = Validator.isZipCode("066502");
		Assert.assertTrue(zipCode);
		// 北京
		zipCode = Validator.isZipCode("102629");
		Assert.assertTrue(zipCode);
	}

	@Test
	public void isBetweenTest() {
		Assert.assertTrue(Validator.isBetween(0, 0, 1));
		Assert.assertTrue(Validator.isBetween(1L, 0L, 1L));
		Assert.assertTrue(Validator.isBetween(0.19f, 0.1f, 0.2f));
		Assert.assertTrue(Validator.isBetween(0.19, 0.1, 0.2));
	}
}
