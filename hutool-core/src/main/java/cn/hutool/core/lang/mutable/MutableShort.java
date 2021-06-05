package cn.hutool.core.lang.mutable;

import cn.hutool.core.util.NumberUtil;

/**
 * 可变 <code>short</code> 类型
 * 
 * @see Short
 * @since 3.0.1
 */
public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
	private static final long serialVersionUID = 1L;
	
	private short value;

	/**
	 * 构造，默认值0
	 */
	public MutableShort() {
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableShort(final short value) {
		this.value = value;
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableShort(final Number value) {
		this(value.shortValue());
	}

	/**
	 * 构造
	 * @param value String值
	 * @throws NumberFormatException 转为Short错误
	 */
	public MutableShort(final String value) throws NumberFormatException {
		this.value = Short.parseShort(value);
	}

	@Override
	public Short get() {
		return this.value;
	}

	/**
	 * 设置值
	 * @param value 值
	 */
	public void set(final short value) {
		this.value = value;
	}

	@Override
	public void set(final Number value) {
		this.value = value.shortValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * 值+1
	 * @return this
	 */
	public MutableShort increment() {
		value++;
		return this;
	}

	/**
	 * 值减一
	 * @return this
	 */
	public MutableShort decrement() {
		value--;
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * 增加值
	 * @param operand 被增加的值
	 * @return this
	 */
	public MutableShort add(final short operand) {
		this.value += operand;
		return this;
	}

	/**
	 * 增加值
	 * @param operand 被增加的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableShort add(final Number operand) {
		this.value += operand.shortValue();
		return this;
	}

	/**
	 * 减去值
	 * 
	 * @param operand 被减的值
	 * @return this
	 */
	public MutableShort subtract(final short operand) {
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
	public MutableShort subtract(final Number operand) {
		this.value -= operand.shortValue();
		return this;
	}

	// -----------------------------------------------------------------------
	@Override
	public short shortValue() {
		return value;
	}

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
	 * 	<li>类型为 {@link MutableShort}</li>
	 * 	<li>值相等</li>
	 * </ol>
	 * 
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 <code>false</code>
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableShort) {
			return value == ((MutableShort) obj).shortValue();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value;
	}

	// -----------------------------------------------------------------------
	/**
	 * 比较
	 * 
	 * @param other 其它 {@link MutableShort} 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableShort other) {
		return NumberUtil.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
