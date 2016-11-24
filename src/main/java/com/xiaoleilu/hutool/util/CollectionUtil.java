package com.xiaoleilu.hutool.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.lang.BoundedPriorityQueue;
import com.xiaoleilu.hutool.lang.Filter;

/**
 * 集合相关工具类，包括数组
 * 
 * @author xiaoleilu
 * 
 */
public class CollectionUtil {

	private CollectionUtil() {
		// 静态类不可实例化
	}

	/**
	 * 两个集合的并集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c, c]，此结果中只保留了三个c
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isEmpty(coll1)) {
			list.addAll(coll2);
		} else if (isEmpty(coll2)) {
			list.addAll(coll1);
		} else {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.max(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0)); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 多个集合的并集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最多的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c, c]，此结果中只保留了三个c
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> Collection<T> union(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
		Collection<T> union = union(coll1, coll2);
		for (Collection<T> coll : otherColls) {
			union = union(union, coll);
		}
		return union;
	}

	/**
	 * 两个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 交集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.min(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0)); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 多个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 并集的集合，返回 {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> Collection<T> intersection(final Collection<T> coll1, final Collection<T> coll2, final Collection<T>... otherColls) {
		Collection<T> intersection = intersection(coll1, coll2);
		if (isEmpty(intersection)) {
			return intersection;
		}
		for (Collection<T> coll : otherColls) {
			intersection = intersection(intersection, coll);
			if (isEmpty(intersection)) {
				return intersection;
			}
		}
		return intersection;
	}

	/**
	 * 两个集合的差集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留两个集合中此元素个数差的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[c]，此结果中只保留了一个
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 差集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> disjunction(final Collection<T> coll1, final Collection<T> coll2) {
		final ArrayList<T> list = new ArrayList<>();
		if (isNotEmpty(coll1) && isNotEmpty(coll2)) {
			final Map<T, Integer> map1 = countMap(coll1);
			final Map<T, Integer> map2 = countMap(coll2);
			final Set<T> elts = newHashSet(coll2);
			for (T t : elts) {
				for (int i = 0, m = Math.max(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0)) - Math.min(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0)); i < m; i++) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * 其中一个集合在另一个集合中是否至少包含一个元素，既是两个集合是否至少有一个共同的元素
	 * 
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 其中一个集合在另一个集合中是否至少包含一个元素
	 * @since 2.1
	 * @see #intersection
	 */
	public static boolean containsAny(final Collection<?> coll1, final Collection<?> coll2) {
		if (isEmpty(coll1) || isEmpty(coll2)) {
			return false;
		}
		if (coll1.size() < coll2.size()) {
			for (Object object : coll1) {
				if (coll2.contains(object)) {
					return true;
				}
			}
		} else {
			for (Object object : coll2) {
				if (coll1.contains(object)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 * 
	 * @param collection 集合
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Collection<T> collection) {
		HashMap<T, Integer> countMap = new HashMap<>();
		Integer count;
		for (T t : collection) {
			count = countMap.get(t);
			if (null == count) {
				countMap.put(t, 1);
			} else {
				countMap.put(t, count + 1);
			}
		}
		return countMap;
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param iterable {@link Iterable}
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterable<T> iterable, String conjunction) {
		if(null == iterable){
			return null;
		}
		return join(iterable.iterator(), conjunction);
	}
	
	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param iterator 集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, String conjunction) {
		if(null == iterator){
			return null;
		}
		
		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		T item;
		while(iterator.hasNext()){
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			
			item = iterator.next();
			if(isArray(item)) {
				sb.append(join(CollectionUtil.wrap(item), conjunction));
			} else if(item instanceof Iterable<?>) {
				sb.append(join((Iterable<?>)item, conjunction));
			} else if(item instanceof Iterator<?>) {
				sb.append(join((Iterator<?>)item, conjunction));
			} else{
				sb.append(item);
			}
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] array, String conjunction) {
		if(null == array){
			return null;
		}
		
		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			if(isArray(item)) {
				sb.append(join(CollectionUtil.wrap(item), conjunction));
			} else if(item instanceof Iterable<?>) {
				sb.append(join((Iterable<?>)item, conjunction));
			} else if(item instanceof Iterator<?>) {
				sb.append(join((Iterator<?>)item, conjunction));
			} else{
				sb.append(item);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 以 conjunction 为分隔符将多个对象转换为字符串
	 * 
	 * @param conjunction 分隔符
	 * @param objs 数组
	 * @return 连接后的字符串
	 */
	public static String join(String conjunction, Object... objs) {
		return join(objs, conjunction);
	}

	/**
	 * 将多个集合排序并显示不同的段落（分页）
	 * 
	 * @param pageNo 页码，从1开始
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return 分页后的段落内容
	 */
	@SafeVarargs
	public static <T> List<T> sortPageAll(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		final List<T> result = new ArrayList<T>();
		for (Collection<T> coll : colls) {
			result.addAll(coll);
		}

		Collections.sort(result, comparator);

		int resultSize = result.size();
		// 每页条目数大于总数直接返回所有
		if (resultSize <= numPerPage) {
			return result;
		}
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
		if (startEnd[1] > resultSize) {
			// 越界直接返回空
			return new ArrayList<>();
		}

		return result.subList(startEnd[0], startEnd[1]);
	}

	/**
	 * 将多个集合排序并显示不同的段落（分页）
	 * 
	 * @param pageNo 页码
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return 分业后的段落内容
	 */
	@SafeVarargs
	public static <T> List<T> sortPageAll2(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		BoundedPriorityQueue<T> queue = new BoundedPriorityQueue<T>(pageNo * numPerPage);
		for (Collection<T> coll : colls) {
			queue.addAll(coll);
		}

		int resultSize = queue.size();
		// 每页条目数大于总数直接返回所有
		if (resultSize <= numPerPage) {
			return queue.toList();
		}
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
		if (startEnd[1] > resultSize) {
			// 越界直接返回空
			return new ArrayList<>();
		}

		return queue.toList().subList(startEnd[0], startEnd[1]);
	}

	/**
	 * 将Set排序（根据Entry的值）
	 * 
	 * @param set 被排序的Set
	 * @return 排序后的Set
	 */
	public static List<Entry<Long, Long>> sortEntrySetToList(Set<Entry<Long, Long>> set) {
		List<Entry<Long, Long>> list = new LinkedList<Map.Entry<Long, Long>>(set);
		Collections.sort(list, new Comparator<Entry<Long, Long>>(){

			@Override
			public int compare(Entry<Long, Long> o1, Entry<Long, Long> o2) {
				if (o1.getValue() > o2.getValue()) {
					return 1;
				}
				if (o1.getValue() < o2.getValue()) {
					return -1;
				}
				return 0;
			}
		});
		return list;
	}

	/**
	 * 切取部分数据
	 * 
	 * @param <T> 集合元素类型
	 * @param surplusAlaDatas 原数据
	 * @param partSize 每部分数据的长度
	 * @return 切取出的数据或null
	 */
	public static <T> List<T> popPart(Stack<T> surplusAlaDatas, int partSize) {
		if (surplusAlaDatas == null || surplusAlaDatas.size() <= 0) {
			return null;
		}

		final List<T> currentAlaDatas = new ArrayList<T>();
		int size = surplusAlaDatas.size();
		// 切割
		if (size > partSize) {
			for (int i = 0; i < partSize; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		} else {
			for (int i = 0; i < size; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		}
		return currentAlaDatas;
	}

	/**
	 * 切取部分数据
	 * 
	 * @param <T> 集合元素类型
	 * @param surplusAlaDatas 原数据
	 * @param partSize 每部分数据的长度
	 * @return 切取出的数据或null
	 */
	public static <T> List<T> popPart(Deque<T> surplusAlaDatas, int partSize) {
		if (surplusAlaDatas == null || surplusAlaDatas.size() <= 0) {
			return null;
		}

		final List<T> currentAlaDatas = new ArrayList<T>();
		int size = surplusAlaDatas.size();
		// 切割
		if (size > partSize) {
			for (int i = 0; i < partSize; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		} else {
			for (int i = 0; i < size; i++) {
				currentAlaDatas.add(surplusAlaDatas.pop());
			}
		}
		return currentAlaDatas;
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @return HashMap对象
	 */
	public static <T, K> HashMap<T, K> newHashMap() {
		return new HashMap<T, K>();
	}

	/**
	 * 新建一个HashMap
	 * 
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75
	 * @return HashMap对象
	 */
	public static <T, K> HashMap<T, K> newHashMap(int size) {
		return new HashMap<T, K>((int) (size / 0.75));
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @param ts 元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(T... ts) {
		HashSet<T> set = new HashSet<T>();
		for (T t : ts) {
			set.add(t);
		}
		return set;
	}

	/**
	 * 新建一个HashSet
	 * 
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(Collection<T> collection) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(collection);
		return set;
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param values 数组
	 * @return ArrayList对象
	 */
	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... values) {
		ArrayList<T> arrayList = new ArrayList<T>();
		for (T t : values) {
			arrayList.add(t);
		}
		return arrayList;
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param collection 集合
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
		return new ArrayList<T>(collection);
	}

	/**
	 * 将新元素添加到已有数组中<br/>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 * 
	 * @param buffer 已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> T[] append(T[] buffer, T... newElements) {
		if (isEmpty(newElements)) {
			return buffer;
		}

		T[] t = resize(buffer, buffer.length + newElements.length);
		System.arraycopy(newElements, 0, t, buffer.length, newElements.length);
		return t;
	}

	/**
	 * 生成一个新的重新设置大小的数组
	 * 
	 * @param buffer 原数组
	 * @param newSize 新的数组大小
	 * @param componentType 数组元素类型
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(T[] buffer, int newSize, Class<?> componentType) {
		T[] newArray = newArray(componentType, newSize);
		if (isNotEmpty(buffer)) {
			System.arraycopy(buffer, 0, newArray, 0, Math.min(buffer.length, newSize));
		}
		return newArray;
	}

	/**
	 * 新建一个空数组
	 * 
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return 空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	/**
	 * 生成一个新的重新设置大小的数组<br/>
	 * 新数组的类型为原数组的类型
	 * 
	 * @param buffer 原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(T[] buffer, int newSize) {
		return resize(buffer, newSize, buffer.getClass().getComponentType());
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 */
	@SafeVarargs
	public static <T> T[] addAll(T[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		int length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			length += array.length;
		}
		T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);

		length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			System.arraycopy(array, 0, result, length, array.length);
			length += array.length;
		}
		return result;
	}

	/**
	 * 克隆数组
	 * 
	 * @param array 被克隆的数组
	 * @return 新数组
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * 
	 * @param excludedEnd 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(int excludedEnd) {
		return range(0, excludedEnd, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * 
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(int includedStart, int excludedEnd) {
		return range(includedStart, excludedEnd, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * 
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @param step 步进
	 * @return 数字列表
	 */
	public static int[] range(int includedStart, int excludedEnd, int step) {
		if (includedStart > excludedEnd) {
			int tmp = includedStart;
			includedStart = excludedEnd;
			excludedEnd = tmp;
		}

		if (step <= 0) {
			step = 1;
		}

		int deviation = excludedEnd - includedStart;
		int length = deviation / step;
		if (deviation % step != 0) {
			length += 1;
		}
		int[] range = new int[length];
		for (int i = 0; i < length; i++) {
			range[i] = includedStart;
			includedStart += step;
		}
		return range;
	}

	/**
	 * 截取数组的部分
	 * 
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(List<T> list, int start, int end) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}

		final int size = list.size();
		if (end > size) {
			if (start >= size) {
				return null;
			}
			end = size;
		}

		return list.subList(start, end);
	}

	/**
	 * 截取集合的部分
	 * 
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(Collection<T> list, int start, int end) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		return sub(new ArrayList<T>(list), start, end);
	}

	/**
	 * 过滤
	 * 
	 * @param array 数组
	 * @param filter 过滤器接口
	 * @return 过滤后的数组
	 */
	public static <T> T[] filter(T[] array, Filter<T> filter) {
		ArrayList<T> list = new ArrayList<T>();
		T modified;
		for (T t : array) {
			modified = filter.modify(t);
			if (null != modified) {
				list.add(t);
			}
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * 过滤<br>
	 * 过滤会改变原集合的内容
	 * 
	 * @param collection 集合
	 * @param filter 过滤器接口
	 * @return 过滤后的数组
	 */
	public static <T> Collection<T> filter(Collection<T> collection, Filter<T> filter) {
		Collection<T> collection2 = ObjectUtil.clone(collection);
		collection2.clear();

		T modified;
		for (T t : collection) {
			modified = filter.modify(t);
			if (null != modified) {
				collection2.add(t);
			}
		}
		return collection2;
	}

	/**
	 * 过滤
	 * 
	 * @param map Map
	 * @param filter 过滤器
	 * @return 过滤后的Map
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Filter<Entry<K, V>> filter) {
		Map<K, V> map2 = ObjectUtil.clone(map);
		map2.clear();

		Entry<K, V> modified;
		for (Entry<K, V> entry : map.entrySet()) {
			modified = filter.modify(entry);
			if (null != modified) {
				map2.put(entry.getKey(), entry.getValue());
			}
		}
		return map2;
	}

	// ---------------------------------------------------------------------- isEmpty
	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(final T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 集合是否为空
	 * 
	 * @param collection 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Map是否为空
	 * 
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterator<?> Iterator) {
		return null == Iterator || false == Iterator.hasNext();
	}
	
	/**
	 * Enumeration是否为空
	 * 
	 * @param enumeration {@link Enumeration}
	 * @return 是否为空
	 */
	public static boolean isEmpty(Enumeration<?> enumeration) {
		return null == enumeration || false == enumeration.hasMoreElements();
	}

	// ---------------------------------------------------------------------- isNotEmpty
	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final long[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final int[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final short[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final char[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final double[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final float[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 集合是否为非空
	 * 
	 * @param collection 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return false == isEmpty(collection);
	}

	/**
	 * Map是否为非空
	 * 
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static <T> boolean isNotEmpty(Map<?, ?> map) {
		return false == isEmpty(map);
	}

	/**
	 * Iterable是否为空
	 * 
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 * 
	 * @param Iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterator<?> Iterator) {
		return null != Iterator && Iterator.hasNext();
	}
	
	/**
	 * Enumeration是否为空
	 * 
	 * @param enumeration {@link Enumeration}
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Enumeration<?> enumeration) {
		return null != enumeration && enumeration.hasMoreElements();
	}

	// ---------------------------------------------------------------------- zip
	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param keys 键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static <T, K> Map<T, K> zip(T[] keys, K[] values) {
		if (isEmpty(keys) || isEmpty(values)) {
			return null;
		}

		final int size = Math.min(keys.length, values.length);
		final Map<T, K> map = new HashMap<T, K>((int) (size / 0.75));
		for (int i = 0; i < size; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = a,b,c,d<br>
	 * values = 1,2,3,4<br>
	 * delimiter = , 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param keys 键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static Map<String, String> zip(String keys, String values, String delimiter) {
		return zip(StrUtil.split(keys, delimiter), StrUtil.split(values, delimiter));
	}

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param keys 键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static <T, K> Map<T, K> zip(Collection<T> keys, Collection<K> values) {
		if (isEmpty(keys) || isEmpty(values)) {
			return null;
		}

		final List<T> keyList = new ArrayList<T>(keys);
		final List<K> valueList = new ArrayList<K>(values);

		final int size = Math.min(keys.size(), values.size());
		final Map<T, K> map = new HashMap<T, K>((int) (size / 0.75));
		for (int i = 0; i < size; i++) {
			map.put(keyList.get(i), valueList.get(i));
		}

		return map;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static <T> boolean contains(T[] array, T value) {
		final Class<?> componetType = array.getClass().getComponentType();
		boolean isPrimitive = false;
		if (null != componetType) {
			isPrimitive = componetType.isPrimitive();
		}
		for (T t : array) {
			if (t == value) {
				return true;
			} else if (false == isPrimitive && null != value && value.equals(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将Entry集合转换为HashMap
	 * 
	 * @param entryCollection entry集合
	 * @return Map
	 */
	public static <T, K> HashMap<T, K> toMap(Collection<Entry<T, K>> entryCollection) {
		HashMap<T, K> map = new HashMap<T, K>();
		for (Entry<T, K> entry : entryCollection) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	/**
	 * 将集合转换为排序后的TreeSet
	 * 
	 * @param collection 集合
	 * @param comparator 比较器
	 * @return treeSet
	 */
	public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
		final TreeSet<T> treeSet = new TreeSet<T>(comparator);
		for (T t : collection) {
			treeSet.add(t);
		}
		return treeSet;
	}

	/**
	 * 排序集合
	 * 
	 * @param collection 集合
	 * @param comparator 比较器
	 * @return treeSet
	 */
	public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
		List<T> list = new ArrayList<T>(collection);
		Collections.sort(list, comparator);
		return list;
	}

	// ------------------------------------------------------------------- 基本类型的数组转换为包装类型数组
	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Integer[] wrap(int... values) {
		final int length = values.length;
		Integer[] array = new Integer[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Long[] wrap(long... values) {
		final int length = values.length;
		Long[] array = new Long[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Character[] wrap(char... values) {
		final int length = values.length;
		Character[] array = new Character[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Byte[] wrap(byte... values) {
		final int length = values.length;
		Byte[] array = new Byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Short[] wrap(short... values) {
		final int length = values.length;
		Short[] array = new Short[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Float[] wrap(float... values) {
		final int length = values.length;
		Float[] array = new Float[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Double[] wrap(double... values) {
		final int length = values.length;
		Double[] array = new Double[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 将基本类型数组包装为包装类型
	 * 
	 * @param values 基本类型数组
	 * @return 包装类型数组
	 */
	public static Boolean[] wrap(boolean... values) {
		final int length = values.length;
		Boolean[] array = new Boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装数组对象
	 * 
	 * @param obj 对象，可以是对象数组或者基本类型数组
	 * @return 包装类型数组或对象数组
	 * @throws UtilException 对象为非数组
	 */
	public static Object[] wrap(Object obj) {
		if (isArray(obj)) {
			try {
				return (Object[]) obj;
			} catch (Exception e) {
				final String className = obj.getClass().getComponentType().getName();
				switch (className) {
					case "long":
						return wrap((long[]) obj);
					case "int":
						return wrap((int[]) obj);
					case "short":
						return wrap((short[]) obj);
					case "char":
						return wrap((char[]) obj);
					case "byte":
						return wrap((byte[]) obj);
					case "boolean":
						return wrap((boolean[]) obj);
					case "float":
						return wrap((float[]) obj);
					case "double":
						return wrap((double[]) obj);
					default:
						throw new UtilException(e);
				}
			}
		}
		throw new UtilException(StrUtil.format("[{}] is not Array!", obj.getClass()));
	}

	/**
	 * 对象是否为数组对象
	 * 
	 * @param obj 对象
	 * @return 是否为数组对象
	 */
	public static boolean isArray(Object obj) {
		if (null == obj) {
			throw new NullPointerException("Object check for isArray is null");
		}
		return obj.getClass().isArray();
	}

	/**
	 * Iterator转换为Enumeration Adapt the specified <code>Iterator</code> to the <code>Enumeration</code> interface.
	 * 
	 * @param iter Iterator
	 * @return Enumeration
	 */
	public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
		return new Enumeration<E>(){
			@Override
			public boolean hasMoreElements() {
				return iter.hasNext();
			}

			@Override
			public E nextElement() {
				return iter.next();
			}
		};
	}

	/**
	 * Enumeration转换为Iterator<br>
	 * Adapt the specified <code>Enumeration</code> to the <code>Iterator</code> interface
	 * 
	 * @param e Enumeration
	 * @return Iterator
	 */
	public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
		return new Iterator<E>(){
			@Override
			public boolean hasNext() {
				return e.hasMoreElements();
			}

			@Override
			public E next() {
				return e.nextElement();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * 数组或集合转String
	 * 
	 * @param obj 集合或数组对象
	 * @return 数组字符串，与集合转字符串格式相同
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			try {
				return Arrays.deepToString((Object[]) obj);
			} catch (Exception e) {
				final String className = obj.getClass().getComponentType().getName();
				switch (className) {
					case "long":
						return Arrays.toString((long[]) obj);
					case "int":
						return Arrays.toString((int[]) obj);
					case "short":
						return Arrays.toString((short[]) obj);
					case "char":
						return Arrays.toString((char[]) obj);
					case "byte":
						return Arrays.toString((byte[]) obj);
					case "boolean":
						return Arrays.toString((boolean[]) obj);
					case "float":
						return Arrays.toString((float[]) obj);
					case "double":
						return Arrays.toString((double[]) obj);
					default:
						throw new UtilException(e);
				}
			}
		}
		return obj.toString();
	}
	
	/**
	 * 加入全部
	 * @param collection 集合 {@link Collection}
	 * @param iterator {@link Iterator}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
		if(null != collection && null != iterator){
			while (iterator.hasNext()) {
				collection.add(iterator.next());
			}
		}
		return collection;
	}
	
	/**
	 * 加入全部
	 * @param collection 集合 {@link Collection}
	 * @param iterable {@link Iterable}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
		return addAll(collection, iterable.iterator());
	}

	/**
	 * 加入全部
	 * @param collection 集合 {@link Collection}
	 * @param enumeration {@link Enumeration}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
		if(null != collection && null != enumeration){
			while (enumeration.hasMoreElements()) {
				collection.add(enumeration.nextElement());
			}
		}
		return collection;
	}
	
	//------------------------------------------------------------------------------------------------- forEach
	
	/**
	 * 循环遍历 {@link Iterator}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
	 * @param iterator {@link Iterator}
	 * @param consumer {@link Consumer} 遍历的每条数据处理器
	 */
	public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer){
		int index = 0;
		while(iterator.hasNext()){
			consumer.accept(iterator.next(), index);
			index ++;
		}
	}
	
	/**
	 * 循环遍历 {@link Enumeration}，使用{@link Consumer} 接受遍历的每条数据，并针对每条数据做处理
	 * @param enumeration {@link Enumeration}
	 * @param consumer {@link Consumer} 遍历的每条数据处理器
	 */
	public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer){
		int index = 0;
		while(enumeration.hasMoreElements()){
			consumer.accept(enumeration.nextElement(), index);
			index ++;
		}
	}
	
	/**
	 * 循环遍历Map，使用{@link KVConsumer} 接受遍历的每条数据，并针对每条数据做处理
	 * @param map {@link Map}
	 * @param kvConsumer {@link KVConsumer} 遍历的每条数据处理器
	 */
	public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer){
		int index = 0;
		for(Entry<K, V> entry : map.entrySet()){
			kvConsumer.accept(entry.getKey(), entry.getValue(), index);
			index++;
		}
	}
	
	/**
	 * 针对一个参数做相应的操作
	 * @author Looly
	 *
	 *@param <T> 处理参数类型
	 */
	public static interface Consumer<T>{
		/**
		 * 接受并处理一个参数
		 * @param value 参数值
		 * @param index 参数在集合中的索引
		 */
		void accept(T value, int index);
	}
	
	/**
	 * 针对两个参数做相应的操作，例如Map中的KEY和VALUE
	 * @author Looly
	 *
	 * @param <K> KEY类型
	 * @param <V> VALUE类型
	 */
	public static interface KVConsumer<K, V>{
		/**
		 * 接受并处理一对参数
		 * @param key 键
		 * @param value 值
		 * @param index 参数在集合中的索引
		 */
		void accept(K key, V value, int index);
	}
}
