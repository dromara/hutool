package com.xiaoleilu.hutool.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.getter.BasicTypeGetter;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 1、字典对象，扩充了HashMap中的方法
 * @author loolly
 *
 */
public class Dict extends HashMap<String, Object> implements BasicTypeGetter<String>{
	private static final long serialVersionUID = 6135423866861206530L;

	//--------------------------------------------------------------- Static method start
	/**
	 * 创建Dict
	 * @return Dict
	 */
	public static Dict create() {
		return new Dict();
	}
	
	/**
	 * 将PO对象转为Dict
	 * @param <T>
	 * @param bean Bean对象
	 * @return Vo
	 */
	public static <T> Dict parse(T bean) {
		return create().parseBean(bean);
	}
	//--------------------------------------------------------------- Static method end
	
	//--------------------------------------------------------------- Constructor start
	public Dict() {
	}
	//--------------------------------------------------------------- Constructor end
	
	/**
	 * 转换为Bean对象
	 * @param <T>
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBean(T bean) {
		BeanUtil.fillBeanWithMap(this, bean);
		return bean;
	}
	
	/**
	 * 填充Value Object对象
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBean(Class<T> clazz) {
		return BeanUtil.mapToBean(this, clazz);
	}
	
	/**
	 * 填充Value Object对象，忽略大小写
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBeanIgnoreCase(Class<T> clazz) {
		return BeanUtil.mapToBeanIgnoreCase(this, clazz);
	}
	
	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 * @param <T>
	 * @param bean 值对象
	 * @return 自己
	 */
	public <T> Dict parseBean(T bean) {
		this.putAll(BeanUtil.beanToMap(bean));
		return this;
	}
	
	/**
	 * 与给定实体对比并去除相同的部分<br>
	 * 此方法用于在更新操作时避免所有字段被更新，跳过不需要更新的字段
	 * version from 2.0.0
	 * @param dict
	 * @param withoutNames 不需要去除的字段名
	 */
	public <T extends Dict> void removeEqual(T dict, String... withoutNames) {
		HashSet<String> withoutSet = CollectionUtil.newHashSet(withoutNames);
		for(Entry<String, Object> entry : dict.entrySet()) {
			if(withoutSet.contains(entry.getKey())) {
				continue;
			}
			
			final Object value = this.get(entry.getKey());
			if(null != value && value.equals(entry.getValue())) {
				this.remove(entry.getKey());
			}
		}
	}
	
	//-------------------------------------------------------------------- Set start
	/**
	 * 设置列
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict set(String attr, Object value) {
		this.put(attr, value);
		return this;
	}
	
	/**
	 * 设置列，当键或值为null时忽略
	 * @param attr 属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict setIgnoreNull(String attr, Object value) {
		if(null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	//-------------------------------------------------------------------- Set end
	
	//-------------------------------------------------------------------- Get start
	
	@Override
	public Object getObj(String key) {
		return get(key);
	}
	
	/**
	 * 获得特定类型值
	 * @param attr 字段名
	 * @param defaultValue 默认值
	 * @return 字段值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T)(result != null ? result : defaultValue);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public String getStr(String attr) {
		return Conver.toStr(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Integer getInt(String attr) {
		return Conver.toInt(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Long getLong(String attr) {
		return Conver.toLong(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Float getFloat(String attr) {
		return Conver.toFloat(get(attr), null);
	}
	
	@Override
	public Short getShort(String attr) {
		return Conver.toShort(get(attr), null);
	}

	@Override
	public Character getChar(String attr) {
		return Conver.toChar(get(attr), null);
	}

	@Override
	public Double getDouble(String attr) {
		return Conver.toDouble(get(attr), null);
	}

	@Override
	public Byte getByte(String attr) {
		return Conver.toByte(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Boolean getBool(String attr) {
		return Conver.toBool(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public byte[] getBytes(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Date getDate(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Time getTime(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Number getNumber(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public BigDecimal getBigDecimal(String attr) {
		return get(attr, null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public BigInteger getBigInteger(String attr) {
		return get(attr, null);
	}
	//-------------------------------------------------------------------- Get end
	
	
	@Override
	public Dict clone() {
		return (Dict) super.clone();
	}
}
