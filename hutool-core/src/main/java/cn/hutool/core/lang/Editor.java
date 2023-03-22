package cn.hutool.core.lang;

/**
 * 编辑器接口，常用于对于集合中的元素做统一编辑<br>
 * 此编辑器两个作用：
 *
 * <pre>
 * 1、如果返回值为{@code null}，表示此值被抛弃
 * 2、对对象做修改
 * </pre>
 *
 * @param <T> 被编辑对象类型
 * @author Looly
 */
@FunctionalInterface
public interface Editor<T> {
	/**
	 * 修改过滤后的结果
	 *
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回{@code null}
	 */
	T edit(T t);
}
