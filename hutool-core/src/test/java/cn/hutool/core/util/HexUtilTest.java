package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;

/**
 * HexUtil单元测试
 * @author Looly
 *
 */
public class HexUtilTest {
	
	@Test
	public void hexStrTest(){
		String str = "我是一个字符串";
		
		String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
		String decodedStr = HexUtil.decodeHexStr(hex);
		
		Assert.assertEquals(str, decodedStr);
	}
	
	@Test
	public void toUnicodeHexTest() {
		String unicodeHex = HexUtil.toUnicodeHex('\u2001');
		Assert.assertEquals("\\u2001", unicodeHex);
		
		unicodeHex = HexUtil.toUnicodeHex('你');
		Assert.assertEquals("\\u4f60", unicodeHex);
	}
}
