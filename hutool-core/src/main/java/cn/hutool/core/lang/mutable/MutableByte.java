package cn.hutool.core.lang.mutable;

import cn.hutool.core.util.NumberUtil;

/**
 * 可变 <code>byte</code> 类型
 * 
 * @see Byte
 * @since 3.0.1
 */
public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {
	private static final long serialVersionUID = 1L;
	
	private byte value;

	/**
	 * 构造，默认值0
	 */
	public MutableByte() {
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableByte(final byte value) {
		this.value = value;
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableByte(final Number value) {
		this(value.byteValue());
	}

	/**
	 * 构造
	 * @param value String值
	 * @throws NumberFormatException 转为Byte错误
	 */
	public MutableByte(final String value) throws NumberFormatException {
		this.value = Byte.parseByte(value);
	}

	@Override
	public Byte get() {
		return Byte.valueOf(this.value);
	}

	/**
	 * 设置值
	 * @param value 值
	 */
	public void set(final byte value) {
		this.value = value;
	}

	@Override
	public void set(final Number value) {
		this.value = value.byteValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * 值+1
	 * @return this
	 */
	public MutableByte increment() {
		value++;
		return this;
	}

	/**
	 * 值减一
	 * @return this
	 */
	public MutableByte decrement() {
		value--;
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * 增加值
	 * @param operand 被增加的值
	 * @return this
	 */
	public MutableByte add(final byte operand) {
		this.value += operand;
		return this;
	}

	/**
	 * 增加值
	 * @param operand 被增加的值，非空
	 * @return this
	 * @throws NullPointerException if the object is null
	 */
	public MutableByte add(final Number operand) {
		this.value += operand.byteValue();
		return this;
	}

	/**
	 * 减去值
	 * 
	 * @param operand 被减的值
	 * @return this
	 */
	public MutableByte subtract(final byte operand) {
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
	public MutableByte subtract(final Number operand) {
		this.value -= operand.byteValue();
		return this;
	}

	// -----------------------------------------------------------------------
	@Override
	public byte byteValue() {
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
	 * 	<li>类型为 {@link MutableByte}</li>
	 * 	<li>值相等</li>
	 * </ol>
	 * 
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 <code>false</code>
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableByte) {
			return value == ((MutableByte) obj).byteValue();
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
	 * @param other 其它 {@link MutableByte} 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableByte other) {
		return NumberUtil.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
