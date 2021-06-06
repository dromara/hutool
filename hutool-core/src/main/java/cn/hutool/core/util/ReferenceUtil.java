package cn.hutool.core.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 引用工具类，主要针对{@link Reference} 工具化封装<br>
 * 主要封装包括：
 * <pre>
 * 1. {@link SoftReference} 软引用，在GC报告内存不足时会被GC回收
 * 2. {@link WeakReference} 弱引用，在GC时发现弱引用会回收其对象
 * 3. {@link PhantomReference} 虚引用，在GC时发现虚引用对象，会将{@link PhantomReference}插入{@link ReferenceQueue}。 此时对象未被真正回收，要等到{@link ReferenceQueue}被真正处理后才会被回收。
 * </pre>
 *
 * @author looly
 * @since 3.1.2
 */
public class ReferenceUtil {

	/**
	 * 获得引用
	 *
	 * @param <T> 被引用对象类型
	 * @param type 引用类型枚举
	 * @param referent 被引用对象
	 * @return {@link Reference}
	 */
	public static <T> Reference<T> create(ReferenceType type, T referent) {
		return create(type, referent, null);
	}

	/**
	 * 获得引用
	 *
	 * @param <T> 被引用对象类型
	 * @param type 引用类型枚举
	 * @param referent 被引用对象
	 * @param queue 引用队列
	 * @return {@link Reference}
	 */
	public static <T> Reference<T> create(ReferenceType type, T referent, ReferenceQueue<T> queue) {
		switch (type) {
		case SOFT:
			return new SoftReference<>(referent);
		case WEAK:
			return new WeakReference<>(referent);
		case PHANTOM:
			return new PhantomReference<>(referent, queue);
		default:
			return null;
		}
	}

	/**
	 * 引用类型
	 *
	 * @author looly
	 *
	 */
	public enum ReferenceType {
		/** 软引用，在GC报告内存不足时会被GC回收 */
		SOFT,
		/** 弱引用，在GC时发现弱引用会回收其对象 */
		WEAK,
		/**
		 * 虚引用，在GC时发现虚引用对象，会将{@link PhantomReference}插入{@link ReferenceQueue}。 <br>
		 * 此时对象未被真正回收，要等到{@link ReferenceQueue}被真正处理后才会被回收。
		 */
		PHANTOM
	}

}
