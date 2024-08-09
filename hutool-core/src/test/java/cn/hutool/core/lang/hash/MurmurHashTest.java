package cn.hutool.core.lang.hash;

import cn.hutool.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MurmurHashTest {

	@Test
	public void hash32Test() {
		int hv = MurmurHash.hash32(StrUtil.utf8Bytes("你"));
		assertEquals(-1898877446, hv);

		hv = MurmurHash.hash32(StrUtil.utf8Bytes("你好"));
		assertEquals(337357348, hv);

		hv = MurmurHash.hash32(StrUtil.utf8Bytes("见到你很高兴"));
		assertEquals(1101306141, hv);
		hv = MurmurHash.hash32(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		assertEquals(-785444229, hv);
	}

	@Test
	public void hash64Test() {
		long hv = MurmurHash.hash64(StrUtil.utf8Bytes("你"));
		assertEquals(-1349759534971957051L, hv);

		hv = MurmurHash.hash64(StrUtil.utf8Bytes("你好"));
		assertEquals(-7563732748897304996L, hv);

		hv = MurmurHash.hash64(StrUtil.utf8Bytes("见到你很高兴"));
		assertEquals(-766658210119995316L, hv);
		hv = MurmurHash.hash64(StrUtil.utf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		assertEquals(-7469283059271653317L, hv);
	}
}
