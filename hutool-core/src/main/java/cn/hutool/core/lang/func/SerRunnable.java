package cn.hutool.core.lang.func;


import cn.hutool.core.exceptions.UtilException;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao
 * @see Runnable
 */
@FunctionalInterface
public interface SerRunnable extends Runnable, Serializable {

	/**
	 * When an object implementing interface {@code Runnable} is used
	 * to create a thread, starting the thread causes the object's
	 * {@code run} method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method {@code run} is that it may
	 * take any action whatsoever.
	 *
	 * @throws Exception wrapped checked exceptions
	 * @see Thread#run()
	 */
	void running() throws Exception;

	/**
	 * When an object implementing interface {@code Runnable} is used
	 * to create a thread, starting the thread causes the object's
	 * {@code run} method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method {@code run} is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	default void run() {
		try {
			running();
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * multi
	 *
	 * @param serRunnableArray lambda
	 * @return lambda
	 */
	static SerRunnable multi(final SerRunnable... serRunnableArray) {
		return () -> Stream.of(serRunnableArray).forEach(SerRunnable::run);
	}

}
