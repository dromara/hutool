package cn.hutool.core.util;

import cn.hutool.core.text.MaskingUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * 脱敏工具类 MaskingUtil 安全测试
 *
 * @author dazer and nuesoft
 * @see MaskingUtil
 */
public class MaskingUtilTest {

	@Test
	public void desensitizedTest() {
		Assert.assertEquals("0", MaskingUtil.masking("100", MaskingUtil.MaskingType.USER_ID));
		Assert.assertEquals("段**", MaskingUtil.masking("段正淳", MaskingUtil.MaskingType.CHINESE_NAME));
		Assert.assertEquals("5***************1X", MaskingUtil.masking("51343620000320711X", MaskingUtil.MaskingType.ID_CARD));
		Assert.assertEquals("0915*****79", MaskingUtil.masking("09157518479", MaskingUtil.MaskingType.FIXED_PHONE));
		Assert.assertEquals("180****1999", MaskingUtil.masking("18049531999", MaskingUtil.MaskingType.MOBILE_PHONE));
		Assert.assertEquals("北京市海淀区马********", MaskingUtil.masking("北京市海淀区马连洼街道289号", MaskingUtil.MaskingType.ADDRESS));
		Assert.assertEquals("d*************@gmail.com.cn", MaskingUtil.masking("duandazhi-jack@gmail.com.cn", MaskingUtil.MaskingType.EMAIL));
		Assert.assertEquals("**********", MaskingUtil.masking("1234567890", MaskingUtil.MaskingType.PASSWORD));

		Assert.assertEquals("0", MaskingUtil.masking("100", MaskingUtil.MaskingType.USER_ID));
		Assert.assertEquals("段**", MaskingUtil.masking("段正淳", MaskingUtil.MaskingType.CHINESE_NAME));
		Assert.assertEquals("5***************1X", MaskingUtil.masking("51343620000320711X", MaskingUtil.MaskingType.ID_CARD));
		Assert.assertEquals("0915*****79", MaskingUtil.masking("09157518479", MaskingUtil.MaskingType.FIXED_PHONE));
		Assert.assertEquals("180****1999", MaskingUtil.masking("18049531999", MaskingUtil.MaskingType.MOBILE_PHONE));
		Assert.assertEquals("北京市海淀区马********", MaskingUtil.masking("北京市海淀区马连洼街道289号", MaskingUtil.MaskingType.ADDRESS));
		Assert.assertEquals("d*************@gmail.com.cn", MaskingUtil.masking("duandazhi-jack@gmail.com.cn", MaskingUtil.MaskingType.EMAIL));
		Assert.assertEquals("**********", MaskingUtil.masking("1234567890", MaskingUtil.MaskingType.PASSWORD));
		Assert.assertEquals("1101 **** **** **** 3256", MaskingUtil.masking("11011111222233333256", MaskingUtil.MaskingType.BANK_CARD));
		Assert.assertEquals("6227 **** **** *** 5123", MaskingUtil.masking("6227880100100105123", MaskingUtil.MaskingType.BANK_CARD));
		Assert.assertEquals("192.*.*.*", MaskingUtil.masking("192.168.1.1", MaskingUtil.MaskingType.IPV4));
		Assert.assertEquals("2001:*:*:*:*:*:*:*", MaskingUtil.masking("2001:0db8:86a3:08d3:1319:8a2e:0370:7344", MaskingUtil.MaskingType.IPV6));
	}

	@Test
	public void userIdTest() {
		Assert.assertEquals(Long.valueOf(0L), MaskingUtil.userId());
	}

	@Test
	public void chineseNameTest() {
		Assert.assertEquals("段**", MaskingUtil.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		Assert.assertEquals("5***************1X", MaskingUtil.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		Assert.assertEquals("0915*****79", MaskingUtil.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		Assert.assertEquals("180****1999", MaskingUtil.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		Assert.assertEquals("北京市海淀区马连洼街*****", MaskingUtil.address("北京市海淀区马连洼街道289号", 5));
		Assert.assertEquals("***************", MaskingUtil.address("北京市海淀区马连洼街道289号", 50));
		Assert.assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", 0));
		Assert.assertEquals("北京市海淀区马连洼街道289号", MaskingUtil.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		Assert.assertEquals("d********@126.com", MaskingUtil.email("duandazhi@126.com"));
		Assert.assertEquals("d********@gmail.com.cn", MaskingUtil.email("duandazhi@gmail.com.cn"));
		Assert.assertEquals("d*************@gmail.com.cn", MaskingUtil.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		Assert.assertEquals("**********", MaskingUtil.password("1234567890"));
	}

	@Test
	public void carLicenseTest() {
		Assert.assertEquals("", MaskingUtil.carLicense(null));
		Assert.assertEquals("", MaskingUtil.carLicense(""));
		Assert.assertEquals("苏D4***0", MaskingUtil.carLicense("苏D40000"));
		Assert.assertEquals("陕A1****D", MaskingUtil.carLicense("陕A12345D"));
		Assert.assertEquals("京A123", MaskingUtil.carLicense("京A123"));
	}
}
