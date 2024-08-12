/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.dromara.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * {@link ZipFile} 资源包装
 *
 * @author looly
 * @since 6.0.0
 */
public class ZipFileResource implements ZipResource {

	private final ZipFile zipFile;

	/**
	 * 构造
	 *
	 * @param zipFile {@link ZipFile}
	 */
	public ZipFileResource(final ZipFile zipFile) {
		this.zipFile = zipFile;
	}

	@Override
	public void read(final Consumer<ZipEntry> consumer, final int maxSizeDiff) {
		final Enumeration<? extends ZipEntry> em = zipFile.entries();
		while (em.hasMoreElements()) {
			consumer.accept(ZipSecurityUtil.checkZipBomb(em.nextElement(), maxSizeDiff));
		}
	}

	@Override
	public InputStream get(final String path){
		final ZipFile zipFile = this.zipFile;
		final ZipEntry entry = zipFile.getEntry(path);
		if (null != entry) {
			return ZipUtil.getStream(zipFile, entry);
		}
		return null;
	}

	@Override
	public InputStream get(final ZipEntry entry) {
		return ZipUtil.getStream(this.zipFile, entry);
	}

	@Override
	public void close() throws IOException {
		IoUtil.closeQuietly(this.zipFile);
	}
}
