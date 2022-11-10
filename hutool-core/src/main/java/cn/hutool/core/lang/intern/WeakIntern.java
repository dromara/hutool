package cn.hutool.core.lang.intern;

import cn.hutool.core.map.WeakConcurrentMap;

import java.lang.ref.WeakReference;

/**
 * 使用WeakHashMap(线程安全)存储对象的规范化对象，注意此对象需单例使用！<br>
 *
 * @param <T> key 类型
 * @author looly
 * @since 5.4.3
 */
public class WeakIntern<T> implements Intern<T> {

	private final WeakConcurrentMap<T, WeakReference<T>> cache = new WeakConcurrentMap<>();

	@Override
	public T intern(final T sample) {
		if(null == sample){
			return null;
		}
		T val;
		// 循环避免刚创建就被回收的情况
		do {
			val = this.cache.computeIfAbsent(sample, WeakReference::new).get();
		} while (val == null);
		return val;
	}
}
