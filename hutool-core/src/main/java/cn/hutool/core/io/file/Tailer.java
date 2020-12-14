package cn.hutool.core.io.file;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 文件内容跟随器，实现类似Linux下"tail -f"命令功能
 * 
 * @author looly
 * @since 4.5.2
 */
public class Tailer implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final LineHandler CONSOLE_HANDLER = new ConsoleLineHandler();

	/** 编码 */
	private final Charset charset;
	/** 行处理器 */
	private final LineHandler lineHandler;
	/** 初始读取的行数 */
	private final int initReadLine;
	/** 定时任务检查间隔时长 */
	private final long period;

	private final RandomAccessFile randomAccessFile;
	private final ScheduledExecutorService executorService;

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
		this(file, CharsetUtil.CHARSET_UTF_8, lineHandler, initReadLine, DateUnit.SECOND.getMillis());
	}

	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler 行处理器
	 */
	public Tailer(File file, Charset charset, LineHandler lineHandler) {
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
	public Tailer(File file, Charset charset, LineHandler lineHandler, int initReadLine, long period) {
		checkFile(file);
		this.charset = charset;
		this.lineHandler = lineHandler;
		this.period = period;
		this.initReadLine = initReadLine;
		this.randomAccessFile = FileUtil.createRandomAccessFile(file, FileMode.r);
		this.executorService = Executors.newSingleThreadScheduledExecutor();
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

		final LineReadWatcher lineReadWatcher = new LineReadWatcher(this.randomAccessFile, this.charset, this.lineHandler);
		final ScheduledFuture<?> scheduledFuture = this.executorService.scheduleAtFixedRate(//
				lineReadWatcher, //
				0, //
				this.period, TimeUnit.MILLISECONDS//
		);

		if (false == async) {
			try {
				scheduledFuture.get();
			} catch (ExecutionException e) {
				throw new UtilException(e);
			} catch (InterruptedException e) {
				// ignore and exist
			}
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
			Stack<String> stack = new Stack<>();

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
			while (false == stack.isEmpty()) {
				this.lineHandler.handle(stack.pop());
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
	 * @since 4.5.2
	 */
	public static class ConsoleLineHandler implements LineHandler {
		@Override
		public void handle(String line) {
			Console.log(line);
		}
	}
}
