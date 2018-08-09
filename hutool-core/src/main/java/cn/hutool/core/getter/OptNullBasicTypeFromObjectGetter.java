package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import cn.hutool.core.convert.Convert;

/**
 * 基本类型的getter接口抽象实现，所有类型的值获取都是通过将getObj获得的值转换而来<br>
 * 用户只需实现getObj方法即可，其他类型将会从Object结果中转换
 * 在不提供默认值的情况下， 如果值不存在或获取错误，返回null<br>
 * @author Looly
 */
public abstract class OptNullBasicTypeFromObjectGetter<K> extends OptNullBasicTypeGetter<K>{
	
	@Override
	public abstract Object getObj(K key, Object defaultValue);
	
	@Override
	public String getStr(K key, String defaultValue) {
		return Convert.toStr(getObj(key), defaultValue);
	}
	
	@Override
	public Integer getInt(K key, Integer defaultValue) {
		return Convert.toInt(getObj(key), defaultValue);
	}

	@Override
	public Short getShort(K key, Short defaultValue) {
		return Convert.toShort(getObj(key), defaultValue);
	}

	@Override
	public Boolean getBool(K key, Boolean defaultValue) {
		return Convert.toBool(getObj(key), defaultValue);
	}

	@Override
	public Long getLong(K key, Long defaultValue) {
		return Convert.toLong(getObj(key), defaultValue);
	}

	@Override
	public Character getChar(K key, Character defaultValue) {
		return Convert.toChar(getObj(key), defaultValue);
	}
	
	@Override
	public Float getFloat(K key, Float defaultValue) {
		return Convert.toFloat(getObj(key), defaultValue);
	}

	@Override
	public Double getDouble(K key, Double defaultValue) {
		return Convert.toDouble(getObj(key), defaultValue);
	}

	@Override
	public Byte getByte(K key, Byte defaultValue) {
		return Convert.toByte(getObj(key), defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
		return Convert.toBigDecimal(getObj(key), defaultValue);
	}

	@Override
	public BigInteger getBigInteger(K key, BigInteger defaultValue) {
		return Convert.toBigInteger(getObj(key), defaultValue);
	}
	
	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, K key, E defaultValue) {
		return Convert.toEnum(clazz, getObj(key), defaultValue);
	}
	
	@Override
	public Date getDate(K key, Date defaultValue) {
		return Convert.toDate(getObj(key), defaultValue);
	}
}
