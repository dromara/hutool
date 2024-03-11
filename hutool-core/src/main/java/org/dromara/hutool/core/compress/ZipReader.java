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

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Zip文件或流读取器，一般用于Zip文件解压
 *
 * @author looly
 * @since 5.7.8
 */
public class ZipReader implements Closeable {

	// size of uncompressed zip entry shouldn't be bigger of compressed in MAX_SIZE_DIFF times
	private static final int DEFAULT_MAX_SIZE_DIFF = 100;

	/**
	 * 创建ZipReader
	 *
	 * @param zipFile 生成的Zip文件
	 * @param charset 编码
	 * @return ZipReader
	 */
	public static ZipReader of(final File zipFile, final Charset charset) {
		return new ZipReader(ZipUtil.toZipFile(zipFile, charset));
	}

	/**
	 * 创建ZipReader
	 *
	 * @param in      Zip输入的流，一般为输入文件流
	 * @param charset 编码
	 * @return ZipReader
	 */
	public static ZipReader of(final InputStream in, final Charset charset) {
		return new ZipReader(new ZipInputStream(in, charset));
	}

	private final ZipResource resource;
	/**
	 * 检查ZipBomb文件差异倍数，-1表示不检查ZipBomb
	 */
	private int maxSizeDiff = DEFAULT_MAX_SIZE_DIFF;

	/**
	 * 构造
	 *
	 * @param zipFile 读取的的Zip文件
	 */
	public ZipReader(final ZipFile zipFile) {
		this(new ZipFileResource(zipFile));
	}

	/**
	 * 构造
	 *
	 * @param zin 读取的的Zip文件流
	 */
	public ZipReader(final ZipInputStream zin) {
		this(new ZipStreamResource(zin));
	}

	/**
	 * 构造
	 *
	 * @param resource 读取的的Zip文件流
	 */
	public ZipReader(final ZipResource resource) {
		this.resource = resource;
	}

	/**
	 * 设置检查ZipBomb文件差异倍数，-1表示不检查ZipBomb
	 *
	 * @param maxSizeDiff 检查ZipBomb文件差异倍数，-1表示不检查ZipBomb
	 * @return this
	 * @since 6.0.0
	 */
	public ZipReader setMaxSizeDiff(final int maxSizeDiff) {
		this.maxSizeDiff = maxSizeDiff;
		return this;
	}

	/**
	 * 获取指定路径的文件流<br>
	 * 如果是文件模式，则直接获取Entry对应的流，如果是流模式，则遍历entry后，找到对应流返回
	 *
	 * @param path 路径
	 * @return 文件流
	 */
	public InputStream get(final String path) {
		return this.resource.get(path);
	}

	/**
	 * 解压到指定目录中
	 *
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 */
	public File readTo(final File outFile) throws IORuntimeException {
		return readTo(outFile, null);
	}

	/**
	 * 解压到指定目录中
	 *
	 * @param outFile     解压到的目录
	 * @param entryFilter 过滤器，只保留{@link Predicate#test(Object)}结果为{@code true}的文件
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * @since 5.7.12
	 */
	@SuppressWarnings("resource")
	public File readTo(final File outFile, final Predicate<ZipEntry> entryFilter) throws IORuntimeException {
		read((zipEntry) -> {
			if (null == entryFilter || entryFilter.test(zipEntry)) {
				readEntry(zipEntry, outFile);
			}
		});
		return outFile;
	}

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param consumer {@link ZipEntry}处理器
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ZipReader read(final Consumer<ZipEntry> consumer) throws IORuntimeException {
		resource.read(consumer, this.maxSizeDiff);
		return this;
	}

	@Override
	public void close() throws IORuntimeException {
		IoUtil.closeQuietly(this.resource);
	}

	/**
	 * 读取一个ZipEntry的数据到目标目录下，如果entry是个目录，则创建对应目录，否则解压并写出到文件
	 *
	 * @param zipEntry entry
	 * @param outFile  写出到的目录
	 */
	private void readEntry(final ZipEntry zipEntry, final File outFile) {
		//gitee issue #I4ZDQI
		String path = zipEntry.getName();
		if (FileUtil.isWindows()) {
			// Win系统下
			path = StrUtil.replace(path, "*", "_");
		}
		// FileUtil.file会检查slip漏洞，漏洞说明见http://blog.nsfocus.net/zip-slip-2/
		final File outItemFile = FileUtil.file(outFile, path);
		if (zipEntry.isDirectory()) {
			// 目录
			//noinspection ResultOfMethodCallIgnored
			outItemFile.mkdirs();
		} else {
			// 文件
			FileUtil.copy(this.resource.get(zipEntry), outItemFile);
		}
	}
}
