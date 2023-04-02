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

package org.dromara.hutool.compress.archiver;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.function.Predicate;

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
	public SevenZArchiver(final File file) {
		try {
			this.sevenZOutputFile = new SevenZOutputFile(file);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param out 归档输出的流
	 */
	public SevenZArchiver(final OutputStream out) {
		this.out = out;
		this.channel = new SeekableInMemoryByteChannel();
		try {
			this.sevenZOutputFile = new SevenZOutputFile(channel);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 构造
	 *
	 * @param channel 归档输出的文件
	 */
	public SevenZArchiver(final SeekableByteChannel channel) {
		try {
			this.sevenZOutputFile = new SevenZOutputFile(channel);
		} catch (final IOException e) {
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
	public SevenZArchiver add(final File file, final String path, final Predicate<File> filter) {
		try {
			addInternal(file, path, filter);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public SevenZArchiver finish() {
		try {
			this.sevenZOutputFile.finish();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			finish();
		} catch (final Exception ignore) {
			//ignore
		}
		if (null != out && this.channel instanceof SeekableInMemoryByteChannel) {
			try {
				out.write(((SeekableInMemoryByteChannel) this.channel).array());
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		IoUtil.closeQuietly(this.sevenZOutputFile);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Predicate#test(Object)}为{@code true}保留，null表示保留全部
	 */
	private void addInternal(final File file, final String path, final Predicate<File> filter) throws IOException {
		if (null != filter && false == filter.test(file)) {
			return;
		}
		final SevenZOutputFile out = this.sevenZOutputFile;

		final String entryName;
		if (StrUtil.isNotEmpty(path)) {
			// 非空拼接路径，格式为：path/name
			entryName = StrUtil.addSuffixIfNot(path, StrUtil.SLASH) + file.getName();
		} else {
			// 路径空直接使用文件名或目录名
			entryName = file.getName();
		}
		out.putArchiveEntry(out.createArchiveEntry(file, entryName));

		if (file.isDirectory()) {
			// 目录遍历写入
			final File[] files = file.listFiles();
			if (ArrayUtil.isNotEmpty(files)) {
				for (final File childFile : files) {
					addInternal(childFile, entryName, filter);
				}
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
