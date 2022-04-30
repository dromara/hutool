package cn.hutool.core.lang.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import cn.hutool.core.convert.Convert;

/**
 * 基本类型的getter接口抽象实现，所有类型的值获取都是通过将String转换而来<br>
 * 用户只需实现getStr方法即可，其他类型将会从String结果中转换 在不提供默认值的情况下， 如果值不存在或获取错误，返回null<br>
 *
 * @author Looly
 */
public interface OptNullBasicTypeFromStringGetter<K> extends OptNullBasicTypeGetter<K> {
	@Override
	default Object getObj(final K key, final Object defaultValue) {
		return getStr(key, null == defaultValue ? null : defaultValue.toString());
	}

	@Override
	default Integer getInt(final K key, final Integer defaultValue) {
		return Convert.toInt(getStr(key), defaultValue);
	}

	@Override
	default Short getShort(final K key, final Short defaultValue) {
		return Convert.toShort(getStr(key), defaultValue);
	}

	@Override
	default Boolean getBool(final K key, final Boolean defaultValue) {
		return Convert.toBool(getStr(key), defaultValue);
	}

	@Override
	default Long getLong(final K key, final Long defaultValue) {
		return Convert.toLong(getStr(key), defaultValue);
	}

	@Override
	default Character getChar(final K key, final Character defaultValue) {
		return Convert.toChar(getStr(key), defaultValue);
	}

	@Override
	default Float getFloat(final K key, final Float defaultValue) {
		return Convert.toFloat(getStr(key), defaultValue);
	}

	@Override
	default Double getDouble(final K key, final Double defaultValue) {
		return Convert.toDouble(getStr(key), defaultValue);
	}

	@Override
	default Byte getByte(final K key, final Byte defaultValue) {
		return Convert.toByte(getStr(key), defaultValue);
	}

	@Override
	default BigDecimal getBigDecimal(final K key, final BigDecimal defaultValue) {
		return Convert.toBigDecimal(getStr(key), defaultValue);
	}

	@Override
	default BigInteger getBigInteger(final K key, final BigInteger defaultValue) {
		return Convert.toBigInteger(getStr(key), defaultValue);
	}

	@Override
	default <E extends Enum<E>> E getEnum(final Class<E> clazz, final K key, final E defaultValue) {
		return Convert.toEnum(clazz, getStr(key), defaultValue);
	}

	@Override
	default Date getDate(final K key, final Date defaultValue) {
		return Convert.toDate(getStr(key), defaultValue);
	}
}
