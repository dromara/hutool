package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 可选默认值的基本类型的getter接口<br>
 * 提供一个统一的接口定义返回不同类型的值（基本类型）<br>
 * 如果值不存在或获取错误，返回默认值
 * @author Looly
 */
public interface OptBasicTypeGetter<K> {
	/*-------------------------- 基本类型 start -------------------------------*/

	/**
	 * 获取Object属性值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Object getObj(K key, Object defaultValue);

	/**
	 * 获取字符串型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	String getStr(K key, String defaultValue);

	/**
	 * 获取int型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Integer getInt(K key, Integer defaultValue);

	/**
	 * 获取short型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Short getShort(K key, Short defaultValue);

	/**
	 * 获取boolean型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Boolean getBool(K key, Boolean defaultValue);

	/**
	 * 获取Long型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Long getLong(K key, Long defaultValue);

	/**
	 * 获取char型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Character getChar(K key, Character defaultValue);

	/**
	 * 获取float型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Float getFloat(K key, Float defaultValue);

	/**
	 * 获取double型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Double getDouble(K key, Double defaultValue);

	/**
	 * 获取byte型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Byte getByte(K key, Byte defaultValue);

	/**
	 * 获取BigDecimal型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	BigDecimal getBigDecimal(K key, BigDecimal defaultValue);

	/**
	 * 获取BigInteger型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	BigInteger getBigInteger(K key, BigInteger defaultValue);

	/**
	 * 获得Enum类型的值
	 *
	 * @param <E> 枚举类型
	 * @param clazz Enum的Class
	 * @param key KEY
	 * @param defaultValue 默认值
	 * @return Enum类型的值，无则返回Null
	 */
	<E extends Enum<E>> E getEnum(Class<E> clazz, K key, E defaultValue);

	/**
	 * 获取Date类型值
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return Date类型属性值
	 */
	Date getDate(K key, Date defaultValue);
	/*-------------------------- 基本类型 end -------------------------------*/
}
