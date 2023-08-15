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

package org.dromara.hutool.extra.compress.extractor;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.compress.CompressException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Predicate;

/**
 * 数据解压器，即将归档打包的数据释放
 *
 * @author looly
 * @since 5.5.0
 */
public class StreamExtractor implements Extractor {

	private final ArchiveInputStream in;

	/**
	 * 构造
	 *
	 * @param charset 编码
	 * @param file    包文件
	 */
	public StreamExtractor(final Charset charset, final File file) {
		this(charset, null, file);
	}

	/**
	 * 构造
	 *
	 * @param charset      编码
	 * @param archiverName 归档包格式，null表示自动检测
	 * @param file         包文件
	 */
	public StreamExtractor(final Charset charset, final String archiverName, final File file) {
		this(charset, archiverName, FileUtil.getInputStream(file));
	}

	/**
	 * 构造
	 *
	 * @param charset 编码
	 * @param in      包流
	 */
	public StreamExtractor(final Charset charset, final InputStream in) {
		this(charset, null, in);
	}

	/**
	 * 构造
	 *
	 * @param charset      编码
	 * @param archiverName 归档包格式，null表示自动检测
	 * @param in           包流
	 */
	public StreamExtractor(final Charset charset, final String archiverName, InputStream in) {
		if (in instanceof ArchiveInputStream) {
			this.in = (ArchiveInputStream) in;
			return;
		}

		final ArchiveStreamFactory factory = new ArchiveStreamFactory(charset.name());
		try {
			in = IoUtil.toBuffered(in);
			if (StrUtil.isBlank(archiverName)) {
				this.in = factory.createArchiveInputStream(in);
			} else if ("tgz".equalsIgnoreCase(archiverName) || "tar.gz".equalsIgnoreCase(archiverName)) {
				//issue#I5J33E，支持tgz格式解压
				try {
					this.in = new TarArchiveInputStream(new GzipCompressorInputStream(in));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			} else {
				this.in = factory.createArchiveInputStream(archiverName, in);
			}
		} catch (final ArchiveException e) {
			// issue#2384，如果报错可能持有文件句柄，导致无法删除文件
			IoUtil.closeQuietly(in);
			throw new CompressException(e);
		}
	}

	@Override
	public InputStream getFirst(final Predicate<ArchiveEntry> predicate) {
		final ArchiveInputStream in = this.in;
		ArchiveEntry entry;
		try {
			while (null != (entry = in.getNextEntry())) {
				if (null != predicate && !predicate.test(entry)) {
					continue;
				}
				if (entry.isDirectory() || !in.canReadEntryData(entry)) {
					// 目录或无法读取的文件直接跳过
					continue;
				}

				return in;
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return null;
	}

	/**
	 * 释放（解压）到指定目录，结束后自动关闭流，此方法只能调用一次
	 *
	 * @param targetDir 目标目录
	 * @param predicate 解压文件过滤器，用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}时释放。
	 */
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
	 * 释放（解压）到指定目录
	 *
	 * @param targetDir 目标目录
	 * @param predicate 解压文件过滤器，用于指定需要释放的文件，null表示不过滤。当{@link Predicate#test(Object)}为{@code true}释放。
	 * @throws IOException IO异常
	 */
	private void extractInternal(final File targetDir, final Predicate<ArchiveEntry> predicate) throws IOException {
		Assert.isTrue(null != targetDir && ((!targetDir.exists()) || targetDir.isDirectory()), "target must be dir.");
		final ArchiveInputStream in = this.in;
		ArchiveEntry entry;
		File outItemFile;
		while (null != (entry = in.getNextEntry())) {
			if (null != predicate && !predicate.test(entry)) {
				continue;
			}
			if (!in.canReadEntryData(entry)) {
				// 无法读取的文件直接跳过
				continue;
			}
			outItemFile = FileUtil.file(targetDir, entry.getName());
			if (entry.isDirectory()) {
				// 创建对应目录
				//noinspection ResultOfMethodCallIgnored
				outItemFile.mkdirs();
			} else {
				FileUtil.writeFromStream(in, outItemFile, false);
			}
		}
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.in);
	}
}
