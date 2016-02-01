package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 可选的基本类型的getter接口<br>
 * 提供一个统一的接口定义返回不同类型的值（基本类型）<br>
 * 如果值不存在或获取错误，返回默认值
 * @author Looly
 */
public interface BasicTypeGetter {
	/*-------------------------- 基本类型 start -------------------------------*/
	
	/**
	 * 获取Object属性值
	 * @param key 属性名
	 * @return 属性值
	 */
	Object getObj(String key);
	
	/**
	 * 获取字符串型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	String getStr(String key);
	
	/**
	 * 获取int型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Integer getInt(String key);
	
	/**
	 * 获取short型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Short getShort(String key);
	
	/**
	 * 获取boolean型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Boolean getBool(String key);
	
	/**
	 * 获取long型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Long getLong(String key);
	
	/**
	 * 获取char型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Character getChar(String key);
	
	/**
	 * 获取float型属性值<br>
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Float getFloat(String key);
	
	/**
	 * 获取double型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Double getDouble(String key);
	
	/**
	 * 获取byte型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	Byte getByte(String key);
	
	/**
	 * 获取BigDecimal型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	BigDecimal getBigDecimal(String key);
	
	/**
	 * 获取BigInteger型属性值
	 * 
	 * @param key 属性名
	 * @return 属性值
	 */
	BigInteger getBigInteger(String key);
	/*-------------------------- 基本类型 end -------------------------------*/
}
