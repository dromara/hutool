package cn.hutool.core.lang.generator;

import cn.hutool.core.util.ReflectUtil;

/**
 * 对象生成器，通过指定对象的Class类型，调用next方法时生成新的对象。
 *
 * @param <T> 对象类型
 * @author looly
 * @since 5.4.3
 */
public class ObjectGenerator<T> implements Generator<T> {

	private final Class<T> clazz;

	/**
	 * 构造
	 * @param clazz 对象类型
	 */
	public ObjectGenerator(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T next() {
		return ReflectUtil.newInstanceIfPossible(this.clazz);
	}
}
