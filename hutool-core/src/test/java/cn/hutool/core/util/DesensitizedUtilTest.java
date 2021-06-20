package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 脱敏工具类 DesensitizedUtils 安全测试
 *
 * @author dazer and nuesoft
 * @see DesensitizedUtil
 */
public class DesensitizedUtilTest {

	@Test
	public void desensitizedTest() {
		Assert.assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
		Assert.assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
		Assert.assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
		Assert.assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
		Assert.assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
		Assert.assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
		Assert.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
		Assert.assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));

		Assert.assertEquals("0", DesensitizedUtil.desensitized("100", DesensitizedUtil.DesensitizedType.USER_ID));
		Assert.assertEquals("段**", DesensitizedUtil.desensitized("段正淳", DesensitizedUtil.DesensitizedType.CHINESE_NAME));
		Assert.assertEquals("5***************1X", DesensitizedUtil.desensitized("51343620000320711X", DesensitizedUtil.DesensitizedType.ID_CARD));
		Assert.assertEquals("0915*****79", DesensitizedUtil.desensitized("09157518479", DesensitizedUtil.DesensitizedType.FIXED_PHONE));
		Assert.assertEquals("180****1999", DesensitizedUtil.desensitized("18049531999", DesensitizedUtil.DesensitizedType.MOBILE_PHONE));
		Assert.assertEquals("北京市海淀区马********", DesensitizedUtil.desensitized("北京市海淀区马连洼街道289号", DesensitizedUtil.DesensitizedType.ADDRESS));
		Assert.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.desensitized("duandazhi-jack@gmail.com.cn", DesensitizedUtil.DesensitizedType.EMAIL));
		Assert.assertEquals("**********", DesensitizedUtil.desensitized("1234567890", DesensitizedUtil.DesensitizedType.PASSWORD));
		Assert.assertEquals("1101 **** **** **** 3256", DesensitizedUtil.desensitized("11011111222233333256", DesensitizedUtil.DesensitizedType.BANK_CARD));
		Assert.assertEquals("6227 **** **** *** 5123", DesensitizedUtil.desensitized("6227880100100105123", DesensitizedUtil.DesensitizedType.BANK_CARD));
	}

	@Test
	public void userIdTest() {
		Assert.assertEquals(Long.valueOf(0L), DesensitizedUtil.userId());
	}

	@Test
	public void chineseNameTest() {
		Assert.assertEquals("段**", DesensitizedUtil.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		Assert.assertEquals("5***************1X", DesensitizedUtil.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		Assert.assertEquals("0915*****79", DesensitizedUtil.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		Assert.assertEquals("180****1999", DesensitizedUtil.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		Assert.assertEquals("北京市海淀区马连洼街*****", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 5));
		Assert.assertEquals("***************", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 50));
		Assert.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", 0));
		Assert.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtil.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		Assert.assertEquals("d********@126.com", DesensitizedUtil.email("duandazhi@126.com"));
		Assert.assertEquals("d********@gmail.com.cn", DesensitizedUtil.email("duandazhi@gmail.com.cn"));
		Assert.assertEquals("d*************@gmail.com.cn", DesensitizedUtil.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		Assert.assertEquals("**********", DesensitizedUtil.password("1234567890"));
	}

	@Test
	public void carLicenseTest() {
		Assert.assertEquals("", DesensitizedUtil.carLicense(null));
		Assert.assertEquals("", DesensitizedUtil.carLicense(""));
		Assert.assertEquals("苏D4***0", DesensitizedUtil.carLicense("苏D40000"));
		Assert.assertEquals("陕A1****D", DesensitizedUtil.carLicense("陕A12345D"));
		Assert.assertEquals("京A123", DesensitizedUtil.carLicense("京A123"));
	}
}
