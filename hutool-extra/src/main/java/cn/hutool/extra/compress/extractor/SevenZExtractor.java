package cn.hutool.extra.compress.extractor;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrUtil;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.RandomAccess;
import java.util.function.Predicate;

/**
 * 7z格式数据解压器，即将归档打包的数据释放
 *
 * @author looly
 * @since 5.5.0
 */
public class SevenZExtractor implements Extractor, RandomAccess {

	private final SevenZFile sevenZFile;

	/**
	 * 构造
	 *
	 * @param file 包文件
	 */
	public SevenZExtractor(final File file) {
		this(file, null);
	}

	/**
	 * 构造
	 *
	 * @param file     包文件
	 * @param password 密码，null表示无密码
	 */
	public SevenZExtractor(final File file, final char[] password) {
		try {
			this.sevenZFile = new SevenZFile(file, password);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param in 包流
	 */
	public SevenZExtractor(final InputStream in) {
		this(in, null);
	}

	/**
	 * 构造
	 *
	 * @param in       包流
	 * @param password 密码，null表示无密码
	 */
	public SevenZExtractor(final InputStream in, final char[] password) {
		this(new SeekableInMemoryByteChannel(IoUtil.readBytes(in)), password);
	}

	/**
	 * 构造
	 *
	 * @param channel {@link SeekableByteChannel}
	 */
	public SevenZExtractor(final SeekableByteChannel channel) {
		this(channel, null);
	}

	/**
	 * 构造
	 *
	 * @param channel  {@link SeekableByteChannel}
	 * @param password 密码，null表示无密码
	 */
	public SevenZExtractor(final SeekableByteChannel channel, final char[] password) {
		try {
			this.sevenZFile = new SevenZFile(channel, password);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void extract(final File targetDir, final Predicate<ArchiveEntry> predicate) {
		try {
			extractInternal(targetDir, predicate);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			close();
		}
	}

	/**
	 * 获取满足指定过滤要求的压缩包内的第一个文件流
	 *
	 * @param predicate 用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}返回对应流。
	 * @return 满足过滤要求的第一个文件的流, 无满足条件的文件返回{@code null}
	 * @since 5.7.14
	 */
	public InputStream getFirst(final Predicate<ArchiveEntry> predicate) {
		final SevenZFile sevenZFile = this.sevenZFile;
		for (final SevenZArchiveEntry entry : sevenZFile.getEntries()) {
			if (null != predicate && false == predicate.test(entry)) {
				continue;
			}
			if (entry.isDirectory()) {
				continue;
			}

			try {
				return sevenZFile.getInputStream(entry);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}

		return null;
	}

	/**
	 * 获取指定名称的文件流
	 *
	 * @param entryName entry名称
	 * @return 文件流，无文件返回{@code null}
	 * @since 5.7.14
	 */
	public InputStream get(final String entryName) {
		return getFirst((entry) -> StrUtil.equals(entryName, entry.getName()));
	}

	/**
	 * 释放（解压）到指定目录
	 *
	 * @param targetDir 目标目录
	 * @param predicate 解压文件过滤器，用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}时释放。
	 * @throws IOException IO异常
	 */
	private void extractInternal(final File targetDir, final Predicate<ArchiveEntry> predicate) throws IOException {
		Assert.isTrue(null != targetDir && ((false == targetDir.exists()) || targetDir.isDirectory()), "target must be dir.");
		final SevenZFile sevenZFile = this.sevenZFile;
		SevenZArchiveEntry entry;
		File outItemFile;
		while (null != (entry = this.sevenZFile.getNextEntry())) {
			if (null != predicate && false == predicate.test(entry)) {
				continue;
			}
			outItemFile = FileUtil.file(targetDir, entry.getName());
			if (entry.isDirectory()) {
				// 创建对应目录
				//noinspection ResultOfMethodCallIgnored
				outItemFile.mkdirs();
			} else if (entry.hasStream()) {
				// 读取entry对应数据流
				FileUtil.writeFromStream(new Seven7EntryInputStream(sevenZFile, entry), outItemFile);
			} else {
				// 无数据流的文件创建为空文件
				FileUtil.touch(outItemFile);
			}
		}
	}

	@Override
	public void close() {
		IoUtil.close(this.sevenZFile);
	}
}
