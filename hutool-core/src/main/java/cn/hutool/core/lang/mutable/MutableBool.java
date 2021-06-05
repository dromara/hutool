package cn.hutool.core.lang.mutable;

import java.io.Serializable;

/**
 * 可变 <code>boolean</code> 类型
 * 
 * @see Boolean
 * @since 3.0.1
 */
public class MutableBool implements Comparable<MutableBool>, Mutable<Boolean>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean value;

	/**
	 * 构造，默认值0
	 */
	public MutableBool() {
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableBool(final boolean value) {
		this.value = value;
	}

	/**
	 * 构造
	 * @param value String值
	 * @throws NumberFormatException 转为Boolean错误
	 */
	public MutableBool(final String value) throws NumberFormatException {
		this.value = Boolean.parseBoolean(value);
	}

	@Override
	public Boolean get() {
		return this.value;
	}

	/**
	 * 设置值
	 * @param value 值
	 */
	public void set(final boolean value) {
		this.value = value;
	}

	@Override
	public void set(final Boolean value) {
		this.value = value.booleanValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * 相等需同时满足如下条件：
	 * <ol>
	 * 	<li>非空</li>
	 * 	<li>类型为 {@link MutableBool}</li>
	 * 	<li>值相等</li>
	 * </ol>
	 * 
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 <code>false</code>
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableBool) {
			return value == ((MutableBool) obj).value;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
	}

	// -----------------------------------------------------------------------
	/**
	 * 比较
	 * 
	 * @param other 其它 {@link MutableBool} 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableBool other) {
		return Boolean.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
