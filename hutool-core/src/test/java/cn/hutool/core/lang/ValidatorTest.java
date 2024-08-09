package cn.hutool.core.lang;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验证器单元测试
 *
 * @author Looly
 */
public class ValidatorTest {

	@Test
	public void isNumberTest() {
		assertTrue(Validator.isNumber("45345365465"));
		assertTrue(Validator.isNumber("0004545435"));
		assertTrue(Validator.isNumber("5.222"));
		assertTrue(Validator.isNumber("0.33323"));
	}

	@Test
	public void hasNumberTest() {
		final String var1 = "";
		final String var2 = "str";
		final String var3 = "180";
		final String var4 = "身高180体重180";
		assertFalse(Validator.hasNumber(var1));
		assertFalse(Validator.hasNumber(var2));
		assertTrue(Validator.hasNumber(var3));
		assertTrue(Validator.hasNumber(var4));
	}

	@Test
	public void isLetterTest() {
		assertTrue(Validator.isLetter("asfdsdsfds"));
		assertTrue(Validator.isLetter("asfdsdfdsfVCDFDFGdsfds"));
		assertTrue(Validator.isLetter("asfdsdf你好dsfVCDFDFGdsfds"));
	}

	@Test
	public void isUperCaseTest() {
		assertTrue(Validator.isUpperCase("VCDFDFG"));
		assertTrue(Validator.isUpperCase("ASSFD"));

		assertFalse(Validator.isUpperCase("asfdsdsfds"));
		assertFalse(Validator.isUpperCase("ASSFD你好"));
	}

	@Test
	public void isLowerCaseTest() {
		assertTrue(Validator.isLowerCase("asfdsdsfds"));

		assertFalse(Validator.isLowerCase("aaaa你好"));
		assertFalse(Validator.isLowerCase("VCDFDFG"));
		assertFalse(Validator.isLowerCase("ASSFD"));
		assertFalse(Validator.isLowerCase("ASSFD你好"));
	}

	@Test
	public void isBirthdayTest() {
		final boolean b = Validator.isBirthday("20150101");
		assertTrue(b);
		final boolean b2 = Validator.isBirthday("2015-01-01");
		assertTrue(b2);
		final boolean b3 = Validator.isBirthday("2015.01.01");
		assertTrue(b3);
		final boolean b4 = Validator.isBirthday("2015年01月01日");
		assertTrue(b4);
		final boolean b5 = Validator.isBirthday("2015.01.01");
		assertTrue(b5);
		final boolean b6 = Validator.isBirthday("2018-08-15");
		assertTrue(b6);

		//验证年非法
		assertFalse(Validator.isBirthday("2095.05.01"));
		//验证月非法
		assertFalse(Validator.isBirthday("2015.13.01"));
		//验证日非法
		assertFalse(Validator.isBirthday("2015.02.29"));
	}

	@Test
	public void isCitizenIdTest() {
		// 18为身份证号码验证
		final boolean b = Validator.isCitizenId("110101199003074477");
		assertTrue(b);

		// 15位身份证号码验证
		final boolean b1 = Validator.isCitizenId("410001910101123");
		assertTrue(b1);

		// 10位身份证号码验证
		final boolean b2 = Validator.isCitizenId("U193683453");
		assertTrue(b2);
	}

	@Test
	public void validateTest() throws ValidateException {
		assertThrows(ValidateException.class, () -> {
			Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
		});
	}

	@Test
	public void isEmailTest() {
		final boolean email = Validator.isEmail("abc_cde@163.com");
		assertTrue(email);
		final boolean email1 = Validator.isEmail("abc_%cde@163.com");
		assertTrue(email1);
		final boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		assertTrue(email2);
		final boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		assertTrue(email3);
		final boolean email4 = Validator.isEmail("xiaolei.Lu@aaa.b");
		assertTrue(email4);
		final boolean email5 = Validator.isEmail("luxiaolei_小磊@小磊.com", true);
		assertTrue(email5);
	}

	@Test
	public void isMobileTest() {
		final boolean m1 = Validator.isMobile("13900221432");
		assertTrue(m1);
		final boolean m2 = Validator.isMobile("015100221432");
		assertTrue(m2);
		final boolean m3 = Validator.isMobile("+8618600221432");
		assertTrue(m3);
		final boolean m4 = Validator.isMobile("19312341234");
		assertTrue(m4);
	}

	@Test
	public void isMatchTest() {
		String url = "http://aaa-bbb.somthing.com/a.php?a=b&c=2";
		assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com/a.php?a=b&c=2";
		assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com:8080/a.php?a=b&c=2";
		assertTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));
	}

	@Test
	public void isGeneralTest() {
		String str = "";
		boolean general = Validator.isGeneral(str, -1, 5);
		assertTrue(general);

		str = "123_abc_ccc";
		general = Validator.isGeneral(str, -1, 100);
		assertTrue(general);

		// 不允许中文
		str = "123_abc_ccc中文";
		general = Validator.isGeneral(str, -1, 100);
		assertFalse(general);
	}

	@Test
	public void isPlateNumberTest() {
		assertTrue(Validator.isPlateNumber("粤BA03205"));
		assertTrue(Validator.isPlateNumber("闽20401领"));
	}

	@Test
	public void isChineseTest() {
		assertTrue(Validator.isChinese("全都是中文"));
		assertTrue(Validator.isChinese("㐓㐘"));
		assertFalse(Validator.isChinese("not全都是中文"));
	}

	@Test
	public void hasChineseTest() {
		assertTrue(Validator.hasChinese("黄单桑米"));
		assertTrue(Validator.hasChinese("Kn 四兄弟"));
		assertTrue(Validator.hasChinese("\uD840\uDDA3"));
		assertFalse(Validator.hasChinese("Abc"));
	}

	@Test
	public void isUUIDTest() {
		assertTrue(Validator.isUUID(IdUtil.randomUUID()));
		assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID()));

		assertTrue(Validator.isUUID(IdUtil.randomUUID().toUpperCase()));
		assertTrue(Validator.isUUID(IdUtil.fastSimpleUUID().toUpperCase()));
	}

	@Test
	public void isZipCodeTest() {
		// 港
		boolean zipCode = Validator.isZipCode("999077");
		assertTrue(zipCode);
		// 澳
		zipCode = Validator.isZipCode("999078");
		assertTrue(zipCode);
		// 台（2020年3月起改用6位邮编，3+3）
		zipCode = Validator.isZipCode("822001");
		assertTrue(zipCode);

		// 内蒙
		zipCode = Validator.isZipCode("016063");
		assertTrue(zipCode);
		// 山西
		zipCode = Validator.isZipCode("045246");
		assertTrue(zipCode);
		// 河北
		zipCode = Validator.isZipCode("066502");
		assertTrue(zipCode);
		// 北京
		zipCode = Validator.isZipCode("102629");
		assertTrue(zipCode);
	}

	@Test
	public void isBetweenTest() {
		assertTrue(Validator.isBetween(0, 0, 1));
		assertTrue(Validator.isBetween(1L, 0L, 1L));
		assertTrue(Validator.isBetween(0.19f, 0.1f, 0.2f));
		assertTrue(Validator.isBetween(0.19, 0.1, 0.2));
	}

	@Test
	public void isCarVinTest() {
		assertTrue(Validator.isCarVin("LSJA24U62JG269225"));
		assertTrue(Validator.isCarVin("LDC613P23A1305189"));
		assertFalse(Validator.isCarVin("LOC613P23A1305189"));

		assertTrue(Validator.isCarVin("LSJA24U62JG269225"));    //标准分类1
		assertTrue(Validator.isCarVin("LDC613P23A1305189"));    //标准分类1
		assertTrue(Validator.isCarVin("LBV5S3102ESJ25655"));    //标准分类1
		assertTrue(Validator.isCarVin("LBV5S3102ESJPE655"));    //标准分类2
		assertFalse(Validator.isCarVin("LOC613P23A1305189"));    //错误示例
	}

	@Test
	public void isCarDrivingLicenceTest() {
		assertTrue(Validator.isCarDrivingLicence("430101758218"));
	}

	@Test
	public void validateIpv4Test() {
		Validator.validateIpv4("192.168.1.1", "Error ip");
		Validator.validateIpv4("8.8.8.8", "Error ip");
		Validator.validateIpv4("0.0.0.0", "Error ip");
		Validator.validateIpv4("255.255.255.255", "Error ip");
		Validator.validateIpv4("127.0.0.0", "Error ip");
	}

	@Test
	public void isUrlTest() {
		final String content = "https://detail.tmall.com/item.htm?" +
			"id=639428931841&ali_refid=a3_430582_1006:1152464078:N:Sk5vwkMVsn5O6DcnvicELrFucL21A32m:0af8611e23c1d07697e";

		assertTrue(Validator.isMatchRegex(Validator.URL, content));
		assertTrue(Validator.isMatchRegex(Validator.URL_HTTP, content));
	}

	@Test
	public void isChineseNameTest() {
		assertTrue(Validator.isChineseName("阿卜杜尼亚孜·毛力尼亚孜"));
		assertFalse(Validator.isChineseName("阿卜杜尼亚孜./毛力尼亚孜"));
		assertTrue(Validator.isChineseName("段正淳"));
		assertFalse(Validator.isChineseName("孟  伟"));
		assertFalse(Validator.isChineseName("李"));
		assertFalse(Validator.isChineseName("连逍遥0"));
		assertFalse(Validator.isChineseName("SHE"));
	}
}
