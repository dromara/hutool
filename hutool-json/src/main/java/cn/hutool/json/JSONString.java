package cn.hutool.json;

/**
 * {@code JSONString}接口定义了一个{@code toJSONString()}<br>
 * 实现此接口的类可以通过实现{@code toJSONString()}方法来改变转JSON字符串的方式。
 *
 * @author Looly
 *
 */
public interface JSONString {

	/**
	 * 自定义转JSON字符串的方法
	 *
	 * @return JSON字符串
	 */
	String toJSONString();
}
