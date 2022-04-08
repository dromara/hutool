package cn.hutool.core.io.file;

import cn.hutool.core.thread.lock.LockUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 文件追加器<br>
 * 持有一个文件，在内存中积累一定量的数据后统一追加到文件<br>
 * 此类只有在写入文件时打开文件，并在写入结束后关闭之。因此此类不需要关闭<br>
 * 在调用append方法后会缓存于内存，只有超过容量后才会一次性写入文件，因此内存中随时有剩余未写入文件的内容，在最后必须调用flush方法将剩余内容刷入文件
 *
 * @author looly
 * @since 3.1.2
 */
public class FileAppender implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 写出锁，用于保护写出线程安全
	 */
	private final Lock lock;

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param capacity      当行数积累多少条时刷入到文件
	 * @param isNewLineMode 追加内容是否为新行
	 */
	public FileAppender(File destFile, int capacity, boolean isNewLineMode) {
		this(destFile, CharsetUtil.CHARSET_UTF_8, capacity, isNewLineMode);
	}

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param charset       编码
	 * @param capacity      当行数积累多少条时刷入到文件
	 * @param isNewLineMode 追加内容是否为新行
	 */
	public FileAppender(File destFile, Charset charset, int capacity, boolean isNewLineMode) {
		this(destFile, charset, capacity, isNewLineMode, null);
	}

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param charset       编码
	 * @param capacity      当行数积累多少条时刷入到文件
	 * @param isNewLineMode 追加内容是否为新行
	 * @param lock          是否加锁，添加则使用给定锁保护写出，保证线程安全，{@code null}则表示无锁
	 */
	public FileAppender(File destFile, Charset charset, int capacity, boolean isNewLineMode, Lock lock) {
		this.capacity = capacity;
		this.list = new ArrayList<>(capacity);
		this.isNewLineMode = isNewLineMode;
		this.writer = FileWriter.create(destFile, charset);
		this.lock = ObjectUtil.defaultIfNull(lock, LockUtil::getNoLock);
	}

	/**
	 * 追加
	 *
	 * @param line 行
	 * @return this
	 */
	public FileAppender append(String line) {
		if (list.size() >= capacity) {
			flush();
		}

		this.lock.lock();
		try{
			list.add(line);
		} finally {
			this.lock.unlock();
		}
		return this;
	}

	/**
	 * 刷入到文件
	 *
	 * @return this
	 */
	public FileAppender flush() {
		this.lock.lock();
		try{
			try (PrintWriter pw = writer.getPrintWriter(true)) {
				for (String str : list) {
					pw.print(str);
					if (isNewLineMode) {
						pw.println();
					}
				}
			}
			list.clear();
		} finally {
			this.lock.unlock();
		}
		return this;
	}
}
