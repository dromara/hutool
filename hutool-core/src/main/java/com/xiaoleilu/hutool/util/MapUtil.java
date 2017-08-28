package com.xiaoleilu.hutool.util;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.lang.Editor;
import com.xiaoleilu.hutool.lang.Filter;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Map相关工具类
 * 
 * @author Looly
 * @since 3.1.1
 */
public class MapUtil {

	/** 默认初始大小 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	/** 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	/**
	 * Map是否为空
	 * 
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}
	
	/**
	 * Map是否为非空
	 * 
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && false == map.isEmpty();
	}

	// ----------------------------------------------------------------------------------------------- new HashMap
	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
	 * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 * @since 3.0.4
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
		int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR);
		return isOrder ? new LinkedHashMap<K, V>(initialCapacity) : new HashMap<K, V>(initialCapacity);
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return newHashMap(size, false);
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(boolean isOrder) {
		return newHashMap(DEFAULT_INITIAL_CAPACITY, false);
	}
	
	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 * 
	 * @param <K> map键类型
	 * @param <V> map值类型
	 * @param mapType map类型
	 * @return {@link Map}实例
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> createMap(Class<?> mapType) {
		if (mapType.isAssignableFrom(AbstractMap.class)) {
			return new HashMap<>();
		} else {
			try {
				return (Map<K, V>) ClassUtil.newInstance(mapType);
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------- value of
	/**
	 * 将单一键值对转换为Map
	 * @param key 键
	 * @param value 值
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(K key, V value){
		return of(key, value, false);
	}
	
	/**
	 * 将单一键值对转换为Map
	 * @param key 键
	 * @param value 值
	 * @param isOrder 是否有序
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(K key, V value, boolean isOrder){
		final HashMap<K, V> map = newHashMap(isOrder);
		map.put(key, value);
		return map;
	}
	
	/**
	 * 将数组转换为Map（HashMap），支持数组元素类型为：
	 * 
	 * <pre>
	 * Map.Entry
	 * 长度大于1的数组（取前两个值），如果不满足跳过此元素
	 * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * </pre>
	 * 
	 * <pre>
	 * Map&lt;Object, Object&gt; colorMap = CollectionUtil.toMap(new String[][] {{
	 *     {"RED", "#FF0000"},
	 *     {"GREEN", "#00FF00"},
	 *     {"BLUE", "#0000FF"}});
	 * </pre>
	 * 
	 * 参考：commons-lang
	 * 
	 * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
	 * @return {@link HashMap}
	 * @since 3.0.8
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<Object, Object> of(Object[] array) {
		if (array == null) {
			return null;
		}
		final HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			if (object instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) object;
				map.put(entry.getKey(), entry.getValue());
			} else if (object instanceof Object[]) {
				final Object[] entry = (Object[]) object;
				if (entry.length > 1) {
					map.put(entry[0], entry[1]);
				}
			} else if (object instanceof Iterable) {
				Iterator iter = ((Iterable) object).iterator();
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else if (object instanceof Iterator) {
				Iterator iter = ((Iterator) object);
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else {
				throw new IllegalArgumentException(StrUtil.format("Array element {}, '{}', is not type of Map.Entry or Array or Iterable or Iterator", i, object));
			}
		}
		return map;
	}
	
	/**
	 * 行转列，合并相同的键，值合并为列表<br>
	 * 将Map列表中相同key的值组成列表做为Map的value<br>
	 * 是{@link #toMapList(Map)}的逆方法<br>
	 * 比如传入数据：
	 * 
	 * <pre>
	 * [
	 *  {a: 1, b: 1, c: 1}
	 *  {a: 2, b: 2}
	 *  {a: 3, b: 3}
	 *  {a: 4}
	 * ]
	 * </pre>
	 * 
	 * 结果是：
	 * 
	 * <pre>
	 * {
	 *   a: [1,2,3,4]
	 *   b: [1,2,3,]
	 *   c: [1]
	 * }
	 * </pre>
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param mapList Map列表
	 * @return Map
	 */
	public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
		final HashMap<K, List<V>> resultMap = new HashMap<>();
		if (CollectionUtil.isEmpty(mapList)) {
			return resultMap;
		}

		Set<Entry<K, V>> entrySet;
		for (Map<K, V> map : mapList) {
			entrySet = map.entrySet();
			K key;
			List<V> valueList;
			for (Entry<K, V> entry : entrySet) {
				key = entry.getKey();
				valueList = resultMap.get(key);
				if (null == valueList) {
					valueList = CollectionUtil.newArrayList(entry.getValue());
					resultMap.put(key, valueList);
				} else {
					valueList.add(entry.getValue());
				}
			}
		}

		return resultMap;
	}

	/**
	 * 列转行。将Map中值列表分别按照其位置与key组成新的map。<br>
	 * 是{@link #toListMap(Iterable)}的逆方法<br>
	 * 比如传入数据：
	 * 
	 * <pre>
	 * {
	 *   a: [1,2,3,4]
	 *   b: [1,2,3,]
	 *   c: [1]
	 * }
	 * </pre>
	 * 
	 * 结果是：
	 * 
	 * <pre>
	 * [
	 *  {a: 1, b: 1, c: 1}
	 *  {a: 2, b: 2}
	 *  {a: 3, b: 3}
	 *  {a: 4}
	 * ]
	 * </pre>
	 * 
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param listMap 列表Map
	 * @return Map列表
	 */
	public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
		final List<Map<K, V>> resultList = new ArrayList<>();
		if (isEmpty(listMap)) {
			return resultList;
		}

		boolean isEnd = true;// 是否结束。标准是元素列表已耗尽
		int index = 0;// 值索引
		Map<K, V> map;
		do {
			isEnd = true;
			map = new HashMap<>();
			List<V> vList;
			int vListSize;
			for (Entry<K, ? extends Iterable<V>> entry : listMap.entrySet()) {
				vList = CollectionUtil.newArrayList(entry.getValue());
				vListSize = vList.size();
				if (index < vListSize) {
					map.put(entry.getKey(), vList.get(index));
					if (index != vListSize - 1) {
						// 当值列表中还有更多值（非最后一个），继续循环
						isEnd = false;
					}
				}
			}
			if (false == map.isEmpty()) {
				resultList.add(map);
			}
			index++;
		} while (false == isEnd);

		return resultList;
	}
	
	// ----------------------------------------------------------------------------------------------- filter
	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 * 
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map Map
	 * @param editor 编辑器接口
	 * @return 过滤后的Map
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Editor<Entry<K, V>> editor) {
		final Map<K, V> map2 = ObjectUtil.clone(map);
		if (isEmpty(map2)) {
			return map2;
		}

		map2.clear();
		Entry<K, V> modified;
		for (Entry<K, V> entry : map.entrySet()) {
			modified = editor.edit(entry);
			if (null != modified) {
				map2.put(entry.getKey(), entry.getValue());
			}
		}
		return map2;
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 * 
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map Map
	 * @param filter 编辑器接口
	 * @return 过滤后的Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Filter<Entry<K, V>> filter) {
		final Map<K, V> map2 = ObjectUtil.clone(map);
		if (isEmpty(map2)) {
			return map2;
		}

		map2.clear();
		for (Entry<K, V> entry : map.entrySet()) {
			if (filter.accept(entry)) {
				map2.put(entry.getKey(), entry.getValue());
			}
		}
		return map2;
	}
	
	/**
	 * Map创建类
	 * @param <K> Key类型
	 * @param <V> Value类型
	 */
	public static class MapBuilder<K, V> {
		private Map<K, V> map;
		
		/**
		 * 链式Map创建类
		 * @param map 要使用的Map实现类
		 */
		public MapBuilder(Map<K, V> map) {
			this.map = map;
		}
		
		/**
		 * 链式Map创建
		 * @param k Key类型
		 * @param v Value类型
		 * @return 当前类
		 */
		public MapBuilder<K, V> put(K k, V v) {
			map.put(k, v);
			return this;
		}
		
		/**
		 * 链式Map创建
		 * @param k1 Key类型
		 * @param v1 Value类型
		 * @param k2 Key类型
		 * @param v2 Value类型
		 * @return 当前类
		 */
		public MapBuilder<K, V> put(K k1, V v1, K k2, V v2) {
			map.put(k1, v1);
			map.put(k2, v2);
			return this;
		}
		
		/**
		 * 链式Map创建 
		 * @param k1 Key类型
		 * @param v1 Value类型
		 * @param k2 Key类型
		 * @param v2 Value类型
		 * @param k3 Key类型
		 * @param v3 Value类型
		 * @return 当前类
		 */
		public MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3) {
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			return this;
		}
		
		/**
		 * 链式Map创建
		 * @param k1 Key类型
		 * @param v1 Value类型
		 * @param k2 Key类型
		 * @param v2 Value类型
		 * @param k3 Key类型
		 * @param v3 Value类型
		 * @param k4 Key类型
		 * @param v4 Value类型
		 * @return 当前类
		 */
		public MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			map.put(k4, v4);
			return this;
		}
		
		/**
		 * 链式Map创建
		 * @param k1 Key类型
		 * @param v1 Value类型
		 * @param k2 Key类型
		 * @param v2 Value类型
		 * @param k3 Key类型
		 * @param v3 Value类型
		 * @param k4 Key类型
		 * @param v4 Value类型
		 * @param k5 Key类型
		 * @param v5 Value类型
		 * @return 当前类
		 */
		public MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			map.put(k4, v4);
			map.put(k5, v5);
			return this;
		}
		
		/**
		 * 链式Map创建
		 * @param map 合并map
		 * @return 当前类
		 */
		public MapBuilder<K, V> putAll(Map<K, V> map) {
			this.map.putAll(map);
			return this;
		}
		
		/**
		 * 创建后的map
		 * @return 创建后的map
		 */
		public Map<K, V> map() {
			return map;
		}
		
		/**
		 * 将map转成字符串
		 * @param separator entry之间的连接符
		 * @param keyValueSeparator kv之间的连接符 
		 * @return 连接字符串
		 */
		public String join(String separator, final String keyValueSeparator) {
			return map.entrySet()
			          .stream()
			          .map(e -> String.valueOf(e.getKey()) + keyValueSeparator + String.valueOf(e.getValue()))
			          .collect(Collectors.joining(separator));
		}
		
		/**
		 * 将map转成字符串
		 * @param separator entry之间的连接符
		 * @param keyValueSeparator kv之间的连接符
		 * @return 连接字符串
		 */
		public String joinFilterNull(String separator, final String keyValueSeparator) {
			return map.entrySet()
			          .stream()
			          .filter(e -> Objects.nonNull(e.getKey()) || Objects.nonNull(e.getValue()))
			          .map(e -> String.valueOf(e.getKey()) + keyValueSeparator + String.valueOf(e.getValue()))
			          .collect(Collectors.joining(separator));
		}
		
		/**
		 * 将map转成字符串
		 * @param separator entry之间的连接符
		 * @param keyValueSeparator kv之间的连接符
		 * @param filterPredicate 过滤处理
		 * @return 连接字符串
		 */
		public String join(String separator, String keyValueSeparator, Predicate<? super Entry<K, V>> filterPredicate) {
			return map.entrySet()
			          .stream()
			          .filter(filterPredicate)
			          .map(e -> String.valueOf(e.getKey()) + keyValueSeparator + String.valueOf(e.getValue()))
			          .collect(Collectors.joining(separator));
		}
		
	}
	
	/**
	 * 创建链接调用map
	 * @param map 实际使用的map
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> map(Map<K, V> map) {
		return new MapBuilder<>(map);
	}
	
	/**
	 * 创建链接调用map
	 * @param k   key
	 * @param v   value
	 * @param <K> Key类型
	 * @param <V> Key类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> put(K k, V v) {
		return (map(new HashMap<K, V>())).put(k, v);
	}
	
	/**
	 * 创建链接调用map
	 * @param k1   key
	 * @param v1   value
	 * @param k2   key
	 * @param v2   value
	 * @param <K> Key类型
	 * @param <V> Key类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> put(K k1, V v1, K k2, V v2) {
		return (map(new HashMap<K, V>())).put(k1, v1, k2, v2);
	}
	
	/**
	 * 创建链接调用map
	 * @param k1   key
	 * @param v1   value
	 * @param k2   key
	 * @param v2   value
	 * @param k3   key
	 * @param v3   value
	 * @param <K> Key类型
	 * @param <V> Key类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3) {
		return (map(new HashMap<K, V>())).put(k1, v1, k2, v2, k3, v3);
	}
	
	/**
	 * 创建链接调用map
	 * @param k1   key
	 * @param v1   value
	 * @param k2   key
	 * @param v2   value
	 * @param k3   key
	 * @param v3   value
	 * @param k4   key
	 * @param v4   value
	 * @param <K> Key类型
	 * @param <V> Key类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		return (map(new HashMap<K, V>())).put(k1, v1, k2, v2, k3, v3, k4, v4);
	}
	
	/**
	 * 创建链接调用map
	 * @param k1   key
	 * @param v1   value
	 * @param k2   key
	 * @param v2   value
	 * @param k3   key
	 * @param v3   value
	 * @param k4   key
	 * @param v4   value
	 * @param k5   key
	 * @param v5   value
	 * @param <K> Key类型
	 * @param <V> Key类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> put(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
		return (map(new HashMap<K, V>())).put(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}
}
