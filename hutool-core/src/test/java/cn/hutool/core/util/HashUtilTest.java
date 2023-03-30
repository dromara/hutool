package cn.hutool.core.util;

import cn.hutool.core.codec.hash.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashUtilTest {

	@Test
	public void cityHash128Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long[] hash = HashUtil.cityHash128(ByteUtil.toUtf8Bytes(s));
		Assertions.assertEquals(0x5944f1e788a18db0L, hash[0]);
		Assertions.assertEquals(0xc2f68d8b2bf4a5cfL, hash[1]);
	}

	@Test
	public void cityHash64Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final long hash = HashUtil.cityHash64(ByteUtil.toUtf8Bytes(s));
		Assertions.assertEquals(0x1d408f2bbf967e2aL, hash);
	}

	@Test
	public void cityHash32Test(){
		final String s="Google发布的Hash计算算法：CityHash64 与 CityHash128";
		final int hash = HashUtil.cityHash32(ByteUtil.toUtf8Bytes(s));
		Assertions.assertEquals(0xa8944fbe, hash);
	}
}
