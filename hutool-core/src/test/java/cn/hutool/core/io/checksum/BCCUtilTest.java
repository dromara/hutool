package cn.hutool.core.io.checksum;


import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sunday123
 */
public class BCCUtilTest {
	@Test
	public void hexBCCHexStrTest() {
		Assert.assertEquals(BCCUtil.hexBCCHexStr("A"), "a");
		Assert.assertEquals(BCCUtil.hexBCCHexStr("01A0"), "a1");
		Assert.assertEquals(BCCUtil.hexBCCHexStr("AA"), "aa");
		Assert.assertEquals(BCCUtil.hexBCCHexStr("01A07CFF02"), "20");
	}

	@Test
	public void asciiBCCHexStrTest() {
		Assert.assertEquals(BCCUtil.asciiBCCHexStr(" "), "20");
		Assert.assertEquals(BCCUtil.asciiBCCHexStr("  "), "0");
		Assert.assertEquals(BCCUtil.asciiBCCHexStr("A"), "41");
		Assert.assertEquals(BCCUtil.asciiBCCHexStr("AA"), "0");
		Assert.assertEquals(BCCUtil.asciiBCCHexStr("1234"), "4");
	}
}
