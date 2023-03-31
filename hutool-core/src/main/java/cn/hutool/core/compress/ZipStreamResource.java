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

package cn.hutool.core.compress;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * {@link ZipInputStream} 资源包装
 *
 * @author looly
 * @since 6.0.0
 */
public class ZipStreamResource implements ZipResource {

	private final ZipInputStream in;

	/**
	 * 构造
	 *
	 * @param in {@link ZipInputStream}
	 */
	public ZipStreamResource(final ZipInputStream in) {
		this.in = in;
	}

	@Override
	public void read(final Consumer<ZipEntry> consumer, final int maxSizeDiff) {
		try {
			ZipEntry zipEntry;
			while (null != (zipEntry = in.getNextEntry())) {
				consumer.accept(zipEntry);
				// 检查ZipBomb放在读取内容之后，以便entry中的信息正常读取
				ZipSecurityUtil.checkZipBomb(zipEntry, maxSizeDiff);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public InputStream get(final String path) {
		try {
			this.in.reset();
			ZipEntry zipEntry;
			while (null != (zipEntry = in.getNextEntry())) {
				if (zipEntry.getName().equals(path)) {
					return this.in;
				}
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return null;
	}

	@Override
	public InputStream get(final ZipEntry entry) {
		return this.in;
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.in);
	}
}
