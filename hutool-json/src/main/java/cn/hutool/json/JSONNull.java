package cn.hutool.json;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * 用于定义{@code null}，与Javascript中null相对应<br>
 * Java中的{@code null}值在js中表示为undefined。
 * @author Looly
 *
 */
public class JSONNull implements Serializable{
	private static final long serialVersionUID = 2633815155870764938L;

	/**
	 * {@code NULL} 对象用于减少歧义来表示Java 中的{@code null} <br>
	 * {@code NULL.equals(null)} 返回 {@code true}. <br>
	 * {@code NULL.toString()} 返回 {@code "null"}.
	 */
	public static final JSONNull NULL = new JSONNull();

	/**
	 * A Null object is equal to the null value and to itself.
	 * 对象与其本身和{@code null}值相等
	 *
	 * @param object An object to test for nullness.
	 * @return true if the object parameter is the JSONObject.NULL object or null.
	 */
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object object) {
		return object == null || (object == this);
	}

	/**
	 * Get the "null" string value.
	 *获得“null”字符串
	 *
	 * @return The string "null".
	 */
	@Override
	public String toString() {
		return StrUtil.NULL;
	}
}
