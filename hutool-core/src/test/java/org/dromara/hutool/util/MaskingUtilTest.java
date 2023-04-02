package org.dromara.hutool.util;

import org.dromara.hutool.text.MaskingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 脱敏工具类 MaskingUtil 安全测试
 *
 * @author dazer and nuesoft
 * @see MaskingUtil
 */
public class MaskingUtilTest {

	@Test
	public void desensitizedTest() {
		Assertions.assertEquals("0", MaskingUtil.masking("100", MaskingUtil.MaskingType.USER_ID));
		Assertions.assertEquals("段**", MaskingUtil.masking("段正淳", MaskingUtil.MaskingType.CHINESE_NAME));
		Assertions.assertEquals("5***************1X", MaskingUtil.masking("51343620000320711X", MaskingUtil.MaskingType.ID_CARD));
		Assertions.assertEquals("0915*****79", MaskingUtil.masking("09157518479", MaskingUtil.MaskingType.FIXED_PHONE));
		Assertions.assertEquals("180****1999", MaskingUtil.masking("18049531999", MaskingUtil.MaskingType.MOBILE_PHONE));
		Assertions.assertEquals("北京市海淀区马********", MaskingUtil.masking("北京市海淀区马连洼街道289号", MaskingUtil.MaskingType.ADDRESS));
		Assertions.assertEquals("d*************@gmail.com.cn", MaskingUtil.masking("duandazhi-jack@gmail.com.cn", MaskingUtil.MaskingType.EMAIL));
		Assertions.assertEquals("**********", MaskingUtil.masking("1234567890", MaskingUtil.MaskingType.PASSWORD));

		Assertions.assertEquals("0", MaskingUtil.masking("100", MaskingUtil.MaskingType.USER_ID));
		Assertions.assertEquals("段**", MaskingUtil.masking("段正淳", MaskingUtil.MaskingType.CHINESE_NAME));
		Assertions.assertEquals("5***************1X", MaskingUtil.masking("51343620000320711X", MaskingUtil.MaskingType.ID_CARD));
		Assertions.assertEquals("0915*****79", MaskingUtil.masking("09157518479", MaskingUtil.MaskingType.FIXED_PHONE));
		Assertions.assertEquals("180****1999", MaskingUtil.masking("18049531999", MaskingUtil.MaskingType.MOBILE_PHONE));
		Assertions.assertEquals("北京市海淀区马********", MaskingUtil.masking("北京市海淀区马连洼街道289号", MaskingUtil.MaskingType.ADDRESS));
		Assertions.assertEquals("d*************@gmail.com.cn", MaskingUtil.masking("duandazhi-jack@gmail.com.cn", MaskingUtil.MaskingType.EMAIL));
		Assertions.assertEquals("**********", MaskingUtil.masking("1234567890", MaskingUtil.MaskingType.PASSWORD));
		Assertions.assertEquals("1101 **** **** **** 3256", MaskingUtil.masking("11011111222233333256", MaskingUtil.MaskingType.BANK_CARD));
		Assertions.assertEquals("6227 **** **** *** 5123", MaskingUtil.masking("6227880100100105123", MaskingUtil.MaskingType.BANK_CARD));
		Assertions.assertEquals("192.*.*.*", MaskingUtil.masking("192.168.1.1", MaskingUtil.MaskingType.IPV4));
		Assertions.assertEquals("2001:*:*:*:*:*:*:*", MaskingUtil.masking("2001:0db8:86a3:08d3:1319:8a2e:0370:7344", MaskingUtil.MaskingType.IPV6));
	}

	@Test
	public void userIdTest() {
		Assertions.assertEquals(Long.valueOf(0L), MaskingUtil.userId());
	}

	@Test
	public void chineseNameTest() {
		Assertions.assertEquals("段**", MaskingUtil.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		Assertions.assertEquals("5***************1X", MaskingUtil.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		Assertions.assertEquals("0915*****79", MaskingUtil.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		Assertions.assertEquals("180****1999", MaskingUtil.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		Assertions.assertEquals("北京市海淀区马连洼街*****", MaskingUtil.address("北京市海淀区马连洼街道289号", 5));
		Assertions.assertEquals("***************", MaskingUtil.address("北京市海淀区马连洼街道289号", 50));
		Assertions.assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", 0));
		Assertions.assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		Assertions.assertEquals("d********@126.com", MaskingUtil.email("duandazhi@126.com"));
		Assertions.assertEquals("d********@gmail.com.cn", MaskingUtil.email("duandazhi@gmail.com.cn"));
		Assertions.assertEquals("d*************@gmail.com.cn", MaskingUtil.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		Assertions.assertEquals("**********", MaskingUtil.password("1234567890"));
	}

	@Test
	public void carLicenseTest() {
		Assertions.assertEquals("", MaskingUtil.carLicense(null));
		Assertions.assertEquals("", MaskingUtil.carLicense(""));
		Assertions.assertEquals("苏D4***0", MaskingUtil.carLicense("苏D40000"));
		Assertions.assertEquals("陕A1****D", MaskingUtil.carLicense("陕A12345D"));
		Assertions.assertEquals("京A123", MaskingUtil.carLicense("京A123"));
	}
}
