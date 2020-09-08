package cn.hutool.core.builder;

import java.io.Serializable;

/**
 * 包装唯一键（System.identityHashCode()）使对象只有和自己 equals
 *
 * 此对象用于消除小概率下System.identityHashCode()产生的ID重复问题。
 *
 * 来自于Apache-Commons-Lang3
 * @author looly，Apache-Commons
 * @since 4.2.2
 */
final class IDKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Object value;
	private final int id;

	/**
	 * 构造
	 * 
	 * @param obj 计算唯一ID的对象
	 */
	public IDKey(final Object obj) {
		id = System.identityHashCode(obj);
		// There have been some cases (LANG-459) that return the
		// same identity hash code for different objects. So
		// the value is also added to disambiguate these cases.
		value = obj;
	}

	/**
	 * returns hashcode - i.e. the system identity hashcode.
	 * 
	 * @return the hashcode
	 */
	@Override
	public int hashCode() {
		return id;
	}

	/**
	 * checks if instances are equal
	 * 
	 * @param other The other object to compare to
	 * @return if the instances are for the same object
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof IDKey)) {
			return false;
		}
		final IDKey idKey = (IDKey) other;
		if (id != idKey.id) {
			return false;
		}
		// Note that identity equals is used.
		return value == idKey.value;
	}
}
