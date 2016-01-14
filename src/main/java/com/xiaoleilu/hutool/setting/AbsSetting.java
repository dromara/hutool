package com.xiaoleilu.hutool.setting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;

import com.xiaoleilu.hutool.lang.Conver;
import com.xiaoleilu.hutool.setting.getter.IBasicGetter;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 设定抽象类
 * @author Looly
 *
 */
public abstract class AbsSetting implements IBasicGetter{
	
	/** 本设置对象的字符集 */
	protected Charset charset;
	/** 是否使用变量 */
	protected boolean isUseVariable;
	/** 设定文件的URL */
	protected URL settingUrl;

	/**
	 * @return 配置文件大小（key的个数）
	 */
	public abstract int size();

	/**
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public abstract String getStr(String key);

	@Override
	public String getStr(String key, String defaultValue) {
		final String value = getStr(key);
		if(StrUtil.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	@Override
	public Integer getInt(String key) {
		return getInt(key, null);
	}

	@Override
	public Integer getInt(String key, Integer defaultValue) {
		return Conver.toInt(getStr(key), defaultValue);
	}

	@Override
	public Short getShort(String key) {
		return getShort(key, null);
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		return Conver.toShort(getStr(key), defaultValue);
	}

	@Override
	public Boolean getBool(String key) {
		return getBool(key, null);
	}

	@Override
	public Boolean getBool(String key, Boolean defaultValue) {
		return Conver.toBool(getStr(key), defaultValue);
	}

	@Override
	public Long getLong(String key) {
		return getLong(key, null);
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		return Conver.toLong(getStr(key), defaultValue);
	}

	@Override
	public Character getChar(String key) {
		return getChar(key, null);
	}

	@Override
	public Character getChar(String key, Character defaultValue) {
		return Conver.toChar(getStr(key), defaultValue);
	}

	@Override
	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		return Conver.toDouble(getStr(key), defaultValue);
	}

	@Override
	public Byte getByte(String key) {
		return getByte(key, null);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return Conver.toByte(getStr(key), defaultValue);
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(key, null);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		final String valueStr = getStr(key);
		if(StrUtil.isBlank(valueStr)) {
			return defaultValue;
		}
		
		try {
			return new BigDecimal(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		final String valueStr = getStr(key);
		if(StrUtil.isBlank(valueStr)) {
			return defaultValue;
		}
		
		try {
			return new BigInteger(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
}
