package cn.hutool.crypto.test;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * SM单元测试
 * 
 * @author looly
 *
 */
public class SmTest {
	
	@Test
	public void sm3Test() {
		String digestHex = SmUtil.sm3("aaaaa");
		Assert.assertEquals("136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be", digestHex);
	}
	
	@Test
	public void sm4Test() {
		String content = "test中文";
		SymmetricCrypto sm4 = SmUtil.sm4();
		
		String encryptHex = sm4.encryptHex(content);
		String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(content, decryptStr);
	}
	@Test
	public void sm4Test2() {
		String content = "test中文";
		SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding");
		
		String encryptHex = sm4.encryptHex(content);
		String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(content, decryptStr);
	}
	
	@Test
	public void hmacSm3Test() {
		String content = "test中文";
		HMac hMac = SmUtil.hmacSm3("password".getBytes());
		String digest = hMac.digestHex(content);
		Assert.assertEquals("493e3f9a1896b43075fbe54658076727960d69632ac6b6ed932195857a6840c6", digest);
	}
}
