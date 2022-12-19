package cn.hutool.core.io.checksum;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sunday123
 */
public class LRCUtilTest {
	@Test
	public void asciiLRCHexStrTest() {
		Assert.assertEquals(LRCUtil.asciiLRCHexStr(" "), "e0");
		Assert.assertEquals(LRCUtil.asciiLRCHexStr("1"), "cf");
		Assert.assertEquals(LRCUtil.asciiLRCHexStr("~"), "82");
		Assert.assertEquals(LRCUtil.asciiLRCHexStr("@@@@"), "100");
	}

	@Test
	public void hexLRCHexStrTest() {
		Assert.assertEquals(LRCUtil.hexLRCHexStr("0"), "100");
		Assert.assertEquals(LRCUtil.hexLRCHexStr("80"), "80");
		Assert.assertEquals(LRCUtil.hexLRCHexStr("8000"), "80");
		Assert.assertEquals(LRCUtil.hexLRCHexStr("1122FFEE"), "e0");
	}
}
