package com.xiaoleilu.hutool.crypto.test;
import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.crypto.digest.DigestAlgorithm;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import com.xiaoleilu.hutool.crypto.digest.Digester;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * 摘要算法单元测试
 * @author Looly
 *
 */
public class DigestTest {
	
	@Test
	public void digesterTest(){
		String testStr = "test中文";
		
		Digester md5 = new Digester(DigestAlgorithm.MD5);
		String digestHex = md5.digestHex(testStr);
		Assert.assertEquals("5393554e94bf0eb6436f240a4fd71282", digestHex);
	}
	
	@Test
	public void md5Test(){
		String testStr = "test中文";
		
		String md5Hex1 = DigestUtil.md5Hex(testStr);
		Assert.assertEquals("5393554e94bf0eb6436f240a4fd71282", md5Hex1);
		
		String md5Hex2 = DigestUtil.md5Hex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("5393554e94bf0eb6436f240a4fd71282", md5Hex2);
	}
	
	@Test
	public void sha1Test(){
		String testStr = "test中文";
		
		String sha1Hex1 = DigestUtil.sha1Hex(testStr);
		Assert.assertEquals("ecabf586cef0d3b11c56549433ad50b81110a836", sha1Hex1);
		
		String sha1Hex2 = DigestUtil.sha1Hex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("ecabf586cef0d3b11c56549433ad50b81110a836", sha1Hex2);
	}
}
