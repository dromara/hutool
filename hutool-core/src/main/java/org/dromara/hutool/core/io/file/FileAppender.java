/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.thread.lock.LockUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;

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
	public FileAppender(final File destFile, final int capacity, final boolean isNewLineMode) {
		this(destFile, CharsetUtil.UTF_8, capacity, isNewLineMode);
	}

	/**
	 * 构造
	 *
	 * @param destFile      目标文件
	 * @param charset       编码
	 * @param capacity      当行数积累多少条时刷入到文件
	 * @param isNewLineMode 追加内容是否为新行
	 */
	public FileAppender(final File destFile, final Charset charset, final int capacity, final boolean isNewLineMode) {
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
	public FileAppender(final File destFile, final Charset charset, final int capacity, final boolean isNewLineMode, final Lock lock) {
		this.capacity = capacity;
		this.list = new ArrayList<>(capacity);
		this.isNewLineMode = isNewLineMode;
		this.writer = FileWriter.of(destFile, charset);
		this.lock = ObjUtil.defaultIfNull(lock, LockUtil::getNoLock);
	}

	/**
	 * 追加
	 *
	 * @param line 行
	 * @return this
	 */
	public FileAppender append(final String line) {
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
			try (final PrintWriter pw = writer.getPrintWriter(true)) {
				for (final String str : list) {
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
