package org.dromara.hutool.core.lang.mutable;

/**
 * @author huangchengxing
 */
public class MutableFloatTest extends BaseMutableTest<Number, MutableFloat> {
	/**
	 * 创建一个值，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	Number getValue1() {
		return Float.MAX_VALUE;
	}

	/**
	 * 创建一个值，与{@link #getValue1()}不同，且反复调用应该返回完全相同的值
	 *
	 * @return 值
	 */
	@Override
	Number getValue2() {
		return Float.MIN_VALUE;
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableFloat getMutable(Number value) {
		return new MutableFloat(value);
	}
}
