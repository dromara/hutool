package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 列表类型的Get接口
 * @author Looly
 *
 */
public interface ListTypeGetter {
	/*-------------------------- List类型 start -------------------------------*/
	List<Object> getObjList(String key);
	
	List<String> getStrList(String key);
	
	List<Integer> getIntList(String key);
	
	List<Short> getShortList(String key);
	
	List<Boolean> getBoolList(String key);
	
	List<Long> getLongList(String key);
	
	List<Character> getCharList(String key);
	
	List<Double> getDoubleList(String key);
	
	List<Byte> getByteList(String key);
	
	/**
	 * 获取BigDecimal型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<BigDecimal> getBigDecimalList(String key);
	
	/**
	 * 获取BigInteger型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<BigInteger> getBigIntegerList(String key);
	/*-------------------------- List类型 end -------------------------------*/
}
