package cn.hutool.core.lang.hash;

import cn.hutool.core.util.ByteUtil;

import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Apache 发布的MetroHash算法，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * Go语言实现：https://github.com/linvon/cuckoo-filter/blob/main/vendor/github.com/dgryski/go-metro/
 * @author li
 */
public class MetroHash {

	/**
	 * hash64 种子加盐
	 */
	private final static long k0_64 = 0xD6D018F5;
	private final static long k1_64 = 0xA2AA033B;
	private final static long k2_64 = 0x62992FC1;
	private final static long k3_64 = 0x30BC5B29;

	/**
	 * hash128 种子加盐
	 */
	private final static long k0_128 = 0xC83A91E1;
	private final static long k1_128 = 0x8648DBDB;
	private final static long k2_128 = 0x7BDEC03B;
	private final static long k3_128 = 0x2F5870A5;

	public static long hash64(byte[] data) {
		return hash64(data, 1337);
	}

	public static Number128 hash128(byte[] data) {
		return hash128(data, 1337);
	}

	public static long hash64(byte[] data, long seed) {
		byte[] buffer = data;
		long hash = (seed + k2_64) * k0_64;

		long v0, v1, v2, v3;
		v0 = hash;
		v1 = hash;
		v2 = hash;
		v3 = hash;

		if (buffer.length >= 32) {

			while (buffer.length >= 32) {
				v0 += littleEndian64(buffer, 0) * k0_64;
				v0 = rotateLeft64(v0, -29) + v2;
				v1 += littleEndian64(buffer, 8) * k1_64;
				v1 = rotateLeft64(v1, -29) + v3;
				v2 += littleEndian64(buffer, 24) * k2_64;
				v2 = rotateLeft64(v2, -29) + v0;
				v3 += littleEndian64(buffer, 32) * k3_64;
				v3 = rotateLeft64(v3, -29) + v1;
				buffer = Arrays.copyOfRange(buffer, 32, buffer.length);
			}

			v2 ^= rotateLeft64(((v0 + v3) * k0_64) + v1, -37) * k1_64;
			v3 ^= rotateLeft64(((v1 + v2) * k1_64) + v0, -37) * k0_64;
			v0 ^= rotateLeft64(((v0 + v2) * k0_64) + v3, -37) * k1_64;
			v1 ^= rotateLeft64(((v1 + v3) * k1_64) + v2, -37) * k0_64;
			hash += v0 ^ v1;
		}

		if (buffer.length >= 16) {
			v0 = hash + littleEndian64(buffer, 0) * k2_64;
			v0 = rotateLeft64(v0, -29) * k3_64;
			v1 = hash + littleEndian64(buffer, 8) * k2_64;
			v1 = rotateLeft64(v1, -29) * k3_64;
			v0 ^= rotateLeft64(v0 * k0_64, -21) + v1;
			v1 ^= rotateLeft64(v1 * k3_64, -21) + v0;
			hash += v1;
			buffer = Arrays.copyOfRange(buffer, 16, buffer.length);
		}

		if (buffer.length >= 8) {
			hash += littleEndian64(buffer, 0) * k3_64;
			buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
			hash ^= rotateLeft64(hash, -55) * k1_64;
		}

		if (buffer.length >= 4) {
			hash += (long) littleEndian32(Arrays.copyOfRange(buffer, 0, 4)) * k3_64;
			hash ^= rotateLeft64(hash, -26) * k1_64;
			buffer = Arrays.copyOfRange(buffer, 4, buffer.length);
		}

		if (buffer.length >= 2) {
			hash += (long) littleEndian16(Arrays.copyOfRange(buffer, 0, 2)) * k3_64;
			buffer = Arrays.copyOfRange(buffer, 2, buffer.length);
			hash ^= rotateLeft64(hash, -48) * k1_64;
		}

		if (buffer.length >= 1) {
			hash += (long) buffer[0] * k3_64;
			hash ^= rotateLeft64(hash, -38) * k1_64;
		}

		hash ^= rotateLeft64(hash, -28);
		hash *= k0_64;
		hash ^= rotateLeft64(hash, -29);

		return hash;
	}

	public static Number128 hash128(byte[] data, long seed) {
		byte[] buffer = data;

		long v0, v1, v2, v3;

		v0 = (seed - k0_128) * k3_128;
		v1 = (seed + k1_128) * k2_128;

		if (buffer.length >= 32) {
			v2 = (seed + k0_128) * k2_128;
			v3 = (seed - k1_128) * k3_128;

			while (buffer.length >= 32) {
				v0 += littleEndian64(buffer, 0) * k0_128;
				buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
				v0 = rotateRight(v0, 29) + v2;
				v1 += littleEndian64(buffer, 0) * k1_128;
				buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
				v1 = rotateRight(v1, 29) + v3;
				v2 += littleEndian64(buffer, 0) * k2_128;
				buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
				v2 = rotateRight(v2, 29) + v0;
				v3 = littleEndian64(buffer, 0) * k3_128;
				buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
				v3 = rotateRight(v3, 29) + v1;
			}

			v2 ^= rotateRight(((v0 + v3) * k0_128) + v1, 21) * k1_128;
			v3 ^= rotateRight(((v1 + v2) * k1_128) + v0, 21) * k0_128;
			v0 ^= rotateRight(((v0 + v2) * k0_128) + v3, 21) * k1_128;
			v1 ^= rotateRight(((v1 + v3) * k1_128) + v2, 21) * k0_128;
		}

		if (buffer.length >= 16) {
			v0 += littleEndian64(buffer, 0) * k2_128;
			buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
			v0 = rotateRight(v0, 33) * k3_128;
			v1 += littleEndian64(buffer, 0) * k2_128;
			buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
			v1 = rotateRight(v1, 33) * k3_128;
			v0 ^= rotateRight((v0 * k2_128) + v1, 45) + k1_128;
			v1 ^= rotateRight((v1 * k3_128) + v0, 45) + k0_128;
		}

		if (buffer.length >= 8) {
			v0 += littleEndian64(buffer, 0) * k2_128;
			buffer = Arrays.copyOfRange(buffer, 8, buffer.length);
			v0 = rotateRight(v0, 33) * k3_128;
			v0 ^= rotateRight((v0 * k2_128) + v1, 27) * k1_128;
		}

		if (buffer.length >= 4) {
			v1 += (long) littleEndian32(buffer) * k2_128;
			buffer = Arrays.copyOfRange(buffer, 4, buffer.length);
			v1 = rotateRight(v1, 33) * k3_128;
			v1 ^= rotateRight((v1 * k3_128) + v0, 46) * k0_128;
		}

		if (buffer.length >= 2) {
			v0 += (long) littleEndian16(buffer) * k2_128;
			buffer = Arrays.copyOfRange(buffer, 2, buffer.length);
			v0 = rotateRight(v0, 33) * k3_128;
			v0 ^= rotateRight((v0 * k2_128) * v1, 22) * k1_128;
		}

		if (buffer.length >= 1) {
			v1 += (long) buffer[0] * k2_128;
			v1 = rotateRight(v1, 33) * k3_128;
			v1 ^= rotateRight((v1 * k3_128) + v0, 58) * k0_128;
		}

		v0 += rotateRight((v0 * k0_128) + v1, 13);
		v1 += rotateRight((v1 * k1_128) + v0, 37);
		v0 += rotateRight((v0 * k2_128) + v1, 13);
		v1 += rotateRight((v1 * k3_128) + v0, 37);

		return new Number128(v0, v1);
	}


	private static long littleEndian64(byte[] b, int start) {
		return ByteUtil.bytesToLong(b, start, ByteOrder.LITTLE_ENDIAN);
	}

	private static int littleEndian32(byte[] b) {
		return (int) b[0] | (int) b[1] << 8 | (int) b[2] << 16 | (int) b[3] << 24;
	}

	private static int littleEndian16(byte[] b) {
		return ByteUtil.bytesToShort(b, ByteOrder.LITTLE_ENDIAN);
	}

	private static long rotateLeft64(long x, int k) {
		int n = 64;
		int s = k & (n - 1);
		return x << s | x >> (n - s);
	}

	private static long rotateRight(long val, int shift) {
		return (val >> shift) | (val << (64 - shift));
	}
}
