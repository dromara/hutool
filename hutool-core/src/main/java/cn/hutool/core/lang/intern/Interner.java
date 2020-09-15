package cn.hutool.core.lang.intern;

/**
 * 规范化表示形式封装<br>
 * 所谓规范化，即当两个对象equals时，规范化的对象则可以实现==<br>
 * 此包中的相关封装类似于 {@link String#intern()}
 *
 * @param <T> 规范化的对象类型
 * @author looly
 * @since 5.4.3
 */
public interface Interner<T> {

	/**
	 * 返回指定对象对应的规范化对象，sample对象可能有多个，但是这些对象如果都equals，则返回的是同一个对象
	 *
	 * @param sample 对象
	 * @return 样例对象
	 */
	T intern(T sample);
}