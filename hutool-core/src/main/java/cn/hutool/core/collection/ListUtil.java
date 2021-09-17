package cn.hutool.core.collection;

import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.comparator.PropertyComparator;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * List相关工具类
 *
 * @author looly
 */
public class ListUtil {
	/**
	 * 新建一个空List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @return List对象
	 * @since 4.1.2
	 */
	public static <T> List<T> list(boolean isLinked) {
		return isLinked ? new LinkedList<>() : new ArrayList<>();
	}

	/**
	 * 新建一个List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @param values   数组
	 * @return List对象
	 * @since 4.1.2
	 */
	@SafeVarargs
	public static <T> List<T> list(boolean isLinked, T... values) {
		if (ArrayUtil.isEmpty(values)) {
			return list(isLinked);
		}
		final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
		Collections.addAll(arrayList, values);
		return arrayList;
	}

	/**
	 * 新建一个List
	 *
	 * @param <T>        集合元素类型
	 * @param isLinked   是否新建LinkedList
	 * @param collection 集合
	 * @return List对象
	 * @since 4.1.2
	 */
	public static <T> List<T> list(boolean isLinked, Collection<T> collection) {
		if (null == collection) {
			return list(isLinked);
		}
		return isLinked ? new LinkedList<>(collection) : new ArrayList<>(collection);
	}

	/**
	 * 新建一个List<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @param iterable {@link Iterable}
	 * @return List对象
	 * @since 4.1.2
	 */
	public static <T> List<T> list(boolean isLinked, Iterable<T> iterable) {
		if (null == iterable) {
			return list(isLinked);
		}
		return list(isLinked, iterable.iterator());
	}

	/**
	 * 新建一个List<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @param iter     {@link Iterator}
	 * @return ArrayList对象
	 * @since 4.1.2
	 */
	public static <T> List<T> list(boolean isLinked, Iterator<T> iter) {
		final List<T> list = list(isLinked);
		if (null != iter) {
			while (iter.hasNext()) {
				list.add(iter.next());
			}
		}
		return list;
	}

	/**
	 * 新建一个List<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>        集合元素类型
	 * @param isLinked   是否新建LinkedList
	 * @param enumration {@link Enumeration}
	 * @return ArrayList对象
	 * @since 3.0.8
	 */
	public static <T> List<T> list(boolean isLinked, Enumeration<T> enumration) {
		final List<T> list = list(isLinked);
		if (null != enumration) {
			while (enumration.hasMoreElements()) {
				list.add(enumration.nextElement());
			}
		}
		return list;
	}

	/**
	 * 新建一个ArrayList
	 *
	 * @param <T>    集合元素类型
	 * @param values 数组
	 * @return ArrayList对象
	 */
	@SafeVarargs
	public static <T> ArrayList<T> toList(T... values) {
		return (ArrayList<T>) list(false, values);
	}

	/**
	 * 新建LinkedList
	 *
	 * @param values 数组
	 * @param <T>    类型
	 * @return LinkedList
	 * @since 4.1.2
	 */
	@SafeVarargs
	public static <T> LinkedList<T> toLinkedList(T... values) {
		return (LinkedList<T>) list(true, values);
	}

	/**
	 * 数组转为一个不可变List<br>
	 * 类似于Java9中的List.of
	 *
	 * @param ts  对象
	 * @param <T> 对象类型
	 * @return 不可修改List
	 * @since 5.4.3
	 */
	@SafeVarargs
	public static <T> List<T> of(T... ts) {
		if (ArrayUtil.isEmpty(ts)) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(toList(ts));
	}

	/**
	 * 新建一个CopyOnWriteArrayList
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return {@link CopyOnWriteArrayList}
	 */
	public static <T> CopyOnWriteArrayList<T> toCopyOnWriteArrayList(Collection<T> collection) {
		return (null == collection) ? (new CopyOnWriteArrayList<>()) : (new CopyOnWriteArrayList<>(collection));
	}

	/**
	 * 新建一个ArrayList
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> toList(Collection<T> collection) {
		return (ArrayList<T>) list(false, collection);
	}

	/**
	 * 新建一个ArrayList<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return ArrayList对象
	 * @since 3.1.0
	 */
	public static <T> ArrayList<T> toList(Iterable<T> iterable) {
		return (ArrayList<T>) list(false, iterable);
	}

	/**
	 * 新建一个ArrayList<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return ArrayList对象
	 * @since 3.0.8
	 */
	public static <T> ArrayList<T> toList(Iterator<T> iterator) {
		return (ArrayList<T>) list(false, iterator);
	}

	/**
	 * 新建一个ArrayList<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>         集合元素类型
	 * @param enumeration {@link Enumeration}
	 * @return ArrayList对象
	 * @since 3.0.8
	 */
	public static <T> ArrayList<T> toList(Enumeration<T> enumeration) {
		return (ArrayList<T>) list(false, enumeration);
	}

	/**
	 * 对指定List分页取值
	 *
	 * @param <T>      集合元素类型
	 * @param pageNo   页码，第一页的页码取决于{@link PageUtil#getFirstPageNo()}，默认0
	 * @param pageSize 每页的条目数
	 * @param list     列表
	 * @return 分页后的段落内容
	 * @since 4.1.20
	 */
	public static <T> List<T> page(int pageNo, int pageSize, List<T> list) {
		if (CollUtil.isEmpty(list)) {
			return new ArrayList<>(0);
		}

		int resultSize = list.size();
		// 每页条目数大于总数直接返回所有
		if (resultSize <= pageSize) {
			if (pageNo < (PageUtil.getFirstPageNo() + 1)) {
				return unmodifiable(list);
			} else {
				// 越界直接返回空
				return new ArrayList<>(0);
			}
		}
		// 相乘可能会导致越界 临时用long
		if (((long) (pageNo - PageUtil.getFirstPageNo()) * pageSize) > resultSize) {
			// 越界直接返回空
			return new ArrayList<>(0);
		}

		final int[] startEnd = PageUtil.transToStartEnd(pageNo, pageSize);
		if (startEnd[1] > resultSize) {
			startEnd[1] = resultSize;
			if (startEnd[0] > startEnd[1]) {
				return new ArrayList<>(0);
			}
		}

		return sub(list, startEnd[0], startEnd[1]);
	}

	/**
	 * 对指定List进行分页，逐页返回数据
	 *
	 * @param <T>              集合元素类型
	 * @param list             源数据列表
	 * @param pageSize         每页的条目数
	 * @param pageListConsumer 单页数据函数式返回
	 * @since 5.7.10
	 */
	public static <T> void page(List<T> list, int pageSize, Consumer<List<T>> pageListConsumer) {
		if (CollUtil.isEmpty(list) || pageSize <= 0) {
			return;
		}

		final int total = list.size();
		final int totalPage = PageUtil.totalPage(total, pageSize);
		for (int pageNo = PageUtil.getFirstPageNo(); pageNo < totalPage + PageUtil.getFirstPageNo(); pageNo++) {
			// 获取当前页在列表中对应的起止序号
			final int[] startEnd = PageUtil.transToStartEnd(pageNo, pageSize);
			if (startEnd[1] > total) {
				startEnd[1] = total;
			}

			// 返回数据
			pageListConsumer.accept(sub(list, startEnd[0], startEnd[1]));
		}
	}

	/**
	 * 针对List排序，排序会修改原List
	 *
	 * @param <T>  元素类型
	 * @param list 被排序的List
	 * @param c    {@link Comparator}
	 * @return 原list
	 * @see Collections#sort(List, Comparator)
	 */
	public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
		if (CollUtil.isEmpty(list)) {
			return list;
		}
		list.sort(c);
		return list;
	}

	/**
	 * 根据Bean的属性排序
	 *
	 * @param <T>      元素类型
	 * @param list     List
	 * @param property 属性名
	 * @return 排序后的List
	 * @since 4.0.6
	 */
	public static <T> List<T> sortByProperty(List<T> list, String property) {
		return sort(list, new PropertyComparator<>(property));
	}

	/**
	 * 根据汉字的拼音顺序排序
	 *
	 * @param list List
	 * @return 排序后的List
	 * @since 4.0.8
	 */
	public static List<String> sortByPinyin(List<String> list) {
		return sort(list, new PinyinComparator());
	}

	/**
	 * 反序给定List，会在原List基础上直接修改
	 *
	 * @param <T>  元素类型
	 * @param list 被反转的List
	 * @return 反转后的List
	 * @since 4.0.6
	 */
	public static <T> List<T> reverse(List<T> list) {
		Collections.reverse(list);
		return list;
	}

	/**
	 * 反序给定List，会创建一个新的List，原List数据不变
	 *
	 * @param <T>  元素类型
	 * @param list 被反转的List
	 * @return 反转后的List
	 * @since 4.0.6
	 */
	public static <T> List<T> reverseNew(List<T> list) {
		List<T> list2 = ObjectUtil.clone(list);
		if (null == list2) {
			// 不支持clone
			list2 = new ArrayList<>(list);
		}
		return reverse(list2);
	}

	/**
	 * 设置或增加元素。当index小于List的长度时，替换指定位置的值，否则在尾部追加
	 *
	 * @param <T>     元素类型
	 * @param list    List列表
	 * @param index   位置
	 * @param element 新元素
	 * @return 原List
	 * @since 4.1.2
	 */
	public static <T> List<T> setOrAppend(List<T> list, int index, T element) {
		if (index < list.size()) {
			list.set(index, element);
		} else {
			list.add(element);
		}
		return list;
	}

	/**
	 * 截取集合的部分
	 *
	 * @param <T>   集合元素类型
	 * @param list  被截取的数组
	 * @param start 开始位置（包含）
	 * @param end   结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回空的List
	 */
	public static <T> List<T> sub(List<T> list, int start, int end) {
		return sub(list, start, end, 1);
	}

	/**
	 * 截取集合的部分<br>
	 * 此方法与{@link List#subList(int, int)} 不同在于子列表是新的副本，操作子列表不会影响原列表。
	 *
	 * @param <T>   集合元素类型
	 * @param list  被截取的数组
	 * @param start 开始位置（包含）
	 * @param end   结束位置（不包含）
	 * @param step  步进
	 * @return 截取后的数组，当开始位置超过最大时，返回空的List
	 * @since 4.0.6
	 */
	public static <T> List<T> sub(List<T> list, int start, int end, int step) {
		if (list == null) {
			return null;
		}

		if (list.isEmpty()) {
			return new ArrayList<>(0);
		}

		final int size = list.size();
		if (start < 0) {
			start += size;
		}
		if (end < 0) {
			end += size;
		}
		if (start == size) {
			return new ArrayList<>(0);
		}
		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > size) {
			if (start >= size) {
				return new ArrayList<>(0);
			}
			end = size;
		}

		if (step < 1) {
			step = 1;
		}

		final List<T> result = new ArrayList<>();
		for (int i = start; i < end; i += step) {
			result.add(list.get(i));
		}
		return result;
	}

	/**
	 * 获取匹配规则定义中匹配到元素的最后位置<br>
	 * 此方法对于某些无序集合的位置信息，以转换为数组后的位置为准。
	 *
	 * @param <T>     元素类型
	 * @param list    List集合
	 * @param matcher 匹配器，为空则全部匹配
	 * @return 最后一个位置
	 * @since 5.6.6
	 */
	public static <T> int lastIndexOf(List<T> list, Matcher<T> matcher) {
		if (null != list) {
			final int size = list.size();
			if (size > 0) {
				for (int i = size - 1; i >= 0; i--) {
					if (null == matcher || matcher.match(list.get(i))) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 获取匹配规则定义中匹配到元素的所有位置
	 *
	 * @param <T>     元素类型
	 * @param list    列表
	 * @param matcher 匹配器，为空则全部匹配
	 * @return 位置数组
	 * @since 5.2.5
	 */
	public static <T> int[] indexOfAll(List<T> list, Matcher<T> matcher) {
		return CollUtil.indexOfAll(list, matcher);
	}

	/**
	 * 将对应List转换为不可修改的List
	 *
	 * @param list List
	 * @param <T>  元素类型
	 * @return 不可修改List
	 * @since 5.2.6
	 */
	public static <T> List<T> unmodifiable(List<T> list) {
		if (null == list) {
			return null;
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * 获取一个空List，这个空List不可变
	 *
	 * @param <T> 元素类型
	 * @return 空的List
	 * @see Collections#emptyList()
	 * @since 5.2.6
	 */
	public static <T> List<T> empty() {
		return Collections.emptyList();
	}

	/**
	 * 通过传入分区长度，将指定列表分区为不同的块，每块区域的长度相同（最后一块可能小于长度）<br>
	 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
	 *
	 * <p>
	 * 需要特别注意的是，此方法调用{@link List#subList(int, int)}切分List，
	 * 此方法返回的是原List的视图，也就是说原List有变更，切分后的结果也会变更。
	 * </p>
	 *
	 * @param <T>  集合元素类型
	 * @param list 列表，为空时返回{@link #empty()}
	 * @param size 每个段的长度，当长度超过list长度时，size按照list长度计算，即只返回一个节点
	 * @return 分段列表
	 * @since 5.4.5
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {
		if (CollUtil.isEmpty(list)) {
			return empty();
		}

		return (list instanceof RandomAccess)
				? new RandomAccessPartition<>(list, size)
				: new Partition<>(list, size);
	}

	/**
	 * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
	 *
	 * <p>
	 * 需要特别注意的是，此方法调用{@link List#subList(int, int)}切分List，
	 * 此方法返回的是原List的视图，也就是说原List有变更，切分后的结果也会变更。
	 * </p>
	 *
	 * @param <T>  集合元素类型
	 * @param list 列表，为空时返回{@link #empty()}
	 * @param size 每个段的长度，当长度超过list长度时，size按照list长度计算，即只返回一个节点
	 * @return 分段列表
	 * @see #partition(List, int)
	 * @since 5.4.5
	 */
	public static <T> List<List<T>> split(List<T> list, int size) {
		return partition(list, size);
	}

	/**
	 * 将集合平均分成多个list，返回这个集合的列表
	 * <p>例：</p>
	 * <pre>
	 *     ListUtil.splitAvg(null, 3);	// []
	 *     ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 2);	// [[1, 2], [3, 4]]
	 *     ListUtil.splitAvg(Arrays.asList(1, 2, 3), 5);	// [[1], [2], [3], [], []]
	 *     ListUtil.splitAvg(Arrays.asList(1, 2, 3), 2);	// [[1, 2], [3]]
	 * </pre>
	 *
	 * @param <T>   集合元素类型
	 * @param list  集合
	 * @param limit 要均分成几个list
	 * @return 分段列表
	 * @author lileming
	 * @since 5.7.10
	 */
	public static <T> List<List<T>> splitAvg(List<T> list, int limit) {
		if (CollUtil.isEmpty(list)) {
			return empty();
		}

		return (list instanceof RandomAccess)
				? new RandomAccessAvgPartition<>(list, limit)
				: new AvgPartition<>(list, limit);
	}

	/**
	 * 将指定元素交换到指定索引位置,其他元素的索引值不变<br>
	 * 交换会修改原List<br>
	 * 如果集合中有多个相同元素，只交换第一个找到的元素
	 *
	 * @param list        列表
	 * @param element     需交换元素
	 * @param targetIndex 目标索引
	 * @since 5.7.13
	 */
	public static <T> void swapTo(List<T> list, T element, Integer targetIndex) {
		if (CollUtil.isNotEmpty(list)) {
			final int index = list.indexOf(element);
			if (index > 0) {
				Collections.swap(list, index, targetIndex);
			}
		}
	}

	/**
	 * 将指定元素交换到指定元素位置,其他元素的索引值不变<br>
	 * 交换会修改原List<br>
	 * 如果集合中有多个相同元素，只交换第一个找到的元素
	 *
	 * @param list          列表
	 * @param element       需交换元素
	 * @param targetElement 目标元素
	 */
	public static <T> void swapElement(List<T> list, T element, T targetElement) {
		if (CollUtil.isNotEmpty(list)) {
			final int targetIndex = list.indexOf(targetElement);
			if (targetIndex > 0) {
				swapTo(list, element, targetIndex);
			}
		}
	}
}
