package cn.hutool.core.codec.hash;

import cn.hutool.core.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

public class MurmurHashTest {

	@Test
	public void hash32Test() {
		int hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("你"));
		Assert.assertEquals(-1898877446, hv);

		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("你好"));
		Assert.assertEquals(337357348, hv);

		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assert.assertEquals(1101306141, hv);
		hv = MurmurHash.INSTANCE.hash32(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assert.assertEquals(-785444229, hv);
	}

	@Test
	public void hash64Test() {
		long hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("你"));
		Assert.assertEquals(-1349759534971957051L, hv);

		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("你好"));
		Assert.assertEquals(-7563732748897304996L, hv);

		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assert.assertEquals(-766658210119995316L, hv);
		hv = MurmurHash.INSTANCE.hash64(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assert.assertEquals(-7469283059271653317L, hv);
	}
}
