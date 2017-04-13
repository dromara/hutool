package com.xiaoleilu.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;

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
		Console.log(hex);
		
		String decodedStr = HexUtil.decodeHexStr(hex);
		
		Assert.assertEquals(str, decodedStr);
	}
}
