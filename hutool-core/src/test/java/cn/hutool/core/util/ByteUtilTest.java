package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtilTest {
	@Test
	public void intAndBytesLittleEndianTest() {
		// 测试 int 转小端序 byte 数组
		final int int1 = RandomUtil.randomInt((Integer.MAX_VALUE));

		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(int1);
		final byte[] bytesIntFromBuffer = buffer.array();

		final byte[] bytesInt = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		Assert.assertArrayEquals(bytesIntFromBuffer, bytesInt);

		final int int2 = ByteUtil.bytesToInt(bytesInt, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int2);

		final byte[] bytesInt2 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		final int int3 = ByteUtil.bytesToInt(bytesInt2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int3);

		final byte[] bytesInt3 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		final int int4 = ByteUtil.bytesToInt(bytesInt3, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int4);
	}

	@Test
	public void intAndBytesBigEndianTest() {
		// 测试 int 转大端序 byte 数组
		final int int2 = RandomUtil.randomInt(Integer.MAX_VALUE);

		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(int2);
		final byte[] bytesIntFromBuffer = buffer.array();

		final byte[] bytesInt = ByteUtil.intToBytes(int2, ByteOrder.BIG_ENDIAN);
		Assert.assertArrayEquals(bytesIntFromBuffer, bytesInt);

		// 测试大端序 byte 数组转 int
		final int int3 = ByteUtil.bytesToInt(bytesInt, ByteOrder.BIG_ENDIAN);
		Assert.assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesLittleEndianTest() {
		// 测试 long 转 byte 数组
		final long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putLong(long1);
		final byte[] bytesLongFromBuffer = buffer.array();

		final byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
		Assert.assertArrayEquals(bytesLongFromBuffer, bytesLong);

		final long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(long1, long2);

		final byte[] bytesLong2 = ByteUtil.longToBytes(long1);
		final long long3 = ByteUtil.bytesToLong(bytesLong2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(long1, long3);

		final byte[] bytesLong3 = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
		final long long4 = ByteUtil.bytesToLong(bytesLong3);
		Assert.assertEquals(long1, long4);
	}

	@Test
	public void longAndBytesBigEndianTest() {
		// 测试大端序 long 转 byte 数组
		final long long1 = RandomUtil.randomLong(Long.MAX_VALUE);

		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(long1);
		final byte[] bytesLongFromBuffer = buffer.array();

		final byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.BIG_ENDIAN);
		Assert.assertArrayEquals(bytesLongFromBuffer, bytesLong);

		final long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.BIG_ENDIAN);
		Assert.assertEquals(long1, long2);
	}

	@Test
	public void floatAndBytesLittleEndianTest() {
		// 测试 long 转 byte 数组
		final float f1 = (float) RandomUtil.randomDouble();

		final byte[] bytesLong = ByteUtil.floatToBytes(f1, ByteOrder.LITTLE_ENDIAN);
		final float f2 = ByteUtil.bytesToFloat(bytesLong, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(f1, f2, 0);
	}

	@Test
	public void floatAndBytesBigEndianTest() {
		// 测试大端序 long 转 byte 数组
		final float f1 = (float) RandomUtil.randomDouble();

		final byte[] bytesLong = ByteUtil.floatToBytes(f1, ByteOrder.BIG_ENDIAN);
		final float f2 = ByteUtil.bytesToFloat(bytesLong, ByteOrder.BIG_ENDIAN);

		Assert.assertEquals(f1, f2, 0);
	}

	@Test
	public void shortAndBytesLittleEndianTest() {
		final short short1 = (short) RandomUtil.randomInt();

		final byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
		final short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(short2, short1);

		final byte[] bytes2 = ByteUtil.shortToBytes(short1);
		final short short3 = ByteUtil.bytesToShort(bytes2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(short3, short1);

		final byte[] bytes3 = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
		final short short4 = ByteUtil.bytesToShort(bytes3);
		Assert.assertEquals(short4, short1);
	}

	@Test
	public void shortAndBytesBigEndianTest() {
		final short short1 = 122;
		final byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.BIG_ENDIAN);
		final short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.BIG_ENDIAN);

		Assert.assertEquals(short2, short1);
	}

	@Test
	public void bytesToLongTest(){
		final long a = RandomUtil.randomLong(0, Long.MAX_VALUE);
		ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a));
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		long aLong = wrap.getLong();
		Assert.assertEquals(a, aLong);

		wrap = ByteBuffer.wrap(ByteUtil.longToBytes(a, ByteOrder.BIG_ENDIAN));
		wrap.order(ByteOrder.BIG_ENDIAN);
		aLong = wrap.getLong();
		Assert.assertEquals(a, aLong);
	}

	@Test
	public void bytesToIntTest(){
		final int a = RandomUtil.randomInt(0, Integer.MAX_VALUE);
		ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a));
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		int aInt = wrap.getInt();
		Assert.assertEquals(a, aInt);

		wrap = ByteBuffer.wrap(ByteUtil.intToBytes(a, ByteOrder.BIG_ENDIAN));
		wrap.order(ByteOrder.BIG_ENDIAN);
		aInt = wrap.getInt();
		Assert.assertEquals(a, aInt);
	}

	@Test
	public void bytesToShortTest(){
		final short a = (short) RandomUtil.randomInt(0, Short.MAX_VALUE);

		ByteBuffer wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a));
		wrap.order(ByteOrder.LITTLE_ENDIAN);
		short aShort = wrap.getShort();
		Assert.assertEquals(a, aShort);

		wrap = ByteBuffer.wrap(ByteUtil.shortToBytes(a, ByteOrder.BIG_ENDIAN));
		wrap.order(ByteOrder.BIG_ENDIAN);
		aShort = wrap.getShort();
		Assert.assertEquals(a, aShort);
	}
}
