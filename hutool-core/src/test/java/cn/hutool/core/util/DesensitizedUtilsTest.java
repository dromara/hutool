package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 脱敏工具类 DesensitizedUtils 安全测试
 * @author dazer and nuesoft
 * @date 2021/3/20 22:34
 * @see DesensitizedUtils
 */
public class DesensitizedUtilsTest {

	@Test
	public void userIdTest() {
		Assert.assertEquals(Long.valueOf(0L), DesensitizedUtils.userId());
	}

	@Test
	public void chineseNameTest() {
		Assert.assertEquals("段**", DesensitizedUtils.chineseName("段正淳"));
	}

	@Test
	public void idCardNumTest() {
		Assert.assertEquals("5***************1X", DesensitizedUtils.idCardNum("51343620000320711X", 1, 2));
	}

	@Test
	public void fixedPhoneTest() {
		Assert.assertEquals("0915*****79", DesensitizedUtils.fixedPhone("09157518479"));
	}

	@Test
	public void mobilePhoneTest() {
		Assert.assertEquals("180****1999", DesensitizedUtils.mobilePhone("18049531999"));
	}

	@Test
	public void addressTest() {
		Assert.assertEquals("北京市海淀区马连洼街*****", DesensitizedUtils.address("北京市海淀区马连洼街道289号", 5));
		Assert.assertEquals("***************", DesensitizedUtils.address("北京市海淀区马连洼街道289号", 50));
		Assert.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtils.address("北京市海淀区马连洼街道289号", 0));
		Assert.assertEquals("北京市海淀区马连洼街道289号", DesensitizedUtils.address("北京市海淀区马连洼街道289号", -1));
	}

	@Test
	public void emailTest() {
		Assert.assertEquals("d********@126.com", DesensitizedUtils.email("duandazhi@126.com"));
		Assert.assertEquals("d********@gmail.com.cn", DesensitizedUtils.email("duandazhi@gmail.com.cn"));
		Assert.assertEquals("d*************@gmail.com.cn", DesensitizedUtils.email("duandazhi-jack@gmail.com.cn"));
	}

	@Test
	public void passwordTest() {
		Assert.assertEquals("**********", DesensitizedUtils.password("1234567890"));
	}
}
