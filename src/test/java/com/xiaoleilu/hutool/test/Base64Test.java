package com.xiaoleilu.hutool.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Base64;

public class Base64Test {
	
	String text = "我是待编码的字符串5sdksdvksdskldgklfsd///////////";

	@Test
	public void encodeAndDecode(){
		String encode = Base64.encode(text);
		Assert.assertEquals(Base64.decodeStr(encode), text);
	}
}
