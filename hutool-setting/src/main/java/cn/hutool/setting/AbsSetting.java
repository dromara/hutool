package cn.hutool.setting;

import java.io.Serializable;
import java.lang.reflect.Type;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.OptNullBasicTypeFromStringGetter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * Setting抽象类
 *
 * @author Looly
 */
public abstract class AbsSetting implements OptNullBasicTypeFromStringGetter<String>, Serializable {
	private static final long serialVersionUID = 6200156302595905863L;
	private final static Log log = LogFactory.get();

	/**
	 * 数组类型值默认分隔符
	 */
	public final static String DEFAULT_DELIMITER = ",";
	/**
	 * 默认分组
	 */
	public final static String DEFAULT_GROUP = StrUtil.EMPTY;

	@Override
	public String getStr(String key, String defaultValue) {
		return getStr(key, DEFAULT_GROUP, defaultValue);
	}

	/**
	 * 获得字符串类型值
	 *
	 * @param key          KEY
	 * @param group        分组
	 * @param defaultValue 默认值
	 * @return 值，如果字符串为{@code null}返回默认值
	 */
	public String getStr(String key, String group, String defaultValue) {
		final String value = getByGroup(key, group);
		return ObjectUtil.defaultIfNull(value, defaultValue);
	}

	/**
	 * 获得字符串类型值，如果字符串为{@code null}或者""返回默认值
	 *
	 * @param key          KEY
	 * @param group        分组
	 * @param defaultValue 默认值
	 * @return 值，如果字符串为{@code null}或者""返回默认值
	 * @since 5.2。4
	 */
	public String getStrNotEmpty(String key, String group, String defaultValue) {
		final String value = getByGroup(key, group);
		return ObjectUtil.defaultIfEmpty(value, defaultValue);
	}

	/**
	 * 获得指定分组的键对应值
	 *
	 * @param key   键
	 * @param group 分组
	 * @return 值
	 */
	public abstract String getByGroup(String key, String group);

	// --------------------------------------------------------------- Get

	/**
	 * 带有日志提示的get，如果没有定义指定的KEY，则打印debug日志
	 *
	 * @param key 键
	 * @return 值
	 */
	public String getWithLog(String key) {
		final String value = getStr(key);
		if (value == null) {
			log.debug("No key define for [{}]!", key);
		}
		return value;
	}

	/**
	 * 带有日志提示的get，如果没有定义指定的KEY，则打印debug日志
	 *
	 * @param key   键
	 * @param group 分组
	 * @return 值
	 */
	public String getByGroupWithLog(String key, String group) {
		final String value = getByGroup(key, group);
		if (value == null) {
			log.debug("No key define for [{}] of group [{}] !", key, group);
		}
		return value;
	}

	// --------------------------------------------------------------- Get string array

	/**
	 * 获得数组型
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	public String[] getStrings(String key) {
		return getStrings(key, null);
	}

	/**
	 * 获得数组型
	 *
	 * @param key          属性名
	 * @param defaultValue 默认的值
	 * @return 属性值
	 */
	public String[] getStringsWithDefault(String key, String[] defaultValue) {
		String[] value = getStrings(key, null);
		if (null == value) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * 获得数组型
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public String[] getStrings(String key, String group) {
		return getStrings(key, group, DEFAULT_DELIMITER);
	}

	/**
	 * 获得数组型
	 *
	 * @param key       属性名
	 * @param group     分组名
	 * @param delimiter 分隔符
	 * @return 属性值
	 */
	public String[] getStrings(String key, String group, String delimiter) {
		final String value = getByGroup(key, group);
		if (StrUtil.isBlank(value)) {
			return null;
		}
		return StrUtil.split(value, delimiter);
	}

	// --------------------------------------------------------------- Get int

	/**
	 * 获取数字型型属性值
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Integer getInt(String key, String group) {
		return getInt(key, group, null);
	}

	/**
	 * 获取数字型型属性值
	 *
	 * @param key          属性名
	 * @param group        分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Integer getInt(String key, String group, Integer defaultValue) {
		return Convert.toInt(getByGroup(key, group), defaultValue);
	}

	// --------------------------------------------------------------- Get bool

	/**
	 * 获取波尔型属性值
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Boolean getBool(String key, String group) {
		return getBool(key, group, null);
	}

	/**
	 * 获取波尔型型属性值
	 *
	 * @param key          属性名
	 * @param group        分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Boolean getBool(String key, String group, Boolean defaultValue) {
		return Convert.toBool(getByGroup(key, group), defaultValue);
	}

	// --------------------------------------------------------------- Get long

	/**
	 * 获取long类型属性值
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Long getLong(String key, String group) {
		return getLong(key, group, null);
	}

	/**
	 * 获取long类型属性值
	 *
	 * @param key          属性名
	 * @param group        分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Long getLong(String key, String group, Long defaultValue) {
		return Convert.toLong(getByGroup(key, group), defaultValue);
	}

	// --------------------------------------------------------------- Get char

	/**
	 * 获取char类型属性值
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Character getChar(String key, String group) {
		final String value = getByGroup(key, group);
		if (StrUtil.isBlank(value)) {
			return null;
		}
		return value.charAt(0);
	}

	// --------------------------------------------------------------- Get double

	/**
	 * 获取double类型属性值
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public Double getDouble(String key, String group) {
		return getDouble(key, group, null);
	}

	/**
	 * 获取double类型属性值
	 *
	 * @param key          属性名
	 * @param group        分组名
	 * @param defaultValue 默认值
	 * @return 属性值
	 */
	public Double getDouble(String key, String group, Double defaultValue) {
		return Convert.toDouble(getByGroup(key, group), defaultValue);
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
	 * 只支持基本类型的转换
	 *
	 * @param <T>   Bean类型
	 * @param group 分组
	 * @param bean  Bean对象
	 * @return Bean
	 */
	public <T> T toBean(final String group, T bean) {
		return BeanUtil.fillBean(bean, new ValueProvider<String>() {

			@Override
			public Object value(String key, Type valueType) {
				return getByGroup(key, group);
			}

			@Override
			public boolean containsKey(String key) {
				return null != getByGroup(key, group);
			}
		}, CopyOptions.create());
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
	 * 只支持基本类型的转换
	 *
	 * @param <T>       Bean类型
	 * @param group     分组
	 * @param beanClass Bean类型
	 * @return Bean
	 * @since 5.0.6
	 */
	public <T> T toBean(String group, Class<T> beanClass) {
		return toBean(group, ReflectUtil.newInstanceIfPossible(beanClass));
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
	 * 只支持基本类型的转换
	 *
	 * @param <T>  bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBean(T bean) {
		return toBean(null, bean);
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
	 * 只支持基本类型的转换
	 *
	 * @param <T>       bean类型
	 * @param beanClass Bean类型
	 * @return Bean
	 * @since 5.0.6
	 */
	public <T> T toBean(Class<T> beanClass) {
		return toBean(null, beanClass);
	}
}
