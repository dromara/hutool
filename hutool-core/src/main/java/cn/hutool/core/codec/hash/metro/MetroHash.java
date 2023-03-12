package cn.hutool.core.codec.hash.metro;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Apache 发布的MetroHash算法接口，是一组用于非加密用例的最先进的哈希函数。
 * 除了卓越的性能外，他们还以算法生成而著称。
 *
 * <p>
 * 官方实现：https://github.com/jandrewrogers/MetroHash
 * 官方文档：http://www.jandrewrogers.com/2015/05/27/metrohash/
 * 来自：https://github.com/postamar/java-metrohash/
 *
 * @param <R> 返回值类型，为this类型
 * @author Marius Posta
 */
public interface MetroHash<R extends MetroHash<R>> {

	/**
	 * 创建 {@code MetroHash}对象
	 *
	 * @param seed  种子
	 * @param is128 是否128位
	 * @return {@code MetroHash}对象
	 */
	static MetroHash<?> of(final long seed, final boolean is128) {
		return is128 ? new MetroHash128(seed) : new MetroHash64(seed);
	}

	/**
	 * Applies the instance's Metro hash function to the bytes in the given buffer.
	 * This updates this instance's hashing state.
	 *
	 * @param input 内容
	 * @return this
	 */
	R apply(final ByteBuffer input);

	/**
	 * Writes the current hash to the given byte buffer in big-endian order.
	 * 将结果hash值写出到{@link ByteBuffer}中
	 *
	 * @param output    输出
	 * @param byteOrder 端序
	 * @return this
	 */
	R write(ByteBuffer output, final ByteOrder byteOrder);

	/**
	 * 重置，重置后可复用对象开启新的计算
	 *
	 * @return this
	 */
	R reset();
}
