/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;

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
				// issue#3018 检查ZipBomb放在读取内容之后，以便entry中的信息正常读取
				ZipSecurityUtil.checkZipBomb(zipEntry, maxSizeDiff);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public InputStream get(final String path) {
		try {
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
