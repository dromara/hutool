package cn.hutool.setting;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.lang.getter.GroupedTypeGetter;
import cn.hutool.core.lang.getter.TypeGetter;
import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Setting抽象类
 *
 * @author Looly
 */
public abstract class AbsSetting implements TypeGetter<CharSequence>,
		GroupedTypeGetter<CharSequence, CharSequence>, Serializable {
	private static final long serialVersionUID = 6200156302595905863L;

	/**
	 * 数组类型值默认分隔符
	 */
	public final static String DEFAULT_DELIMITER = StrUtil.COMMA;
	/**
	 * 默认分组
	 */
	public final static String DEFAULT_GROUP = StrUtil.EMPTY;

	@Override
	public Object getObj(final CharSequence key, final Object defaultValue) {
		return ObjUtil.defaultIfNull(getObjByGroup(key, DEFAULT_GROUP), defaultValue);
	}

	/**
	 * 获得字符串类型值，如果字符串为{@code null}或者""返回默认值
	 *
	 * @param key          KEY
	 * @param group        分组
	 * @param defaultValue 默认值
	 * @return 值，如果字符串为{@code null}或者""返回默认值
	 */
	public String getByGroupNotEmpty(final String key, final String group, final String defaultValue) {
		final String value = getStrByGroup(key, group);
		return StrUtil.defaultIfEmpty(value, defaultValue);
	}
	// --------------------------------------------------------------- Get string array

	/**
	 * 获得数组型
	 *
	 * @param key 属性名
	 * @return 属性值
	 */
	public String[] getStrs(final String key) {
		return getStrs(key, null);
	}

	/**
	 * 获得数组型
	 *
	 * @param key          属性名
	 * @param defaultValue 默认的值
	 * @return 属性值
	 */
	public String[] getStrs(final CharSequence key, final String[] defaultValue) {
		String[] value = getStrsByGroup(key, null);
		if (null == value) {
			value = defaultValue;
		}

		return value;
	}

	/**
	 * 获得数组型默认逗号分隔<br>
	 * 若配置文件中键值对类似于：
	 * <pre>
	 *     a = 1,2,3,4
	 * </pre>
	 * 则获取结果为：[1, 2, 3, 4]
	 *
	 * @param key   属性名
	 * @param group 分组名
	 * @return 属性值
	 */
	public String[] getStrsByGroup(final CharSequence key, final CharSequence group) {
		return getStrsByGroup(key, group, DEFAULT_DELIMITER);
	}

	/**
	 * 获得数组型，可自定义分隔符<br>
	 * 假定分隔符为逗号，若配置文件中键值对类似于：
	 * <pre>
	 *     a = 1,2,3,4
	 * </pre>
	 * 则获取结果为：[1, 2, 3, 4]
	 *
	 * @param key       属性名
	 * @param group     分组名
	 * @param delimiter 分隔符
	 * @return 属性值
	 */
	public String[] getStrsByGroup(final CharSequence key, final CharSequence group, final CharSequence delimiter) {
		final String value = getStrByGroup(key, group);
		if (StrUtil.isBlank(value)) {
			return null;
		}
		return StrUtil.splitToArray(value, delimiter);
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
	public <T> T toBean(final CharSequence group, final T bean) {
		return BeanUtil.fillBean(bean, new ValueProvider<String>() {

			@Override
			public Object value(final String key, final Type valueType) {
				return getObjByGroup(key, group);
			}

			@Override
			public boolean containsKey(final String key) {
				return null != getObjByGroup(key, group);
			}
		}, CopyOptions.of());
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
	public <T> T toBean(final CharSequence group, final Class<T> beanClass) {
		return toBean(group, ConstructorUtil.newInstanceIfPossible(beanClass));
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
	 * 只支持基本类型的转换
	 *
	 * @param <T>  bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBean(final T bean) {
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
	public <T> T toBean(final Class<T> beanClass) {
		return toBean(null, beanClass);
	}
}
