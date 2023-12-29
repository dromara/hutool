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

import org.dromara.hutool.core.date.DateUnit;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.func.SerConsumer;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.watch.WatchKind;
import org.dromara.hutool.core.io.watch.WatchMonitor;
import org.dromara.hutool.core.io.watch.WatchUtil;
import org.dromara.hutool.core.io.watch.watchers.SimpleWatcher;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Stack;
import java.util.concurrent.*;

/**
 * 文件内容跟随器，实现类似Linux下"tail -f"命令功能
 *
 * @author looly
 * @since 4.5.2
 */
public class Tailer implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 控制台打印的处理类
	 */
	public static final SerConsumer<String> CONSOLE_HANDLER = new ConsoleLineHandler();

	/** 编码 */
	private final Charset charset;
	/** 行处理器 */
	private final SerConsumer<String> lineHandler;
	/** 初始读取的行数 */
	private final int initReadLine;
	/** 定时任务检查间隔时长 */
	private final long period;

	private final String filePath;
	private final RandomAccessFile randomAccessFile;
	private final ScheduledExecutorService executorService;
	private WatchMonitor fileDeleteWatchMonitor;

	private boolean stopOnDelete;

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param file 文件
	 * @param lineHandler 行处理器
	 */
	public Tailer(final File file, final SerConsumer<String> lineHandler) {
		this(file, lineHandler, 0);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param file 文件
	 * @param lineHandler 行处理器
	 * @param initReadLine 启动时预读取的行数
	 */
	public Tailer(final File file, final SerConsumer<String> lineHandler, final int initReadLine) {
		this(file, CharsetUtil.UTF_8, lineHandler, initReadLine, DateUnit.SECOND.getMillis());
	}

	/**
	 * 构造
	 *
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 */
	public Tailer(final File file, final Charset charset, final SerConsumer<String> lineHandler) {
		this(file, charset, lineHandler, 0, DateUnit.SECOND.getMillis());
	}

	/**
	 * 构造
	 *
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 * @param initReadLine 启动时预读取的行数
	 * @param period 检查间隔
	 */
	public Tailer(final File file, final Charset charset, final SerConsumer<String> lineHandler, final int initReadLine, final long period) {
		checkFile(file);
		this.filePath = file.getAbsolutePath();
		this.charset = charset;
		this.lineHandler = lineHandler;
		this.period = period;
		this.initReadLine = initReadLine;
		this.randomAccessFile = FileUtil.createRandomAccessFile(file, FileMode.r);
		this.executorService = Executors.newSingleThreadScheduledExecutor();
	}

	/**
	 * 设置删除文件后是否退出并抛出异常
	 *
	 * @param stopOnDelete 删除文件后是否退出并抛出异常
	 */
	public void setStopOnDelete(final boolean stopOnDelete) {
		this.stopOnDelete = stopOnDelete;
	}

	/**
	 * 开始监听
	 */
	public void start() {
		start(false);
	}

	/**
	 * 开始监听
	 *
	 * @param async 是否异步执行
	 */
	public void start(final boolean async) {
		// 初始读取
		try {
			this.readTail();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		final LineReadWatcher lineReadWatcher = new LineReadWatcher(this.randomAccessFile, this.charset, this.lineHandler);
		final ScheduledFuture<?> scheduledFuture = this.executorService.scheduleAtFixedRate(//
				lineReadWatcher, //
				0, //
				this.period, TimeUnit.MILLISECONDS//
		);

		// 监听删除
		if(stopOnDelete){
			fileDeleteWatchMonitor = WatchUtil.of(this.filePath, WatchKind.DELETE.getValue());
			fileDeleteWatchMonitor.setWatcher(new SimpleWatcher(){
				private static final long serialVersionUID = 4497160994840060329L;

				@Override
				public void onDelete(final WatchEvent<?> event, final WatchKey key) {
					super.onDelete(event, key);
					stop();
					throw new IORuntimeException("{} has been deleted", filePath);
				}
			});
			fileDeleteWatchMonitor.start();
		}

		if (!async) {
			try {
				scheduledFuture.get();
			} catch (final ExecutionException e) {
				throw new HutoolException(e);
			} catch (final InterruptedException e) {
				// ignore and exist
			}
		}
	}

	/**
	 * 结束，此方法需在异步模式或
	 */
	public void stop(){
		try{
			this.executorService.shutdown();
		} finally {
			IoUtil.closeQuietly(this.randomAccessFile);
			IoUtil.closeQuietly(this.fileDeleteWatchMonitor);
		}
	}

	// ---------------------------------------------------------------------------------------- Private method start
	/**
	 * 预读取行
	 *
	 * @throws IOException IO异常
	 */
	private void readTail() throws IOException {
		final long len = this.randomAccessFile.length();

		if (initReadLine > 0) {
			final Stack<String> stack = new Stack<>();

			final long start = this.randomAccessFile.getFilePointer();
			long nextEnd = (len - 1) < 0 ? 0 : len - 1;
			this.randomAccessFile.seek(nextEnd);
			int c;
			int currentLine = 0;
			while (nextEnd > start) {
				// 满
				if (currentLine > initReadLine) {
					break;
				}

				c = this.randomAccessFile.read();
				if (c == CharUtil.LF || c == CharUtil.CR) {
					// FileUtil.readLine(this.randomAccessFile, this.charset, this.lineHandler);
					final String line = FileUtil.readLine(this.randomAccessFile, this.charset);
					if(null != line) {
						stack.push(line);
					}
					currentLine++;
					nextEnd--;
				}
				nextEnd--;
				this.randomAccessFile.seek(nextEnd);
				if (nextEnd == 0) {
					// 当文件指针退至文件开始处，输出第一行
					// FileUtil.readLine(this.randomAccessFile, this.charset, this.lineHandler);
					final String line = FileUtil.readLine(this.randomAccessFile, this.charset);
					if(null != line) {
						stack.push(line);
					}
					break;
				}
			}

			// 输出缓存栈中的内容
			while (!stack.isEmpty()) {
				this.lineHandler.accept(stack.pop());
			}
		}

		// 将指针置于末尾
		try {
			this.randomAccessFile.seek(len);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 检查文件有效性
	 *
	 * @param file 文件
	 */
	private static void checkFile(final File file) {
		if (!file.exists()) {
			throw new HutoolException("File [{}] not exist !", file.getAbsolutePath());
		}
		if (!file.isFile()) {
			throw new HutoolException("Path [{}] is not a file !", file.getAbsolutePath());
		}
	}
	// ---------------------------------------------------------------------------------------- Private method end

	/**
	 * 命令行打印的行处理器
	 *
	 * @author looly
	 * @since 4.5.2
	 */
	public static class ConsoleLineHandler implements SerConsumer<String> {
		private static final long serialVersionUID = 1L;
		@Override
		public void accepting(final String line) {
			Console.log(line);
		}
	}

}
