package cn.hutool.crypto.test;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

/**
 * SM3单元测试
 * 
 * @author looly
 *
 */
public class Sm3Test {

	@Test
	public void sm3Test() {
		Digester digester = DigestUtil.digester("sm3");
		String digestHex = digester.digestHex("aaaaa");
		Assert.assertEquals("136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be", digestHex);
	}
}
