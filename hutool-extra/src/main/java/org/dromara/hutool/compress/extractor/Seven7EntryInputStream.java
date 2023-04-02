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

package org.dromara.hutool.compress.extractor;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 7z解压中文件流读取的封装
 *
 * @author looly
 * @since 5.5.0
 */
public class Seven7EntryInputStream extends InputStream {

	private final SevenZFile sevenZFile;
	private final long size;
	private long readSize = 0;

	/**
	 * 构造
	 *
	 * @param sevenZFile {@link SevenZFile}
	 * @param entry      {@link SevenZArchiveEntry}
	 */
	public Seven7EntryInputStream(final SevenZFile sevenZFile, final SevenZArchiveEntry entry) {
		this(sevenZFile, entry.getSize());
	}

	/**
	 * 构造
	 *
	 * @param sevenZFile {@link SevenZFile}
	 * @param size       读取长度
	 */
	public Seven7EntryInputStream(final SevenZFile sevenZFile, final long size) {
		this.sevenZFile = sevenZFile;
		this.size = size;
	}

	@Override
	public int available() throws IOException {
		try {
			return Math.toIntExact(this.size);
		} catch (final ArithmeticException e) {
			throw new IOException("Entry size is too large!(max than Integer.MAX)", e);
		}
	}

	/**
	 * 获取读取的长度（字节数）
	 *
	 * @return 读取的字节数
	 * @since 5.7.14
	 */
	public long getReadSize() {
		return this.readSize;
	}

	@Override
	public int read() throws IOException {
		this.readSize++;
		return this.sevenZFile.read();
	}
}
