package com.xiaoleilu.hutool.setting;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 基础类型设定接口
 * @author Looly
 *
 */
interface IGroupSetting {
	/*-------------------------- 基本类型 start -------------------------------*/
	/**
	 * 获取字符串型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	String getStr(String key);
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
	 * 获取int型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Integer getInt(String key);
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
	 * 获取short型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Short getShort(String key);
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
	 * 获取boolean型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Boolean getBool(String key);
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
	 * 获取long型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Long getLong(String key);
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
	 * 获取char型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Character getChar(String key);
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
	 * 获取double型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Double getDouble(String key);
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
	 * 获取byte型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Byte getByte(String key);
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
	 * 获取BigDecimal型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	BigDecimal getBigDecimal(String key);
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
	 * 获取BigInteger型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	BigInteger getBigInteger(String key);
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
