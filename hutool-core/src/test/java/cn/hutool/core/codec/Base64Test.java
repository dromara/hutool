package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Base64单元测试
 * 
 * @author looly
 *
 */
public class Base64Test {
	
	@Test
	public void decodeTest() {
		String a = "wO7LxKO6DQogICAgxPq6w6Os1q7HsLXE0vjQ0L+o1N3No8q508PBy6Osx+vNqLn91qe4trGm16rV\r\n" + 
				"y6Osx7DBvczsuPjE47XEMTI208rP5KOs0LvQu6GjDQoNCg0KICAgICAgICAgICAgICAgICAgICAg\r\n" + 
				"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg\r\n" + 
				"ICAgICAgICDVxcj9DQo=\r\n" + 
				"";
		byte[] decode = Base64.decode(a);
		Console.log(StrUtil.str(decode, CharsetUtil.GBK));
	}

	@Test
	public void encodeAndDecodeTest() {
		String a = "伦家是一个非常长的字符串66";
		String encode = Base64.encode(a);
		Assert.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY=", encode);
		
		String decodeStr = Base64.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}

	@Test
	public void urlSafeEncodeAndDecodeTest() {
		String a = "广州伦家需要安全感55";
		String encode = StrUtil.utf8Str(Base64.encodeUrlSafe(StrUtil.utf8Bytes(a), false));
		Assert.assertEquals("5bm_5bee5Lym5a626ZyA6KaB5a6J5YWo5oSfNTU", encode);

		String decodeStr = Base64.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}
}
