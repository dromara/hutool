/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.compress.extractor;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
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

	@Override
	public InputStream getFirst(final Predicate<ArchiveEntry> predicate) {
		final SevenZFile sevenZFile = this.sevenZFile;
		for (final SevenZArchiveEntry entry : sevenZFile.getEntries()) {
			if (null != predicate && ! predicate.test(entry)) {
				continue;
			}
			if (entry.isDirectory()) {
				continue;
			}

			try {
				// 此处使用查找entry对应Stream方式，由于只调用一次，也只遍历一次
				return sevenZFile.getInputStream(entry);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}

		return null;
	}

	/**
	 * 释放（解压）到指定目录
	 *
	 * @param targetDir 目标目录
	 * @param predicate 解压文件过滤器，用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}时释放。
	 * @throws IOException IO异常
	 */
	private void extractInternal(final File targetDir, final Predicate<ArchiveEntry> predicate) throws IOException {
		Assert.isTrue(null != targetDir && ((! targetDir.exists()) || targetDir.isDirectory()), "target must be dir.");
		final SevenZFile sevenZFile = this.sevenZFile;
		SevenZArchiveEntry entry;
		File outItemFile;
		while (null != (entry = sevenZFile.getNextEntry())) {
			if (null != predicate && ! predicate.test(entry)) {
				continue;
			}
			outItemFile = FileUtil.file(targetDir, entry.getName());
			if (entry.isDirectory()) {
				// 创建对应目录
				//noinspection ResultOfMethodCallIgnored
				outItemFile.mkdirs();
			} else if (entry.hasStream()) {
				// 读取entry对应数据流
				// 此处直接读取而非调用sevenZFile.getInputStream(entry)，因为此方法需要遍历查找entry对应位置，性能不好。
				FileUtil.writeFromStream(new Seven7EntryInputStream(sevenZFile, entry), outItemFile);
			} else {
				// 无数据流的文件创建为空文件
				FileUtil.touch(outItemFile);
			}
		}
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.sevenZFile);
	}
}
