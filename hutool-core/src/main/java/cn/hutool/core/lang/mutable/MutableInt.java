package cn.hutool.core.lang.mutable;

import cn.hutool.core.util.NumberUtil;

/**
 * 可变 <code>int</code> 类型
 * 
 * @see Integer
 * @since 3.0.1
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {
	private static final long serialVersionUID = 1L;
	
	private int value;

	/**
	 * 构造，默认值0
	 */
	public MutableInt() {
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableInt(final int value) {
		this.value = value;
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableInt(final Number value) {
		this(value.intValue());
	}

	/**
	 * 构造
	 * @param value String值
	 * @throws NumberFormatException 数字转换错误
	 */
	public MutableInt(final String value) throws NumberFormatException {
		this.value = Integer.parseInt(value);
	}

	@Override
	public Integer get() {
		return Integer.valueOf(this.value);
	}

	/**
	 * 设置值
	 * @param value 值
	 */
	public void set(final int value) {
		this.value = value;
	}

	@Override
	public void set(final Number value) {
		this.value = value.intValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * 值+1
	 * @return this
	 */
	public MutableInt increment() {
		value++;
		return this;
	}

	/**
	 * 值减一
	 * @return this
	 */
	public MutableInt decrement() {
		value--;
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * 增加值
	 * @param operand 被增加的值
	 * @return this
	 */
	public MutableInt add(final int operand) {
		this.value += operand;
		return this;
	}

	/**
	 * 增加值
	 * @param operand 被增加的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableInt add(final Number operand) {
		this.value += operand.intValue();
		return this;
	}

	/**
	 * 减去值
	 * 
	 * @param operand 被减的值
	 * @return this
	 */
	public MutableInt subtract(final int operand) {
		this.value -= operand;
		return this;
	}

	/**
	 * 减去值
	 * 
	 * @param operand 被减的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableInt subtract(final Number operand) {
		this.value -= operand.intValue();
		return this;
	}

	// -----------------------------------------------------------------------
	@Override
	public int intValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	// -----------------------------------------------------------------------
	/**
	 * 相等需同时满足如下条件：
	 * <ol>
	 * 	<li>非空</li>
	 * 	<li>类型为 {@link MutableInt}</li>
	 * 	<li>值相等</li>
	 * </ol>
	 * 
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 <code>false</code>
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableInt) {
			return value == ((MutableInt) obj).intValue();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.value;
	}

	// -----------------------------------------------------------------------
	/**
	 * 比较
	 * 
	 * @param other 其它 {@link MutableInt} 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableInt other) {
		return NumberUtil.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
