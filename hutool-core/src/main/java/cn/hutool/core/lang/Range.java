package cn.hutool.core.lang;

import cn.hutool.core.thread.lock.NoLock;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 范围生成器。根据给定的初始值、结束值和步进生成一个步进列表生成器<br>
 * 由于用户自行实现{@link Steper}来定义步进，因此Range本身无法判定边界（是否达到end），需在step实现边界判定逻辑。
 * 
 * <p>
 * 此类使用{@link ReentrantReadWriteLock}保证线程安全
 * </p>
 * 
 * @author Looly
 *
 * @param <T> 生成范围对象的类型
 */
public class Range<T> implements Iterable<T>, Iterator<T>, Serializable {
	private static final long serialVersionUID = 1L;

	/** 锁保证线程安全 */
	private Lock lock = new ReentrantLock();
	/** 起始对象 */
	private final T start;
	/** 结束对象 */
	private final T end;
	/** 当前对象 */
	private T current;
	/** 下一个对象 */
	private T next;
	/** 步进 */
	private final Steper<T> steper;
	/** 索引 */
	private int index = 0;
	/** 是否包含第一个元素 */
	private final boolean includeStart;
	/** 是否包含最后一个元素 */
	private boolean includeEnd;

	/**
	 * 构造
	 * 
	 * @param start 起始对象
	 * @param steper 步进
	 */
	public Range(T start, Steper<T> steper) {
		this(start, null, steper);
	}

	/**
	 * 构造
	 * 
	 * @param start 起始对象（包含）
	 * @param end 结束对象（包含）
	 * @param steper 步进
	 */
	public Range(T start, T end, Steper<T> steper) {
		this(start, end, steper, true, true);
	}

	/**
	 * 构造
	 * 
	 * @param start 起始对象
	 * @param end 结束对象
	 * @param steper 步进
	 * @param isIncludeStart 是否包含第一个元素
	 * @param isIncludeEnd 是否包含最后一个元素
	 */
	public Range(T start, T end, Steper<T> steper, boolean isIncludeStart, boolean isIncludeEnd) {
		this.start = start;
		this.current = start;
		this.end = end;
		this.steper = steper;
		this.next = safeStep(this.current);
		this.includeStart = isIncludeStart;
		includeEnd = true;
		this.includeEnd = isIncludeEnd;
	}

	/**
	 * 禁用锁，调用此方法后不在 使用锁保护
	 * 
	 * @return this
	 * @since 4.3.1
	 */
	public Range<T> disableLock() {
		this.lock = new NoLock();
		return this;
	}

	@Override
	public boolean hasNext() {
		lock.lock();
		try {
			if(0 == this.index && this.includeStart) {
				return true;
			}
			if (null == this.next) {
				return false;
			} else if (false == includeEnd && this.next.equals(this.end)) {
				return false;
			}
		} finally {
			lock.unlock();
		}
		return true;
	}

	@Override
	public T next() {
		lock.lock();
		try {
			if (false == this.hasNext()) {
				throw new NoSuchElementException("Has no next range!");
			}
			return nextUncheck();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取下一个元素，并将下下个元素准备好
	 */
	private T nextUncheck() {
		if (0 != this.index || false == this.includeStart) {
			// 非第一个元素或不包含第一个元素增加步进
			this.current = this.next;
			if (null != this.current) {
				this.next = safeStep(this.next);
			}
		}
		index++;
		return this.current;
	}

	/**
	 * 不抛异常的获取下一步进的元素，如果获取失败返回{@code null}
	 * 
	 * @param base 上一个元素
	 * @return 下一步进
	 */
	private T safeStep(T base) {
		T next = null;
		try {
			next = steper.step(base, this.end, this.index);
		} catch (Exception e) {
			// ignore
		}
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Can not remove ranged element!");
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * 重置{@link Range}
	 * 
	 * @return this
	 */
	public Range<T> reset() {
		lock.lock();
		try {
			this.current = this.start;
			this.index = 0;
		} finally {
			lock.unlock();
		}
		return this;
	}

	/**
	 * 步进接口，此接口用于实现如何对一个对象按照指定步进增加步进<br>
	 * 步进接口可以定义以下逻辑：
	 * 
	 * <pre>
	 * 1、步进规则，即对象如何做步进
	 * 2、步进大小，通过实现此接口，在实现类中定义一个对象属性，可灵活定义步进大小
	 * 3、限制range个数，通过实现此接口，在实现类中定义一个对象属性，可灵活定义limit，限制range个数
	 * </pre>
	 * 
	 * @author Looly
	 *
	 * @param <T> 需要增加步进的对象
	 */
	public interface Steper<T> {
		/**
		 * 增加步进<br>
		 * 增加步进后的返回值如果为{@code null}则表示步进结束<br>
		 * 用户需根据end参数自行定义边界，当达到边界时返回null表示结束，否则Range中边界对象无效，会导致无限循环
		 * 
		 * @param current 上一次增加步进后的基础对象
		 * @param end 结束对象
		 * @param index 当前索引（步进到第几个元素），从0开始计数
		 * @return 增加步进后的对象
		 */
		T step(T current, T end, int index);
	}
}
