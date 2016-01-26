package com.xiaoleilu.hutool.getter;

import java.util.List;

/**
 * 列表类型的Get接口
 * @author Looly
 *
 */
public interface ListTypeGetter {
	/*-------------------------- List类型 start -------------------------------*/
	List<String> getStrList(String key);
	
	List<Integer> getIntList(String key);
	
	List<Short> getShortList(String key);
	
	List<Boolean> getBoolList(String key);
	
	List<Long> getLongList(String key);
	
	List<Character> getCharList(String key);
	
	List<Double> getDoubleList(String key);
	
	List<Byte> getByteList(String key);
	/*-------------------------- List类型 end -------------------------------*/
}
