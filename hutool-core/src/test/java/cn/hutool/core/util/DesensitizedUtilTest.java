package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 脱敏工具类 DesensitizedUtils 安全测试
 *
 * @author dazer and nuesoft
 * @see DesensitizedUtil
 */
public class DesensitizedUtilTest {

	@Test
	public void desensitizedTest() {
		Assertions.assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
		Assertions.assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
		Assertions.assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
		Assertions.assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
		Assertions.assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
		Assertions.assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
		Assertions.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
		Assertions.assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));

		Assertions.assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
		Assertions.assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
		Assertions.assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
		Assertions.assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
		Assertions.assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
		Assertions.assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
		Assertions.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
		Assertions.assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));
		Assertions.assertEquals("1101 **** **** **** 3256", DesensitizedUtil.desensitized("11011111222233333256", DesensitizedUtil.DesensitizedType.BANK_CARD));
		Assertions.assertEquals("6227 **** **** *** 5123", DesensitizedUtil.desensitized("6227880100100105123", DesensitizedUtil.DesensitizedType.BANK_CARD));
	}

	@Test
	public void userIdTest() {
		Assertions.assertEquals(Long.valueOf(0L), DesensitizedUtil.userId());
	}

	@Test
	public void chineseNameTest() {
		Assertions.assertEquals("段**", DesensitizedUtil.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		Assertions.assertEquals("5***************1X", DesensitizedUtil.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		Assertions.assertEquals("0915*****79", DesensitizedUtil.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		Assertions.assertEquals("180****1999", DesensitizedUtil.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		Assertions.assertEquals("北京市海淀区马连洼街*****", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 5));
		Assertions.assertEquals("***************", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 50));
		Assertions.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 0));
		Assertions.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		Assertions.assertEquals("d********@126.com", DesensitizedUtil.email("duandazhi@126.com"));
		Assertions.assertEquals("d********@gmail.com.cn", DesensitizedUtil.email("duandazhi@gmail.com.cn"));
		Assertions.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		Assertions.assertEquals("**********", DesensitizedUtil.password("1234567890"));
	}

	@Test
	public void carLicenseTest() {
		Assertions.assertEquals("", DesensitizedUtil.carLicense(null));
		Assertions.assertEquals("", DesensitizedUtil.carLicense(""));
		Assertions.assertEquals("苏D4***0", DesensitizedUtil.carLicense("苏D40000"));
		Assertions.assertEquals("陕A1****D", DesensitizedUtil.carLicense("陕A12345D"));
		Assertions.assertEquals("京A123", DesensitizedUtil.carLicense("京A123"));
	}
}
