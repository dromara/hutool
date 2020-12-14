package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.watch.SimpleWatcher;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 行处理的Watcher实现
 * 
 * @author looly
 * @since 4.5.2
 */
public class LineReadWatcher extends SimpleWatcher implements Runnable {

	private final RandomAccessFile randomAccessFile;
	private final Charset charset;
	private final LineHandler lineHandler;

	/**
	 * 构造
	 * 
	 * @param randomAccessFile {@link RandomAccessFile}
	 * @param charset 编码
	 * @param lineHandler 行处理器{@link LineHandler}实现
	 */
	public LineReadWatcher(RandomAccessFile randomAccessFile, Charset charset, LineHandler lineHandler) {
		this.randomAccessFile = randomAccessFile;
		this.charset = charset;
		this.lineHandler = lineHandler;
	}

	@Override
	public void run() {
		onModify(null, null);
	}

	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
		final RandomAccessFile randomAccessFile = this.randomAccessFile;
		final Charset charset = this.charset;
		final LineHandler lineHandler = this.lineHandler;

		try {
			final long currentLength = randomAccessFile.length();
			final long position = randomAccessFile.getFilePointer();
			if (0 == currentLength || position == currentLength) {
				// 内容长度不变时忽略此次事件
				return;
			} else if (currentLength < position) {
				// 如果内容变短，说明文件做了删改，回到内容末尾
				randomAccessFile.seek(currentLength);
				return;
			}

			// 读取行
			FileUtil.readLines(randomAccessFile, charset, lineHandler);

			// 记录当前读到的位置
			randomAccessFile.seek(currentLength);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
