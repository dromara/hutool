package cn.hutool.core.lang.getter;

import cn.hutool.core.convert.Convert;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 基本类型的getter接口<br>
 * 提供一个统一的接口定义返回不同类型的值（基本类型），定义类型包括：
 * <ul>
 *     <li>Object</li>
 *     <li>String</li>
 *     <li>Integer</li>
 *     <li>Short</li>
 *     <li>Boolean</li>
 *     <li>Long</li>
 *     <li>Character</li>
 *     <li>Float</li>
 *     <li>Double</li>
 *     <li>Byte</li>
 *     <li>BigDecimal</li>
 *     <li>BigInteger</li>
 *     <li>Enum</li>
 *     <li>Number</li>
 *     <li>Date</li>
 *     <li>java.sql.Time</li>
 *     <li>java.sql.Timestamp</li>
 *     <li>java.sql.Timestamp</li>
 *     <li>LocalDateTime</li>
 *     <li>LocalDate</li>
 *     <li>LocalTime</li>
 * </ul>
 * 通过实现此接口，最简单方式为通过实现{@link #getObj(Object, Object)}方法，完成所有类型的值获取，获取默认采用
 * {@link Convert}方式自动转换。如果有自定义实现，重写对应getXXX方法即可。
 *
 * @param <K> 键类型
 * @author Looly
 * @since 6.0.0
 */
public interface TypeGetter<K> {
	/*-------------------------- 基本类型 start -------------------------------*/

	/**
	 * 获取Object属性值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	Object getObj(K key, Object defaultValue);

	/**
	 * 获取Object属性值，最原始的对象获取，没有任何转换或类型判断
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Object getObj(final K key) {
		return getObj(key, null);
	}

	/**
	 * 获取指定类型的值，默认自动转换值类型
	 *
	 * @param <T>          目标类型
	 * @param key          键
	 * @param type         目标类型
	 * @return 结果值
	 */
	default <T> T get(final K key, final Type type) {
		return get(key, type, null);
	}

	/**
	 * 获取指定类型的值，默认自动转换值类型
	 *
	 * @param <T>          目标类型
	 * @param key          键
	 * @param type         目标类型
	 * @param defaultValue 默认值
	 * @return 结果值
	 */
	default <T> T get(final K key, final Type type, final T defaultValue) {
		return Convert.convert(type, getObj(key), defaultValue);
	}

	/**
	 * 获取字符串型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default String getStr(final K key, final String defaultValue) {
		return get(key, String.class, defaultValue);
	}

	/**
	 * 获取字符串型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default String getStr(final K key) {
		return getStr(key, null);
	}

	/**
	 * 获取int型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Integer getInt(final K key, final Integer defaultValue) {
		return get(key, Integer.class, defaultValue);
	}

	/**
	 * 获取int型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Integer getInt(final K key) {
		return getInt(key, null);
	}

	/**
	 * 获取short型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Short getShort(final K key, final Short defaultValue) {
		return get(key, Short.class, defaultValue);
	}

	/**
	 * 获取short型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Short getShort(final K key) {
		return getShort(key, null);
	}

	/**
	 * 获取boolean型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Boolean getBool(final K key, final Boolean defaultValue) {
		return get(key, Boolean.class, defaultValue);
	}

	/**
	 * 获取boolean型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Boolean getBool(final K key) {
		return getBool(key, null);
	}

	/**
	 * 获取Long型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Long getLong(final K key, final Long defaultValue) {
		return get(key, Long.class, defaultValue);
	}

	/**
	 * 获取long型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Long getLong(final K key) {
		return getLong(key, null);
	}

	/**
	 * 获取char型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Character getChar(final K key, final Character defaultValue) {
		return get(key, Character.class, defaultValue);
	}

	/**
	 * 获取char型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Character getChar(final K key) {
		return getChar(key, null);
	}

	/**
	 * 获取float型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Float getFloat(final K key, final Float defaultValue) {
		return get(key, Float.class, defaultValue);
	}

	/**
	 * 获取float型属性值<br>
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Float getFloat(final K key) {
		return getFloat(key, null);
	}

	/**
	 * 获取double型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Double getDouble(final K key, final Double defaultValue) {
		return get(key, Double.class, defaultValue);
	}

	/**
	 * 获取double型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Double getDouble(final K key) {
		return getDouble(key, null);
	}

	/**
	 * 获取byte型属性值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default Byte getByte(final K key, final Byte defaultValue) {
		return get(key, Byte.class, defaultValue);
	}

	/**
	 * 获取byte型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default Byte getByte(final K key) {
		return getByte(key, null);
	}

	/**
	 * 获取bytes型属性值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default byte[] getBytes(final K key, final byte[] defaultValue) {
		return get(key, byte[].class, defaultValue);
	}

	/**
	 * 获取bytes型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default byte[] getBytes(final K key) {
		return getBytes(key, null);
	}

	/**
	 * 获取BigDecimal型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default BigDecimal getBigDecimal(final K key, final BigDecimal defaultValue) {
		return get(key, BigDecimal.class, defaultValue);
	}

	/**
	 * 获取BigDecimal型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default BigDecimal getBigDecimal(final K key) {
		return getBigDecimal(key, null);
	}

	/**
	 * 获取BigInteger型属性值<br>
	 * 若获得的值为不可见字符，使用默认值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 属性值，无对应值返回defaultValue
	 */
	default BigInteger getBigInteger(final K key, final BigInteger defaultValue) {
		return get(key, BigInteger.class, defaultValue);
	}

	/**
	 * 获取BigInteger型属性值
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	default BigInteger getBigInteger(final K key) {
		return getBigInteger(key, null);
	}

	/**
	 * 获得Enum类型的值
	 *
	 * @param <E>          枚举类型
	 * @param clazz        Enum的Class
	 * @param key          KEY
	 * @param defaultValue 默认值
	 * @return Enum类型的值，无则返回Null
	 */
	default <E extends Enum<E>> E getEnum(final Class<E> clazz, final K key, final E defaultValue) {
		return get(key, clazz, defaultValue);
	}

	/**
	 * 获得Enum类型的值
	 *
	 * @param <E>   枚举类型
	 * @param clazz Enum的Class
	 * @param key   KEY
	 * @return Enum类型的值，无则返回Null
	 */
	default <E extends Enum<E>> E getEnum(final Class<E> clazz, final K key) {
		return getEnum(clazz, key, null);
	}

	/**
	 * 获取Number类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return Number类型属性值
	 */
	default Number getNumber(final K key, final Number defaultValue) {
		return get(key, Number.class, defaultValue);
	}

	/**
	 * 获取Number类型值
	 *
	 * @param key 属性名
	 * @return Number类型属性值
	 */
	default Number getNumber(final K key) {
		return getNumber(key, null);
	}

	/**
	 * 获取Date类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return Date类型属性值
	 */
	default Date getDate(final K key, final Date defaultValue) {
		return get(key, Date.class, defaultValue);
	}

	/**
	 * 获取Date类型值
	 *
	 * @param key 属性名
	 * @return Date类型属性值
	 */
	default Date getDate(final K key) {
		return getDate(key, null);
	}

	/**
	 * 获取LocalTime类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return LocalTime类型属性值
	 */
	default Time getSqlTime(final K key, final Time defaultValue) {
		return get(key, Time.class, defaultValue);
	}

	/**
	 * 获取Time类型值
	 *
	 * @param key 属性名
	 * @return Time类型属性值
	 */
	default Time getSqlTime(final K key) {
		return getSqlTime(key, null);
	}

	/**
	 * 获取Timestamp类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return Timestamp类型属性值
	 */
	default Timestamp getSqlTimestamp(final K key, final Timestamp defaultValue) {
		return get(key, Timestamp.class, defaultValue);
	}

	/**
	 * 获取Timestamp类型值
	 *
	 * @param key 属性名
	 * @return Timestamp类型属性值
	 */
	default Timestamp getSqlTimestamp(final K key) {
		return getSqlTimestamp(key, null);
	}

	/**
	 * 获取LocalDateTime类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return LocalDateTime类型属性值
	 */
	default LocalDateTime getLocalDateTime(final K key, final LocalDateTime defaultValue) {
		return get(key, LocalDateTime.class, defaultValue);
	}

	/**
	 * 获取LocalDateTime类型值
	 *
	 * @param key 属性名
	 * @return LocalDateTime类型属性值
	 */
	default LocalDateTime getLocalDateTime(final K key) {
		return getLocalDateTime(key, null);
	}

	/**
	 * 获取LocalDate类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return LocalTime类型属性值
	 */
	default LocalDate getLocalDate(final K key, final LocalDate defaultValue) {
		return get(key, LocalDate.class, defaultValue);
	}

	/**
	 * 获取LocalDate类型值
	 *
	 * @param key 属性名
	 * @return LocalTime类型属性值
	 */
	default LocalDate getLocalDate(final K key) {
		return getLocalDate(key, null);
	}

	/**
	 * 获取LocalTime类型值
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return LocalTime类型属性值
	 */
	default LocalTime getLocalTime(final K key, final LocalTime defaultValue) {
		return get(key, LocalTime.class, defaultValue);
	}

	/**
	 * 获取LocalTime类型值
	 *
	 * @param key 属性名
	 * @return LocalTime类型属性值
	 */
	default LocalTime getLocalTime(final K key) {
		return getLocalTime(key, null);
	}
	/*-------------------------- 基本类型 end -------------------------------*/
}
