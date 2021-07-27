package cn.hutool.extra.wubi;

import org.junit.Assert;
import org.junit.Test;

/**
 * 五笔码工具类,中文转五笔码单元测试
 *
 * @author ruansheng
 */
public class WubiCodeUtilTest {

	@Test
	public void getFirstLetterTest1() {
		final String wubiCode = WubiCodeUtil.getFirstLetter("你好怡");
		Assert.assertEquals("WVN", wubiCode);
	}

	@Test
	public void getFirstLetterTest2() {
		final String wubiCode = WubiCodeUtil.getFirstLetter("H是第一个");
		Assert.assertEquals("HJTGW", wubiCode);
	}

	@Test
	public void getFirstLetterTest3() {
		final String wubiCode = WubiCodeUtil.getFirstLetter("崞阳");
		Assert.assertEquals("MB", wubiCode);
	}

	@Test
	public void getFirstLetterTest4() {
		final String wubiCode = WubiCodeUtil.getFirstLetter("@标点测试~");
		Assert.assertEquals("SHIY", wubiCode);
	}

	@Test
	public void getFirstLetterTest5() {
		final String wubiCode = WubiCodeUtil.getFirstLetter(null);
		Assert.assertEquals("", wubiCode);
	}

}
