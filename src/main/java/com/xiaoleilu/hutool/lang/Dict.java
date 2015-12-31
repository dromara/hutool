package com.xiaoleilu.hutool.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.Conver;
import com.xiaoleilu.hutool.InjectUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 1、字典对象，扩充了HashMap中的方法
 * @author loolly
 *
 */
public class Dict extends HashMap<String, Object>{
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
	 * @param vo 值对象
	 * @return Vo
	 */
	public static <T> Dict parse(T vo) {
		return create().fromVo(vo);
	}
	//--------------------------------------------------------------- Static method end
	
	//--------------------------------------------------------------- Constructor start
	public Dict() {
	}
	//--------------------------------------------------------------- Constructor end
	
	//--------------------------------------------------------------- Getters and Setters start
	//--------------------------------------------------------------- Getters and Setters end
	
	/**
	 * 填充Value Object对象
	 * @param <T>
	 * @param vo Value Object（或者POJO）
	 * @return Dict
	 */
	public <T> Dict fillVo(T vo) {
		InjectUtil.injectFromMap(vo, this);
		return this;
	}
	
	/**
	 * 填充Value Object对象
	 * @param clazz Value Object（或者POJO）的类
	 * @param ignoreCase 是否忽略大小写
	 * @return vo
	 */
	public <T> T toVo(Class<T> clazz, boolean ignoreCase) {
		if(clazz == null) {
			throw new NullPointerException("Provided Class is null!");
		}
		T vo;
		try {
			vo = clazz.newInstance();
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Instance Value Object [] error!", clazz.getName()));
		}
		
		if(ignoreCase){
			InjectUtil.injectFromMapIgnoreCase(vo, this);
		}else{
			InjectUtil.injectFromMap(vo, this);
		}
		
		return vo;
	}
	
	/**
	 * 填充Value Object对象
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toVo(Class<T> clazz) {
		return toVo(clazz, false);
	}
	
	/**
	 * 填充Value Object对象，忽略大小写
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toVoIgnoreCase(Class<T> clazz) {
		return toVo(clazz, true);
	}
	
	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 * @param <T>
	 * @param vo 值对象
	 * @return 自己
	 */
	public <T> Dict fromVo(T vo) {
		this.putAll(InjectUtil.toMap(vo, false));
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
		HashSet<String> withoutSet = new HashSet<String>();
		for (String name : withoutNames) {
			withoutSet.add(name);
		}
		
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
	
	//-------------------------------------------------------------------- 特定类型值
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
	public String getStr(String attr) {
		return Conver.toStr(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Integer getInt(String attr) {
		return Conver.toInt(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Long getLong(String attr) {
		return Conver.toLong(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Float getFloat(String attr) {
		return Conver.toFloat(get(attr), null);
	}
	
	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
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
	
	//-------------------------------------------------------------------- 特定类型值
	
	
	@Override
	public Dict clone() {
		return (Dict) super.clone();
	}
	//-------------------------------------------------------------------- 特定类型值
}
