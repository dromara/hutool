package cn.hutool.json;

/**
 * <code>JSONString</code>接口定义了一个<code>toJSONString()</code><br>
 * 实现此接口的类可以通过实现<code>toJSONString()</code>方法来改变转JSON字符串的方式。
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
	public String toJSONString();
}
