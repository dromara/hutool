package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;

public class ByteUtilTest {
	@Test
	public void intAndBytesLittleEndianTest() {
		// 测试 int 转小端序 byte 数组
		int int1 = 1417;

		byte[] bytesInt = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		int int2 = ByteUtil.bytesToInt(bytesInt, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int2);

		byte[] bytesInt2 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		int int3 = ByteUtil.bytesToInt(bytesInt2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int3);

		byte[] bytesInt3 = ByteUtil.intToBytes(int1, ByteOrder.LITTLE_ENDIAN);
		int int4 = ByteUtil.bytesToInt(bytesInt3, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(int1, int4);
	}

	@Test
	public void intAndBytesBigEndianTest() {
		// 测试 int 转大端序 byte 数组
		int int2 = 1417;
		byte[] bytesInt = ByteUtil.intToBytes(int2, ByteOrder.BIG_ENDIAN);

		// 测试大端序 byte 数组转 int
		int int3 = ByteUtil.bytesToInt(bytesInt, ByteOrder.BIG_ENDIAN);
		Assert.assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesLittleEndianTest() {
		// 测试 long 转 byte 数组
		long long1 = 2223;

		byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
		long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(long1, long2);

		byte[] bytesLong2 = ByteUtil.longToBytes(long1);
		long long3 = ByteUtil.bytesToLong(bytesLong2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(long1, long3);

		byte[] bytesLong3 = ByteUtil.longToBytes(long1, ByteOrder.LITTLE_ENDIAN);
		long long4 = ByteUtil.bytesToLong(bytesLong3);
		Assert.assertEquals(long1, long4);
	}

	@Test
	public void longAndBytesBigEndianTest() {
		// 测试大端序 long 转 byte 数组
		long long1 = 2223;

		byte[] bytesLong = ByteUtil.longToBytes(long1, ByteOrder.BIG_ENDIAN);
		long long2 = ByteUtil.bytesToLong(bytesLong, ByteOrder.BIG_ENDIAN);

		Assert.assertEquals(long1, long2);
	}

	@Test
	public void shortAndBytesLittleEndianTest() {
		short short1 = 122;

		byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
		short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(short2, short1);

		byte[] bytes2 = ByteUtil.shortToBytes(short1);
		short short3 = ByteUtil.bytesToShort(bytes2, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals(short3, short1);

		byte[] bytes3 = ByteUtil.shortToBytes(short1, ByteOrder.LITTLE_ENDIAN);
		short short4 = ByteUtil.bytesToShort(bytes3);
		Assert.assertEquals(short4, short1);
	}

	@Test
	public void shortAndBytesBigEndianTest() {
		short short1 = 122;
		byte[] bytes = ByteUtil.shortToBytes(short1, ByteOrder.BIG_ENDIAN);
		short short2 = ByteUtil.bytesToShort(bytes, ByteOrder.BIG_ENDIAN);

		Assert.assertEquals(short2, short1);
	}
}
