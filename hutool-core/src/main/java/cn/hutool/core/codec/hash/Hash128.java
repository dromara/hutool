package cn.hutool.core.codec.hash;

import cn.hutool.core.codec.Encoder;
import cn.hutool.core.codec.Number128;

/**
 * Hash计算接口
 *
 * @param <T> 被计算hash的对象类型
 * @author looly
 * @since 5.2.5
 */
@FunctionalInterface
public interface Hash128<T> extends Encoder<T, Number> {

	/**
	 * 计算Hash值
	 *
	 * @param t 对象
	 * @return hash
	 */
	Number128 hash128(T t);

	@Override
	default Number encode(final T t){
		return hash128(t);
	}
}
