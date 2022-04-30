package cn.hutool.core.text.bloom;

import java.util.function.Function;

/**
 * 基于Hash函数方法的{@link BloomFilter}
 *
 * @author looly
 * @since 5.8.0
 */
public class FuncFilter extends AbstractFilter {
	private static final long serialVersionUID = 1L;

	private final Function<String, Number> hashFunc;

	/**
	 * @param size   最大值
	 * @param hashFunc   Hash函数
	 */
	public FuncFilter(int size, Function<String, Number> hashFunc) {
		super(size);
		this.hashFunc = hashFunc;
	}

	@Override
	public int hash(String str) {
		return hashFunc.apply(str).intValue() % size;
	}
}
