package cn.hutool.bloomfilter.filter;

import cn.hutool.bloomfilter.BloomFilter;

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
	 * 构造
	 *
	 * @param maxValue 最大值
	 * @param hashFunc Hash函数
	 */
	public FuncFilter(long maxValue, Function<String, Number> hashFunc) {
		this(maxValue, DEFAULT_MACHINE_NUM, hashFunc);
	}

	/**
	 * @param maxValue   最大值
	 * @param machineNum 机器位数
	 * @param hashFunc   Hash函数
	 */
	public FuncFilter(long maxValue, int machineNum, Function<String, Number> hashFunc) {
		super(maxValue, machineNum);
		this.hashFunc = hashFunc;
	}

	@Override
	public long hash(String str) {
		return hashFunc.apply(str).longValue() % size;
	}
}
