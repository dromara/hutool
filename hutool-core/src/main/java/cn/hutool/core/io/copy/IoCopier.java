package cn.hutool.core.io.copy;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.StreamProgress;

/**
 * IO拷贝抽象，可自定义包括缓存、进度条等信息<br>
 * 此对象非线程安全
 *
 * @param <S> 拷贝源类型，如InputStream、Reader等
 * @param <T> 拷贝目标类型，如OutputStream、Writer等
 * @author looly
 * @since 5.7.8
 */
public abstract class IoCopier<S, T> {

	protected final int bufferSize;
	/**
	 * 拷贝总数
	 */
	protected final long count;

	/**
	 * 进度条
	 */
	protected StreamProgress progress;

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小，&lt; 0 表示默认{@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * @param count      拷贝总数，-1表示无限制
	 * @param progress   进度条
	 */
	public IoCopier(int bufferSize, long count, StreamProgress progress) {
		this.bufferSize = bufferSize > 0 ? bufferSize : IoUtil.DEFAULT_BUFFER_SIZE;
		this.count = count <= 0 ? Long.MAX_VALUE : count;
		this.progress = progress;
	}

	/**
	 * 执行拷贝
	 *
	 * @param source 拷贝源，如InputStream、Reader等
	 * @param target 拷贝目标，如OutputStream、Writer等
	 * @return 拷贝的实际长度
	 */
	public abstract long copy(S source, T target);

	/**
	 * 缓存大小，取默认缓存和目标长度最小值
	 *
	 * @param count 目标长度
	 * @return 缓存大小
	 */
	protected int bufferSize(long count) {
		return (int) Math.min(this.bufferSize, count);
	}
}
