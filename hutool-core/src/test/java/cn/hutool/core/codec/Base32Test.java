package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class Base32Test {
	
	@Test
	public void encodeAndDecodeTest(){
		String a = "伦家是一个非常长的字符串";
		String encode = Base32.encode(a);
		Assert.assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI", encode);
		
		String decodeStr = Base32.decodeStr(encode);
		Assert.assertEquals(a, decodeStr);
	}
}
