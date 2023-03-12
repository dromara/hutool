package cn.hutool.core.codec.hash.metro;

import java.nio.ByteBuffer;

/**
 * Apache 发布的MetroHash算法抽象实现，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * 来自：https://github.com/postamar/java-metrohash/
 *
 * @author Marius Posta
 * @param <R> 返回值类型，为this类型
 */
public abstract class AbstractMetroHash<R extends AbstractMetroHash<R>> implements MetroHash<R> {

	final long seed;
	long v0, v1, v2, v3;
	long nChunks;

	/**
	 * 使用指定种子构造
	 *
	 * @param seed 种子
	 */
	public AbstractMetroHash(final long seed) {
		this.seed = seed;
		reset();
	}

	/**
	 * Applies the instance's Metro hash function to the bytes in the given buffer.
	 * This updates this instance's hashing state.
	 *
	 * @param input 内容
	 * @return this
	 */
	public R apply(final ByteBuffer input) {
		reset();
		while (input.remaining() >= 32) {
			partialApply32ByteChunk(input);
		}
		return partialApplyRemaining(input);
	}

	/**
	 * Consumes a 32-byte chunk from the byte buffer and updates the hashing state.
	 *
	 * @param partialInput byte buffer with at least 32 bytes remaining.
	 * @return this
	 */
	abstract R partialApply32ByteChunk(ByteBuffer partialInput);

	/**
	 * Consumes the remaining bytes from the byte buffer and updates the hashing state.
	 *
	 * @param partialInput byte buffer with less than 32 bytes remaining.
	 * @return this
	 */
	abstract R partialApplyRemaining(ByteBuffer partialInput);
}
