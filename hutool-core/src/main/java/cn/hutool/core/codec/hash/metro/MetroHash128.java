package cn.hutool.core.codec.hash.metro;

import cn.hutool.core.codec.Number128;
import cn.hutool.core.codec.hash.Hash128;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Apache 发布的MetroHash算法的128位实现，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * 来自：https://github.com/postamar/java-metrohash/
 * @author Marius Posta
 */
public class MetroHash128 extends AbstractMetroHash<MetroHash128> implements Hash128<byte[]> {

	/**
	 * 创建 {@code MetroHash128}对象
	 *
	 * @param seed  种子
	 * @return {@code MetroHash128}对象
	 */
	public static MetroHash128 of(final long seed) {
		return new MetroHash128(seed);
	}

	private static final long K0 = 0xC83A91E1L;
	private static final long K1 = 0x8648DBDBL;
	private static final long K2 = 0x7BDEC03BL;
	private static final long K3 = 0x2F5870A5L;

	/**
	 * 使用指定种子构造
	 *
	 * @param seed 种子
	 */
	public MetroHash128(final long seed) {
		super(seed);
	}

	@Override
	public MetroHash128 reset() {
		v0 = (seed - K0) * K3;
		v1 = (seed + K1) * K2;
		v2 = (seed + K0) * K2;
		v3 = (seed - K1) * K3;
		nChunks = 0;
		return this;
	}

	/**
	 * 获取结果hash值
	 *
	 * @return hash值
	 */
	public Number128 get() {
		return new Number128(v1, v0);
	}

	@Override
	public Number128 hash128(final byte[] bytes) {
		return apply(ByteBuffer.wrap(bytes)).get();
	}

	@Override
	public MetroHash128 write(final ByteBuffer output, final ByteOrder byteOrder) {
		if (ByteOrder.LITTLE_ENDIAN == byteOrder) {
			writeLittleEndian(v0, output);
			writeLittleEndian(v1, output);
		} else {
			output.asLongBuffer().put(v1).put(v0);
		}
		return this;
	}

	@Override
	MetroHash128 partialApply32ByteChunk(final ByteBuffer partialInput) {
		assert partialInput.remaining() >= 32;
		v0 += grab(partialInput, 8) * K0;
		v0 = Long.rotateRight(v0, 29) + v2;
		v1 += grab(partialInput, 8) * K1;
		v1 = Long.rotateRight(v1, 29) + v3;
		v2 += grab(partialInput, 8) * K2;
		v2 = Long.rotateRight(v2, 29) + v0;
		v3 += grab(partialInput, 8) * K3;
		v3 = Long.rotateRight(v3, 29) + v1;
		++nChunks;
		return this;
	}

	@Override
	MetroHash128 partialApplyRemaining(final ByteBuffer partialInput) {
		assert partialInput.remaining() < 32;
		if (nChunks > 0) {
			metroHash128_32();
		}
		if (partialInput.remaining() >= 16) {
			metroHash128_16(partialInput);
		}
		if (partialInput.remaining() >= 8) {
			metroHash128_8(partialInput);
		}
		if (partialInput.remaining() >= 4) {
			metroHash128_4(partialInput);
		}
		if (partialInput.remaining() >= 2) {
			metroHash128_2(partialInput);
		}
		if (partialInput.remaining() >= 1) {
			metroHash128_1(partialInput);
		}
		v0 += Long.rotateRight(v0 * K0 + v1, 13);
		v1 += Long.rotateRight(v1 * K1 + v0, 37);
		v0 += Long.rotateRight(v0 * K2 + v1, 13);
		v1 += Long.rotateRight(v1 * K3 + v0, 37);
		return this;
	}

	// region ----- private methods
	private void metroHash128_32() {
		v2 ^= Long.rotateRight((v0 + v3) * K0 + v1, 21) * K1;
		v3 ^= Long.rotateRight((v1 + v2) * K1 + v0, 21) * K0;
		v0 ^= Long.rotateRight((v0 + v2) * K0 + v3, 21) * K1;
		v1 ^= Long.rotateRight((v1 + v3) * K1 + v2, 21) * K0;
	}

	private void metroHash128_16(final ByteBuffer bb) {
		v0 += grab(bb, 8) * K2;
		v0 = Long.rotateRight(v0, 33) * K3;
		v1 += grab(bb, 8) * K2;
		v1 = Long.rotateRight(v1, 33) * K3;
		v0 ^= Long.rotateRight(v0 * K2 + v1, 45) * K1;
		v1 ^= Long.rotateRight(v1 * K3 + v0, 45) * K0;
	}

	private void metroHash128_8(final ByteBuffer bb) {
		v0 += grab(bb, 8) * K2;
		v0 = Long.rotateRight(v0, 33) * K3;
		v0 ^= Long.rotateRight(v0 * K2 + v1, 27) * K1;
	}

	private void metroHash128_4(final ByteBuffer bb) {
		v1 += grab(bb, 4) * K2;
		v1 = Long.rotateRight(v1, 33) * K3;
		v1 ^= Long.rotateRight(v1 * K3 + v0, 46) * K0;
	}

	private void metroHash128_2(final ByteBuffer bb) {
		v0 += grab(bb, 2) * K2;
		v0 = Long.rotateRight(v0, 33) * K3;
		v0 ^= Long.rotateRight(v0 * K2 + v1, 22) * K1;
	}

	private void metroHash128_1(final ByteBuffer bb) {
		v1 += grab(bb, 1) * K2;
		v1 = Long.rotateRight(v1, 33) * K3;
		v1 ^= Long.rotateRight(v1 * K3 + v0, 58) * K0;
	}
	// endregion
}
