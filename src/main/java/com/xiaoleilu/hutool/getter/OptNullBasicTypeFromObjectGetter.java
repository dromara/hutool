package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.xiaoleilu.hutool.lang.Conver;

/**
 * 基本类型的getter接口抽象实现，所有类型的值获取都是通过将getObj获得的值转换而来<br>
 * 用户只需实现getStr方法即可，其他类型将会从String结果中转换
 * 在不提供默认值的情况下， 如果值不存在或获取错误，返回null<br>
 * @author Looly
 */
public abstract class OptNullBasicTypeFromObjectGetter<K> extends OptNullBasicTypeGetter<K>{
	
	@Override
	public abstract Object getObj(K key, Object defaultValue);
	
	@Override
	public String getStr(K key, String defaultValue) {
		return Conver.toStr(getObj(key), defaultValue);
	}
	
	@Override
	public Integer getInt(K key, Integer defaultValue) {
		return Conver.toInt(getObj(key), defaultValue);
	}

	@Override
	public Short getShort(K key, Short defaultValue) {
		return Conver.toShort(getObj(key), defaultValue);
	}

	@Override
	public Boolean getBool(K key, Boolean defaultValue) {
		return Conver.toBool(getObj(key), defaultValue);
	}

	@Override
	public Long getLong(K key, Long defaultValue) {
		return Conver.toLong(getObj(key), defaultValue);
	}

	@Override
	public Character getChar(K key, Character defaultValue) {
		return Conver.toChar(getObj(key), defaultValue);
	}
	
	@Override
	public Float getFloat(K key, Float defaultValue) {
		return Conver.toFloat(getObj(key), defaultValue);
	}

	@Override
	public Double getDouble(K key, Double defaultValue) {
		return Conver.toDouble(getObj(key), defaultValue);
	}

	@Override
	public Byte getByte(K key, Byte defaultValue) {
		return Conver.toByte(getObj(key), defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
		return Conver.toBigDecimal(getObj(key), defaultValue);
	}

	@Override
	public BigInteger getBigInteger(K key, BigInteger defaultValue) {
		return Conver.toBigInteger(getObj(key), defaultValue);
	}
}
