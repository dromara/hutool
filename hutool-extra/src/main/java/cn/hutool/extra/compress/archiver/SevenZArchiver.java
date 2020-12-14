package cn.hutool.extra.compress.archiver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;

/**
 * 7zip格式的归档封装
 *
 * @author looly
 */
public class SevenZArchiver implements Archiver {

	private final SevenZOutputFile sevenZOutputFile;

	private SeekableByteChannel channel;
	private OutputStream out;

	/**
	 * 构造
	 *
	 * @param file 归档输出的文件
	 */
	public SevenZArchiver(File file) {
		try {
			this.sevenZOutputFile = new SevenZOutputFile(file);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param out 归档输出的流
	 */
	public SevenZArchiver(OutputStream out) {
		this.out = out;
		this.channel = new SeekableInMemoryByteChannel();
		try {
			this.sevenZOutputFile = new SevenZOutputFile(channel);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param channel 归档输出的文件
	 */
	public SevenZArchiver(SeekableByteChannel channel) {
		try {
			this.sevenZOutputFile = new SevenZOutputFile(channel);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取{@link SevenZOutputFile}以便自定义相关设置
	 *
	 * @return {@link SevenZOutputFile}
	 */
	public SevenZOutputFile getSevenZOutputFile() {
		return this.sevenZOutputFile;
	}

	@Override
	public SevenZArchiver add(File file, String path, Filter<File> filter) {
		try {
			addInternal(file, path, filter);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public SevenZArchiver finish() {
		try {
			this.sevenZOutputFile.finish();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			finish();
		} catch (Exception ignore) {
			//ignore
		}
		if (null != out && this.channel instanceof SeekableInMemoryByteChannel) {
			try {
				out.write(((SeekableInMemoryByteChannel) this.channel).array());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		IoUtil.close(this.sevenZOutputFile);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Filter#accept(Object)}为true时加入。
	 */
	private void addInternal(File file, String path, Filter<File> filter) throws IOException {
		if (null != filter && false == filter.accept(file)) {
			return;
		}
		final SevenZOutputFile out = this.sevenZOutputFile;

		final String entryName = StrUtil.addSuffixIfNot(StrUtil.nullToEmpty(path), StrUtil.SLASH) + file.getName();
		out.putArchiveEntry(out.createArchiveEntry(file, entryName));

		if (file.isDirectory()) {
			// 目录遍历写入
			final File[] files = file.listFiles();
			for (File childFile : files) {
				addInternal(childFile, entryName, filter);
			}
		} else {
			if (file.isFile()) {
				// 文件直接写入
				out.write(FileUtil.readBytes(file));
			}
			out.closeArchiveEntry();
		}
	}
}
