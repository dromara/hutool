package cn.hutool.json.serialize;

import cn.hutool.core.lang.func.Wrapper;

/**
 * {@code JSONString}接口定义了一个{@code toJSONString()}<br>
 * 实现此接口的类可以通过实现{@code toJSONString()}方法来改变转JSON字符串的方式。
 *
 * @author Looly
 *
 */
@FunctionalInterface
public interface JSONStringer extends Wrapper<Object> {

	/**
	 * 自定义转JSON字符串的方法
	 *
	 * @return JSON字符串
	 */
	String toJSONString();

	/**
	 * 获取原始的对象，默认为this
	 *
	 * @return 原始对象
	 */
	@Override
	default Object getRaw() {
		return this;
	}
}
