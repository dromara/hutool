package com.xiaoleilu.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.StrUtil;

public class Base64Test {
	
	@Test
	public void encodeAndDecodeTest(){
		String a = "伦家是一个非常长的字符串66";
		String encode = Base64.encode(a);
		Assert.assertEquals("5Lym5a625piv5LiA5Liq6Z2e5bi46ZW/55qE5a2X56ym5LiyNjY=", encode);
		
		String decodeStr = Base64.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}
	
	@Test
	public void urlSafeEncodeAndDecodeTest(){
		String a = "伦家需要安全感55";
		String encode = StrUtil.utf8Str(Base64.encode(StrUtil.utf8Bytes(a), false, true));
		Assert.assertEquals("5Lym5a626ZyA6KaB5a6J5YWo5oSfNTU", encode);
		
		String decodeStr = Base64.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}
}
