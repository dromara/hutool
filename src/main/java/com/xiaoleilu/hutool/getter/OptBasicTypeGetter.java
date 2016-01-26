package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 可选的基本类型的getter接口<br>
 * 提供一个统一的接口定义返回不同类型的值（基本类型）<br>
 * 如果值不存在或获取错误，返回默认值
 * @author Looly
 */
public interface OptBasicTypeGetter {
	/*-------------------------- 基本类型 start -------------------------------*/
	/**
	 * 获取字符串型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	String getStr(String key, String defaultValue);
	
	/**
	 * 获取int型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Integer getInt(String key, Integer defaultValue);
	
	/**
	 * 获取short型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Short getShort(String key, Short defaultValue);
	
	/**
	 * 获取boolean型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Boolean getBool(String key, Boolean defaultValue);
	
	/**
	 * 获取Long型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Long getLong(String key, Long defaultValue);
	
	/**
	 * 获取char型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Character getChar(String key, Character defaultValue);
	
	/**
	 * 获取float型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Float getFloat(String key, Float defaultValue);
	
	/**
	 * 获取double型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Double getDouble(String key, Double defaultValue);
	
	/**
	 * 获取byte型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	Byte getByte(String key, Byte defaultValue);
	
	/**
	 * 获取BigDecimal型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	BigDecimal getBigDecimal(String key, BigDecimal defaultValue);
	
	/**
	 * 获取BigInteger型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	BigInteger getBigInteger(String key, BigInteger defaultValue);
	
	/*-------------------------- 基本类型 end -------------------------------*/
}
