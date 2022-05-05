package cn.hutool.crypto;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.symmetric.SM4;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * SM单元测试
 *
 * @author looly
 *
 */
public class SmTest {

	@Test
	public void sm3Test() {
		final String digestHex = SmUtil.sm3("aaaaa");
		Assert.assertEquals("136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be", digestHex);
	}

	@Test
	public void sm4Test() {
		final String content = "test中文";
		final SM4 sm4 = SmUtil.sm4();

		final String encryptHex = sm4.encryptHex(content);
		final String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.UTF_8);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void sm4Test2() {
		final String content = "test中文";
		final SM4 sm4 = new SM4(Mode.CTR, Padding.PKCS5Padding);
		sm4.setIv("aaaabbbb".getBytes());

		final String encryptHex = sm4.encryptHex(content);
		final String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.UTF_8);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void sm4ECBPKCS5PaddingTest2() {
		final String content = "test中文";
		final SM4 sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding);
		Assert.assertEquals("SM4/ECB/PKCS5Padding", sm4.getCipher().getAlgorithm());

		final String encryptHex = sm4.encryptHex(content);
		final String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.UTF_8);
		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void sm4TestWithCustomKeyTest() {
		final String content = "test中文";

		final SecretKey key = KeyUtil.generateKey(SM4.ALGORITHM_NAME);

		final SM4 sm4 = new SM4(Mode.ECB, Padding.PKCS5Padding, key);
		Assert.assertEquals("SM4/ECB/PKCS5Padding", sm4.getCipher().getAlgorithm());

		final String encryptHex = sm4.encryptHex(content);
		final String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.UTF_8);
		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void sm4TestWithCustomKeyTest2() {
		final String content = "test中文frfewrewrwerwer---------------------------------------------------";

		final byte[] key = KeyUtil.generateKey(SM4.ALGORITHM_NAME, 128).getEncoded();

		final SM4 sm4 = SmUtil.sm4(key);
		Assert.assertEquals("SM4", sm4.getCipher().getAlgorithm());

		final String encryptHex = sm4.encryptHex(content);
		final String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.UTF_8);
		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void hmacSm3Test() {
		final String content = "test中文";
		final HMac hMac = SmUtil.hmacSm3("password".getBytes());
		final String digest = hMac.digestHex(content);
		Assert.assertEquals("493e3f9a1896b43075fbe54658076727960d69632ac6b6ed932195857a6840c6", digest);
	}


}
