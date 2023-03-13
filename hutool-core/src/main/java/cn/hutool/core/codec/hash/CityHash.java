package cn.hutool.core.codec.hash;

import cn.hutool.core.codec.Number128;
import cn.hutool.core.util.ByteUtil;

import java.util.Arrays;

/**
 * Google发布的Hash计算算法：CityHash64 与 CityHash128。<br>
 * 它们分别根据字串计算 64 和 128 位的散列值。这些算法不适用于加密，但适合用在散列表等处。
 *
 * <p>
 * 代码来自：https://github.com/rolandhe/string-tools<br>
 * 原始算法：https://github.com/google/cityhash
 *
 * @author hexiufeng
 * @since 5.2.5
 */
public class CityHash implements Hash32<byte[]>, Hash64<byte[]>, Hash128<byte[]>{
	/**
	 * 单例
	 */
	public static CityHash INSTANCE = new CityHash();

	// Some primes between 2^63 and 2^64 for various uses.
	private static final long k0 = 0xc3a5c85c97cb3127L;
	private static final long k1 = 0xb492b66fbe98f273L;
	private static final long k2 = 0x9ae16a3b2f90404fL;

	// Magic numbers for 32-bit hashing.  Copied from Murmur3.
	private static final int c1 = 0xcc9e2d51;
	private static final int c2 = 0x1b873593;

	@Override
	public Number encode(final byte[] bytes) {
		return hash64(bytes);
	}

	/**
	 * 计算32位City Hash值
	 *
	 * @param data 数据
	 * @return hash值
	 */
	@Override
	public int hash32(final byte[] data) {
		final int len = data.length;
		if (len <= 24) {
			return len <= 12 ?
					(len <= 4 ? hash32Len0to4(data) : hash32Len5to12(data)) :
					hash32Len13to24(data);
		}

		// len > 24
		int h = len, g = c1 * len, f = g;
		int a0 = Integer.rotateRight(fetch32(data, len - 4) * c1, 17) * c2;
		int a1 = Integer.rotateRight(fetch32(data, len - 8) * c1, 17) * c2;
		int a2 = Integer.rotateRight(fetch32(data, len - 16) * c1, 17) * c2;
		int a3 = Integer.rotateRight(fetch32(data, len - 12) * c1, 17) * c2;
		int a4 = Integer.rotateRight(fetch32(data, len - 20) * c1, 17) * c2;
		h ^= a0;
		h = Integer.rotateRight(h, 19);
		h = h * 5 + 0xe6546b64;
		h ^= a2;
		h = Integer.rotateRight(h, 19);
		h = h * 5 + 0xe6546b64;
		g ^= a1;
		g = Integer.rotateRight(g, 19);
		g = g * 5 + 0xe6546b64;
		g ^= a3;
		g = Integer.rotateRight(g, 19);
		g = g * 5 + 0xe6546b64;
		f += a4;
		f = Integer.rotateRight(f, 19);
		f = f * 5 + 0xe6546b64;
		int iters = (len - 1) / 20;

		int pos = 0;
		do {
			a0 = Integer.rotateRight(fetch32(data, pos) * c1, 17) * c2;
			a1 = fetch32(data, pos + 4);
			a2 = Integer.rotateRight(fetch32(data, pos + 8) * c1, 17) * c2;
			a3 = Integer.rotateRight(fetch32(data, pos + 12) * c1, 17) * c2;
			a4 = fetch32(data, pos + 16);
			h ^= a0;
			h = Integer.rotateRight(h, 18);
			h = h * 5 + 0xe6546b64;
			f += a1;
			f = Integer.rotateRight(f, 19);
			f = f * c1;
			g += a2;
			g = Integer.rotateRight(g, 18);
			g = g * 5 + 0xe6546b64;
			h ^= a3 + a1;
			h = Integer.rotateRight(h, 19);
			h = h * 5 + 0xe6546b64;
			g ^= a4;
			g = Integer.reverseBytes(g) * 5;
			h += a4 * 5;
			h = Integer.reverseBytes(h);
			f += a0;
			final int swapValue = f;
			f = g;
			g = h;
			h = swapValue;

			pos += 20;
		} while (--iters != 0);

		g = Integer.rotateRight(g, 11) * c1;
		g = Integer.rotateRight(g, 17) * c1;
		f = Integer.rotateRight(f, 11) * c1;
		f = Integer.rotateRight(f, 17) * c1;
		h = Integer.rotateRight(h + g, 19);
		h = h * 5 + 0xe6546b64;
		h = Integer.rotateRight(h, 17) * c1;
		h = Integer.rotateRight(h + f, 19);
		h = h * 5 + 0xe6546b64;
		h = Integer.rotateRight(h, 17) * c1;
		return h;
	}

	/**
	 * 计算64位City Hash值
	 *
	 * @param data 数据
	 * @return hash值
	 */
	@Override
	public long hash64(final byte[] data) {
		int len = data.length;
		if (len <= 32) {
			if (len <= 16) {
				return hashLen0to16(data);
			} else {
				return hashLen17to32(data);
			}
		} else if (len <= 64) {
			return hashLen33to64(data);
		}

		// For strings over 64 bytes we hash the end first, and then as we
		// loop we keep 56 bytes of state: v, w, x, y, and z.
		long x = fetch64(data, len - 40);
		long y = fetch64(data, len - 16) + fetch64(data, len - 56);
		long z = hashLen16(fetch64(data, len - 48) + len, fetch64(data, len - 24));
		Number128 v = weakHashLen32WithSeeds(data, len - 64, len, z);
		Number128 w = weakHashLen32WithSeeds(data, len - 32, y + k1, x);
		x = x * k1 + fetch64(data, 0);

		// Decrease len to the nearest multiple of 64, and operate on 64-byte chunks.
		len = (len - 1) & ~63;
		int pos = 0;
		do {
			x = Long.rotateRight(x + y + v.getLowValue() + fetch64(data, pos + 8), 37) * k1;
			y = Long.rotateRight(y + v.getHighValue() + fetch64(data, pos + 48), 42) * k1;
			x ^= w.getHighValue();
			y += v.getLowValue() + fetch64(data, pos + 40);
			z = Long.rotateRight(z + w.getLowValue(), 33) * k1;
			v = weakHashLen32WithSeeds(data, pos, v.getHighValue() * k1, x + w.getLowValue());
			w = weakHashLen32WithSeeds(data, pos + 32, z + w.getHighValue(), y + fetch64(data, pos + 16));
			// swap z,x value
			final long swapValue = x;
			x = z;
			z = swapValue;
			pos += 64;
			len -= 64;
		} while (len != 0);
		return hashLen16(hashLen16(v.getLowValue(), w.getLowValue()) + shiftMix(y) * k1 + z,
				hashLen16(v.getHighValue(), w.getHighValue()) + x);
	}

	/**
	 * 计算64位City Hash值
	 *
	 * @param data  数据
	 * @param seed0 种子1
	 * @param seed1 种子2
	 * @return hash值
	 */
	public long hash64(final byte[] data, final long seed0, final long seed1) {
		return hashLen16(hash64(data) - seed0, seed1);
	}

	/**
	 * 计算64位City Hash值，种子1使用默认的{@link #k2}
	 *
	 * @param data 数据
	 * @param seed 种子2
	 * @return hash值
	 */
	public long hash64(final byte[] data, final long seed) {
		return hash64(data, k2, seed);
	}

	/**
	 * 计算128位City Hash值
	 *
	 * @param data 数据
	 * @return hash值
	 */
	@Override
	public Number128 hash128(final byte[] data) {
		final int len = data.length;
		return len >= 16 ?
				hash128(data, 16,
						new Number128(fetch64(data, 0), fetch64(data, 8) + k0)) :
				hash128(data, 0, new Number128(k0, k1));
	}

	/**
	 * 计算128位City Hash值
	 *
	 * @param data 数据
	 * @param seed 种子
	 * @return hash值
	 */
	public Number128 hash128(final byte[] data, final Number128 seed) {
		return hash128(data, 0, seed);
	}

	//------------------------------------------------------------------------------------------------------- Private method start
	private Number128 hash128(final byte[] byteArray, final int start, final Number128 seed) {
		int len = byteArray.length - start;

		if (len < 128) {
			return cityMurmur(Arrays.copyOfRange(byteArray, start, byteArray.length), seed);
		}

		// We expect len >= 128 to be the common case.  Keep 56 bytes of state:
		// v, w, x, y, and z.
		Number128 v = new Number128(0L, 0L);
		Number128 w = new Number128(0L, 0L);
		long x = seed.getLowValue();
		long y = seed.getHighValue();
		long z = len * k1;
		v.setLowValue(Long.rotateRight(y ^ k1, 49) * k1 + fetch64(byteArray, start));
		v.setHighValue(Long.rotateRight(v.getLowValue(), 42) * k1 + fetch64(byteArray, start + 8));
		w.setLowValue(Long.rotateRight(y + z, 35) * k1 + x);
		w.setHighValue(Long.rotateRight(x + fetch64(byteArray, start + 88), 53) * k1);

		// This is the same inner loop as CityHash64(), manually unrolled.
		int pos = start;
		do {
			x = Long.rotateRight(x + y + v.getLowValue() + fetch64(byteArray, pos + 8), 37) * k1;
			y = Long.rotateRight(y + v.getHighValue() + fetch64(byteArray, pos + 48), 42) * k1;
			x ^= w.getHighValue();
			y += v.getLowValue() + fetch64(byteArray, pos + 40);
			z = Long.rotateRight(z + w.getLowValue(), 33) * k1;
			v = weakHashLen32WithSeeds(byteArray, pos, v.getHighValue() * k1, x + w.getLowValue());
			w = weakHashLen32WithSeeds(byteArray, pos + 32, z + w.getHighValue(), y + fetch64(byteArray, pos + 16));

			long swapValue = x;
			x = z;
			z = swapValue;
			pos += 64;
			x = Long.rotateRight(x + y + v.getLowValue() + fetch64(byteArray, pos + 8), 37) * k1;
			y = Long.rotateRight(y + v.getHighValue() + fetch64(byteArray, pos + 48), 42) * k1;
			x ^= w.getHighValue();
			y += v.getLowValue() + fetch64(byteArray, pos + 40);
			z = Long.rotateRight(z + w.getLowValue(), 33) * k1;
			v = weakHashLen32WithSeeds(byteArray, pos, v.getHighValue() * k1, x + w.getLowValue());
			w = weakHashLen32WithSeeds(byteArray, pos + 32, z + w.getHighValue(), y + fetch64(byteArray, pos + 16));
			swapValue = x;
			x = z;
			z = swapValue;
			pos += 64;
			len -= 128;
		} while (len >= 128);
		x += Long.rotateRight(v.getLowValue() + z, 49) * k0;
		y = y * k0 + Long.rotateRight(w.getHighValue(), 37);
		z = z * k0 + Long.rotateRight(w.getLowValue(), 27);
		w.setLowValue(w.getLowValue() * 9);
		v.setLowValue(v.getLowValue() * k0);

		// If 0 < len < 128, hash up to 4 chunks of 32 bytes each from the end of s.
		for (int tail_done = 0; tail_done < len; ) {
			tail_done += 32;
			y = Long.rotateRight(x + y, 42) * k0 + v.getHighValue();
			w.setLowValue(w.getLowValue() + fetch64(byteArray, pos + len - tail_done + 16));
			x = x * k0 + w.getLowValue();
			z += w.getHighValue() + fetch64(byteArray, pos + len - tail_done);
			w.setHighValue(w.getHighValue() + v.getLowValue());
			v = weakHashLen32WithSeeds(byteArray, pos + len - tail_done, v.getLowValue() + z, v.getHighValue());
			v.setLowValue(v.getLowValue() * k0);
		}
		// At this point our 56 bytes of state should contain more than
		// enough information for a strong 128-bit hash.  We use two
		// different 56-byte-to-8-byte hashes to get a 16-byte final result.
		x = hashLen16(x, v.getLowValue());
		y = hashLen16(y + z, w.getLowValue());
		return new Number128(hashLen16(x + v.getHighValue(), w.getHighValue()) + y,
				hashLen16(x + w.getHighValue(), y + v.getHighValue()));

	}

	private int hash32Len0to4(final byte[] byteArray) {
		int b = 0;
		int c = 9;
		final int len = byteArray.length;
		for (final int v : byteArray) {
			b = b * c1 + v;
			c ^= b;
		}
		return fmix(mur(b, mur(len, c)));
	}

	private int hash32Len5to12(final byte[] byteArray) {
		final int len = byteArray.length;
		int a = len;
		int b = len * 5;
		int c = 9;
		final int d = b;
		a += fetch32(byteArray, 0);
		b += fetch32(byteArray, len - 4);
		c += fetch32(byteArray, ((len >>> 1) & 4));
		return fmix(mur(c, mur(b, mur(a, d))));
	}

	private int hash32Len13to24(final byte[] byteArray) {
		final int len = byteArray.length;
		final int a = fetch32(byteArray, (len >>> 1) - 4);
		final int b = fetch32(byteArray, 4);
		final int c = fetch32(byteArray, len - 8);
		final int d = fetch32(byteArray, (len >>> 1));
		final int e = fetch32(byteArray, 0);
		final int f = fetch32(byteArray, len - 4);
		@SuppressWarnings("UnnecessaryLocalVariable") final int h = len;

		return fmix(mur(f, mur(e, mur(d, mur(c, mur(b, mur(a, h)))))));
	}

	private long hashLen0to16(final byte[] byteArray) {
		final int len = byteArray.length;
		if (len >= 8) {
			final long mul = k2 + len * 2L;
			final long a = fetch64(byteArray, 0) + k2;
			final long b = fetch64(byteArray, len - 8);
			final long c = Long.rotateRight(b, 37) * mul + a;
			final long d = (Long.rotateRight(a, 25) + b) * mul;
			return hashLen16(c, d, mul);
		}
		if (len >= 4) {
			final long mul = k2 + len * 2;
			final long a = fetch32(byteArray, 0) & 0xffffffffL;
			return hashLen16(len + (a << 3), fetch32(byteArray, len - 4) & 0xffffffffL, mul);
		}
		if (len > 0) {
			final int a = byteArray[0] & 0xff;
			final int b = byteArray[len >>> 1] & 0xff;
			final int c = byteArray[len - 1] & 0xff;
			final int y = a + (b << 8);
			final int z = len + (c << 2);
			return shiftMix(y * k2 ^ z * k0) * k2;
		}
		return k2;
	}

	// This probably works well for 16-byte strings as well, but it may be overkill in that case.
	private long hashLen17to32(final byte[] byteArray) {
		final int len = byteArray.length;
		final long mul = k2 + len * 2L;
		final long a = fetch64(byteArray, 0) * k1;
		final long b = fetch64(byteArray, 8);
		final long c = fetch64(byteArray, len - 8) * mul;
		final long d = fetch64(byteArray, len - 16) * k2;
		return hashLen16(Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d,
				a + Long.rotateRight(b + k2, 18) + c, mul);
	}

	private long hashLen33to64(final byte[] byteArray) {
		final int len = byteArray.length;
		final long mul = k2 + len * 2L;
		long a = fetch64(byteArray, 0) * k2;
		long b = fetch64(byteArray, 8);
		final long c = fetch64(byteArray, len - 24);
		final long d = fetch64(byteArray, len - 32);
		final long e = fetch64(byteArray, 16) * k2;
		final long f = fetch64(byteArray, 24) * 9;
		final long g = fetch64(byteArray, len - 8);
		final long h = fetch64(byteArray, len - 16) * mul;
		final long u = Long.rotateRight(a + g, 43) + (Long.rotateRight(b, 30) + c) * 9;
		final long v = ((a + g) ^ d) + f + 1;
		final long w = Long.reverseBytes((u + v) * mul) + h;
		final long x = Long.rotateRight(e + f, 42) + c;
		final long y = (Long.reverseBytes((v + w) * mul) + g) * mul;
		final long z = e + f + c;
		a = Long.reverseBytes((x + z) * mul + y) + b;
		b = shiftMix((z + a) * mul + d + h) * mul;
		return b + x;
	}

	private static long fetch64(final byte[] byteArray, final int start) {
		return ByteUtil.bytesToLong(byteArray, start, ByteUtil.CPU_ENDIAN);
	}

	private static int fetch32(final byte[] byteArray, final int start) {
		return ByteUtil.bytesToInt(byteArray, start, ByteUtil.CPU_ENDIAN);
	}

	private static long hashLen16(final long u, final long v, final long mul) {
		// Murmur-inspired hashing.
		long a = (u ^ v) * mul;
		a ^= (a >>> 47);
		long b = (v ^ a) * mul;
		b ^= (b >>> 47);
		b *= mul;
		return b;
	}

	private long hashLen16(final long u, final long v) {
		return hash128to64(new Number128(u, v));
	}

	private long hash128to64(final Number128 number128) {
		// Murmur-inspired hashing.
		final long kMul = 0x9ddfea08eb382d69L;
		long a = (number128.getLowValue() ^ number128.getHighValue()) * kMul;
		a ^= (a >>> 47);
		long b = (number128.getHighValue() ^ a) * kMul;
		b ^= (b >>> 47);
		b *= kMul;
		return b;
	}

	private static long shiftMix(final long val) {
		return val ^ (val >>> 47);
	}

	private static int fmix(int h) {
		h ^= h >>> 16;
		h *= 0x85ebca6b;
		h ^= h >>> 13;
		h *= 0xc2b2ae35;
		h ^= h >>> 16;
		return h;
	}

	private int mur(int a, int h) {
		// Helper from Murmur3 for combining two 32-bit values.
		a *= c1;
		a = Integer.rotateRight(a, 17);
		a *= c2;
		h ^= a;
		h = Integer.rotateRight(h, 19);
		return h * 5 + 0xe6546b64;
	}

	private static Number128 weakHashLen32WithSeeds(
			final long w, final long x, final long y, final long z, long a, long b) {
		a += w;
		b = Long.rotateRight(b + a + z, 21);
		final long c = a;
		a += x;
		a += y;
		b += Long.rotateRight(a, 44);
		return new Number128(a + z, b + c);
	}

	// Return a 16-byte hash for s[0] ... s[31], a, and b.  Quick and dirty.
	private static Number128 weakHashLen32WithSeeds(
			final byte[] byteArray, final int start, final long a, final long b) {
		return weakHashLen32WithSeeds(fetch64(byteArray, start),
				fetch64(byteArray, start + 8),
				fetch64(byteArray, start + 16),
				fetch64(byteArray, start + 24),
				a,
				b);
	}

	private Number128 cityMurmur(final byte[] byteArray, final Number128 seed) {
		final int len = byteArray.length;
		long a = seed.getLowValue();
		long b = seed.getHighValue();
		long c;
		long d;
		int l = len - 16;
		if (l <= 0) {  // len <= 16
			a = shiftMix(a * k1) * k1;
			c = b * k1 + hashLen0to16(byteArray);
			d = shiftMix(a + (len >= 8 ? fetch64(byteArray, 0) : c));
		} else {  // len > 16
			c = hashLen16(fetch64(byteArray, len - 8) + k1, a);
			d = hashLen16(b + len, c + fetch64(byteArray, len - 16));
			a += d;
			int pos = 0;
			do {
				a ^= shiftMix(fetch64(byteArray, pos) * k1) * k1;
				a *= k1;
				b ^= a;
				c ^= shiftMix(fetch64(byteArray, pos + 8) * k1) * k1;
				c *= k1;
				d ^= c;
				pos += 16;
				l -= 16;
			} while (l > 0);
		}
		a = hashLen16(a, c);
		b = hashLen16(d, b);
		return new Number128(a ^ b, hashLen16(b, a));
	}
	//------------------------------------------------------------------------------------------------------- Private method end
}
