/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exception.CloneException;
import org.dromara.hutool.core.func.LambdaInfo;
import org.dromara.hutool.core.func.LambdaUtil;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.getter.TypeGetter;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 字典对象，扩充了LinkedHashMap中的方法
 *
 * @author looly
 */
public class Dict extends CustomKeyMap<String, Object> implements TypeGetter<String> {
	private static final long serialVersionUID = 6135423866861206530L;

	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

	/**
	 * 是否大小写不敏感
	 */
	private boolean caseInsensitive;

	// --------------------------------------------------------------- Static method start

	/**
	 * 创建Dict
	 *
	 * @return Dict
	 */
	public static Dict of() {
		return new Dict();
	}

	/**
	 * 将PO对象转为Dict
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean对象
	 * @return Vo
	 */
	public static <T> Dict parse(final T bean) {
		return of().parseBean(bean);
	}

	/**
	 * 根据给定的Entry数组创建Dict对象
	 *
	 * @param pairs 键值对
	 * @return Dict
	 */
	@SafeVarargs
	public static Dict ofEntries(final Map.Entry<String, Object>... pairs) {
		final Dict dict = of();
		for (final Map.Entry<String, Object> pair : pairs) {
			dict.put(pair.getKey(), pair.getValue());
		}
		return dict;
	}

	/**
	 * 根据给定的键值对数组创建Dict对象，传入参数必须为key,value,key,value...
	 *
	 * <p>奇数参数必须为key，key最后会转换为String类型。</p>
	 * <p>偶数参数必须为value，可以为任意类型。</p>
	 *
	 * <pre>
	 * Dict dict = Dict.of(
	 * 	"RED", "#FF0000",
	 * 	"GREEN", "#00FF00",
	 * 	"BLUE", "#0000FF"
	 * );
	 * </pre>
	 *
	 * @param keysAndValues 键值对列表，必须奇数参数为key，偶数参数为value
	 * @return Dict
	 * @since 5.4.1
	 */
	public static Dict ofKvs(final Object... keysAndValues) {
		final Dict dict = of();

		String key = null;
		for (int i = 0; i < keysAndValues.length; i++) {
			// 偶数
			if ((i & 1) == 0) {
				key = Convert.toStr(keysAndValues[i]);
			} else {
				dict.put(key, keysAndValues[i]);
			}
		}

		return dict;
	}
	// --------------------------------------------------------------- Static method end

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public Dict() {
		this(false);
	}

	/**
	 * 构造
	 *
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dict(final boolean caseInsensitive) {
		this(DEFAULT_INITIAL_CAPACITY, caseInsensitive);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 */
	public Dict(final int initialCapacity) {
		this(initialCapacity, false);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dict(final int initialCapacity, final boolean caseInsensitive) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR, caseInsensitive);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      容量增长因子，0~1，即达到容量的百分之多少时扩容
	 */
	public Dict(final int initialCapacity, final float loadFactor) {
		this(initialCapacity, loadFactor, false);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      容量增长因子，0~1，即达到容量的百分之多少时扩容
	 * @param caseInsensitive 是否大小写不敏感
	 * @since 4.5.16
	 */
	public Dict(final int initialCapacity, final float loadFactor, final boolean caseInsensitive) {
		super(new LinkedHashMap<>(initialCapacity, loadFactor));
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public Dict(final Map<String, Object> m) {
		super((null == m) ? new HashMap<>() : m);
	}
	// --------------------------------------------------------------- Constructor end

	/**
	 * 转换为Bean对象
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBean(final T bean) {
		return BeanUtil.fillBeanWithMap(this, bean, CopyOptions.of());
	}

	/**
	 * 转换为Bean对象
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean
	 * @return Bean
	 * @since 3.3.1
	 */
	public <T> T toBeanIgnoreCase(final T bean) {
		return BeanUtil.fillBeanWithMap(this, bean, CopyOptions.of().setIgnoreCase(true));
	}

	/**
	 * 填充Value Object对象
	 *
	 * @param <T>   Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBean(final Class<T> clazz) {
		return BeanUtil.toBean(this, clazz);
	}

	/**
	 * 填充Value Object对象，忽略大小写
	 *
	 * @param <T>   Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBeanIgnoreCase(final Class<T> clazz) {
		return BeanUtil.toBean(this, clazz, CopyOptions.of().setIgnoreCase(true));
	}

	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 *
	 * @param <T>  Bean类型
	 * @param bean 值对象
	 * @return 自己
	 */
	public <T> Dict parseBean(final T bean) {
		Assert.notNull(bean, "Bean must not be null");
		this.putAll(BeanUtil.beanToMap(bean));
		return this;
	}

	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 *
	 * @param <T>               Bean类型
	 * @param bean              值对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return 自己
	 */
	public <T> Dict parseBean(final T bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		Assert.notNull(bean, "Bean must not be null");
		this.putAll(BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue));
		return this;
	}

	/**
	 * 与给定实体对比并去除相同的部分<br>
	 * 此方法用于在更新操作时避免所有字段被更新，跳过不需要更新的字段 version from 2.0.0
	 *
	 * @param <T>          字典对象类型
	 * @param dict         字典对象
	 * @param withoutNames 不需要去除的字段名
	 */
	public <T extends Dict> void removeEqual(final T dict, final String... withoutNames) {
		final HashSet<String> withoutSet = SetUtil.of(withoutNames);
		for (final Map.Entry<String, Object> entry : dict.entrySet()) {
			if (withoutSet.contains(entry.getKey())) {
				continue;
			}

			final Object value = this.get(entry.getKey());
			if (Objects.equals(value, entry.getValue())) {
				this.remove(entry.getKey());
			}
		}
	}

	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 *
	 * @param keys 键列表
	 * @return Dict 结果
	 * @since 4.0.10
	 */
	public Dict filterNew(final String... keys) {
		final Dict result = new Dict(keys.length, 1);

		for (final String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	/**
	 * 过滤Map去除指定键值对，如果键不存在跳过
	 *
	 * @param keys 键列表
	 * @return Dict 结果
	 * @since 4.0.10
	 */
	public Dict removeNew(final String... keys) {
		return MapUtil.removeAny(this.clone(), keys);
	}

	// -------------------------------------------------------------------- Set start

	/**
	 * 设置列
	 *
	 * @param attr  属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict set(final String attr, final Object value) {
		this.put(attr, value);
		return this;
	}

	/**
	 * 设置列，当键或值为null时忽略
	 *
	 * @param attr  属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict setIgnoreNull(final String attr, final Object value) {
		if (null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	// -------------------------------------------------------------------- Set end

	// -------------------------------------------------------------------- Get start
	@Override
	public Object getObj(final String key, final Object defaultValue) {
		return getOrDefault(key, defaultValue);
	}

	/**
	 * 根据lambda的方法引用，获取
	 *
	 * @param func 方法引用
	 * @param <P>  参数类型
	 * @param <T>  返回值类型
	 * @return 获取表达式对应属性和返回的对象
	 */
	public <P, T> T get(final SerFunction<P, T> func) {
		final LambdaInfo lambdaInfo = LambdaUtil.resolve(func);
		return get(lambdaInfo.getFieldName(), lambdaInfo.getReturnType());
	}

	/**
	 * 获得特定类型值
	 *
	 * @param <T>  值类型
	 * @param attr 字段名
	 * @return 字段值
	 * @since 4.6.3
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(final String attr) {
		return (T) get(attr);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 *
	 * @param <T>        目标类型
	 * @param expression 表达式
	 * @return 对象
	 * @see BeanPath#getValue(Object)
	 * @since 5.7.14
	 */
	@SuppressWarnings("unchecked")
	public <T> T getByPath(final String expression) {
		return (T) BeanPath.of(expression).getValue(this);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * <p>
	 * 获取表达式对应值后转换为对应类型的值
	 *
	 * @param <T>        返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanPath#getValue(Object)
	 * @since 5.7.14
	 */
	public <T> T getByPath(final String expression, final Type resultType) {
		return Convert.convert(resultType, getByPath(expression));
	}
	// -------------------------------------------------------------------- Get end

	@Override
	public Dict clone() {
		try {
			return (Dict) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneException(e);
		}
	}

	@Override
	protected String customKey(Object key) {
		if (this.caseInsensitive && null != key) {
			key = ((String) key).toLowerCase();
		}
		return (String) key;
	}

	/**
	 * 通过lambda批量设置值<br>
	 * 实际使用时，可以使用getXXX的方法引用来完成键值对的赋值：
	 * <pre>
	 *     User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
	 *     Dict.create().setFields(user::getNickname, user::getUsername);
	 * </pre>
	 *
	 * @param fields lambda,不能为空
	 * @return this
	 * @since 5.7.23
	 */
	public Dict setFields(final SerSupplier<?>... fields) {
		Arrays.stream(fields).forEach(f -> set(LambdaUtil.getFieldName(f), f.get()));
		return this;
	}
}
