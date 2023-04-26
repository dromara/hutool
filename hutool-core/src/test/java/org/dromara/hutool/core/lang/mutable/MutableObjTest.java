package org.dromara.hutool.core.lang.mutable;

/**
 * test for {@link MutableObj}
 *
 * @author huangchengxing
 */
class MutableObjTest extends BaseMutableTest<String, MutableObj<String>> {

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	String getValue1() {
		return "test1";
	}

	/**
	 * 创建一个值
	 *
	 * @return 值
	 */
	@Override
	String getValue2() {
		return "test2";
	}

	/**
	 * 创建一个{@link Mutable}
	 *
	 * @param value 值
	 * @return 值
	 */
	@Override
	MutableObj<String> getMutable(String value) {
		return new MutableObj<>(value);
	}
}
