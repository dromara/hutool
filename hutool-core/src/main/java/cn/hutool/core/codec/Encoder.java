package cn.hutool.core.codec;

/**
 * 编码接口
 *
 * @param <T> 被编码的数据类型
 * @param <R> 编码后的数据类型
 * @author looly
 * @since 5.7.22
 */
public interface Encoder<T, R> {

	/**
	 * 执行编码
	 *
	 * @param data 被编码的数据
	 * @return 编码后的数据
	 */
	R encode(T data);
}
