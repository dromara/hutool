package org.dromara.hutool.codec.hash;

import org.dromara.hutool.util.ByteUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CityHashTest {

	@Test
	public void hash32Test() {
		final CityHash cityHash = CityHash.INSTANCE;
		int hv = cityHash.hash32(ByteUtil.toUtf8Bytes("你"));
		Assertions.assertEquals(1290029860, hv);

		hv = cityHash.hash32(ByteUtil.toUtf8Bytes("你好"));
		Assertions.assertEquals(1374181357, hv);

		hv = cityHash.hash32(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assertions.assertEquals(1475516842, hv);
		hv = cityHash.hash32(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assertions.assertEquals(0x51020cae, hv);
	}

	@Test
	public void hash64Test() {
		final CityHash cityHash = CityHash.INSTANCE;
		long hv = cityHash.hash64(ByteUtil.toUtf8Bytes("你"));
		Assertions.assertEquals(-4296898700418225525L, hv);

		hv = cityHash.hash64(ByteUtil.toUtf8Bytes("你好"));
		Assertions.assertEquals(-4294276205456761303L, hv);

		hv = cityHash.hash64(ByteUtil.toUtf8Bytes("见到你很高兴"));
		Assertions.assertEquals(272351505337503793L, hv);
		hv = cityHash.hash64(ByteUtil.toUtf8Bytes("我们将通过生成一个大的文件的方式来检验各种方法的执行效率因为这种方式在结束的时候需要执行文件"));
		Assertions.assertEquals(-8234735310919228703L, hv);
	}
}
