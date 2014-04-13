package looly.github.hutool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

/**
 * 集合相关工具类，包括数组
 * 
 * @author xiaoleilu
 * 
 */
public class CollectionUtil {
	/**
	 * 以 conjunction 为分隔符将集合转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param collection 集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterable<T> collection, String conjunction) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : collection) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
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
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}
	
	/**
	 * 将多个集合排序并显示不同的段落（分页）
	 * @param pageNo 页码
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return
	 */
	public static <T> List<T> sortPageAll(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		final List<T> result = new ArrayList<T>();
		for (Collection<T> coll : colls) {
			result.addAll(coll);
		}
		
		Collections.sort(result, comparator);
		
		//第一页且数目少于第一页显示的数目
		if(pageNo <=1 && result.size() <= numPerPage) {
			return result;
		}
		
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
		return result.subList(startEnd[0], startEnd[1]);
	}
	
	/**
	 * 将多个集合排序并显示不同的段落（分页）
	 * @param pageNo 页码
	 * @param numPerPage 每页的条目数
	 * @param comparator 比较器
	 * @param colls 集合数组
	 * @return
	 */
	public static <T> List<T> sortPageAll2(int pageNo, int numPerPage, Comparator<T> comparator, Collection<T>... colls) {
		BoundedPriorityQueue<T> queue = new BoundedPriorityQueue<T>(pageNo * numPerPage);
		for (Collection<T> coll : colls) {
			queue.addAll(coll);
		}
		
		//第一页且数目少于第一页显示的数目
		if(pageNo <=1 && queue.size() <= numPerPage) {
			return queue.toList();
		}
		
		final int[] startEnd = PageUtil.transToStartEnd(pageNo, numPerPage);
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
				if (o1.getValue() > o2.getValue()) return 1;
				if (o1.getValue() < o2.getValue()) return -1;
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
		if (surplusAlaDatas == null || surplusAlaDatas.size() <= 0) return null;

		List<T> currentAlaDatas = new ArrayList<T>();
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
	 * 新建一个HashSet
	 * 
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet() {
		return new HashSet<T>();
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	/**
	 * 将新元素添加到已有数组中<br/>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 * 
	 * @param buffer 已有数组
	 * @param newElement 新元素
	 * @return 新数组
	 */
	public static <T> T[] append(T[] buffer, T newElement) {
		T[] t = resize(buffer, buffer.length + 1, newElement.getClass());
		t[buffer.length] = newElement;
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
		System.arraycopy(buffer, 0, newArray, 0, buffer.length >= newSize ? newSize : buffer.length);
		return newArray;
	}
	
	/**
	 * 新建一个空数组
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return
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
	 * @param array1 数组1
	 * @param array2 数组2
	 * @return 合并后的数组
	 */
	public static <T> T[] addAll(T[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}
		
		int length = 0;
		for (T[] array : arrays) {
			if(array == null) {
				continue;
			}
			length += array.length;
		}
		T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);

		length = 0;
		for (T[] array : arrays) {
			if(array == null) {
				continue;
			}
			System.arraycopy(array, 0, result, length, array.length);
			length += array.length;
		}
		return result;
	}

	/**
	 * 克隆数组
	 * @param array 被克隆的数组
	 * @return
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
	 * @param excludedEnd 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(int excludedEnd) {
		return range(0, excludedEnd, 1);
	}
	
	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @return
	 */
	public static int[] range(int includedStart, int excludedEnd) {
		return range(includedStart, excludedEnd, 1);
	}
	
	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @param step 步进
	 * @return 数字列表
	 */
	public static int[] range(int includedStart, int excludedEnd, int step) {
		if(includedStart > excludedEnd) {
			int tmp = includedStart;
			includedStart = excludedEnd;
			excludedEnd = tmp;
		}
		
		if(step <=0) {
			step = 1;
		}
		
		int length = (excludedEnd - includedStart) / step;
		int[] range = new int[length];
		for(int i = 0; i < length; i++) {
			range[i] = includedStart;
			includedStart += step;
		}
		return range;
	}
	
	/**
	 * 截取数组的部分
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(List<T> list, int start, int end) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		
		if(start < 0) {
			start = 0;
		}
		if(end < 0) {
			end = 0;
		}
		
		if(start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		
		final int size = list.size();
		if(end > size) {
			if(start >= size) {
				return null;
			}
			end = size;
		}
		
		return list.subList(start, end);
	}
	
	/**
	 * 截取集合的部分
	 * @param list 被截取的数组
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回null
	 */
	public static <T> List<T> sub(Collection<T> list, int start, int end) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		
		return sub(new ArrayList<T>(list), start, end);
	}
	
	/**
	 * 数组是否为空
	 * @param array 数字
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}
	
	/**
	 * 集合是否为空
	 * @param collection 集合
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}
}
