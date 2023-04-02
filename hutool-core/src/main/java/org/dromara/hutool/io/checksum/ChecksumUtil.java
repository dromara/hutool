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

package org.dromara.hutool.io.checksum;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.io.stream.EmptyOutputStream;
import org.dromara.hutool.lang.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * 校验码工具
 *
 * @author looly
 */
public class ChecksumUtil {

	/**
	 * 计算文件CRC32校验码
	 *
	 * @param file 文件，不能为目录
	 * @return CRC32值
	 * @throws IORuntimeException IO异常
	 */
	public static long checksumCRC32(final File file) throws IORuntimeException {
		return checksum(file, new CRC32()).getValue();
	}

	/**
	 * 计算流CRC32校验码，计算后关闭流
	 *
	 * @param in 文件，不能为目录
	 * @return CRC32值
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static long checksumCRC32(final InputStream in) throws IORuntimeException {
		return checksum(in, new CRC32()).getValue();
	}

	/**
	 * 计算文件校验码
	 *
	 * @param file     文件，不能为目录
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 */
	public static Checksum checksum(final File file, final Checksum checksum) throws IORuntimeException {
		Assert.notNull(file, "File is null !");
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		try {
			return checksum(Files.newInputStream(file.toPath()), checksum);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 计算流的校验码，计算后关闭流
	 *
	 * @param in       流
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 * @since 4.0.10
	 */
	public static Checksum checksum(InputStream in, Checksum checksum) throws IORuntimeException {
		Assert.notNull(in, "InputStream is null !");
		if (null == checksum) {
			checksum = new CRC32();
		}
		try {
			in = new CheckedInputStream(in, checksum);
			IoUtil.copy(in, EmptyOutputStream.INSTANCE);
		} finally {
			IoUtil.closeQuietly(in);
		}
		return checksum;
	}

	/**
	 * 计算流的校验码，计算后关闭流
	 *
	 * @param in       流
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 * @since 5.4.0
	 */
	public static long checksumValue(final InputStream in, final Checksum checksum) {
		return checksum(in, checksum).getValue();
	}
}
