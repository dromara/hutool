package cn.hutool.core.lang;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.BasicTypeGetter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字典对象，扩充了HashMap中的方法
 *
 * @author loolly
 */
public class Dict extends LinkedHashMap<String, Object> implements BasicTypeGetter<String> {
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
	public static Dict create() {
		return new Dict();
	}

	/**
	 * 将PO对象转为Dict
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean对象
	 * @return Vo
	 */
	public static <T> Dict parse(T bean) {
		return create().parseBean(bean);
	}

	/**
	 * 根据给定的Pair数组创建Dict对象
	 *
	 * @param pairs 键值对
	 * @return Dict
	 * @since 5.4.1
	 */
	@SafeVarargs
	public static Dict of(Pair<String, Object>... pairs) {
		final Dict dict = create();
		for (Pair<String, Object> pair : pairs) {
			dict.put(pair.getKey(), pair.getValue());
		}
		return dict;
	}

	/**
	 * 根据给定的键值对数组创建Dict对象，传入参数必须为key,value,key,value...
	 *
	 * <p>奇数参数必须为key，key最后会转换为String类型。</p>
	 * <p>奇数参数必须为value，可以为任意类型。</p>
	 *
	 * <pre>
	   Dict dict = Dict.of(
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
	public static Dict of(Object... keysAndValues) {
		final Dict dict = create();

		String key = null;
		for(int i = 0; i < keysAndValues.length; i++){
			if(i % 2 == 0){
				key = Convert.toStr(keysAndValues[i]);
			} else{
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
	public Dict(boolean caseInsensitive) {
		this(DEFAULT_INITIAL_CAPACITY, caseInsensitive);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 */
	public Dict(int initialCapacity) {
		this(initialCapacity, false);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param caseInsensitive 是否大小写不敏感
	 */
	public Dict(int initialCapacity, boolean caseInsensitive) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR, caseInsensitive);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      容量增长因子，0~1，即达到容量的百分之多少时扩容
	 */
	public Dict(int initialCapacity, float loadFactor) {
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
	public Dict(int initialCapacity, float loadFactor, boolean caseInsensitive) {
		super(initialCapacity, loadFactor);
		this.caseInsensitive = caseInsensitive;
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public Dict(Map<String, Object> m) {
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
	public <T> T toBean(T bean) {
		return toBean(bean, false);
	}

	/**
	 * 转换为Bean对象
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean
	 * @return Bean
	 * @since 3.3.1
	 */
	public <T> T toBeanIgnoreCase(T bean) {
		BeanUtil.fillBeanWithMapIgnoreCase(this, bean, false);
		return bean;
	}

	/**
	 * 转换为Bean对象
	 *
	 * @param <T>           Bean类型
	 * @param bean          Bean
	 * @param isToCamelCase 是否转换为驼峰模式
	 * @return Bean
	 */
	public <T> T toBean(T bean, boolean isToCamelCase) {
		BeanUtil.fillBeanWithMap(this, bean, isToCamelCase, false);
		return bean;
	}

	/**
	 * 转换为Bean对象,并使用驼峰法模式转换
	 *
	 * @param <T>  Bean类型
	 * @param bean Bean
	 * @return Bean
	 */
	public <T> T toBeanWithCamelCase(T bean) {
		BeanUtil.fillBeanWithMap(this, bean, true, false);
		return bean;
	}

	/**
	 * 填充Value Object对象
	 *
	 * @param <T>   Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBean(Class<T> clazz) {
		return BeanUtil.toBean(this, clazz);
	}

	/**
	 * 填充Value Object对象，忽略大小写
	 *
	 * @param <T>   Bean类型
	 * @param clazz Value Object（或者POJO）的类
	 * @return vo
	 */
	public <T> T toBeanIgnoreCase(Class<T> clazz) {
		return BeanUtil.toBeanIgnoreCase(this, clazz, false);
	}

	/**
	 * 将值对象转换为Dict<br>
	 * 类名会被当作表名，小写第一个字母
	 *
	 * @param <T>  Bean类型
	 * @param bean 值对象
	 * @return 自己
	 */
	public <T> Dict parseBean(T bean) {
		Assert.notNull(bean, "Bean class must be not null");
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
	public <T> Dict parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		Assert.notNull(bean, "Bean class must be not null");
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
	public <T extends Dict> void removeEqual(T dict, String... withoutNames) {
		HashSet<String> withoutSet = CollUtil.newHashSet(withoutNames);
		for (Map.Entry<String, Object> entry : dict.entrySet()) {
			if (withoutSet.contains(entry.getKey())) {
				continue;
			}

			final Object value = this.get(entry.getKey());
			if (null != value && value.equals(entry.getValue())) {
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
	public Dict filter(String... keys) {
		final Dict result = new Dict(keys.length, 1);

		for (String key : keys) {
			if (this.containsKey(key)) {
				result.put(key, this.get(key));
			}
		}
		return result;
	}

	// -------------------------------------------------------------------- Set start

	/**
	 * 设置列
	 *
	 * @param attr  属性
	 * @param value 值
	 * @return 本身
	 */
	public Dict set(String attr, Object value) {
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
	public Dict setIgnoreNull(String attr, Object value) {
		if (null != attr && null != value) {
			set(attr, value);
		}
		return this;
	}
	// -------------------------------------------------------------------- Set end

	// -------------------------------------------------------------------- Get start

	@Override
	public Object getObj(String key) {
		return super.get(key);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param <T>  值类型
	 * @param attr 字段名
	 * @return 字段值
	 * @since 4.6.3
	 */
	public <T> T getBean(String attr) {
		return get(attr, null);
	}

	/**
	 * 获得特定类型值
	 *
	 * @param <T>          值类型
	 * @param attr         字段名
	 * @param defaultValue 默认值
	 * @return 字段值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr, T defaultValue) {
		final Object result = get(attr);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public String getStr(String attr) {
		return Convert.toStr(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Integer getInt(String attr) {
		return Convert.toInt(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Long getLong(String attr) {
		return Convert.toLong(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Float getFloat(String attr) {
		return Convert.toFloat(get(attr), null);
	}

	@Override
	public Short getShort(String attr) {
		return Convert.toShort(get(attr), null);
	}

	@Override
	public Character getChar(String attr) {
		return Convert.toChar(get(attr), null);
	}

	@Override
	public Double getDouble(String attr) {
		return Convert.toDouble(get(attr), null);
	}

	@Override
	public Byte getByte(String attr) {
		return Convert.toByte(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Boolean getBool(String attr) {
		return Convert.toBool(get(attr), null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public BigDecimal getBigDecimal(String attr) {
		return Convert.toBigDecimal(get(attr));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public BigInteger getBigInteger(String attr) {
		return Convert.toBigInteger(get(attr));
	}

	@Override
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
		return Convert.toEnum(clazz, get(key));
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public byte[] getBytes(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	@Override
	public Date getDate(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Time getTime(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Timestamp getTimestamp(String attr) {
		return get(attr, null);
	}

	/**
	 * @param attr 字段名
	 * @return 字段值
	 */
	public Number getNumber(String attr) {
		return get(attr, null);
	}
	// -------------------------------------------------------------------- Get end

	@Override
	public Object get(Object key) {
		return super.get(customKey((String) key));
	}

	@Override
	public Object put(String key, Object value) {
		return super.put(customKey(key), value);
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		m.forEach(this::put);
	}

	@Override
	public Dict clone() {
		return (Dict) super.clone();
	}

	/**
	 * 将Key转为小写
	 *
	 * @param key KEY
	 * @return 小写KEY
	 */
	private String customKey(String key) {
		if (this.caseInsensitive && null != key) {
			key = key.toLowerCase();
		}
		return key;
	}
}
