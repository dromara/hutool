package cn.hutool.core.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 转换工具类，提供集合、Map等向上向下转换工具
 *
 * @author looly
 * @since 5.8.1
 */
public class CastUtil {
	/**
	 * 泛型集合向上转型。例如将Collection&lt;Integer&gt;转换为Collection&lt;Number&gt;
	 *
	 * @param collection 集合
	 * @param <T>        元素类型
	 * @return 转换后的集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> castUp(Collection<? extends T> collection) {
		return (Collection<T>) collection;
	}

	/**
	 * 泛型集合向下转型。例如将Collection&lt;Number&gt;转换为Collection&lt;Integer&gt;
	 *
	 * @param collection 集合
	 * @param <T>        元素类型
	 * @return 转换后的集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> castDown(Collection<? super T> collection) {
		return (Collection<T>) collection;
	}

	/**
	 * 泛型集合向上转型。例如将Set&lt;Integer&gt;转换为Set&lt;Number&gt;
	 *
	 * @param set 集合
	 * @param <T> 泛型
	 * @return 泛化集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> castUp(Set<? extends T> set) {
		return (Set<T>) set;
	}

	/**
	 * 泛型集合向下转型。例如将Set&lt;Number&gt;转换为Set&lt;Integer&gt;
	 *
	 * @param set 集合
	 * @param <T> 泛型子类
	 * @return 泛化集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> castDown(Set<? super T> set) {
		return (Set<T>) set;
	}

	/**
	 * 泛型接口向上转型。例如将List&lt;Integer&gt;转换为List&lt;Number&gt;
	 *
	 * @param list 集合
	 * @param <T>  泛型的父类
	 * @return 泛化集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> castUp(List<? extends T> list) {
		return (List<T>) list;
	}

	/**
	 * 泛型集合向下转型。例如将List&lt;Number&gt;转换为List&lt;Integer&gt;
	 *
	 * @param list 集合
	 * @param <T>  泛型的子类
	 * @return 泛化集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> castDown(List<? super T> list) {
		return (List<T>) list;
	}

	/**
	 * 泛型集合向下转型。例如将Map&lt;Integer, Integer&gt;转换为Map&lt;Number,Number&gt;
	 *
	 * @param map 集合
	 * @param <K> 泛型父类
	 * @param <V> 泛型父类
	 * @return 泛化集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> castUp(Map<? extends K, ? extends V> map) {
		return (Map<K, V>) map;
	}

	/**
	 * 泛型集合向下转型。例如将Map&lt;Number,Number&gt;转换为Map&lt;Integer, Integer&gt;
	 *
	 * @param map 集合
	 * @param <K> 泛型子类
	 * @param <V> 泛型子类
	 * @return 泛化集合
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> castDown(Map<? super K, ? super V> map) {
		return (Map<K, V>) map;
	}
}
