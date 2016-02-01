package com.xiaoleilu.hutool.json;

/**
 * JSONObject.NULL is equivalent to the value that JavaScript calls null,<br>
 * whilst Java's null is equivalent to the value that JavaScript calls undefined.
 */
public class JSONNull {
	
	/**
	 * <code>NULL</code> 对象用于减少歧义来表示Java 中的<code>null</code> <br>
	 * <code>NULL.equals(null)</code> 返回 <code>true</code>. <br>
	 * <code>NULL.toString()</code> 返回 <code>"null"</code>.
	 */
	public static final JSONNull NULL = new JSONNull();

	/**
	 * There is only intended to be a single instance of the NULL object, so the clone method returns itself.
	 *
	 * @return NULL.
	 */
	@Override
	protected final Object clone() {
		return this;
	}

	/**
	 * A Null object is equal to the null value and to itself.
	 *
	 * @param object An object to test for nullness.
	 * @return true if the object parameter is the JSONObject.NULL object or null.
	 */
	@Override
	public boolean equals(Object object) {
		return object == null || object == this;
	}

	/**
	 * Get the "null" string value.
	 *
	 * @return The string "null".
	 */
	public String toString() {
		return "null";
	}
}