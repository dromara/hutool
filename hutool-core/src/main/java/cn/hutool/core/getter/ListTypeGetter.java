package cn.hutool.core.getter;

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
	/**
	 * 获取Object型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Object> getObjList(String key);

	/**
	 * 获取String型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<String> getStrList(String key);
	
	/**
	 * 获取Integer型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Integer> getIntList(String key);
	
	/**
	 * 获取Short型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Short> getShortList(String key);
	
	/**
	 * 获取Boolean型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Boolean> getBoolList(String key);
	
	/**
	 * 获取BigDecimal型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Long> getLongList(String key);
	
	/**
	 * 获取Character型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Character> getCharList(String key);
	
	/**
	 * 获取Double型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
	List<Double> getDoubleList(String key);
	
	/**
	 * 获取Byte型属性值列表
	 * 
	 * @param key 属性名
	 * @return 属性值列表
	 */
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
