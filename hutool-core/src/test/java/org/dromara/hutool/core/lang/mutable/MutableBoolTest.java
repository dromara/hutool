package org.dromara.hutool.core.lang.mutable;

/**
 * test for {@link MutableBool}
 *
 * @author huangchengxing
 */
public class MutableBoolTest extends BaseMutableTest<Boolean, MutableBool> {

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	Boolean getValue1() {
		return Boolean.TRUE;
	}

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	Boolean getValue2() {
		return Boolean.FALSE;
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableBool getMutable(Boolean value) {
		return new MutableBool(value);
	}
}
