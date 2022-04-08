package cn.hutool.core.lang.hash;

import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class MurMurHashTest {

	@Test
	public void hash32Test() {
		int hv = MurmurHash.hash32(StrUtil.utf8Bytes("你"));
		Assert.assertEquals(222142701, hv);

		hv = MurmurHash.hash32(StrUtil.utf8Bytes("你好"));
		Assert.assertEquals(1188098267, hv);

		hv = MurmurHash.hash32(StrUtil.utf8Bytes("见到你很高兴"));
		Assert.assertEquals(-1898490321, hv);
		hv = MurmurHash.hash32(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assert.assertEquals(-1713131054, hv);
	}

	@Test
	public void hash64Test() {
		long hv = MurmurHash.hash64(StrUtil.utf8Bytes("你"));
		Assert.assertEquals(-1349759534971957051L, hv);

		hv = MurmurHash.hash64(StrUtil.utf8Bytes("你好"));
		Assert.assertEquals(-7563732748897304996L, hv);

		hv = MurmurHash.hash64(StrUtil.utf8Bytes("见到你很高兴"));
		Assert.assertEquals(-766658210119995316L, hv);
		hv = MurmurHash.hash64(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assert.assertEquals(-7469283059271653317L, hv);
	}
}
