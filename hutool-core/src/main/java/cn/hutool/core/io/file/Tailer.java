package cn.hutool.core.io.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;

/**
 * 文件内容跟随器，实现类似Linux下"tail -f"命令功能
 * 
 * @author looly
 * @since 4.5.2
 */
public class Tailer {

	private RandomAccessFile randomAccessFile;
	private WatchMonitor monitor;

	/**
	 * 构造，默认UTF-8编码
	 * 
	 * @param file 文件
	 * @param lineHandler 行处理器
	 */
	public Tailer(File file, LineHandler lineHandler) {
		this(file, CharsetUtil.CHARSET_UTF_8, lineHandler);
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 */
	public Tailer(File file, Charset charset, LineHandler lineHandler) {
		init(file, charset, lineHandler);
	}

	/**
	 * 初始化
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 * @return this
	 */
	public Tailer init(File file, final Charset charset, final LineHandler lineHandler) {
		if (false == file.exists()) {
			throw new UtilException("File [{}] not exist !", file.getAbsolutePath());
		}
		if (false == file.isFile()) {
			throw new UtilException("Path [{}] is not a file !", file.getAbsolutePath());
		}
		this.randomAccessFile = FileUtil.createRandomAccessFile(file, FileMode.r);
		// 将指针置于末尾
		try {
			this.randomAccessFile.seek(randomAccessFile.length());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		this.monitor = WatchUtil.create(file, WatchMonitor.ENTRY_MODIFY);
		this.monitor.setWatcher(new SimpleWatcher() {
			@Override
			public void onModify(WatchEvent<?> event, Path currentPath) {
				final RandomAccessFile file = Tailer.this.randomAccessFile;
				try {
					final long currentLength = file.length();
					final long position = file.getFilePointer();
					if (0 == currentLength || currentLength == position) {
						// 内容长度不变时忽略此次事件
						return;
					} else if (currentLength < position) {
						// 如果内容变短，说明文件做了删改，回到内容末尾
						file.seek(currentLength);
						return;
					}

					FileUtil.readLines(file, charset, lineHandler);

					// 记录当前读到的位置
					file.seek(currentLength);
				} catch (IOException e) {
					throw new IORuntimeException(e);
				}
			}
		});

		return this;
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
	public void start(boolean async) {
		if (async) {
			ThreadUtil.execute(this.monitor);
		} else {
			this.monitor.watch();
		}
	}
}
