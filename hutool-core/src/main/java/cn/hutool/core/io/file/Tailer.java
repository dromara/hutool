package cn.hutool.core.io.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 文件内容跟随器，实现类似Linux下"tail -f"命令功能
 * 
 * @author looly
 * @since 4.5.2
 */
public class Tailer {
	
	public static final LineHandler CONSOLE_HANDLER = new ConsoleLineHandler();

	/** 编码 */
	private Charset charset;
	/** 行处理器 */
	private LineHandler lineHandler;
	/** 初始读取的行数 */
	private int initReadLine;

	private RandomAccessFile randomAccessFile;
	private WatchMonitor monitor;

	/**
	 * 构造，默认UTF-8编码
	 * 
	 * @param file 文件
	 * @param lineHandler 行处理器
	 */
	public Tailer(File file, LineHandler lineHandler) {
		this(file, lineHandler, 0);
	}

	/**
	 * 构造，默认UTF-8编码
	 * 
	 * @param file 文件
	 * @param lineHandler 行处理器
	 * @param initReadLine 启动时预读取的行数
	 */
	public Tailer(File file, LineHandler lineHandler, int initReadLine) {
		this(file, CharsetUtil.CHARSET_UTF_8, lineHandler, initReadLine);
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 */
	public Tailer(File file, Charset charset, LineHandler lineHandler) {
		this(file, charset, lineHandler, 0);
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 * @param initReadLine 启动时预读取的行数
	 */
	public Tailer(File file, Charset charset, LineHandler lineHandler, int initReadLine) {
		checkFile(file);
		this.charset = charset;
		this.lineHandler = lineHandler;
		this.randomAccessFile = FileUtil.createRandomAccessFile(file, FileMode.r);
		this.monitor = WatchUtil.create(file, WatchMonitor.ENTRY_MODIFY);
		this.initReadLine = initReadLine;
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
		// 初始读取
		try {
			this.readTail();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		final WatchMonitor monitor = this.monitor.setWatcher(new LineReadWatcher(this.randomAccessFile, this.charset, this.lineHandler));
		if (async) {
			ThreadUtil.execute(monitor);
		} else {
			monitor.watch();
		}
	}

	// ---------------------------------------------------------------------------------------- Private method start
	/**
	 * 预读取行
	 * 
	 * @throws IOException
	 */
	private void readTail() throws IOException {
		final long len = this.randomAccessFile.length();

		if (initReadLine > 0) {
			long start = this.randomAccessFile.getFilePointer();
			long nextEnd = len - 1;
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
					FileUtil.readLine(this.randomAccessFile, this.charset, this.lineHandler);
					currentLine++;
					nextEnd--;
				}
				nextEnd--;
				this.randomAccessFile.seek(nextEnd);
				if (nextEnd == 0) {
					// 当文件指针退至文件开始处，输出第一行
					FileUtil.readLine(this.randomAccessFile, this.charset, this.lineHandler);
					break;
				}
			}
		}

		// 将指针置于末尾
		try {
			this.randomAccessFile.seek(len);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 检查文件有效性
	 * 
	 * @param file 文件
	 */
	private static void checkFile(File file) {
		if (false == file.exists()) {
			throw new UtilException("File [{}] not exist !", file.getAbsolutePath());
		}
		if (false == file.isFile()) {
			throw new UtilException("Path [{}] is not a file !", file.getAbsolutePath());
		}
	}
	// ---------------------------------------------------------------------------------------- Private method end

	/**
	 * 命令行打印的行处理器
	 * 
	 * @author looly
	 *@since 4.5.2
	 */
	public static class ConsoleLineHandler implements LineHandler {
		@Override
		public void handle(String line) {
			if (StrUtil.isEmpty(line)) {
				Console.log();
			}
			Console.print("{}", line);
		}
	}
}
