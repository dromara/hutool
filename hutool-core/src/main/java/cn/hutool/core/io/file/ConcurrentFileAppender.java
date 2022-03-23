package cn.hutool.core.io.file;


import cn.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程安全的文件追加器<br>
 * 持有一个文件，在内存中积累一定量的数据后统一追加到文件,但不保证具体写入顺序<br>
 * 此类只有在写入文件时打开文件，并在写入结束后关闭之。因此此类不需要关闭<br>
 * 在调用append方法后会缓存于内存，只有超过容量后才会一次性写入文件，因此内存中随时有剩余未写入文件的内容，在最后必须调用flush方法将剩余内容刷入文件
 * 此类适用于多线程计算append内容，而不适用于多线程同时append(虽然仍然是安全的但性能远不如{@link FileAppender})
 *
 * @author gxz gongxuanzhang@foxmail.com
 * @see FileAppender
 * @since 5.7.234
 **/
public class ConcurrentFileAppender {


	private final FileWriter writer;
	/**
	 * 内存中持有的字符串数
	 */
	private final int capacity;
	/**
	 * 追加内容是否为新行
	 */
	private final boolean isNewLineMode;
	/**
	 * 数据行缓存
	 */
	private final List<String> list;

	/**
	 * 读写锁
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/**
	 * 读锁
	 */
	private final Lock readLock = readWriteLock.readLock();
	/**
	 * 写锁
	 */
	private final Lock writeLock = readWriteLock.writeLock();
	/**
	 * 防止多线程导致的容量扩容因子
	 */
	private static final float CAPACITY_FACTOR = 1.2f;

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param capacity      当行数积累多少条时刷入到文件,只能尽量保证在到达数量时刷入
	 * @param isNewLineMode 追加内容是否为新行
	 */
	public ConcurrentFileAppender(File destFile, int capacity, boolean isNewLineMode) {
		this(destFile, CharsetUtil.CHARSET_UTF_8, capacity, isNewLineMode);
	}

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param charset       编码
	 * @param capacity      当行数积累多少条时刷入到文件,只能尽量保证在到达数量时刷入
	 * @param isNewLineMode 追加内容是否为新行
	 */
	public ConcurrentFileAppender(File destFile, Charset charset, int capacity, boolean isNewLineMode) {
		this.capacity = capacity;
		this.list = Collections.synchronizedList(new ArrayList<>((int) (capacity * CAPACITY_FACTOR)));
		this.isNewLineMode = isNewLineMode;
		this.writer = FileWriter.create(destFile, charset);
	}

	/**
	 * 追加
	 *
	 * @param line 行
	 * @return this
	 */
	public ConcurrentFileAppender append(String line) {

		readLock.lock();
		try {
			list.add(line);
		} finally {
			readLock.unlock();
		}
		if (list.size() >= capacity) {
			flush();
		}
		return this;
	}

	/**
	 * 刷入到文件
	 *
	 * @return this
	 */
	public ConcurrentFileAppender flush() {
		try {
			writeLock.lock();
			try (PrintWriter pw = writer.getPrintWriter(true)) {
				for (String str : list) {
					pw.print(str);
					if (isNewLineMode) {
						pw.println();
					}
				}
			}
			list.clear();
			return this;
		} finally {
			writeLock.unlock();
		}

	}


}
