package cn.hutool.core.reflect;

import java.lang.reflect.Type;

/**
 * 空类型表示
 *
 * @author looly
 * @since 6.0.0
 */
public class NullType implements Type {
	/**
	 * 单例对象
	 */
	public static NullType INSTANCE = new NullType();

	private NullType(){}

	@Override
	public String toString() {
		return "Type of null";
	}
}
