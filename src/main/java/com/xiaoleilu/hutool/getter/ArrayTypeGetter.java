package com.xiaoleilu.hutool.getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数组类型的Get接口
 * @author Looly
 *
 */
public interface ArrayTypeGetter {
	/*-------------------------- 数组类型 start -------------------------------*/
	String[] getObjs(String key);
	
	String[] getStrs(String key);
	
	Integer[] getInts(String key);
	
	Short[] getShorts(String key);
	
	Boolean[] getBools(String key);
	
	Long[] getLongs(String key);
	
	Character[] getChars(String key);
	
	Double[] getDoubles(String key);
	
	Byte[] getBytes(String key);
	
	BigInteger[] getBigIntegers(String key);
	
	BigDecimal[] getBigDecimals(String key);
	/*-------------------------- 数组类型 end -------------------------------*/	
}
