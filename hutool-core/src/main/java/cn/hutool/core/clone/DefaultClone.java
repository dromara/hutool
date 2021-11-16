package cn.hutool.core.clone;


import java.lang.reflect.Method;

public interface DefaultClone extends java.lang.Cloneable {

	/**
	 * 浅拷贝
	 * 功能与 {@link CloneSupport#clone()} 类似, 是一种接口版的实现
	 * 一个类，一般只能继承一个类,但是可以实现多个接口,所以该接口会有一定程度的灵活性
	 *
	 * @param <T> T
	 * @return obj
	 */
	@SuppressWarnings({"unchecked"})
	default <T> T clone0() {
		try {
			final Class<Object> objectClass = Object.class;
			final Method clone = objectClass.getDeclaredMethod("clone");
			clone.setAccessible(true);
			return (T) clone.invoke(this);
		} catch (Exception e) {
			throw new CloneRuntimeException(e);
		}
	}
}


